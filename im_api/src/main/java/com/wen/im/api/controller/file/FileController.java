package com.wen.im.api.controller.file;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.wen.im.api.vo.response.ApiResponse;
import com.wen.im.common.utils.DateUtils;
import com.wen.im.common.utils.FilenameUtils;
import com.wen.im.common.utils.MimeTypeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import static com.wen.im.common.utils.FilenameUtils.indexOfExtension;

/**
 * @author wenting
 */
@RestController
@RequestMapping("")
@CrossOrigin
public class FileController {

    @Value("${oss.upload_base_url:}")
    private String baseUploadUrl;
    @Value("${oss.bucket:}")
    String bucket ;
    @Value("${oss.endpoint:}")
    String endpointInternal ;
    @Value("${oss.endpoint_public:}")
    String endpointPublic ;
    @Value("${oss.accessKeyId:}")
    String accessKeyId ;
    @Value("${oss.accessKeySecret:}")
    String accessKeySecret ;

    @GetMapping("/oss/upload/url")
    public ApiResponse ossUpload(
            @RequestParam("fileName") String fileName,
            @RequestParam("scene") Integer scene
    ){
        JSONObject obj = new JSONObject();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        String finalFileName = DateUtils.dateTime() + "/" + UUID.randomUUID().toString() + "." + ext;
        String dowloadUrl = "https://" + bucket + "." +  endpointPublic + "/" + finalFileName ;
        String uploadUrl = baseUploadUrl + "/oss/upload?fileName=" + finalFileName;
        obj.put("downloadUrl", dowloadUrl);
        obj.put("uploadUrl", uploadUrl);
        return ApiResponse.success(obj);
    }

    /**
     * OSS请求
     */
    @PostMapping("/oss/upload")
    public ApiResponse uploadOSS(@RequestParam("file") MultipartFile multipartFile,
                                 @RequestParam("fileName") String fileName) throws Exception
    {
        try
        {
            // 创建OSSClient实例
            OSS ossClient = new OSSClientBuilder().build(endpointInternal, accessKeyId, accessKeySecret);
            // 填写Byte数组
            byte[] content = multipartFile.getBytes();
            // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）
            // Object完整路径中不能包含Bucket名称。
            ossClient.putObject(bucket, fileName, new ByteArrayInputStream(content));
            // 关闭OSSClient
            ossClient.shutdown();
            return ApiResponse.success();
        }
        catch (Exception e)
        {
            return ApiResponse.error(e.getMessage(), -1);
        }
    }

}
