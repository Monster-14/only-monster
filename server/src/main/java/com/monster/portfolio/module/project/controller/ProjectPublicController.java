package com.monster.portfolio.module.project.controller;

import com.monster.portfolio.common.R;
import com.monster.portfolio.module.project.service.ProjectService;
import com.monster.portfolio.module.project.vo.ProjectVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/projects")
public class ProjectPublicController {

    private final ProjectService projectService;

    public ProjectPublicController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public R<List<ProjectVO>> list() {
        return R.ok(projectService.listPublic());
    }

    @GetMapping("/{slug}")
    public R<ProjectVO> detail(@PathVariable String slug) {
        return R.ok(projectService.getPublicBySlug(slug));
    }
}
