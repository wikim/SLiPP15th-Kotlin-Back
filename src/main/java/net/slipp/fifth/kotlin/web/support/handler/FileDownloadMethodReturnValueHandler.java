package net.slipp.fifth.kotlin.web.support.handler;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import net.slipp.fifth.kotlin.web.support.view.FileDownloadView;
import net.slipp.fifth.kotlin.web.support.view.FileDownloadView.FileDownload;

public class FileDownloadMethodReturnValueHandler implements HandlerMethodReturnValueHandler, InitializingBean {

    private FileDownloadView fileDownloadView;

    public FileDownloadMethodReturnValueHandler(FileDownloadView fileDownloadView) {
        this.fileDownloadView = fileDownloadView;
    }

    @Autowired
    public void setFileDownloadView(FileDownloadView fileDownloadView) {
        this.fileDownloadView = fileDownloadView;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(fileDownloadView);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return FileDownload.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest) throws Exception {
        if (null != returnValue) {
            mavContainer.addAttribute(FileDownloadView.MODEL_KEY);
            mavContainer.setView(fileDownloadView);
        }
    }

}
