package com.monster.portfolio.module.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.monster.portfolio.common.BusinessException;
import com.monster.portfolio.module.project.dto.ProjectSaveRequest;
import com.monster.portfolio.module.project.entity.Project;
import com.monster.portfolio.module.project.mapper.ProjectMapper;
import com.monster.portfolio.module.project.vo.ProjectVO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectMapper projectMapper;

    public ProjectService(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    public List<ProjectVO> listPublic() {
        return projectMapper.selectList(new LambdaQueryWrapper<Project>()
                        .eq(Project::getStatus, Project.STATUS_PUBLISHED)
                        .orderByDesc(Project::getSort)
                        .orderByDesc(Project::getId))
                .stream()
                .map(this::toVo)
                .toList();
    }

    public ProjectVO getPublicBySlug(String slug) {
        Project project = projectMapper.selectOne(new LambdaQueryWrapper<Project>()
                .eq(Project::getSlug, slug)
                .eq(Project::getStatus, Project.STATUS_PUBLISHED));
        if (project == null) {
            throw new BusinessException(404, "项目不存在或未发布");
        }
        return toVo(project);
    }

    public List<ProjectVO> listAdmin() {
        return projectMapper.selectList(new LambdaQueryWrapper<Project>()
                        .orderByDesc(Project::getSort)
                        .orderByDesc(Project::getId))
                .stream()
                .map(this::toVo)
                .toList();
    }

    public ProjectVO getAdmin(Long id) {
        return toVo(requireById(id));
    }

    public ProjectVO create(ProjectSaveRequest request) {
        ensureSlugUnique(request.getSlug(), null);
        Project project = new Project();
        apply(project, request);
        projectMapper.insert(project);
        return toVo(project);
    }

    public ProjectVO update(Long id, ProjectSaveRequest request) {
        Project project = requireById(id);
        ensureSlugUnique(request.getSlug(), id);
        apply(project, request);
        projectMapper.updateById(project);
        return toVo(project);
    }

    public void delete(Long id) {
        requireById(id);
        projectMapper.deleteById(id);
    }

    private Project requireById(Long id) {
        Project project = projectMapper.selectById(id);
        if (project == null) {
            throw new BusinessException("项目不存在");
        }
        return project;
    }

    private void ensureSlugUnique(String slug, Long excludeId) {
        Long count = projectMapper.selectCount(new LambdaQueryWrapper<Project>()
                .eq(Project::getSlug, slug)
                .ne(excludeId != null, Project::getId, excludeId));
        if (count != null && count > 0) {
            throw new BusinessException("路径别名已存在");
        }
    }

    private void apply(Project project, ProjectSaveRequest request) {
        project.setSlug(request.getSlug().trim());
        project.setTitle(request.getTitle().trim());
        project.setCoverUrl(blank(request.getCoverUrl()));
        project.setSummary(blank(request.getSummary()));
        project.setContent(blank(request.getContent()));
        project.setTechStack(join(request.getTechStack()));
        project.setDemoUrl(blank(request.getDemoUrl()));
        project.setRepoUrl(blank(request.getRepoUrl()));
        int status = Project.STATUS_PUBLISHED == request.getStatus()
                ? Project.STATUS_PUBLISHED
                : Project.STATUS_DRAFT;
        project.setStatus(status);
        project.setSort(request.getSort() == null ? 0 : request.getSort());
    }

    private ProjectVO toVo(Project project) {
        ProjectVO vo = new ProjectVO();
        vo.setId(project.getId());
        vo.setSlug(project.getSlug());
        vo.setTitle(project.getTitle());
        vo.setCoverUrl(project.getCoverUrl());
        vo.setSummary(project.getSummary());
        vo.setContent(project.getContent());
        vo.setTechStack(split(project.getTechStack()));
        vo.setDemoUrl(project.getDemoUrl());
        vo.setRepoUrl(project.getRepoUrl());
        vo.setStatus(project.getStatus());
        vo.setSort(project.getSort());
        return vo;
    }

    private String blank(String value) {
        return value == null ? "" : value.trim();
    }

    private String join(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        return tags.stream()
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .collect(Collectors.joining(","));
    }

    private List<String> split(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .toList();
    }
}
