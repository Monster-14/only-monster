package com.monster.portfolio.module.message.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.monster.portfolio.common.BusinessException;
import com.monster.portfolio.common.IpRateLimiter;
import com.monster.portfolio.module.message.dto.MessageCreateRequest;
import com.monster.portfolio.module.message.entity.Message;
import com.monster.portfolio.module.message.mapper.MessageMapper;
import com.monster.portfolio.module.message.vo.MessageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class MessageService {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final MessageMapper messageMapper;
    private final IpRateLimiter ipRateLimiter;

    public MessageService(MessageMapper messageMapper, IpRateLimiter ipRateLimiter) {
        this.messageMapper = messageMapper;
        this.ipRateLimiter = ipRateLimiter;
    }

    public void create(MessageCreateRequest request, String ip) {
        if (!ipRateLimiter.tryAcquire("message:" + ip, 5, 600)) {
            throw new BusinessException("留言太频繁，请稍后再试");
        }
        String email = request.getEmail() == null ? "" : request.getEmail().trim();
        if (!email.isEmpty() && !EMAIL.matcher(email).matches()) {
            throw new BusinessException("邮箱格式不正确");
        }
        Message message = new Message();
        message.setNickname(request.getNickname().trim());
        message.setEmail(email);
        message.setContent(request.getContent().trim());
        message.setStatus(Message.STATUS_UNREAD);
        message.setIp(ip == null ? "" : ip);
        messageMapper.insert(message);
    }

    public List<MessageVO> listAdmin() {
        return messageMapper.selectList(new LambdaQueryWrapper<Message>()
                        .orderByAsc(Message::getStatus)
                        .orderByDesc(Message::getId))
                .stream()
                .map(this::toVo)
                .toList();
    }

    public long unreadCount() {
        Long count = messageMapper.selectCount(new LambdaQueryWrapper<Message>()
                .eq(Message::getStatus, Message.STATUS_UNREAD));
        return count == null ? 0 : count;
    }

    public void markRead(Long id) {
        Message message = requireById(id);
        message.setStatus(Message.STATUS_READ);
        messageMapper.updateById(message);
    }

    public void delete(Long id) {
        requireById(id);
        messageMapper.deleteById(id);
    }

    private Message requireById(Long id) {
        Message message = messageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException("留言不存在");
        }
        return message;
    }

    private MessageVO toVo(Message message) {
        MessageVO vo = new MessageVO();
        BeanUtils.copyProperties(message, vo);
        return vo;
    }
}
