package com.lcw.cloud.web.exception;

import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;
import com.lcw.cloud.web.exception.notice.NoticeToOutContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局controller异常处理类,处理非rest接口异常
 *
 * @author ：yzhang
 * @since ：2022/2/11 13:58
 */
@Controller
@ConditionalOnMissingBean(GlobalExceptionController.class)
public class GlobalExceptionController extends AbstractErrorController {

    @Resource
    private NoticeToOutContext noticeToOutContext;

    public GlobalExceptionController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result error(HttpServletRequest request, HttpServletResponse response) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        switch (statusCode) {
            case 404:
                response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_406.getCode()));
                // 消息往外暴露
                noticeToOutContext.notice(HttpStatus.HTTP_STATUS_406.getCode(), HttpStatus.HTTP_STATUS_406.getMessage());
                return HttpStatus.HTTP_STATUS_406.result();
            case 403:
                response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_403.getCode()));
                return HttpStatus.HTTP_STATUS_403.result();
            default:
                response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_500.getCode()));
                // 消息往外暴露
                noticeToOutContext.notice(HttpStatus.HTTP_STATUS_500.getCode(), HttpStatus.HTTP_STATUS_500.getMessage());
                return HttpStatus.HTTP_STATUS_500.result();
        }
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
