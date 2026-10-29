package com.monster.portfolio.module.article.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.monster.portfolio.common.BusinessException;
import com.monster.portfolio.module.article.dto.ArticleSaveRequest;
import com.monster.portfolio.module.article.entity.Article;
import com.monster.portfolio.module.article.mapper.ArticleMapper;
import com.monster.portfolio.module.article.vo.ArticleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleMapper articleMapper;

    public ArticleService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    public List<ArticleVO> listPublic() {
        return articleMapper.selectList(new LambdaQueryWrapper<Article>()
                        .eq(Article::getStatus, Article.STATUS_PUBLISHED)
                        .orderByDesc(Article::getPublishedAt)
                        .orderByDesc(Article::getId))
                .stream()
                .map(this::toVo)
                .toList();
    }

    public ArticleVO getPublicBySlug(String slug) {
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .eq(Article::getSlug, slug)
                .eq(Article::getStatus, Article.STATUS_PUBLISHED));
        if (article == null) {
            throw new BusinessException(404, "文章不存在或未发布");
        }
        return toVo(article);
    }

    public List<ArticleVO> listAdmin() {
        return articleMapper.selectList(new LambdaQueryWrapper<Article>()
                        .orderByDesc(Article::getUpdatedAt)
                        .orderByDesc(Article::getId))
                .stream()
                .map(this::toVo)
                .toList();
    }

    public ArticleVO getAdmin(Long id) {
        return toVo(requireById(id));
    }

    public ArticleVO create(ArticleSaveRequest request) {
        ensureSlugUnique(request.getSlug(), null);
        Article article = new Article();
        apply(article, request);
        articleMapper.insert(article);
        return toVo(article);
    }

    public ArticleVO update(Long id, ArticleSaveRequest request) {
        Article article = requireById(id);
        ensureSlugUnique(request.getSlug(), id);
        apply(article, request);
        articleMapper.updateById(article);
        return toVo(article);
    }

    public void delete(Long id) {
        requireById(id);
        articleMapper.deleteById(id);
    }

    private Article requireById(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        return article;
    }

    private void ensureSlugUnique(String slug, Long excludeId) {
        Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getSlug, slug)
                .ne(excludeId != null, Article::getId, excludeId));
        if (count != null && count > 0) {
            throw new BusinessException("路径别名已存在");
        }
    }

    private void apply(Article article, ArticleSaveRequest request) {
        article.setSlug(request.getSlug().trim());
        article.setTitle(request.getTitle().trim());
        article.setCoverUrl(blank(request.getCoverUrl()));
        article.setSummary(blank(request.getSummary()));
        article.setContentMd(blank(request.getContentMd()));
        int status = Article.STATUS_PUBLISHED == request.getStatus()
                ? Article.STATUS_PUBLISHED
                : Article.STATUS_DRAFT;
        article.setStatus(status);
        if (status == Article.STATUS_PUBLISHED && article.getPublishedAt() == null) {
            article.setPublishedAt(LocalDateTime.now());
        }
        if (status == Article.STATUS_DRAFT) {
            article.setPublishedAt(null);
        }
    }

    private ArticleVO toVo(Article article) {
        ArticleVO vo = new ArticleVO();
        BeanUtils.copyProperties(article, vo);
        return vo;
    }

    private String blank(String value) {
        return value == null ? "" : value.trim();
    }
}
