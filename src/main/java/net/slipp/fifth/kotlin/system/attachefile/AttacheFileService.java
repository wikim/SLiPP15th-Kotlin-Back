package net.slipp.fifth.kotlin.system.attachefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.common.exception.ApplicationException;
import net.slipp.fifth.kotlin.configuration.ApplicationProperties;

@Slf4j
@Service
public class AttacheFileService {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 임시저장 경로
     */
    private String temporaryPath;
    /**
     * 영속저장 경로
     */
    private String persistencePath;

    @Autowired
    private AttacheFileRepository attacheFileRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Resource
    private ApplicationProperties ujblibProperties;

    @PostConstruct
    public void setup() throws FileNotFoundException {
        this.temporaryPath = ujblibProperties.getTemporaryPath();
        this.persistencePath = ujblibProperties.getPersistencePath();

        File temporaryPathDir = ResourceUtils.getFile(this.temporaryPath);
        if (!temporaryPathDir.exists()) {
            temporaryPathDir.mkdirs();
        }

        File persistencePathDir = ResourceUtils.getFile(this.persistencePath);
        if (!persistencePathDir.exists()) {
            persistencePathDir.mkdirs();
        }
    }

    /**
     * Save file to temporaryPath
     *
     * @param fileName
     * @param contentType
     * @param inputStream
     * @return
     * @throws IOException
     */
    public AttacheFile saveFileToTempoararyPath(String fileName, String contentType, InputStream inputStream)
            throws IOException {
        EmptyTemporaryFile emptyTemporaryFile = createEmptyTemporaryFile();

        FileCopyUtils.copy(inputStream, new FileOutputStream(emptyTemporaryFile.getFile()));
        log.debug("Save file: {}", emptyTemporaryFile.getFile().getAbsolutePath());

        return new AttacheFile(fileName, contentType, emptyTemporaryFile.getSize(), emptyTemporaryFile.getPath(),
                emptyTemporaryFile.getFileId());
    }

    /**
     * Make empty temporary file(until make unique file name)
     *
     * @return unique name {@link EmptyTemporaryFile}
     * @throws ApplicationException
     */
    private EmptyTemporaryFile createEmptyTemporaryFile() throws ApplicationException {
        try {
            String savePath = createTemporarySavePath();
            String saveFileId = UUID.randomUUID().toString();
            File saveFile = Paths.get(temporaryPath, savePath, saveFileId).toFile();

            if (saveFile.exists()) {
                return createEmptyTemporaryFile();
            }

            log.debug("Created EmptyTemporaryFile: {}", Paths.get(savePath, saveFileId).toString());
            return new EmptyTemporaryFile(saveFile, savePath, saveFileId);
        } catch (Exception e) {
            throw new ApplicationException("system.exception.attacheFileUpload", e);
        }
    }

    /**
     * 업르드되는 파일을 'yyyy-MM-dd' 형태를 띄는 경로에 저장하기 위해서 디렉토리 생성
     *
     * @return
     */
    private String createTemporarySavePath() {
        String savePath = sdf.format(Calendar.getInstance().getTime()).toString();
        makeTemorarySaveDir(savePath);
        return savePath;
    }

    /**
     *
     * @param savePath
     *            specific directory path
     */
    public void makeSpecificPathDir(String savePath) {
        File specificPathDir = Paths.get(persistencePath, savePath).toFile();
        if (!specificPathDir.exists()) {
            specificPathDir.mkdirs();
            log.debug("Make Directory: {}", specificPathDir.getAbsolutePath());
        }
    }

    /**
     *
     * @param savePath
     *            format: 'yyyy-MM-dd'
     */
    private void makeTemorarySaveDir(String savePath) {
        File temporarySaveDir = Paths.get(temporaryPath, savePath).toFile();
        if (!temporarySaveDir.exists()) {
            temporarySaveDir.mkdirs();
        }
    }

    /**
     * Remove File at temoraryPath
     *
     * @param attacheFile
     *            {@link AttacheFile}
     * @return true: success / false: failure
     */
    public boolean deleteFileFromTemproaryPath(AttacheFile attacheFile) {
        return deletePhysicalFile(Paths.get(temporaryPath, attacheFile.getFilePath()).toString());
    }

    /**
     * Remove file at define path.
     *
     * @param attacheFile
     *            {@link AttacheFile}
     * @return true: success/ false: failure
     */
    public boolean deleteFileFromPersistencePath(AttacheFile attacheFile) {
        return deletePhysicalFile(Paths.get(persistencePath, attacheFile.getFilePath()).toString());
    }

    /**
     * Move temporary path to specific path
     *
     * @param specificPath
     *            target Path
     * @param attacheFile
     *            {@link AttacheFile}
     * @return change path to specificPath the {@link AttacheFile}
     * @throws IOException
     */
    public AttacheFile moveTempFileToDefinePath(String specificPath, AttacheFile attacheFile) throws IOException {
        makeSpecificPathDir(specificPath);
        moveFileToSpecificPath(specificPath, attacheFile);
        return save(attacheFile);
    }

