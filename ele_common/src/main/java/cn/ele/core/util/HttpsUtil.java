package cn.ele.core.util;

import cn.ele.core.util.wechat.MyX509TrustManager;
import org.apache.commons.lang.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;


/**
 * https ���� ΢��Ϊhttps������
 * @author stone
 *
 */
public class HttpsUtil {

    /**
     * Ĭ�ϱ���
     */
    private static final String DEFAULT_CHARSET = "UTF-8";


    /**
     * HTTPS ��get ����
     * @param url
     * @return
     */
    public static String get(String url) {
        StringBuffer bufferRes = null;
        try {
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // ������SSLContext�����еõ�SSLSocketFactory����
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL urlGet = new URL(url);
            HttpsURLConnection http = (HttpsURLConnection) urlGet.openConnection();
            // ���ӳ�ʱ
            http.setConnectTimeout(25000);
            // ��ȡ��ʱ --��������Ӧ�Ƚ���������ʱ��
            http.setReadTimeout(25000);
            http.setRequestMethod("GET");
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setSSLSocketFactory(ssf);
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();

            InputStream in = http.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
            String valueString = null;
            bufferRes = new StringBuffer();
            while ((valueString = read.readLine()) != null){
                bufferRes.append(valueString);
            }
            in.close();
            if (http != null) {
                // �ر�����
                http.disconnect();
            }
            return bufferRes.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get����https
     * @param url
     * @param params
     * @return
     */
    public static String get(String url, Map<String, String> params) {
        return get(initParams(url, params));
    }

    /**
     * HTTPS ��POST ����
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, String params) {
        StringBuffer bufferRes = null;
        try {
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // ������SSLContext�����еõ�SSLSocketFactory����
            SSLSocketFactory ssf = sslContext.getSocketFactory();


            URL urlGet = new URL(url);
            HttpsURLConnection http = (HttpsURLConnection) urlGet.openConnection();
            // ���ӳ�ʱ
            http.setConnectTimeout(25000);
            // ��ȡ��ʱ --��������Ӧ�Ƚ���������ʱ��
            http.setReadTimeout(25000);
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setSSLSocketFactory(ssf);
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();


            OutputStream out = http.getOutputStream();
            out.write(params.getBytes("UTF-8"));
            out.flush();
            out.close();


            InputStream in = http.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
            String valueString = null;
            bufferRes = new StringBuffer();
            while ((valueString = read.readLine()) != null){
                bufferRes.append(valueString);
            }
            in.close();
            if (http != null) {
                // �ر�����
                http.disconnect();
            }
            return bufferRes.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * �����������
     * @param url
     * @param params
     * @return
     */
    public static String initParams(String url, Map<String, String> params){
        if (null == params || params.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.indexOf("?") == -1) {
            sb.append("?");
        } else {
            sb.append("&");
        }
        boolean first = true;
        for (Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=");
            if (StringUtils.isNotEmpty(value)) {
                try {
                    sb.append(URLEncoder.encode(value, DEFAULT_CHARSET));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
