package com.hanghang.reggie.controller;

import com.hanghang.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 将图片下载并且回显到浏览器
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath ;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
   public R<String> upload(MultipartFile file){

      //  使用uuid重新生成文件名
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString()+substring ;

        //创建目录对象 ， 如果目录不存在则生成目录
        File dir = new File(basePath) ;
        if (!dir.exists()){
            dir.mkdir() ;
        }

       try {
           file.transferTo(new File(basePath + fileName));
       } catch (IOException e){
           e.printStackTrace();
       }
        return R.success(fileName) ;
   }

    /**
     * 文件显示给浏览器
     * @param name
     * @param response
     */
   @GetMapping("/download")
   public void download(String name , HttpServletResponse response){

       try {
           //输入流读取文件类容
           FileInputStream fileInputStream = new FileInputStream(new File(basePath+name))  ;

           //输出流将文件写会浏览器，在浏览器中显示
           ServletOutputStream outputStream = response.getOutputStream();

           response.setContentType("image/jpeg");

           int len = 0 ;
           byte[] bytes = new byte[1024] ;
           while ((len = fileInputStream.read(bytes) )!= -1){
               outputStream.write(bytes , 0 , len);
               outputStream.flush();
           }

           //关闭资源
           outputStream.close();
           fileInputStream.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}
