package jeff.cutigram.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.jni.Error;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Service
public class FileStorageService {

    private String uploadHost = "http://10.80.163.99:8080/uploads/";
//    private String uploadHost = "http://172.20.10.2:8080/uploads/";

    @Value("${file.uploadDir}")
    private String rootUploadDir;

    public String storeFile(MultipartFile file) {
        final String fileType = file.getContentType();
        final String fileName = this.makeFileName(file.getOriginalFilename());

        String uploadDir = rootUploadDir + "/" + fileType;
        if(!this.makeDirectory(uploadDir)) return null;

        try {
            this.writeFile(uploadDir + "/" + fileName, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 반환은 호스트 + 디렉터리 경로 + 파일 이름
        return uploadHost + fileType + "/" + fileName;
    }

    // 반환형: 파일 이름 + 확장자
    public String makeFileName(String fileName) {
        return this.randomString() + this.getFileExtension(fileName);
    }

    // 랜덤 스트링 생성 및 반환
    public String randomString() {
        return RandomStringUtils.randomAlphanumeric(50);
    }

    // 파일이름으로 확장자 추출 및 반환
    public String getFileExtension(String fileName) {
        int position = fileName.lastIndexOf(".");
        return fileName.substring(position);
    }

    public Boolean makeDirectory(String uploadDir) {
        File directory = new File(uploadDir);

        if (!directory.isDirectory()) {
            // 디렉터리가 있다면 true, 디렉터리가 없다면 false
            return directory.mkdirs();
        } else {
            return true;
        }
    }

    public void writeFile(String uploadDir, byte[] fileData) {
        Path path = Paths.get(uploadDir);

        try {
            Files.write(path, fileData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
