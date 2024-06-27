package com.lcw.cloud.web.util;

import com.alibaba.fastjson.JSON;
import com.lcw.cloud.core.base.ErrorMap;
import com.lcw.cloud.core.logger.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.FileCopyUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.lcw.cloud.core.base.ErrorMap.ERROR_COMMON_PARAMS_ERROR;

/**
 * Request对象操作相关工具类
 *
 * @author yzhang
 * @since 1.0
 */
public class RequestUtil {

    private static final AntPathMatcher matcher = new AntPathMatcher();


    /**
     * 从request对象中获取请求参数，注意这里使用的 {@code HttpServletRequest#getParameter(String)}
     * 只能从QueryParams及FormData 中获取参数，无法从body中获取参数
     *
     * @param request {@code HttpServletRequest} 对象
     * @param key     参数名
     * @return String 参数值，如果未获取到 返回null
     * @see HttpServletRequest#getParameter(String)
     */
    public static final String getParam(HttpServletRequest request, String key) {
        return request.getParameter(key);
    }


    /**
     * 从 request的body中获取参数， 支持Json格式的数据; 先从头信息中判断body格式
     * 注意body参数只能获取一次，再后续方法中将无法获取。
     *
     * @param request {@code HttpServletRequest} 对象
     * @return
     */
    public static Map<String, Object> getParamFromBody(HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        // 从header中获取content-type内容,只要包含application/json
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (StringUtils.containsIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE)) {
            try (
                    ServletInputStream inputStream = request.getInputStream();
            ) {
                StringBuilder content = new StringBuilder();
                byte[] b = new byte[1024];
                int lens;
                while ((lens = inputStream.read(b)) > 0) {
                    content.append(new String(b, 0, lens));
                }
                body = JSON.parseObject(content.toString(), Map.class);
            } catch (Exception e) {
                LogFactory.bizErr("解析body参数错误", e);
                throw ErrorMap.ofException(ERROR_COMMON_PARAMS_ERROR);
            }
        }
        return body;
    }


    /**
     * Ant方式匹配URL
     *
     * @param patten 模式
     * @return
     */
    public static Boolean antMatch(String patten, HttpServletRequest request) {
        return matcher.match(patten, getRequestPath(request));

    }

    /**
     * 匹配URL是否符合要求
     *
     * @param patten  模板
     * @param realUrl 真实URL
     * @return
     */
    public static Boolean antMatch(String patten, String realUrl) {
        return matcher.match(patten, realUrl);

    }

    public static String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            url = org.springframework.util.StringUtils.hasLength(url) ? url + pathInfo : pathInfo;
        }
        return url;
    }

    /**
     * 获取客户端IP地址
     *
     * @param request
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }


    /**
     * 往response写出数据
     *
     * @param response   {@code HttpServletResponse} 对象，返回数据给前端
     * @param jsonString 写出的数内容
     * @throws IOException 写出数据发送错误时抛出异常
     */
    public static void writeToResponse(HttpServletResponse response, String jsonString) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (
                ServletOutputStream out = response.getOutputStream();
        ) {
            out.write(jsonString.getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
    }


    /**
     * 服务器下载文件
     *
     * @param inFileName 下载的文件名称
     * @param ins        下载的文件流
     * @return
     */
    public static ResponseEntity<InputStreamResource> downloadInputStreamFile(String inFileName, InputStream ins) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        final String fileName = new String(inFileName.replace(" ", "_").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/octet-stream;charset=utf-8"))
                .body(new InputStreamResource(ins));

    }

    /**
     * 下载文件
     *
     * @param response
     * @param inputStream
     * @param inFileName
     */
    public static void downloadFile(HttpServletResponse response, InputStream inputStream, String inFileName) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/octet-stream;charset=utf-8");
            String fileName = new String(inFileName.replace(" ", "_").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            FileCopyUtils.copy(inputStream, out);
        } catch (Exception var9) {
            var9.printStackTrace();
            throw new RuntimeException("下载文件失败");
        } finally {
            if (Objects.nonNull(out)) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载文件reponse参数设置
     *
     * @param response
     * @param inFileName
     */
    public static void downloadResponseBuild(HttpServletResponse response, String inFileName) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/octet-stream;charset=utf-8");
            String fileName = new String(
                    inFileName.replace(" ", "_").getBytes(StandardCharsets.UTF_8),
                    StandardCharsets.ISO_8859_1);
            response.setHeader("Content-Disposition",
                    String.format("attachment; filename=\"%s\"", fileName));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
        } catch (Exception var9) {
            var9.printStackTrace();
            throw new RuntimeException("下载文件失败");
        } finally {
            if (Objects.nonNull(out)) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载文件
     *
     * @param response
     * @param file
     */
    public static void downloadFile(HttpServletResponse response, File file) {
        final String inFileName = file.getName();
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/octet-stream;charset=utf-8");
            String fileName = new String(inFileName.replace(" ", "_").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            FileCopyUtils.copy(new FileInputStream(file), out);
        } catch (Exception var9) {
            var9.printStackTrace();
            throw new RuntimeException("下载文件失败");
        } finally {
            if (Objects.nonNull(out)) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载文件,下载Excel文件，这个后面再补充依赖
     *
     * @param response
     * @param workbook
     * @param inFileName
     */
    /*public static void downloadFile(HttpServletResponse response, Workbook workbook, String inFileName) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/octet-stream;charset=utf-8");
            String fileName = new String(inFileName.replace(" ", "_").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            workbook.write(out);
        } catch (Exception var8) {
            var8.printStackTrace();
            throw new RuntimeException("下载文件失败");
        } finally {
            try {
                if (Objects.nonNull(out))
                    out.close();
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }*/


}
