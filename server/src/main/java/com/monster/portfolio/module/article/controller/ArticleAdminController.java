package com.monster.portfolio.module.article.controller;

import com.monster.portfolio.common.R;
import com.monster.portfolio.module.article.dto.ArticleSaveRequest;
import com.monster.portfolio.module.article.service.ArticleService;
import com.monster.portfolio.module.article.vo.ArticleVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/articles")
public class ArticleAdminController {

    private final ArticleService articleService;

    public ArticleAdminController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public R<List<ArticleVO>> list() {
        return R.ok(articleService.listAdmin());
    }

    @GetMapping("/{id}")
    public R<ArticleVO> detail(@PathVariable Long id) {
        return R.ok(articleService.getAdmin(id));
    }

    @PostMapping
    public R<ArticleVO> create(@Valid @RequestBody ArticleSaveRequest request) {
        return R.ok(articleService.create(request));
    }

    @PutMapping("/{id}")
    public R<ArticleVO> update(@PathVariable Long id, @Valid @RequestBody ArticleSaveRequest request) {
        return R.ok(articleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        articleService.delete(id);
        return R.ok();
    }
}
