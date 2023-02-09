package com.group04.tgdd.service.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.group04.tgdd.utils.Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@Configuration
public class CloudinaryUpload {
    @Bean
    public Cloudinary cloudinary(){
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "quangdangcloud",
                "api_key", "738222114899535",
                "api_secret", "V37UHtOmQ62U1VH-kXyd7kRLgf4",
                "secure", true));
        return cloudinary;
    }

    public String getPublicId(String urlImage){
        int temp1 = urlImage.lastIndexOf(".");
        int temp2 = urlImage.lastIndexOf("/");
        return urlImage.substring(temp2+1,temp1);
    }

    public String uploadImage(MultipartFile file, String urlDestroy) throws IOException {
        Map params = ObjectUtils.asMap(
                "resource_type", "auto",
                "folder", "tgdd"
        );
        Map map = cloudinary().uploader().upload(Utils.convertMultiPartToFile(file),params);
        if (urlDestroy!= null) {
            deleteImage(urlDestroy);
        }

        return map.get("secure_url").toString();
    }
    public void deleteImage(String urlImage) throws IOException {
        cloudinary().uploader().destroy("tgdd/" + getPublicId(urlImage)
                , ObjectUtils.asMap("resource_type", "image"));
    }

}
