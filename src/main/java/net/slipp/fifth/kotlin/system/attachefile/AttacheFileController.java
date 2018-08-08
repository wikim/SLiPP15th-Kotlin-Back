
package net.slipp.fifth.kotlin.system.attachefile;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import net.slipp.fifth.kotlin.common.exception.ApplicationException;
import net.slipp.fifth.kotlin.configuration.ApplicationProperties;
import net.slipp.fifth.kotlin.web.support.view.AttacheFileResultView;

@RestController
@RequestMapping("/files")
public class AttacheFileController {

    @Autowired
    private AttacheFileService attacheFileService;

    @Resource
    private AttacheFileResultView attacheFileResultView;
    @Resource
    private ApplicationProperties ApplicationProperties;

    /**
     * Upload File
     *
     * @param file
     *            {@link MultipartFile}
     * @param model
     *            {@link Model}
     * @return {@link AttacheFileResultView}
     * @throws IOException
     *             upload Empty File, over Max upload file size
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView fileUpload(@RequestParam(value = "file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new ApplicationException("systoem.exception.dontUploadEmptyFile");
        }
        if (file.getSize() > ApplicationProperties.getMaxUploadFileSize()) {
            throw new ApplicationException("system.exception.overMaxUploadFileSize");
        }
        
        ModelAndView mav = new ModelAndView(this.attacheFileResultView);
        mav.addObject("attacheFile", attacheFileService.saveFileToTempoararyPath(file.getOriginalFilename(),
                file.getContentType(), file.getInputStream()));
        
        return mav;
    }
    
    @RequestMapping(value = "/upload-license", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView licenseFileUpload(@RequestParam(value = "license_file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new ApplicationException("systoem.exception.dontUploadEmptyFile");
        }
        if (file.getSize() > ApplicationProperties.getMaxUploadFileSize()) {
            throw new ApplicationException("system.exception.overMaxUploadFileSize");
        }
        
        ModelAndView mav = new ModelAndView(this.attacheFileResultView);
        mav.addObject("attacheFile", attacheFileService.saveFileToTempoararyPath(file.getOriginalFilename(),
                file.getContentType(), file.getInputStream()));
        
        return mav;
    }

    /**
     * Get attacheFile
     *
     * @param attacheFile
     * @return
     */
    @RequestMapping(value = "/{attacheFile}/download", method = RequestMethod.GET)
    public AttacheFile fileDownload(@PathVariable AttacheFile attacheFile) {
        return attacheFile;
    }

    @RequestMapping(value = "/max-uploadfile-size", method = RequestMethod.GET)
    public ResponseEntity getLimitMaxFilueUploadSize() {
        Long maxUploadFileSize = ApplicationProperties.getMaxUploadFileSize();
        return ResponseEntity.ok(maxUploadFileSize);
    }

    /**
     * Remove attacheFile
     * 
     * @param attacheFile
     *            {@link AttacheFile}
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity deleteTemporaryAttacheFile(@RequestBody AttacheFileDto attacheFile) {
        attacheFileService.deleteAttacheFile(attacheFile);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
