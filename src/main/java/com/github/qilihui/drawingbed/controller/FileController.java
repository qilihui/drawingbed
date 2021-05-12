package com.github.qilihui.drawingbed.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
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

    @Autowired
    public FileController(DrawingBedConfig drawingBedConfig) {
        this.drawingBedConfig = drawingBedConfig;
    }

    @PostMapping("/upload")
    @ResponseBody
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
            int i = drawingBedConfig.getRetryCount();
            String newName = null;
            File newFile = null;
            for (; i > 0; i--) {
                newName = NameUtil.getFileNameByThisDayTime(type);
                FileUtil.mkdir(drawingBedConfig.getPath() + date);
                newFile = new File(drawingBedConfig.getPath() + date + newName);
                if (!newFile.exists()) {
                    break;
                }
                log.warn("文件名{}重读，第{}次尝试", newName, drawingBedConfig.getRetryCount() - i + 1);
            }
            if (i <= 0) {
                log.error("文件名重复，重试{}次后失败", drawingBedConfig.getRetryCount());
                return Result.failServerErr("上传失败，请稍后重试");
            }
            file.transferTo(newFile);
            String retUrl = request
                    .getRequestURL()
                    .toString()
                    .replace(request.getServletPath(), "/image") + date + newName;
            return Result.ok(retUrl);
        } catch (IOException e) {
            log.error("写入文件出错{}", e.getMessage());
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

    private String getYearMonthDayUrl() {
        Date date = DateUtil.date();
        return DateUtil.format(date, "/yyyy/MM/dd/");
    }

    private String getYearMonthDayUrl(String year, String month, String day) {
        return "/" + year + "/" + month + "/" + day + "/";
    }
}
