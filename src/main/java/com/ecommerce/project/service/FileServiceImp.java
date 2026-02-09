package com.ecommerce.project.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceImp implements FileService {

    @Value("${project.imagePath}")
    private String imagePath;


    @Override
    public String uploadImage(MultipartFile image) throws IOException {


        String originalFilename = image.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        assert originalFilename != null;
        String fileName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String filePath = imagePath + File.pathSeparator + fileName;

        File folder = new File(imagePath);
        if(!folder.exists()){
            folder.mkdir();
        }

        Files.copy(image.getInputStream(), Paths.get(filePath));


        return fileName;
    }



}
