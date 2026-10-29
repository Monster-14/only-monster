package com.monster.portfolio.module.profile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileUpdateRequest {

    @NotBlank(message = "请填写姓名")
    @Size(max = 64)
    private String name;

    @Size(max = 128)
    private String headline;

    @Size(max = 4000)
    private String bio;

    @Size(max = 512)
    private String avatarUrl;

    @Size(max = 128)
    private String email;

    @Size(max = 256)
    private String githubUrl;

    @Size(max = 256)
    private String blogUrl;

    @Size(max = 64)
    private String wechat;
}
