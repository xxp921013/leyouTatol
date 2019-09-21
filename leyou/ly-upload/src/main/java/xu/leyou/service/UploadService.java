package xu.leyou.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xu.leyou.config.UploadConfig;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@EnableConfigurationProperties(UploadConfig.class)
public class UploadService {
    //private static final List<String> agreeTypes = Arrays.asList("image/jpeg", "image/png", "image/bmp");
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    //为了不把配置写死,在配置文件中配好然后用配置类导入
    @Autowired
    private UploadConfig uploadConfig;

    public String uploadImage(MultipartFile file) {
        try {
            //校验文件
            String contentType = file.getContentType();//文件类型
            if (!uploadConfig.getAgreeTypes().contains(contentType)) {
                throw new LyException(ExceptionEnums.UPLOAD_ERROR);
            }
            //校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new LyException(ExceptionEnums.UPLOAD_ERROR);
            }
            //文件的保存路径
//            File tagFile = new File("K:\\leyou\\upload", file.getOriginalFilename());
//            System.out.println(tagFile);
            //保存到本地
            //file.transferTo(tagFile);
            //文件后缀
            //String substring = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            //.后就是后缀
            String s = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            //上传到fdfs,文件流+文件大小+文件类型+null
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), s, null);
            //返回路径
            return uploadConfig.getBaseUrl() + storePath.getFullPath();
        } catch (IOException e) {
            //上传失败
            log.error("上传文件失败", e);
            throw new LyException(ExceptionEnums.UPLOAD_ERROR);
        }
    }
}
