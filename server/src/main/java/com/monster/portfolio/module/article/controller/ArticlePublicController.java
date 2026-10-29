package com.monster.portfolio.module.article.controller;

import com.monster.portfolio.common.R;
import com.monster.portfolio.module.article.service.ArticleService;
import com.monster.portfolio.module.article.vo.ArticleVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/articles")
public class ArticlePublicController {

    private final ArticleService articleService;

    public ArticlePublicController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public R<List<ArticleVO>> list() {
        return R.ok(articleService.listPublic());
    }

    @GetMapping("/{slug}")
    public R<ArticleVO> detail(@PathVariable String slug) {
        return R.ok(articleService.getPublicBySlug(slug));
    }
}
