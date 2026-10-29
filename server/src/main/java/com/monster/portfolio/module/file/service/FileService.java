package com.monster.portfolio.module.file.service;

import com.monster.portfolio.common.BusinessException;
import com.monster.portfolio.config.UploadProperties;
import com.monster.portfolio.module.file.vo.UploadVO;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@EnableConfigurationProperties(UploadProperties.class)
public class FileService {

    private static final long MAX_BYTES = 2 * 1024 * 1024;
    private static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("yyyy/MM");

    private final Path root;

    public FileService(UploadProperties properties) {
        this.root = Path.of(properties.getDir()).toAbsolutePath().normalize();
    }

    public UploadVO saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("请选择图片");
        }
        if (file.getSize() > MAX_BYTES) {
            throw new BusinessException("图片不能超过 2MB");
        }
        try {
            Files.createDirectories(root);
            String folder = LocalDate.now().format(MONTH);
            Path dir = root.resolve(folder).normalize();
            if (!dir.startsWith(root)) {
                throw new BusinessException("非法上传路径");
            }
            Files.createDirectories(dir);
            String ext;
            Path dest;
            try (InputStream in = file.getInputStream()) {
                byte[] header = in.readNBytes(16);
                ext = detectExtension(header);
                dest = dir.resolve(UUID.randomUUID().toString().replace("-", "") + "." + ext).normalize();
                if (!dest.startsWith(root)) {
                    throw new BusinessException("非法上传路径");
                }
                try (OutputStream out = Files.newOutputStream(dest)) {
                    out.write(header);
                    in.transferTo(out);
                }
            }
            if (Files.size(dest) > MAX_BYTES) {
                Files.deleteIfExists(dest);
                throw new BusinessException("图片不能超过 2MB");
            }
            return new UploadVO("/uploads/" + folder + "/" + dest.getFileName());
        } catch (BusinessException ex) {
            throw ex;
        } catch (IOException ex) {
            throw new BusinessException("图片保存失败");
        }
    }

    private String detectExtension(byte[] header) {
        if (header.length >= 3
                && (header[0] & 0xFF) == 0xFF
                && (header[1] & 0xFF) == 0xD8
                && (header[2] & 0xFF) == 0xFF) {
            return "jpg";
        }
        if (header.length >= 8
                && (header[0] & 0xFF) == 0x89
                && header[1] == 0x50
                && header[2] == 0x4E
                && header[3] == 0x47) {
            return "png";
        }
        if (header.length >= 12
                && "RIFF".equals(new String(header, 0, 4, StandardCharsets.US_ASCII))
                && "WEBP".equals(new String(header, 8, 4, StandardCharsets.US_ASCII))) {
            return "webp";
        }
        throw new BusinessException("只支持 jpg / png / webp");
    }
}
