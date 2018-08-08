package net.slipp.fifth.kotlin.web.support.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(annotations = { Controller.class })
public class GlobalExceptionHandler {
    private static final String VIEW_404_NOT_FOUND = "error/404";
    private static final String VIEW_DEFAULT_ERROR = "error/default-error";
    private static final String ATTR_REFERER = "referer";
    private static final String MODEL_URL = "url";
    private static final String MODEL_EXCEPTION = "exception";

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    @ExceptionHandler(value = Exception.class)
    public ModelAndView exceptionHandler(Exception exception, HttpServletRequest request) {
        log.error("Referer URL: {}, Exception: {}", request.getHeader(ATTR_REFERER), exception);

        ModelAndView mav = createModelAndView(exception);

        setMessage(exception, mav);

        mav.addObject(MODEL_URL, request.getHeader(ATTR_REFERER));
        return mav;
    }

    private ModelAndView createModelAndView(Exception exception) {
        String viewName = VIEW_DEFAULT_ERROR;

        if (exception instanceof NullPointerException) {
            viewName = VIEW_404_NOT_FOUND;
        }

        return new ModelAndView(viewName);
    }

    private void setMessage(Exception exception, ModelAndView mav) {
        try {
            mav.addObject(MODEL_EXCEPTION, messageSourceAccessor.getMessage(exception.getMessage()));
        } catch (NoSuchMessageException nsme) {
            mav.addObject(MODEL_EXCEPTION, "Occur Excpetion: " + exception.getMessage());
        }
    }
}
