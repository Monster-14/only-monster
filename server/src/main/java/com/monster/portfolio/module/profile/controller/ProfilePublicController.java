package com.monster.portfolio.module.profile.controller;

import com.monster.portfolio.common.R;
import com.monster.portfolio.module.profile.service.ProfileService;
import com.monster.portfolio.module.profile.vo.ProfileVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class ProfilePublicController {

    private final ProfileService profileService;

    public ProfilePublicController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public R<ProfileVO> profile() {
        return R.ok(profileService.getPublic());
    }
}
