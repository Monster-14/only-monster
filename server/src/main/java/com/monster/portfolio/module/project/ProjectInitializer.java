package com.monster.portfolio.module.project;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.monster.portfolio.module.project.entity.Project;
import com.monster.portfolio.module.project.mapper.ProjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProjectInitializer implements ApplicationRunner {

    private final ProjectMapper projectMapper;

    public ProjectInitializer(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (projectMapper.selectCount(new LambdaQueryWrapper<>()) > 0) {
            return;
        }
        Project project = new Project();
        project.setSlug("monster-portfolio");
        project.setTitle("Monster 个人主页");
        project.setCoverUrl("");
        project.setSummary("Vue3 + Spring Boot 的个人站点，支持后台维护资料和项目。");
        project.setContent("这是当前正在开发的个人主页。前台用 Vue3，后台用 Spring Boot 3 和 JDK 17。");
        project.setTechStack("Vue3,Spring Boot,MySQL,Element Plus");
        project.setDemoUrl("");
        project.setRepoUrl("https://github.com/Monster-14/only-monster");
        project.setStatus(Project.STATUS_PUBLISHED);
        project.setSort(10);
        projectMapper.insert(project);
        log.info("Initialized sample project: {}", project.getSlug());
    }
}
