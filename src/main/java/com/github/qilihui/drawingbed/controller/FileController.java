package com.github.qilihui.drawingbed.controller;

import cn.hutool.core.io.FileTypeUtil;
import com.github.qilihui.drawingbed.util.NameUtil;
import com.github.qilihui.drawingbed.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * @author qilihui
 * @date 2021/4/18 21:47
 */
@RestController
@Slf4j
public class FileController {

    @Value("${drawing.path}")
    private String imaPath;

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        log.info(request.getRequestURL().toString());
        log.info(request.getContextPath());
        log.info(request.getServletPath());
        if (file.isEmpty()) {
            return Result.fail("上传失败，请选择文件");
        }
        try {
            String type = FileTypeUtil.getType(file.getInputStream(), file.getOriginalFilename());
            if (!NameUtil.isImage(type)) {
                return Result.fail("文件类型错误");
            }
            String newName = NameUtil.currentTimeMillisToUrl(type);
            File newFile = new File(imaPath + "/" + newName);
            file.transferTo(newFile);
            return Result.ok(newName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.fail("上传失败!");
    }
}
