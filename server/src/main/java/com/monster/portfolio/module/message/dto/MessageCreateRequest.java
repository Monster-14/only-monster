package com.monster.portfolio.module.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MessageCreateRequest {

    @NotBlank(message = "请填写称呼")
    @Size(max = 32, message = "称呼最多 32 个字")
    private String nickname;

    @Size(max = 128, message = "邮箱过长")
    private String email;

    @NotBlank(message = "请填写留言内容")
    @Size(min = 4, max = 500, message = "留言请控制在 4 到 500 个字")
    private String content;
}
