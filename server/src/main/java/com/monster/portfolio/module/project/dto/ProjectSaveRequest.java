package com.monster.portfolio.module.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectSaveRequest {

    @NotBlank(message = "请填写路径别名")
    @Size(max = 80)
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "别名只能用小写字母、数字和连字符")
    private String slug;

    @NotBlank(message = "请填写标题")
    @Size(max = 128)
    private String title;

    @Size(max = 512)
    private String coverUrl;

    @Size(max = 255)
    private String summary;

    private String content;

    private List<String> techStack = new ArrayList<>();

    @Size(max = 256)
    private String demoUrl;

    @Size(max = 256)
    private String repoUrl;

    @NotNull
    private Integer status;

    private Integer sort;
}
