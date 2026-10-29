package com.monster.portfolio.module.project.controller;

import com.monster.portfolio.common.R;
import com.monster.portfolio.module.project.dto.ProjectSaveRequest;
import com.monster.portfolio.module.project.service.ProjectService;
import com.monster.portfolio.module.project.vo.ProjectVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/projects")
public class ProjectAdminController {

    private final ProjectService projectService;

    public ProjectAdminController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public R<List<ProjectVO>> list() {
        return R.ok(projectService.listAdmin());
    }

    @GetMapping("/{id}")
    public R<ProjectVO> detail(@PathVariable Long id) {
        return R.ok(projectService.getAdmin(id));
    }

    @PostMapping
    public R<ProjectVO> create(@Valid @RequestBody ProjectSaveRequest request) {
        return R.ok(projectService.create(request));
    }

    @PutMapping("/{id}")
    public R<ProjectVO> update(@PathVariable Long id, @Valid @RequestBody ProjectSaveRequest request) {
        return R.ok(projectService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return R.ok();
    }
}
