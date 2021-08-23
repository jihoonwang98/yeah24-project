/*
package com.prac.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DisplayApi {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @GetMapping("/display")
    public ResponseEntity<byte[]> getImage(String imgSrc) {
        ResponseEntity<byte[]> result = null;
        // log.info("display");

        try {
            String decodedImgSrc = URLDecoder.decode(imgSrc, "UTF-8");
            // log.info("imgSrc: " + decodedImgSrc);

            File file = new File(uploadPath + File.separator + decodedImgSrc);
            // log.info("file: " + file);

            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }
}
*/
