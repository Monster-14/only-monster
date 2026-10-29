package com.monster.portfolio.module.message.controller;

import com.monster.portfolio.common.R;
import com.monster.portfolio.module.message.dto.MessageCreateRequest;
import com.monster.portfolio.module.message.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/messages")
public class MessagePublicController {

    private final MessageService messageService;

    public MessagePublicController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public R<Void> create(@Valid @RequestBody MessageCreateRequest request, HttpServletRequest httpRequest) {
        messageService.create(request, clientIp(httpRequest));
        return R.ok();
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
