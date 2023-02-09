package com.group04.tgdd.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {
    private static String pathImg = "Images";


    public static String getBaseURL(HttpServletRequest request) {
       return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    public static File convertMultiPartToFile(MultipartFile file) throws IOException {
        if (!Files.isDirectory(Path.of(pathImg))){
            File newDir = new File(pathImg);
            newDir.mkdir();
        }
        File convFile = new File(pathImg+"/" +file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public static Long getIdCurrentUser(){
       return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }
}
