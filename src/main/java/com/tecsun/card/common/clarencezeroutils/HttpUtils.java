package com.tecsun.card.common.clarencezeroutils;

import com.alibaba.fastjson.JSONObject;
import com.tecsun.card.exception.HttpNetWorkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author 0214
 * @description HTTP 方法封装
 * @createTime 2018/10/11
 */
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    /**
     * 请求方ID
     */
    public static final String SENDER_ID = "senderId";
    /**
     * 服务方ID
     */
    public static final String SERVICE_ID = "serviceId";
    /**
     * 授权信息
     */
    public static final String AUTHORIZE_INFO = "authorizeInfo";
    /**
     * 请求的webService接口方法
     */
    public static final String METHOD = "method";
    /**
     * 请求的webService接口方法的参数
     */
    public static final String METHODPARAMETER = "params";
    /**
     * 操作信息
     */
    public static final String OPERATE = "operate";

    /**
     * POST方式调用公安接口
     *
     * @param jsonObject
     * @param url
     * @return
     */
    public static String post(JSONObject jsonObject, String url) throws IOException {
        InputStream           in     = null;
        ByteArrayOutputStream barray = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            conn.setConnectTimeout(10000);
            // 单位: 毫秒
            conn.setReadTimeout(120000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.write(jsonObject.toString().getBytes("utf-8"));
            // 接收返回信息
            in = new DataInputStream(conn.getInputStream());
            byte[] array = new byte[4096];
            int    count = -1;
            barray = new ByteArrayOutputStream();
            while (-1 != (count = in.read(array))) {
                barray.write(array, 0, count);
            }
            byte[] data = barray.toByteArray();
            barray.close();
            String ret = new String(data, "utf-8");
            return ret;
        } catch (UnsupportedEncodingException e) {
            logger.info("[0214 POST接口调用出错],接口入参: {}, 接口URL: {}, 原因: {}", jsonObject.toJSONString(), url, e);
        } catch (ProtocolException e) {
            logger.info("[0214 POST接口调用出错],接口入参: {}, 接口URL: {}, 原因: {}", jsonObject.toJSONString(), url, e);
        } catch (MalformedURLException e) {
            logger.info("[0214 POST接口调用出错],接口入参: {}, 接口URL: {}, 原因: {}", jsonObject.toJSONString(), url, e);
        } catch (IOException e) {
            logger.info("[0214 POST接口调用出错],接口入参: {}, 接口URL: {}, 原因: {}", jsonObject.toJSONString(), url, e);
            throw new HttpNetWorkException("[0214 POST接口调用出错],接口入参: " + jsonObject.toJSONString() + ", 接口URL: " + url);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (barray != null) {
                try {
                    barray.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 调用httpPost获取结果集
     *
     * @param senderId        请求方ID
     * @param serviceId       服务方ID
     * @param method          请求的方法名称
     * @param authorizeInfo   授权信息
     * @param operate         操作信息
     * @param methodParameter 方法的参数
     * @param url             总线节点路径
     * @return
     */
    public static String post(String serviceId,
                       String senderId,
                       String method,
                       String authorizeInfo,
                       String operate,
                       JSONObject methodParameter,
                       String url) throws Exception {
        JSONObject jObject = new JSONObject();
        jObject.put(SERVICE_ID, serviceId);
        jObject.put(SENDER_ID, senderId);
        jObject.put(AUTHORIZE_INFO, authorizeInfo);
        jObject.put(METHOD, method);
        jObject.put(METHODPARAMETER, methodParameter);
        jObject.put(OPERATE, operate);
        return post(jObject, url);
    }

}
