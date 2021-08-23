package com.prac.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ImageUtils {

    private MultipartFile profileImg;
    private String uploadPath;
    private LocalDate imgCreateDate;
    private String uuid;

    public ImageUtils(MultipartFile profileImg, String uploadPath, LocalDate imgCreateDate) {
        this.profileImg = profileImg;
        this.uploadPath = uploadPath;
        this.imgCreateDate = imgCreateDate;
        this.uuid = UUID.randomUUID().toString();
    }


    public void copyImgToFolder() {

        makeFolder();

        String saveFileName = uploadPath + File.separator + getImgSrc();

        Path savePath = Paths.get(saveFileName);

        try {
            profileImg.transferTo(savePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void makeFolder() {
        String folderPath = getFolderPath();

        File uploadPathFolder = new File(uploadPath, folderPath);

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
    }

    private String getFileName() {
        return profileImg.getOriginalFilename();
    }

    private String getFolderPath() {
        return imgCreateDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).replace("/", File.separator);
    }

    public String getImgSrc() {
        return getFolderPath() + File.separator + uuid + "_" + getFileName();
    }
}
