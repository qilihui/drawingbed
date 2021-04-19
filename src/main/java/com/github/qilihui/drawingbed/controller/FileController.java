package com.github.qilihui.drawingbed.controller;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import com.github.qilihui.drawingbed.util.NameUtil;
import com.github.qilihui.drawingbed.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author qilihui
 * @date 2021/4/18 21:47
 */
@Controller
@Slf4j
public class FileController {

    @Value("${drawing.path}")
    private String imaPath;

    @Value("${drawing.retryCount}")
    private int RETRY_COUNT;

    @PostMapping("/upload")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return Result.fail("上传失败，请选择文件");
        }
        try {
            String type = FileTypeUtil.getType(file.getInputStream(), file.getOriginalFilename());
            if (!NameUtil.isImage(type)) {
                return Result.fail("文件类型错误");
            }
            int i = RETRY_COUNT;
            String newName = null;
            File newFile = null;
            for (; i > 0; i--) {
                newName = NameUtil.currentTimeMillisToUrl(type);
                newFile = new File(imaPath + "/" + newName);
                if (!newFile.exists()) {
                    break;
                }
                log.warn("文件名{}重读，第{}次尝试", newName, RETRY_COUNT - i + 1);
            }
            if (i <= 0) {
                log.error("文件名重复，重试{}次后失败", RETRY_COUNT);
                return Result.fail("上传失败，请稍后重试");
            }
            file.transferTo(newFile);
            String retUrl = request.getRequestURL().toString().replace(request.getServletPath(), "/image") + "/" + newName;
            return Result.ok(retUrl);
        } catch (IOException e) {
            log.error("写入文件出错{}", e.getMessage());
        }
        return Result.fail("上传失败!");
    }

    @GetMapping("/image/{name}")
    public void getImage(@PathVariable("name") String name, HttpServletResponse response) {
        File file = new File(imaPath + "/" + name);
        OutputStream os = null;
        FileInputStream fis = null;
        byte[] data = null;
        if (!file.exists()) {
            return;
        }
        try {
            fis = new FileInputStream(file);
            data = new byte[fis.available()];
            int read = fis.read(data);
            String type = FileTypeUtil.getType(fis, name);
            response.setContentType("image/" + type);
            os = response.getOutputStream();
            os.write(data);
            fis.close();
            os.flush();
            os.close();
        } catch (IOException e) {
            log.error("读取文件出错{}", e.getMessage());
        }
    }
}
