package com.monster.portfolio.module.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.monster.portfolio.common.BusinessException;
import com.monster.portfolio.module.auth.dto.LoginRequest;
import com.monster.portfolio.module.auth.entity.AdminUser;
import com.monster.portfolio.module.auth.mapper.AdminUserMapper;
import com.monster.portfolio.module.auth.vo.AdminProfileVO;
import com.monster.portfolio.module.auth.vo.LoginVO;
import com.monster.portfolio.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AdminUserMapper adminUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AdminUserMapper adminUserMapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.adminUserMapper = adminUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginVO login(LoginRequest request) {
        AdminUser admin = adminUserMapper.selectOne(new LambdaQueryWrapper<AdminUser>()
                .eq(AdminUser::getUsername, request.getUsername()));
        if (admin == null || !passwordEncoder.matches(request.getPassword(), admin.getPasswordHash())) {
            throw new BusinessException("用户名或密码错误");
        }
        admin.setLastLoginAt(LocalDateTime.now());
        adminUserMapper.updateById(admin);
        return new LoginVO(jwtService.createToken(admin.getId(), admin.getUsername()), admin.getUsername());
    }

    public AdminProfileVO current(Long userId) {
        AdminUser admin = adminUserMapper.selectById(userId);
        if (admin == null) {
            throw new BusinessException(401, "未登录或登录已过期");
        }
        return new AdminProfileVO(admin.getId(), admin.getUsername());
    }
}
