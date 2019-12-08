package orj.worf.scheduler.report.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {
    static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final int HTTP_CONNECTION_TIMEOUT = 60 * 1000;
    private static final int HTTP_READ_TIMEOUT = 60 * 1000;
    private static final String gateway_SOURCE = "source=alligator";

    /**
     * 获取当前网络ip
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return
     */
    public static String sendGet(String url, String param) {
        return sendGet(url, param, HTTP_CONNECTION_TIMEOUT, HTTP_READ_TIMEOUT);
    }

    /**
     * 
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return
     */
    public static String sendGet(String url, String param, int timeOut) {
        return sendGet(url, param, timeOut, timeOut);
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, int connectionTimeOut, int readTimeOut) {
        String result = "";
        BufferedReader in = null;
        String urlNameString = url + "?";
        try {
            if (!StringUtils.isEmpty(param)) {
                urlNameString += param + "&";
            }
            urlNameString += gateway_SOURCE;
            logger.info("请求url+参数：" + urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setConnectTimeout(connectionTimeOut);
            connection.setReadTimeout(readTimeOut);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！url: " + urlNameString + ", " + e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("close exception", e2);
            }
        }
        return result;
    }

    public static String sendGet4CRM(String url, String data) throws Exception {
        int connectionTimeOut = 1200;
        int readTimeOut = 1200;
        String result = "";
        BufferedReader in = null;
        try {
            data = URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            logger.error("编码CRM  PARAM异常 ");
        }
        try {
            if (!StringUtils.isEmpty(data)) {
                url += data;
            }
            logger.info("请求url+参数：" + url);
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setConnectTimeout(connectionTimeOut);
            connection.setReadTimeout(readTimeOut);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！url: " + url + ", " + e);
            throw e;
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("close exception", e2);
            }
        }
        return result;
    }

    /**
     * 向指定URL发送GET方法的请求=>专门针对想ERP发送的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet4ERP(String url, String param) {
        int connectionTimeOut = HTTP_CONNECTION_TIMEOUT;
        int readTimeOut = HTTP_READ_TIMEOUT;
        String result = "";
        BufferedReader in = null;
        String urlNameString = url + "?";
        try {
            if (!StringUtils.isEmpty(param)) {
                urlNameString += param;
            }
            logger.info("请求url+参数：" + urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setConnectTimeout(connectionTimeOut);
            connection.setReadTimeout(readTimeOut);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！url: " + urlNameString + ", " + e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("close exception", e2);
            }
        }
        return result;
    }

    /**
     * 
     * @param url
     * @param contextType  "image/jpeg","application/Json"
     * @return
     */
    public static byte[] sendHttpsGetUrl(String url, String contextType) {

        // 响应内容
        byte[] bs = null;

        // 创建默认的httpClient实例
        HttpClient httpClient = new DefaultHttpClient();
        // HttpClient httpClient = httpClient1;

        // 创建TrustManager
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
        };
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");

            // 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
            ctx.init(null, new TrustManager[] { xtm }, null);

            SSLSocketFactory sf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme sch = new Scheme("https", 443, sf);
            httpClient.getConnectionManager().getSchemeRegistry().register(sch);
            // 创建HttpPost
            HttpGet httpPost = new HttpGet(url);
            httpPost.setHeader("content-type", contextType);
            // 执行POST请求
            HttpResponse response = httpClient.execute(httpPost);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            bs = IOUtils.toByteArray(entity.getContent());
            if (null != entity) {
                EntityUtils.consume(entity); // Consume response content
            }
            return bs;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            // httpClient.getConnectionManager().shutdown();
        }
        return bs;
    }

    public static String sendJSONPost(String url, String param) {
        return sendPost(url, param, "application/json", "application/json");
    }

    public static String sendJSONPost(String url, String param, int timeOut) {
        return sendPost(url, param, "application/json", "application/json", timeOut, timeOut, true);
    }

    public static String sendPost(String url, String param) {
        return sendPost(url, param, "*/*", "application/json");
    }

    public static String sendPost(String url, String param, String accept, String contentType) {
        return sendPost(url, param, accept, contentType, HTTP_CONNECTION_TIMEOUT, HTTP_READ_TIMEOUT, true);

    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, String accept, String contentType, int connectionTimeOut,
            int readTimeOut, boolean isLogger) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            if (isLogger) {
                logger.info("请求url：" + url);
                logger.info("请求参数：" + param);
            }
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", "application/json");
            conn.setRequestProperty("connection", "Keep-Alive");
            // 前端确认通过请求头放session_token来处理
            // conn.setRequestProperty("session_token", "Jt31Sp4n3Q0kdrxo9cyPHCsbnXePF0Fx98CsWkS6FxA=");
            conn.setConnectTimeout(connectionTimeOut);
            conn.setReadTimeout(readTimeOut);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！url: " + url + ",参数: " + param + "," + e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                logger.error("IO exception", ex);
            }
        }
        return result;
    }

    public static String sendPostNoLogger(String url, String param) {
        return sendPost(url, param, "application/json", "application/json", HTTP_CONNECTION_TIMEOUT, HTTP_READ_TIMEOUT,
                false);
    }
}
