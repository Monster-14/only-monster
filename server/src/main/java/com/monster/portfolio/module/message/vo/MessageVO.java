package com.monster.portfolio.module.message.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageVO {

    private Long id;
    private String nickname;
    private String email;
    private String content;
    private Integer status;
    private String ip;
    private LocalDateTime createdAt;
}
