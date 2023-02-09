package com.group04.tgdd.service.iplm;

import com.group04.tgdd.dto.ProductImageReq;
import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.model.Color;
import com.group04.tgdd.model.Product;
import com.group04.tgdd.model.ProductImage;
import com.group04.tgdd.repository.ColorRepo;
import com.group04.tgdd.repository.ProductImageRepo;
import com.group04.tgdd.repository.ProductRepo;
import com.group04.tgdd.service.Cloudinary.CloudinaryUpload;
import com.group04.tgdd.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceIplm implements ProductImageService{
    private final ProductImageRepo productImageRepo;
    private final ProductRepo productRepo;
    private final ColorRepo colorRepo;
    private final CloudinaryUpload cloudinaryUpload;

    @Override
    public boolean saveNewImage(List<ProductImageReq> productImageReqs) {
        List<ProductImage> productImages = new ArrayList<>();
        productImageReqs.forEach(image -> {
            Product product = productRepo.getReferenceById(image.getProductId());
            Color color = colorRepo.getReferenceById(image.getColorId());
            productImages.add(new ProductImage(null,image.getUrlImage(),product,color));
        });
        return productImageRepo.saveAll(productImages).size() > 0;
    }

    @Override
    public void deleteImageProduct(Long id) {
        ProductImage productImage= productImageRepo.findById(id).orElse(null);
        if (productImage==null)
            throw new AppException(404, "ID image product not found");
        try{
            if (productImage.getUrlImage().startsWith("https://res.cloudinary.com/quangdangcloud/image/upload")) {
                cloudinaryUpload.deleteImage(productImage.getUrlImage());
            }
            productImageRepo.deleteById(id);
        } catch (Exception e) {
            throw new AppException(400, e.getMessage());
        }
    }

    @Override
    public List<String> uploadImageProduct(Long productId, Long colorId, List<MultipartFile> imgs) {
        List<ProductImage> productImages = new ArrayList<>();
        imgs.forEach(img ->{
            Product product = productRepo.getReferenceById(productId);
            Color color = colorRepo.getReferenceById(colorId);
            try {
                String url = cloudinaryUpload.uploadImage(img,null);
                productImages.add(new ProductImage(null,url,product,color));
            } catch (IOException e) {
                throw new AppException(400,"Failed");
            }
        });
        productImageRepo.saveAll(productImages);
        List<String> urls = new ArrayList<>();
        productImages.forEach(productImage -> {
            urls.add(productImage.getUrlImage());
        });
        return urls;
    }
}
