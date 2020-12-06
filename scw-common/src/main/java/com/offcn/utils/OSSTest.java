package com.offcn.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.FileInputStream;
import java.io.InputStream;


public class OSSTest {

    public static void main(String[] args) throws Exception {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI4GCci8mMWdM8F6Zebpic";
        String accessKeySecret = "lqMV0BlzpmkMOYQcp1D6EjwaYGKSFl";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        InputStream inputStream = new FileInputStream("C:\\Users\\Administrator\\Desktop\\upload\\1.jpg");
        ossClient.putObject("scw2020-1201-dbq", "pic/test.jpg", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
