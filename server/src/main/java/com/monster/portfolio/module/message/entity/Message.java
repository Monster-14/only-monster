package com.monster.portfolio.module.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("message")
public class Message {

    public static final int STATUS_UNREAD = 0;
    public static final int STATUS_READ = 1;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String nickname;
    private String email;
    private String content;
    private Integer status;
    private String ip;
    private LocalDateTime createdAt;
}
