package net.slipp.fifth.kotlin.web.support.view;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.common.exception.ApplicationException;
import net.slipp.fifth.kotlin.system.attachefile.AttacheFile;

/**
 * Attache file upload result view
 * 
 * @author jiheon
 *
 */
@Slf4j
public class AttacheFileResultView extends AbstractView {
    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final String TEXT_PLAIN = "text/plain";
    public static final String MODEL_KEY = "attacheFile";

    public AttacheFileResultView() {
        super.setContentType(TEXT_PLAIN);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setContentType(TEXT_PLAIN);
        response.setCharacterEncoding(ENCODING_UTF_8);

        AttacheFile attacheFile = (AttacheFile) model.get(MODEL_KEY);

        OutputStream outputStream = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            outputStream = response.getOutputStream();
            mapper.writeValue(outputStream, ImmutableMap.of(MODEL_KEY, attacheFile));
        } catch (IOException e) {
            log.error("Occur error on file upload: {}", e);
            throw new ApplicationException("system.exception.uploadFile", e);
        } finally {
            if (null != outputStream) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }
}
