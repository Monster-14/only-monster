package com.monster.portfolio.module.profile.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("profile")
public class Profile {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String headline;
    private String bio;
    private String avatarUrl;
    private String email;
    private String githubUrl;
    private String blogUrl;
    private String wechat;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
