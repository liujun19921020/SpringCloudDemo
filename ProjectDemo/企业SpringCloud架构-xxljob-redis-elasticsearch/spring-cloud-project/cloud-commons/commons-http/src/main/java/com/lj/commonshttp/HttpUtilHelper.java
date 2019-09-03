package com.lj.commonshttp;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtilHelper {

    private static final String USER_AGENT = "Mozilla/5.0";

    /**
     * post 请求
     * @param url  请求地址
     * @param nvps 参数列表
     * @return
     * @throws
     */
    public static String postHttp(String url, Map<String,String> header, List<NameValuePair> nvps) throws Exception {
        String responseStr = null;
        HttpPost httpPost = new HttpPost(url);
        //重要！！必须设置 http 头，否则返回为乱码
        httpPost.setHeader("User-Agent", USER_AGENT);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            httpPost.setHeader(entry.getKey(),entry.getValue());
        }
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            // 重要！！ 指定编码，对中文进行编码
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8")));
            Integer statusCode = 0;
            while (statusCode.equals(0)) {

                //设置连接超时时间 、 设置 请求读取数据的超时时间 、 设置从connect Manager获取Connection超时时间、
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(15000)
                        .setSocketTimeout(15000)
                        .setConnectionRequestTimeout(15000)
                        .build();
                httpPost.setConfig(requestConfig);

                //改成 try-with-resources
                try (CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
                    if (response2.getStatusLine().getStatusCode() == 200) {
                        statusCode = 200;
                        HttpEntity entity2 = response2.getEntity();
                        responseStr = EntityUtils.toString(entity2);
                        EntityUtils.consume(entity2);
                    } else {
                        throw new Exception("http error : " + response2.getStatusLine().getStatusCode()
                                + "  ======== msg : " +response2.getStatusLine().getReasonPhrase());
                    }
                }

            }
        }
        return responseStr;
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", "BAIDUID=B03D18E5E6CE316D6EA36D808D823CA7:FG=1");
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("title", "国庆节放假通知"));
        nvps.add(new BasicNameValuePair("indextype", "manht"));
        nvps.add(new BasicNameValuePair("bsToken", "965cc5be0597788aa0030da243f7975e"));
        nvps.add(new BasicNameValuePair("_req_seqid", "0xc51f9d43000c9afe"));
        nvps.add(new BasicNameValuePair("sid", "1425_21108_20697_29523_29519_29721_29568_29220_29640"));
        //请求
        String str = HttpUtilHelper.postHttp("https://www.baidu.com/home/news/submit/mannewsview", header, nvps);
        System.out.println("----resp ---->"+str);
        System.out.println("----respObject ---->"+ JSONObject.parseObject(str,Map.class));
    }
}
