package com.offcn.user.component;


import com.offcn.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: lhq
 * @Date: 2020/11/19 10:12
 * @Description: 短信服务的工具类
 */
@Component
public class SmsTemplate {


    @Value("${sms.appcode}")
    private String appcode;
    @Value("${sms.tpl_id}")
    private String tpl_id;
    @Value("${sms.host}")
    private String host ;
    @Value("${sms.path}")
    private String path ;
    @Value("${sms.method}")
    private String method ;

    public String sendSms(String mobile, String code) {
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", mobile);
        querys.put("param", "code:" + code);
        querys.put("tpl_id", tpl_id);
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
