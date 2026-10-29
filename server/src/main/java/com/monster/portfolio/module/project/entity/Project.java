package com.monster.portfolio.module.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("project")
public class Project {

    public static final int STATUS_DRAFT = 0;
    public static final int STATUS_PUBLISHED = 1;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String slug;
    private String title;
    private String coverUrl;
    private String summary;
    private String content;
    private String techStack;
    private String demoUrl;
    private String repoUrl;
    private Integer status;
    private Integer sort;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
