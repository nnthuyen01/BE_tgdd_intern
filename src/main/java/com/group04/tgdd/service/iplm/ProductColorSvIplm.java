package com.group04.tgdd.service.iplm;

import com.group04.tgdd.dto.ProductColorReq;
import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.model.Color;
import com.group04.tgdd.model.ProductColor;
import com.group04.tgdd.model.ProductOption;
import com.group04.tgdd.repository.ColorRepo;
import com.group04.tgdd.repository.ProductColorRepo;
import com.group04.tgdd.repository.ProductOptionRepo;
import com.group04.tgdd.service.ProductColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductColorSvIplm implements ProductColorService {
    private final ProductOptionRepo product_optionRepo;
    private final ProductColorRepo productColorRepo;
    private final ColorRepo colorRepo;

    @Override
    public ProductColorReq saveNewProductColor(ProductColorReq productColorReq) {
        ProductColor productColor = new ProductColor();
        ProductOption product_option = product_optionRepo.getReferenceById(productColorReq.getProductOptionId());
        Color color = colorRepo.getReferenceById(productColorReq.getColorId());
        productColor.setQuantity(productColorReq.getQuantity());
        productColor.setColor(color);
        productColor.setProductOption(product_option);
        productColor = productColorRepo.save(productColor);

        productColorReq.setId(productColor.getId());

        return productColorReq;
    }

    @Override
    public void updateProductColor(Long productColorId, int quantity) {
        var productColorTemp = productColorRepo.findById(productColorId);
        ProductColor productColor = productColorTemp.orElse(null);
        if (productColor==null) {
            throw new AppException(404,"Product color ID not found");
        }
        productColor.setQuantity(quantity);
        productColorRepo.save(productColor);
    }

    @Override
    public void deleteProductColorById(Long id) {
        boolean check = productColorRepo.existsById(id);
        if (!check){
            throw new AppException(404,"Product color ID not found");
        }
        try {
            productColorRepo.deleteById(id);
        }catch (Exception e){
            throw new AppException(400, "Can't delete because have order for this product ID");
        }

    }

    @Override
    public ProductColor getProductColorById(Long id) {
        var productColorTemp = productColorRepo.findById(id);
        ProductColor productColor = productColorTemp.orElse(null);
        if (productColor == null) {
            throw new AppException(404,"Product color ID not found");
        }
        return productColor;
    }
}