    private void moveFileToSpecificPath(String specificPath, AttacheFile attacheFile)
            throws FileNotFoundException, IOException {
        File temporaryPathFile = findPhysicalFile(Paths.get(temporaryPath, attacheFile.getFilePath()).toString(),
                false);
        File persistencePathFile = findPhysicalFile(
                Paths.get(persistencePath, specificPath, attacheFile.getFileId()).toString(), true);
        if (persistencePathFile.exists()) {
            throw new IOException("system.exception.exist.file");
        }
        log.debug("Move File: {} to {}", temporaryPathFile, persistencePathFile);
        FileCopyUtils.copy(temporaryPathFile, persistencePathFile);
        temporaryPathFile.delete();
        attacheFile.setPath(specificPath);
    }

    /**
     * Find physicalFile from attacheFile
     *
     * @param attacheFile
     * @return
     * @throws FileNotFoundException
     */
    public File convertFile(AttacheFile attacheFile) throws FileNotFoundException {
        try {
            return findPhysicalFile(Paths.get(persistencePath, attacheFile.getFilePath()).toString(), false);
        } catch (FileNotFoundException e) {
            return findPhysicalFile(Paths.get(temporaryPath, attacheFile.getFilePath()).toString(), false);
        }
    }

    /**
     * 첨부파일을 임시 저장소로 복제
     *
     * @param sources
     * @return
     */
    public Set<AttacheFile> replicates(Set<AttacheFile> sources) {
        Set<AttacheFile> replicaFiles = Sets.newHashSet();
        try {
            for (AttacheFile source : sources) {
                replicaFiles.add(replicate(source));
            }
        } catch (IOException e) {
            throw new ApplicationException("system.exception.attacheFileReplicaException", e);
        }

        return replicaFiles;
    }

    public AttacheFile replicate(AttacheFile source) throws FileNotFoundException, IOException {
        File sourceFile = convertFile(source);
        EmptyTemporaryFile emptyTemporaryFile = createEmptyTemporaryFile();

        FileCopyUtils.copy(new FileInputStream(sourceFile), new FileOutputStream(emptyTemporaryFile.getFile()));
        log.debug("Replicate: [source: {}, destination: {}]", sourceFile, emptyTemporaryFile);
        return new AttacheFile(source.getName(), source.getContentType(), emptyTemporaryFile.getSize(),
                emptyTemporaryFile.getPath(), emptyTemporaryFile.getFileId());
    }

    /**
     * Find physical file
     *
     * @param filePath
     * @param isIgnoreFileNotFound
     * @return
     * @throws FileNotFoundException
     */
    private File findPhysicalFile(String filePath, boolean isIgnoreFileNotFound) throws FileNotFoundException {
        if (!StringUtils.hasText(filePath)) {
            throw new ApplicationException("system.exception.attacheFile.hasEmptyPath");
        }
        String resourceFilePath = Paths.get(filePath).toString();
        File file = ResourceUtils.getFile(resourceFilePath);
        if (!file.exists() && !isIgnoreFileNotFound) {
            log.debug("File not found: {}", resourceFilePath);
            throw new FileNotFoundException(resourceFilePath);
        }
        return file;
    }

    /**
     * Remove physical file.
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public boolean deletePhysicalFile(String filePath) {
    	boolean result = Boolean.FALSE;
        try {
            File file = findPhysicalFile(filePath, true);
            if (file.exists()) {
                log.debug("Remove file: {}", Paths.get(filePath).toAbsolutePath());
                file.delete();
                result = Boolean.TRUE;
            } else {
                log.debug("Remove file not found.");
            }
        } catch (Exception e) {
            log.error("Occur exception: file: {} \n {}", Paths.get(filePath).toAbsolutePath(), e);
        }
        return result;
    }

    /**
     * Save AttacheFile
     *
     * @param attacheFile
     *            {@link AttacheFile}
     * @return save {@link AttacheFile}
     */
    @Transactional
    public AttacheFile save(AttacheFile attacheFile) {
        return attacheFileRepository.save(attacheFile);
    }

    @Data
    private class EmptyTemporaryFile {
        private final File file;
        private final String path;
        private final String fileId;

        long getSize() {
            return null == file ? 0 : file.length();
        }
    }

    @Transactional(readOnly = true)
    public AttacheFile findById(Long id) {
        log.debug("FindById: {}", id);
        return attacheFileRepository.findById(id);
    }

    public void deleteAttacheFile(AttacheFileDto attacheFileDto) {
        log.debug("Delete AttacheFile: {}", attacheFileDto);
        AttacheFile attacheFile = modelMapper.map(attacheFileDto, AttacheFile.class);
        deleteAttacheFile(attacheFile);
    }

    @Transactional
    public void deleteAttacheFile(AttacheFile attacheFile) {
        if (attacheFile.isNew()) {
            deleteFileFromTemproaryPath(attacheFile);
        } else {
            deleteFileFromPersistencePath(attacheFile);
        }
    }
}
