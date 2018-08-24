
package cn.ele.core.util;

import cn.ele.core.util.wechat.MyX509TrustManager;
import cn.ele.core.util.wechat.WechatAccount;
import cn.ele.core.util.wechat.WechatInfo;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.protocol.Protocol;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ΢�Ź�����
 * @author hxt
 *
 */

public class WeChatUtil {
    public static String appid = "wxbb2fd87797352e85";
    public static String secret = "86276c239c87d65a3ff760a958a24a8d";
    // �ز��ϴ�(POST)
    private static final String UPLOAD_MEDIA = "http://api.weixin.qq.com/cgi-bin/material/add_material";
    //http://file.api.weixin.qq.com/cgi-bin/media/upload
    //https://api.weixin.qq.com/cgi-bin/material/add_material
    //https://api.weixin.qq.com/cgi-bin/media/uploadimg  �ϴ�ͼ����Ϣ�ڵ�ͼƬ��ȡURL
    //https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE  �ϴ����������ز�
    private static final String UPLOAD_IMG = "https://api.weixin.qq.com/cgi-bin/media/uploadimg";
    private static final String BATCHGET_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/batchget_material";

    /**
     * ���ACCESS_TOKEN
     * @param appid
     * @param secret
     * @return ACCESS_TOKEN
     */

    public static String getAccessToken(String appid, String secret) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        JSONObject jsonObject = httpRequest(url, "GET", null);
        try {
            if(jsonObject.getString("errcode")!=null){
                return "false";
            }
        }catch (Exception e) {
        }
        return jsonObject.getString("access_token");
    }

    /**
     * ����https���󽫷��ص�����ת��Ϊjson����
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // ����SSLContext���󣬲�ʹ������ָ�������ι�������ʼ��
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // ������SSLContext�����еõ�SSLSocketFactory����
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // ��������ʽ��GET/POST��
            httpUrlConn.setRequestMethod(requestMethod);
            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();
            // ����������Ҫ�ύʱ
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // ע������ʽ����ֹ��������
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // �����ص�������ת�����ַ���
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // �ͷ���Դ
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
        } catch (Exception e) {
        }
        return jsonObject;
    }

    /**
     * ���getUserOpenIDs
     * @param accessToken
     * @return JSONObject
     */

    public static JSONObject getUserOpenIDs(String accessToken) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+accessToken+"&next_openid=";
        return httpRequest(url, "GET", null);
    }
