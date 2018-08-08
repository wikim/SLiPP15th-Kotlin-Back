package net.slipp.fifth.kotlin.web.support.handler;

import java.io.File;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import net.slipp.fifth.kotlin.system.attachefile.AttacheFile;
import net.slipp.fifth.kotlin.system.attachefile.AttacheFileService;
import net.slipp.fifth.kotlin.web.support.view.FileDownloadView;
import net.slipp.fifth.kotlin.web.support.view.FileDownloadView.FileDownload;

/**
 * Contoller영역에서 AttacheFile 리턴시 대응
 * 
 * @author jiheon
 *
 */
public class AttacheFileMethodReturnValueHandler implements HandlerMethodReturnValueHandler, InitializingBean {

    private AttacheFileService attacheFileService;
    private FileDownloadView fileDownloadView;

    public AttacheFileMethodReturnValueHandler(AttacheFileService attacheFileService, FileDownloadView fileDownloadView) {
        this.attacheFileService = attacheFileService;
        this.fileDownloadView = fileDownloadView;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(attacheFileService);
        Assert.notNull(fileDownloadView);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return AttacheFile.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest) throws Exception {
        if (null != returnValue) {
            AttacheFile attacheFile = (AttacheFile) returnValue;
            File physicalFile = attacheFileService.convertFile(attacheFile);

            FileDownload fileDownload = new FileDownload(physicalFile, attacheFile.getName());

            mavContainer.addAttribute(FileDownloadView.MODEL_KEY, fileDownload);
            mavContainer.setView(fileDownloadView);
        }
    }
}
