package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.FileRecord;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.service.FileRecordService;
import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.req.FileRecordReq;
import com.jingliang.mall.resp.FileRecordResp;
import com.jingliang.mall.server.FastdfsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 文件Controller
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-24 10:59
 */
@RequestMapping("/back/file")
@RestController
@Slf4j
@Api(tags = "文件")
public class FileController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final FileRecordService fileRecordService;
    private final FastdfsService fastdfsService;

    public FileController(FileRecordService fileRecordService, FastdfsService fastdfsService) {
        this.fileRecordService = fileRecordService;
        this.fastdfsService = fastdfsService;
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public MallResult<FileRecordResp> uploadFile(MultipartFile file, @ApiIgnore HttpSession session) throws IOException {
        log.debug("请求参数：{}", file);
        //1.保存到到文件服务器
        //2.保存上传记录到数据库
        String fileUri = fastdfsService.uploadFile(file.getInputStream(), MallUtils.getExtName(file.getOriginalFilename()));
        FileRecordReq fileRecordReq = new FileRecordReq();
        fileRecordReq.setFileName(file.getOriginalFilename());
        fileRecordReq.setFileSize(file.getSize());
        fileRecordReq.setFileUri(fileUri);
        fileRecordReq.setType(100);
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(fileRecordReq, user);
        FileRecordResp fileUploadRecordResp = MallBeanMapper.map(fileRecordService.save(MallBeanMapper.map(fileRecordReq, FileRecord.class)), FileRecordResp.class);
        log.debug("返回结果：{}", fileUploadRecordResp);
        return MallResult.buildSaveOk(fileUploadRecordResp);
    }

    /**
     * 文件删除
     */
    @PostMapping("/delete")
    @ApiOperation(value = "文件删除")
    public MallResult<FileRecordResp> deleteFile(String fileUri, @ApiIgnore HttpSession session) throws IOException {
        log.debug("请求参数：{}", fileUri);
        //1.保存到到文件服务器
        //2.保存上传记录到数据库
        fastdfsService.deleteFile(fileUri);
        FileRecordReq fileRecordReq = new FileRecordReq();
        fileRecordReq.setFileUri(fileUri);
        fileRecordReq.setType(200);
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(fileRecordReq, user);
        FileRecordResp fileUploadRecordResp = MallBeanMapper.map(fileRecordService.save(MallBeanMapper.map(fileRecordReq, FileRecord.class)), FileRecordResp.class);
        log.debug("返回结果：{}", fileUploadRecordResp);
        return MallResult.buildSaveOk(fileUploadRecordResp);
    }


}
