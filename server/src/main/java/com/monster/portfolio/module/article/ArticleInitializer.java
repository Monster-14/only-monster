package com.monster.portfolio.module.article;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.monster.portfolio.module.article.entity.Article;
import com.monster.portfolio.module.article.mapper.ArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class ArticleInitializer implements ApplicationRunner {

    private final ArticleMapper articleMapper;

    public ArticleInitializer(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (articleMapper.selectCount(new LambdaQueryWrapper<>()) > 0) {
            return;
        }
        Article article = new Article();
        article.setSlug("hello-monster");
        article.setTitle("Hello Monster");
        article.setCoverUrl("");
        article.setSummary("个人主页的第一篇文章，用来确认 Markdown 渲染是否正常。");
        article.setContentMd("""
                ## 为什么做这个站

                用 **Vue3** 和 **Spring Boot** 搭一个能自己改内容的个人主页。

                ```java
                System.out.println("hello monster");
                ```

                - 资料后台可改
                - 项目可以发布或存草稿
                - 文章用 Markdown 写
                """);
        article.setStatus(Article.STATUS_PUBLISHED);
        article.setPublishedAt(LocalDateTime.now());
        articleMapper.insert(article);
        log.info("Initialized sample article: {}", article.getSlug());
    }
}
