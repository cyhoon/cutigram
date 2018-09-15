package jeff.cutigram.controller;

import jeff.cutigram.payload.response.ApiResponse;
import jeff.cutigram.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String filePath = fileStorageService.storeFile(file);
        return ResponseEntity.ok(new ApiResponse(true, filePath));
    }
}
