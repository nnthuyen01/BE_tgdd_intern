package com.group04.tgdd.service.iplm;

import com.group04.tgdd.dto.ProductColorReq;
import com.group04.tgdd.dto.ProductOpReq;
import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.model.Product;
import com.group04.tgdd.model.ProductOption;
import com.group04.tgdd.repository.ProductRepo;
import com.group04.tgdd.repository.ProductOptionRepo;
import com.group04.tgdd.service.ProductColorService;
import com.group04.tgdd.service.ProductOptionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOpIplm implements ProductOptionService {
    private final ProductRepo productRepo;
    private final ProductOptionRepo productOptionRepo;
    private final ProductColorService productColorService;
    @Autowired
    private final ModelMapper mapper;
    @Override
    public ProductOpReq saveNewProductOption(ProductOpReq productOpReq) {

        Product product = productRepo.getReferenceById(productOpReq.getProductId());
        ProductOption productOption = new ProductOption();

        productOption.setProduct(product);
        productOption.setOptionName(productOpReq.getProductOptionName());
        productOption.setPrice(productOpReq.getPrice());
        productOption.setPromotion(productOpReq.getPromotion());
        productOption = productOptionRepo.save(productOption);

        if (productOpReq.getColors()!=null) {
            List<ProductColorReq> productColorReqs = productOpReq.getColors();
            ProductOption finalProductOption = productOption;
            productColorReqs.forEach(productColorReq -> {
                productColorReq.setProductOptionId(finalProductOption.getId());
                productColorService.saveNewProductColor(productColorReq);
            });
        }
        productOpReq.setId(productOption.getId());

        return productOpReq;
    }

    @Override
    public void updateProductOption(ProductOpReq productOpReq) {
        var check = productOptionRepo.findById(productOpReq.getId());
        if (!check.isPresent()) {
            throw new AppException(404, "Product Option ID not found");
        }
        ProductOption productOption = check.get();
        if (productOpReq.getProductOptionName()!=null) {
            productOption.setOptionName(productOpReq.getProductOptionName());
        }
        if (productOpReq.getPromotion() != null) {
            productOption.setPromotion(productOpReq.getPromotion());
        }
        if (productOpReq.getPrice() != null) {
            productOption.setPrice(productOpReq.getPrice());
        }
        productOptionRepo.save(productOption);

    }

    @Override
    public void deleteProductOption(Long id) {
        var check = productOptionRepo.existsById(id);
        if (!check)
            throw new AppException(404,"Product Option ID not found");

        try {
            productOptionRepo.deleteById(id);
        }catch (Exception e){
            throw new AppException(400, "Can't delete because have order for this product ID");
        }

    }

    @Override
    public ProductOption getProductOptionById(Long id) {
        var check = productOptionRepo.findById(id);
        if (!check.isPresent()) {
            throw new AppException(404, "Product Option ID not found");
        }
        ProductOption productOption = check.get();
        return productOption;
    }

}
