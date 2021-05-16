package com.github.qilihui.drawingbed.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.qilihui.drawingbed.annotation.RateLimiter;
import com.github.qilihui.drawingbed.config.DrawingBedConfig;
import com.github.qilihui.drawingbed.util.NameUtil;
import com.github.qilihui.drawingbed.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * @author qilihui
 * @date 2021/4/18 21:47
 */
@Controller
@Slf4j
public class FileController {

    private final DrawingBedConfig drawingBedConfig;

    private final File tmpDirectory;

    @Autowired
    public FileController(DrawingBedConfig drawingBedConfig) {
        this.drawingBedConfig = drawingBedConfig;
        tmpDirectory = new File(this.drawingBedConfig.getPath() + "/tmp");
        FileUtil.mkdir(tmpDirectory);
    }

    @PostMapping("/upload")
    @ResponseBody
    @RateLimiter(max = 5, key = "uploadLimit")
    public Result upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file == null || file.isEmpty()) {
            return Result.failImgNoExist("上传失败，请选择文件");
        }
        String date = getYearMonthDayUrl();
        try {
            String type = FileTypeUtil.getType(file.getInputStream(), file.getOriginalFilename());
            if (!NameUtil.isImage(type)) {
                return Result.failImgTypeErr("文件类型错误");
            }
            File tempFile = File.createTempFile(IdUtil.randomUUID(), "." + type, tmpDirectory);
            file.transferTo(tempFile);
            String md5 = SecureUtil.md5(tempFile);
            boolean delete = tempFile.delete();
            String newName = md5 + "." + type;
            FileUtil.mkdir(drawingBedConfig.getPath() + date);
            File newFile = new File(drawingBedConfig.getPath() + date + newName);
            if (!newFile.exists()) {
                file.transferTo(newFile);
                log.info("上传文件:{}", date + newName);
            }
            String retUrl = request
                    .getRequestURL()
                    .toString()
                    .replace(request.getServletPath(), "/image") + date + newName;
            return Result.ok(retUrl);
        } catch (IOException e) {
            log.error("写入文件出错:{}", e.getMessage());
        }
        return Result.failServerErr("上传失败!");
    }

    @GetMapping("/image/{year}/{month}/{day}/{name}")
    public void getImage(@PathVariable("name") String name,
                         @PathVariable("year") String year,
                         @PathVariable("month") String month,
                         @PathVariable("day") String day,
                         HttpServletResponse response) {
        File file = new File(drawingBedConfig.getPath() + getYearMonthDayUrl(year, month, day) + name);
        OutputStream os;
        FileInputStream fis;
        byte[] data;
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

    private String getYearMonthDayUrl() {
        Date date = DateUtil.date();
        return DateUtil.format(date, "/yyyy/MM/dd/");
    }

    private String getYearMonthDayUrl(String year, String month, String day) {
        return "/" + year + "/" + month + "/" + day + "/";
    }
}
