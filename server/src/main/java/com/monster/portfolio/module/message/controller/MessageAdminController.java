package com.monster.portfolio.module.message.controller;

import com.monster.portfolio.common.R;
import com.monster.portfolio.module.message.service.MessageService;
import com.monster.portfolio.module.message.vo.MessageVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/messages")
public class MessageAdminController {

    private final MessageService messageService;

    public MessageAdminController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public R<List<MessageVO>> list() {
        return R.ok(messageService.listAdmin());
    }

    @GetMapping("/unread-count")
    public R<Map<String, Long>> unreadCount() {
        return R.ok(Map.of("count", messageService.unreadCount()));
    }

    @PutMapping("/{id}/read")
    public R<Void> markRead(@PathVariable Long id) {
        messageService.markRead(id);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        messageService.delete(id);
        return R.ok();
    }
}