//12_cnio_swqV25YvxYPe0eVfWAY0g-5A1gytl7xG70Dktzj0pryMl1TN4_p6hVrPHEKLlRzxy86vvGExtzsqBsECSYkkzS1HjSpeLtO-1lLVTL64R716oScUXqJ14s8tJNGxklqy7nzzJH_xITrPJTfAEAICM
    /**
     * �Ѷ�������ת��Ϊbyte�ֽ�����
     * @param instream
     * @return byte[]
     * @throws Exception
     */

    public static byte[] readInputStream(InputStream instream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1204];
        int len = 0;
        while ((len = instream.read(buffer)) != -1){
            outStream.write(buffer,0,len);
        }
        instream.close();
        return outStream.toByteArray();
    }

    /**
     * ��Urlת���ļ�
     * @param src
     * @return
     */
    public static File UrlToFile(String src){
        if(src.contains("http://wx.jinan.gov.cn")){
            src = src.replace("http://wx.jinan.gov.cn", "C:");
            System.out.println(src);
            return new File(src);
        }
        //newһ���ļ�������������ͼƬ��Ĭ�ϱ��浱ǰ���̸�Ŀ¼
        File imageFile = new File("mmbiz.png");
        try {
            //newһ��URL����
            URL url = new URL(src);
            //������
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //��������ʽΪ"GET"
            conn.setRequestMethod("GET");
            //��ʱ��Ӧʱ��Ϊ5��
            conn.setConnectTimeout(5 * 1000);
            //ͨ����������ȡͼƬ����
            InputStream inStream = conn.getInputStream();
            //�õ�ͼƬ�Ķ��������ݣ��Զ����Ʒ�װ�õ����ݣ�����ͨ����
            byte[] data = readInputStream(inStream);
            FileOutputStream outStream = new FileOutputStream(imageFile);
            //д������
            outStream.write(data);
            //�ر������
            outStream.close();
            return imageFile;
        } catch (Exception e) {
            return imageFile;
        }
    }

    /**
     * ΢�ŷ������ز��ϴ�
     * @param file ������media
     * @param token access_token
     * @param type typeֻ֧�����������ز�(video/image/voice/thumb)
     */

    public static JSONObject uploadMedia(File file, String token, String type) {
        if(file==null||token==null||type==null){
            return null;
        }
        if(!file.exists()){
            return null;
        }
        String url = UPLOAD_MEDIA;
        JSONObject jsonObject = null;
        PostMethod post = new PostMethod(url);
        post.setRequestHeader("Connection", "Keep-Alive");
        post.setRequestHeader("Cache-Control", "no-cache");
        FilePart media = null;
        HttpClient httpClient = new HttpClient();
        //�����κ����͵�֤��
        Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);
        try {
            media = new FilePart("media", file);
            Part[] parts = new Part[] { new StringPart("access_token", token),
                    new StringPart("type", type), media };
            MultipartRequestEntity entity = new MultipartRequestEntity(parts,
                    post.getParams());
            post.setRequestEntity(entity);
            int status = httpClient.executeMethod(post);
            if (status == HttpStatus.SC_OK) {
                String text = post.getResponseBodyAsString();
                jsonObject = JSONObject.fromObject(text);
            } else {
            }
        } catch (FileNotFoundException execption) {
        } catch (HttpException execption) {
        } catch (IOException execption) {
        }
        return jsonObject;
    }

    /**
     * ΢�ŷ�������ȡ�ز��б�
     */

    public static JSONObject batchgetMaterial(String appid, String secret, String type, int offset, int count) {
        try {
            return JSONObject.fromObject(HttpsUtil.post(BATCHGET_MATERIAL+"?access_token="+ getAccessToken(appid, secret), "{\"type\":\""+type+"\",\"offset\":"+offset+",\"count\":"+count+"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * �ϴ�ͼ����Ϣ�ڵ�ͼƬ��ȡURL
     * @param file ������media
     * @param token access_token
     */

    public static JSONObject uploadImg(File file, String token) {
        if(file==null||token==null){
            return null;
        }
        if(!file.exists()){
            return null;
        }
        String url = UPLOAD_IMG;
        JSONObject jsonObject = null;
        PostMethod post = new PostMethod(url);
        post.setRequestHeader("Connection", "Keep-Alive");
        post.setRequestHeader("Cache-Control", "no-cache");
        HttpClient httpClient = new HttpClient();
        //�����κ����͵�֤��
        Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);
        try {
            Part[] parts = new Part[] { new StringPart("access_token", token), new FilePart("media", file) };
            MultipartRequestEntity entity = new MultipartRequestEntity(parts,
                    post.getParams());
            post.setRequestEntity(entity);
            int status = httpClient.executeMethod(post);
            if (status == HttpStatus.SC_OK) {
                String text = post.getResponseBodyAsString();
                jsonObject = JSONObject.fromObject(text);
            } else {
            }
        } catch (FileNotFoundException execption) {
        } catch (HttpException execption) {
        } catch (IOException execption) {
        }
        return jsonObject;
    }

    /**
     * ͼ����Ϣ����
     * @param list ͼ����Ϣ�б�
     * @param wx ΢���˺���Ϣ
     */

    public static String send(List<WechatInfo> list, WechatAccount wx){
        StringBuilder sb=new StringBuilder();
        sb.append("{\"articles\":[");
        boolean t=false;
        for(WechatInfo info:list){
            if(t)sb.append(",");
            Pattern p = Pattern.compile("src\\s*=\\s*'(.*?)'", Pattern.CASE_INSENSITIVE);
            String content = info.getWechatcontent().replace("\"", "'");
            Matcher m = p.matcher(content);
            while (m.find()) {
                String[] str = m.group().split("'");
                if(str.length>1){
                    try {
                        if(!str[1].contains("//mmbiz.")){
                            content = content.replace(str[1], uploadImg(UrlToFile(str[1]),getAccessToken(wx.getAppid(), wx.getAppkey())).getString("url"));
                        }
                    } catch (Exception e) {
                    }
                }
            }
            String o = (String) uploadMedia(new File(info.getWechatcover()), getAccessToken(wx.getAppid(), wx.getAppkey()), "thumb").get("media_id");
            System.out.println(o);
            sb.append("{\"thumb_media_id\":\""+o+"\"," +
                    "\"author\":\""+info.getWechatauthor()+"\"," +
                    "\"title\":\""+info.getWechattitle()+"\"," +
                    "\"content_source_url\":\""+info.getOriginallink()+"\"," +
                    "\"digest\":\""+info.getWechatabstract()+"\"," +
                    "\"show_cover_pic\":\""+info.getShowcover()+"\"," +
                    "\"content\":\""+content+"\"}");
            t=true;
        }
        sb.append("]}");
        JSONObject tt = httpRequest("https://api.weixin.qq.com/cgi-bin/material/add_news?access_token="+getAccessToken(wx.getAppid(), wx.getAppkey()), "POST", sb.toString());
        System.out.println(sb.toString());
        System.out.println(tt);
        /*JSONObject jo = getUserOpenIDs(getAccessToken(wx.getAppid(), wx.getAppkey()));
        System.out.println(jo);
        String outputStr = "{\"touser\":"+jo.getJSONObject("data").getJSONArray("openid")+",\"msgtype\": \"mpnews\",\"mpnews\":{\"media_id\":\""+tt.getString("media_id")+"\"}}";
        Map<String, Object> template = new HashMap<>();
        Map<String, Object> filter = new HashMap<>();
        Map<String, Object> mpnews = new HashMap<>();
        filter.put("is_to_all",true);
        template.put("filter",filter);
        mpnews.put("media_id",tt.getString("media_id"));
        template.put("mpnews",mpnews);
        template.put("msgtype","mpnews");
        outputStr = new Gson().toJson(template);
        System.out.println(outputStr);
        JSONObject post = httpRequest("https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=" + getAccessToken(wx.getAppid(), wx.getAppkey()), "POST", outputStr);
        System.out.println(post);*/
        return tt.getString("media_id");
    }
}
