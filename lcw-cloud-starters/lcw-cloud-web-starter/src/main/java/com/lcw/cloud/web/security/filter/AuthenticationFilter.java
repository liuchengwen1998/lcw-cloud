package com.lcw.cloud.web.security.filter;

import com.alibaba.fastjson2.JSON;
import com.google.common.net.HttpHeaders;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lcw.cloud.core.base.ErrorMap;
import com.lcw.cloud.core.constant.CommonConstants;
import com.lcw.cloud.core.exception.BizException;
import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;
import com.lcw.cloud.core.util.JWTUtils;
import com.lcw.cloud.core.util.SignUtils;
import com.lcw.cloud.web.constant.SecurityConstant;
import com.lcw.cloud.web.security.intercept.IAccessDecisionManager;
import com.lcw.cloud.web.security.session.Authentication;
import com.lcw.cloud.web.security.session.ISessionStore;
import com.lcw.cloud.web.security.session.SecurityContext;
import com.lcw.cloud.web.util.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static com.lcw.cloud.core.base.ErrorMap.*;
import static org.apache.tomcat.util.http.fileupload.FileUploadBase.MULTIPART_FORM_DATA;

/**
 * @author yzhang
 * @since 2022/8/20
 * 会话认证过滤器
 */
public class AuthenticationFilter extends OncePerRequestFilter {

    private ISessionStore sessionStore;

    /**
     * 白名单列表
     */
    private List<String> whiteList = new ArrayList<>();

    /**
     * 是否开启签名验证
     */
    private Boolean openSign = Boolean.FALSE;

    /**
     * 访问策略控制
     */
    private IAccessDecisionManager accessDecisionManager;

    public AuthenticationFilter(List<String> whiteList, IAccessDecisionManager accessDecisionManager, Boolean openSign) {
        this.whiteList.addAll(whiteList);
        this.accessDecisionManager = accessDecisionManager;
        this.openSign = openSign;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // 判断是否是白名单
        if (isWhiteListUrl(request)) {
            // 如果是白名单的话，并且不是登录接口，判断有token也解析里面的内容
            String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            String requestPath = RequestUtil.getRequestPath(request);
            if (StringUtils.isNotBlank(accessToken) && !RequestUtil.antMatch("/system/user/saveLogin", requestPath)
                    && !RequestUtil.antMatch("/open/app/thirdAppInfo/login", requestPath)
                    && !RequestUtil.antMatch("/open/app/thirdAppInfo/logout", requestPath)) {
                if (JWTUtils.verify(accessToken)) {
                    Long userId = Long.valueOf(JWTUtils.getPayloadByBase64Map(accessToken).get(SecurityConstant.USER_ID).toString());
                    String subSystemCode = JWTUtils.getPayloadByBase64Map(accessToken).get(SecurityConstant.SUB_SYSTEM_CODE).toString();
                    Authentication authentication = sessionStore.get(userId, subSystemCode);
                    if (Objects.nonNull(authentication)) {
                        // 存放token
                        storeTempAuthentication(authentication);
//                        if (StringUtils.equalsIgnoreCase(authentication.getAccessToken(), accessToken)) {
//
//                        } else {
//                            // 账号异地登录，强制下线
//                            handlerFailure(request, response, ErrorMap.ofException(ERROR_ACCOUNT_ANOTHER_PLACE_LOGIN));
//                        }
                    }
                }
            }
            chain.doFilter(request, response);
            return;
        }
        try {
            // 处理上下文请求
            authenticationAccessToken(request, response, chain);
        } catch (Exception ex) {
            // 处理失败请求
            handlerFailure(request, response, ex);
        } finally {
            // 清理上下文
            SecurityContext.clearAuthentication();
        }
    }


