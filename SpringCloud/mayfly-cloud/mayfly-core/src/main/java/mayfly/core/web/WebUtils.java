package mayfly.core.web;

import mayfly.core.exception.BizAssert;
import mayfly.core.util.IOUtils;
import mayfly.core.util.JsonUtils;
import mayfly.core.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author meilin.huang
 * @date 2021-05-12 3:09 下午
 */
public class WebUtils {

    /**
     * 响应json对象
     *
     * @param response response
     * @param obj      响应对象
     */
    public static void writeJson(HttpServletResponse response, Object obj) {
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        try {
            response.getWriter().write(JsonUtils.toJSONString(obj));
            response.flushBuffer();
        } catch (Exception e) {
            throw BizAssert.newException("响应json异常: %s", e);
        }
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文件流下载，自动关闭输入流
     *
     * @param response response
     * @param in       输入流
     */
    public static void downloadStream(HttpServletResponse response, String filename, InputStream in) {
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        // 设置文件名
        response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
        try {
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            throw BizAssert.newException("文件下载-读取字节失败");
        } finally {
            IOUtils.close(in);
        }
    }

    /**
     * 文件流下载，适合数据量较小
     *
     * @param response response
     * @param bytes    文件字节数组
     */
    public static void downloadStream(HttpServletResponse response, String filename, byte[] bytes) {
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        // 设置文件名
        response.setHeader("Content-Disposition", "attachment;fileName=" + filename);

        byte[] buffer = new byte[1024];
        BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(bytes));
        try {
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            throw BizAssert.newException("文件下载-读取字节失败");
        } finally {
            IOUtils.close(bis);
        }
    }

    /**
     * 获取请求ip地址
     *
     * @param request request
     * @return ip
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }
        return ip;
    }

    /**
     * 获取请求ip地址
     *
     * @return ip
     */
    public static String getRequestIp() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : getRequestIp(((ServletRequestAttributes) requestAttributes).getRequest());
    }
}
