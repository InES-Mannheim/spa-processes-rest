package de.unimannheim.spa.process.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
   public static File convertMultipartToFile(MultipartFile multipartFile) throws IllegalStateException, IOException{
     File convertedFile = new File(multipartFile.getOriginalFilename());
     FileOutputStream fos = new FileOutputStream(convertedFile); 
     fos.write(multipartFile.getBytes());
     fos.close(); 
     return convertedFile;
   }
}
