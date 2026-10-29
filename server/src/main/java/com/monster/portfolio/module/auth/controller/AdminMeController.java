package com.monster.portfolio.module.auth.controller;

import com.monster.portfolio.common.R;
import com.monster.portfolio.module.auth.service.AuthService;
import com.monster.portfolio.module.auth.vo.AdminProfileVO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminMeController {

    private final AuthService authService;

    public AdminMeController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public R<AdminProfileVO> me(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return R.ok(authService.current(userId));
    }
}
