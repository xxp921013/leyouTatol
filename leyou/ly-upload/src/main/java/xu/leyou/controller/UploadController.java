package xu.leyou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xu.leyou.service.UploadService;

@RestController
@RequestMapping("upload")
@CrossOrigin//跨域
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("image")
    //springmvc会将上传的文件封装到MultipartFile对象
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = uploadService.uploadImage(file);
        System.out.println(url);
        return ResponseEntity.ok(url);
    }
}
