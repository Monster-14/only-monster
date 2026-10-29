package com.monster.portfolio.module.project.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectVO {

    private Long id;
    private String slug;
    private String title;
    private String coverUrl;
    private String summary;
    private String content;
    private List<String> techStack = new ArrayList<>();
    private String demoUrl;
    private String repoUrl;
    private Integer status;
    private Integer sort;
}
