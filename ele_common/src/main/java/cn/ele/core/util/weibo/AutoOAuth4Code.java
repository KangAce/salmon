package cn.ele.core.util.weibo;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import weibo4j.Oauth;
import weibo4j.http.AccessToken;
import weibo4j.util.WeiboConfig;

/**
 * Title: TRS ����Э��ƽ̨��TRS WCM�� <BR>
 * Description: �Զ����������˺���Ȩ<BR>
 * <BR>
 * Copyright: Copyright (c) 2004-2013 �����ض�˼��Ϣ�����ɷ����޹�˾ <BR>
 * Company: www.trs.com.cn <BR>
 * 
 * @author lky
 * @version 1.0
 */
public class AutoOAuth4Code {

    // ��־��¼��
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
            .getLogger(AutoOAuth4Code.class);

    /**
     * ����ת��ַ�л�ȡcode��ֵ
     * 
     * @param location
     *            ��תurl
     * @return code��ֵ
     */
    private static String getCodeFromLocation(String location) {
        int begin = location.indexOf("code=");
        return location.substring(begin + 5);
    }

    /**
     * 
     * @param username
     *            �û���
     * @param password
     *            ����
     * @return token����
     */
    public static AccessToken AccessTokenRefresh(String username, String password) {
        AccessToken access_token = null;
        String location = "";
        String code = "";
        Oauth oauth = new Oauth();
        HttpClient client = new HttpClient();
        WeiboLogin wl = new WeiboLogin();
        wl.setUsername(username);
        wl.setPassword(password);
        String url = null;
        try {
            // �����Ȩurl
            url = oauth.authorize("code","");

            // ģ���¼�Ĵ��루�ж�΢���˺��Զ���¼�Ƿ��¼�ɹ���
            boolean isLogined = wl.login();
            if (!isLogined) {
                logger.error("�û������������");
                return null;
            }

            // ��ȡ��¼cookie
            String cookie = wl.getCookie();
           
            // �Զ�������Ȩҳ��
            HttpMethod method = new PostMethod(url);
            method.addRequestHeader("Cookie", cookie);
            int statusCode = client.executeMethod(method);
           
            // ����Ȩ���ģ�ˢ������token����ʱ��
            if (302 == statusCode) {

                // ��ȡ��תurl
                logger.debug("����Ȩ�����˺ţ�ˢ������token����ʱ��");
                location = method.getResponseHeader("Location").getValue();

                // ����תurl��ץ��code
                code = getCodeFromLocation(location);

                // ʹ��code��ȡaccess token
                access_token = oauth.getAccessTokenByCode(code);
            }
            else if(200 == statusCode){        // δ��Ȩ����Ҫģ����Ȩ����
                logger.debug("δ��Ȩ�����˺ţ���Ҫģ����Ȩ����");

                // �Զ���װ�ύ����ҳ��
                Document html = Jsoup.parse(method.getResponseBodyAsString());
                Elements params = html.select("form[name=authZForm] > input[type=hidden]");
                PostMethod post = new PostMethod("https://api.weibo.com/oauth2/authorize");
               
                // ��������ͷ
                post.addRequestHeader("Cookie", cookie);
                post.addRequestHeader("Referer", "https://api.weibo.com/oauth2/authorize?client_id=" +
                                            WeiboConfig.getValue("client_ID").trim() +
                                            "&redirect_uri=" +
                                            WeiboConfig.getValue("redirect_URI").trim() +
                                            "&response_type=code");
                // ���post����ĸ�������
                for(Element param : params) {
                    post.addParameter(param.attr("name"), param.attr("value"));
                }

                // �ύ�����ȡ����״̬
                statusCode = client.executeMethod(post);
               
                // 302 ��ת���ʾpost�ɹ�
                if(302 == statusCode) {
                    // ��ȡ��תurl
                    location = post.getResponseHeader("Location").getValue();
                   
                    // ��ȡcode��ȡaccess_token
                    code = getCodeFromLocation(location);
                    access_token = oauth.getAccessTokenByCode(code);
                } else {
                    logger.debug("δ��ȡ����ת״̬����Ȩʧ�ܣ�");
                    return null;
                }
            } else {
                logger.debug("δ��ȡ����ȷ��ת״̬����Ȩʧ�ܣ�");
                return null;
            }
        } catch (Exception e) {
            logger.error("δ��ȡ����ȷ��ת״̬����Ȩʧ�ܣ�");
        }
        return access_token;
    }

    /**
     * ���Ի�ȡAccessToken�Ƿ���ȷ
     * 
     * @param args
     */
    public static void main(String[] args) {

        // ��Ҫ��ȷ�������˺ŵ��û���������
        String username = "18710058144";
        String password = "gyw19870919";
        try {
            AccessToken oAccessToken = AutoOAuth4Code.AccessTokenRefresh(username, password);

            System.out.println("��ȡ���ģ�"+oAccessToken.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }
}