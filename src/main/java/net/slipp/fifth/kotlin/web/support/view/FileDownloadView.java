package net.slipp.fifth.kotlin.web.support.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.common.exception.ApplicationException;

@Slf4j
public class FileDownloadView extends AbstractView {
    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final String BINARY = "binary";
    private static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
    private static final String CONTENT_TYPE = "application/x-download";
    public static final String MODEL_KEY = "FileDownloadKey";

    public FileDownloadView() {
        super.setContentType(CONTENT_TYPE);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        FileDownload resource = findFileDownload(model);

        response.setContentType(CONTENT_TYPE);
        response.setContentLength((int) resource.getLength());
        response.setHeader(CONTENT_TRANSFER_ENCODING, BINARY);

        response.setHeader("Content-Disposition", String.format("attachement;filename=%s",
                URLEncoder.encode(resource.getDownloadFileName(), ENCODING_UTF_8)));

        OutputStream outputStream = null;
        InputStream inputStream = null;

        try {
            outputStream = response.getOutputStream();
            inputStream = resource.getFileInputStream();

            FileCopyUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            log.error("Occur Exception: {}", e);
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }

            if (null != outputStream) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    private FileDownload findFileDownload(Map<String, Object> model) {
        FileDownload resource = null;

        if (model.containsKey(MODEL_KEY)) {
            Object value = model.get(MODEL_KEY);
            if (value instanceof FileDownload) {
                resource = (FileDownload) value;
            }
        } else {
            for (Object value : model.values()) {
                if (value instanceof FileDownload) {
                    resource = (FileDownload) value;
                }
            }
        }
        if (null == resource) {
            throw new ApplicationException("system.exception.notFoundDownloadFile");
        }
        return resource;
    }

    @Data
    public static final class FileDownload {
        private File file;
        private String downloadFileName;

        public FileDownload(File file) {
            this.file = file;
        }

        public FileDownload(File file, String downloadFileName) {
            Assert.notNull(file);
            Assert.hasText(downloadFileName);

            this.file = file;
            this.downloadFileName = downloadFileName;
        }

        public InputStream getFileInputStream() throws FileNotFoundException {
            return new FileInputStream(getFile());
        }

        public String getFileName() {
            return getFile().getName();
        }

        public long getLength() {
            return getFile().length();
        }
    }
}
