package com.monster.portfolio.module.profile.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.monster.portfolio.module.profile.dto.ProfileUpdateRequest;
import com.monster.portfolio.module.profile.entity.Profile;
import com.monster.portfolio.module.profile.mapper.ProfileMapper;
import com.monster.portfolio.module.profile.vo.ProfileVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileMapper profileMapper;

    public ProfileService(ProfileMapper profileMapper) {
        this.profileMapper = profileMapper;
    }

    public ProfileVO getPublic() {
        return toVo(requireProfile());
    }

    public ProfileVO update(ProfileUpdateRequest request) {
        Profile profile = requireProfile();
        profile.setName(request.getName().trim());
        profile.setHeadline(emptyToDefault(request.getHeadline()));
        profile.setBio(emptyToDefault(request.getBio()));
        profile.setAvatarUrl(emptyToDefault(request.getAvatarUrl()));
        profile.setEmail(emptyToDefault(request.getEmail()));
        profile.setGithubUrl(emptyToDefault(request.getGithubUrl()));
        profile.setBlogUrl(emptyToDefault(request.getBlogUrl()));
        profile.setWechat(emptyToDefault(request.getWechat()));
        profileMapper.updateById(profile);
        return toVo(profile);
    }

    private Profile requireProfile() {
        Profile profile = profileMapper.selectOne(new LambdaQueryWrapper<Profile>().last("LIMIT 1"));
        if (profile != null) {
            return profile;
        }
        Profile created = new Profile();
        created.setName("Monster");
        created.setHeadline("全栈开发者");
        created.setBio("用 Vue3 和 Spring Boot 搭建的个人站点。");
        created.setAvatarUrl("");
        created.setEmail("");
        created.setGithubUrl("");
        created.setBlogUrl("");
        created.setWechat("");
        profileMapper.insert(created);
        return created;
    }

    private ProfileVO toVo(Profile profile) {
        ProfileVO vo = new ProfileVO();
        BeanUtils.copyProperties(profile, vo);
        return vo;
    }

    private String emptyToDefault(String value) {
        return value == null ? "" : value.trim();
    }
}
