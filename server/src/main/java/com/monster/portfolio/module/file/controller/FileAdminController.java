package com.monster.portfolio.module.file.controller;

import com.monster.portfolio.common.R;
import com.monster.portfolio.module.file.service.FileService;
import com.monster.portfolio.module.file.vo.UploadVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/files")
public class FileAdminController {

    private final FileService fileService;

    public FileAdminController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public R<UploadVO> upload(@RequestParam("file") MultipartFile file) {
        return R.ok(fileService.saveImage(file));
    }
}
