package com.example.admin.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.config.Result.Result;
import com.example.admin.entity.Files;
import com.example.admin.mapper.FileMapper;
import com.example.admin.service.IFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${server.port}")
    private String port;

    @Value("${file.ip}")
    private String serverIp;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private IFileService fileService;

    @PostMapping("/del/batch")
    public Result removeByIds(@RequestBody  List<Integer> ids){
        fileService.removeBatchByIds(ids);
        return Result.success();
    }


    @GetMapping("/page")
    public Result page(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                       @RequestParam(defaultValue = "") String fileName) {
        QueryWrapper<Files> filesQueryWrapper = new QueryWrapper<>();
        filesQueryWrapper.like("name", fileName);
        return Result.success(fileService.page(new Page<>(pageNum, pageSize), filesQueryWrapper));
    }

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;
        File uploadFile = new File(System.getProperty("user.dir") + "/files/" + fileUUID);
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }
        String url;
        file.transferTo(uploadFile);
        url = "http://" + serverIp + ":"+port + "/file/" + fileUUID;
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setFileType(type);
        saveFile.setFileSize(size/(1024*1024));
        saveFile.setFileUrl(url);
        fileMapper.insert(saveFile);

        return url;
    }

    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(System.getProperty("user.dir") + "/files/" + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

}
