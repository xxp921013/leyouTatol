import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.catalina.Store;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(locations = {"classpath:application.yml"})
public class FastDFSTest {
    @Autowired
    public FastFileStorageClient fastFileStorageClient;
    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Test
    public void demo1() throws FileNotFoundException {
        File file = new File("K:\\leyou\\upload\\timg.jpg");
        //上传,上传后返回文件id
        StorePath storePath = fastFileStorageClient.uploadFile(
                new FileInputStream(file), file.length(), "jpg", null);
        //带组,group开头
        System.out.println(storePath.getFullPath());
        //不带组
        System.out.println(storePath.getPath());
    }

    @Test
    public void demo2() throws FileNotFoundException {
        File file = new File("K:\\leyou\\upload\\timg.jpg");
        //上传,并生成缩略图
        StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
                new FileInputStream(file), file.length(), "jpg", null);
        //带组,group开头
        System.out.println(storePath.getFullPath());
        //不带组
        System.out.println(storePath.getPath());
        //获取缩略图路径
        String thumbImagePath = thumbImageConfig.getThumbImagePath(storePath.getPath());
    }
}