    private void authenticationAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(accessToken) && !RequestUtil.antMatch("/open/**", request)) {
            if (JWTUtils.verify(accessToken)) {
                Long userId = Long.valueOf(JWTUtils.getPayloadByBase64Map(accessToken).get(SecurityConstant.USER_ID).toString());
                String subSystemCode = JWTUtils.getPayloadByBase64Map(accessToken).get(SecurityConstant.SUB_SYSTEM_CODE).toString();
                Authentication authentication = sessionStore.get(userId, subSystemCode);
                if (Objects.nonNull(authentication)) {
                    // 权限认证
                    authenticated(authentication, request);
                    // 数据验签
                    HttpServletRequest wrapperRequest = signValidation(request);
                    // 继续执行
                    chain.doFilter(wrapperRequest, response);
                    return;
//                    if (StringUtils.equalsIgnoreCase(authentication.getAccessToken(), accessToken)) {
//
//                    } else {
//                        // 账号异地登录，强制下线
//                        handlerFailure(request, response, ErrorMap.ofException(ERROR_ACCOUNT_ANOTHER_PLACE_LOGIN));
//                    }
                }
            }
        } else if (StringUtils.isNotBlank(accessToken) && RequestUtil.antMatch("/open/**", request)) {
            // 处理open服务
            if (JWTUtils.verify(accessToken)) {
                String appKey = JWTUtils.getPayloadByBase64Map(accessToken).get(SecurityConstant.APP_KEY).toString();
                Long appUniqueId = Long.valueOf(JWTUtils.getPayloadByBase64Map(accessToken).get(SecurityConstant.APP_UNIQUE_ID).toString());
                Authentication authentication = sessionStore.get(appUniqueId, appKey);
                if (Objects.nonNull(authentication)) {
                    authenticatedOpen(authentication, request);
                    // 继续执行
                    chain.doFilter(request, response);
                    return;
                }
            }
        } else {
            // 判断是否有内部的lcw服务调用的token，有的话也可以放过，但是这个时候获取不了用户信息，所以如果需要调用的话也只是调用一些不需要用户信息接口
            String appToken = request.getHeader(CommonConstants.APP_HEADER);
            if (StringUtils.isNotBlank(appToken) && CommonConstants.APP_HEADER_TOKEN.equalsIgnoreCase(appToken)) {
                // 继续执行
                chain.doFilter(request, response);
                return;
            }
        }

        // 匹配失败，报错
        handlerFailure(request, response, ErrorMap.ofException(ERROR_SESSION_OVERDUE));
    }

    /**
     * 权限认证
     *
     * @param authentication
     * @param request
     */
    private void authenticated(Authentication authentication, HttpServletRequest request) {
        // 如果是内部系统调用则不校验权限,没有则走系统校验
        String appToken = request.getHeader(CommonConstants.APP_HEADER);
        if (StringUtils.isBlank(appToken) || !CommonConstants.APP_HEADER_TOKEN.equalsIgnoreCase(appToken)) {
            // 权限检查
            accessDecisionManager.decide(authentication, request);
        }

        storeTempAuthentication(authentication);
    }


    /**
     * open服务权限认证
     *
     * @param authentication
     * @param request
     */
    private void authenticatedOpen(Authentication authentication, HttpServletRequest request) {
        // 权限检查
        accessDecisionManager.decide(authentication, request);

        storeTempAuthenticationOpen(authentication);
    }

    /**
     * 数据验签
     *
     * @param request
     */
    private HttpServletRequest signValidation(HttpServletRequest request) throws IOException {
        // 如果不是合法的请求，直接跳过
        if (!whetherTrace(request)) {
            return request;
        }
        // 获取请求参数
        BodyRequestWrapper wrapperRequest = new BodyRequestWrapper(request);
        // 获取参数
        Map<String, Object> params = extractRequestParams(wrapperRequest);
        // 打印请求日志信息
        LogFactory.bizTraceLog("请求的路径为{}，请求的Ip为{},请求的参数为{}", RequestUtil.getRequestPath(request),
                RequestUtil.getRemoteAddr(request), params);

        // 如果内部系统 || 没有开启校验 || 不需要校验的接口
        String appToken = request.getHeader(CommonConstants.APP_HEADER);
        if ((StringUtils.isNotBlank(appToken) && CommonConstants.APP_HEADER_TOKEN.equalsIgnoreCase(appToken)) ||
                !openSign) {
            return wrapperRequest;
        }

        params.put(SignUtils.REQUEST_METHOD, request.getMethod());
        params.put(SignUtils.REQUEST_PATH, RequestUtil.getRequestPath(request));
        boolean flag = SignUtils.verify(params, "hz", SignUtils.REQUEST_SIGN);
        if (!flag) {
            throw new BizException(ERROR_SIGN_FAILED, ErrorMap.ofException(ERROR_SIGN_FAILED).getMessage());
        }
        return wrapperRequest;
    }


    /**
     * 提取请求里面的参数
     *
     * @param request
     * @return
     */
    private Map<String, Object> extractRequestParams(BodyRequestWrapper request) {
        HashMap<String, Object> params = new HashMap<>();
        // get请求
        if (StringUtils.equalsIgnoreCase(HttpMethod.GET.name(), request.getMethod())) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            parameterMap.forEach((key, val) -> params.put(key, val[0]));
            return params;
        } else {
            String contentType = request.getHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE);
            if (StringUtils.containsIgnoreCase(contentType, MediaType.APPLICATION_FORM_URLENCODED_VALUE)) { // application/x-www-form-urlencoded格式
                String bodyMessage = request.getBodyMessage();
                if (StringUtils.isNotBlank(bodyMessage)) {
                    String[] paramsStr = bodyMessage.split("&");
                    for (String param : paramsStr) {
                        String[] keyVal = param.split("=");
                        params.put(keyVal[0], keyVal[1]);
                    }
                }
                return params;
            } else if (StringUtils.containsIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE)) { // application/json 格式
                String requestBody = request.getBodyMessage();
                if (StringUtils.isNotBlank(requestBody)) {
                    Gson json = new Gson();
                    Type type = new TypeToken<Map<String, Object>>() {
                    }.getType();
                    Map<String, Object> fromJson = json.fromJson(requestBody, type);
                    params.putAll(fromJson);
                }
            }
        }
        return params;
    }


    /**
     * 判断是否需要进行日志跟踪； 需要记录的http请求规则如下：
     * POST 并且不是 文件上传请求；
     *
     * @param request 本次请求的request对象
     * @return 日志信息
     */
    private boolean whetherTrace(HttpServletRequest request) {
        return isRequestValid(request) && isPostAndGetRequest(request) && isNotFileUpdateRequest(request);
    }

    /**
     * 判断是否是合法的Request请求
     *
     * @param request request对象
     * @return 合法request 返回true， 否则返回false
     */
    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }


    /**
     * 判断请求是否是post或者是get请求
     *
     * @param request 本次请求的request对象
     * @return 是post请求方法true， 否则返回false
     */
    private boolean isPostAndGetRequest(HttpServletRequest request) {
        return StringUtils.equalsIgnoreCase(HttpMethod.POST.name(), request.getMethod()) ||
                StringUtils.equalsIgnoreCase(HttpMethod.GET.name(), request.getMethod());
    }

    /**
     * 判断请求是否是文件上传请求
     *
     * @param request 本次请求的request对象
     * @return 是文件上传返回false， 不是文件上传返回true
     */
    private boolean isNotFileUpdateRequest(HttpServletRequest request) {
        String contentType = request.getHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE);
        return !StringUtils.containsIgnoreCase(contentType, MULTIPART_FORM_DATA);
    }

    /**
     * 存储临时会话
     *
     * @param authentication
     */
    private void storeTempAuthenticationOpen(Authentication authentication) {
        // 放入上下文中
        SecurityContext.setAuthentication(authentication);

        // 刷新会话缓存
        sessionStore.refresh(authentication.getAppUniqueId(), authentication.getAppKey());
    }


    /**
     * 存储临时会话
     *
     * @param authentication
     */
    private void storeTempAuthentication(Authentication authentication) {
        // 放入上下文中
        SecurityContext.setAuthentication(authentication);

        // 刷新会话缓存
        sessionStore.refresh(authentication.getUserId(), authentication.getSubSystemCode());
    }


    /**
     * 处理失败请求
     *
     * @param request
     * @param response
     * @param exception
     */
    private void handlerFailure(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_401.getCode()));
        Result<String> result;
        if (exception instanceof BizException) {
            BizException ex = (BizException) exception;
            result = Result.error(ex.getCode(), ex.getMessage());
            LogFactory.bizErr(ex.getMessage());
        } else {
            LogFactory.bizErr(exception.getMessage(), exception);
            result = Result.error(ERROR_AUTHENTICATION_FAILURE, ErrorMap.getMsg(ERROR_AUTHENTICATION_FAILURE));
        }
        RequestUtil.writeToResponse(response, JSON.toJSONString(result));
    }


    public ISessionStore getSessionStore() {
        return sessionStore;
    }

    public void setSessionStore(ISessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }


    /**
     * 是否是白名单
     *
     * @param request
     * @return
     */
    private boolean isWhiteListUrl(HttpServletRequest request) {
        String requestPath = RequestUtil.getRequestPath(request);
        for (String pattern : whiteList) {
            if (RequestUtil.antMatch(pattern, requestPath)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}


