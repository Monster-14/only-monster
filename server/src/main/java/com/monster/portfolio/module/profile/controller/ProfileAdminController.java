package com.monster.portfolio.module.profile.controller;

import com.monster.portfolio.common.R;
import com.monster.portfolio.module.profile.dto.ProfileUpdateRequest;
import com.monster.portfolio.module.profile.service.ProfileService;
import com.monster.portfolio.module.profile.vo.ProfileVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/profile")
public class ProfileAdminController {

    private final ProfileService profileService;

    public ProfileAdminController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public R<ProfileVO> get() {
        return R.ok(profileService.getPublic());
    }

    @PutMapping
    public R<ProfileVO> update(@Valid @RequestBody ProfileUpdateRequest request) {
        return R.ok(profileService.update(request));
    }
}
