package com.monster.portfolio.module.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.monster.portfolio.config.AdminProperties;
import com.monster.portfolio.module.auth.entity.AdminUser;
import com.monster.portfolio.module.auth.mapper.AdminUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableConfigurationProperties(AdminProperties.class)
public class AdminInitializer implements ApplicationRunner {

    private final AdminUserMapper adminUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    public AdminInitializer(
            AdminUserMapper adminUserMapper,
            PasswordEncoder passwordEncoder,
            AdminProperties adminProperties
    ) {
        this.adminUserMapper = adminUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.adminProperties = adminProperties;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (adminUserMapper.selectCount(new LambdaQueryWrapper<>()) > 0) {
            return;
        }
        AdminUser admin = new AdminUser();
        admin.setUsername(adminProperties.getUsername());
        admin.setPasswordHash(passwordEncoder.encode(adminProperties.getPassword()));
        adminUserMapper.insert(admin);
        log.info("Initialized default admin account: {}", admin.getUsername());
    }
}
