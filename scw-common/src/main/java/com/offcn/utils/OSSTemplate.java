package com.offcn.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Auther: lhq
 * @Date: 2020/12/1 14:12
 * @Description: 文件上传模板类
 */
@Data
public class OSSTemplate {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String bucketDomain;


    public String upload(InputStream inputStream, String fileName) {
        //1.创建上传文件目录名称   yyyy-mm-dd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String folderName = sdf.format(new Date());
        //2.上传文件重新命名  UUID
        fileName = UUID.randomUUID().toString().replace("-", "") + "_" + fileName;
        //3.执行上传
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //3、// 上传文件流，指定bucket的名称
        ossClient.putObject(bucketName, "pic/" + folderName + "/" + fileName, inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
        //4.返回上传路径   https://scw20201201.oss-cn-beijing.aliyuncs.com/pic/test.jpg
        return "https://" + bucketDomain + "/pic/" + folderName + "/" + fileName;
    }
}
