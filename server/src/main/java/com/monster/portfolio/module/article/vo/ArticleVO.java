package com.monster.portfolio.module.article.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleVO {

    private Long id;
    private String slug;
    private String title;
    private String coverUrl;
    private String summary;
    private String contentMd;
    private Integer status;
    private LocalDateTime publishedAt;
}
