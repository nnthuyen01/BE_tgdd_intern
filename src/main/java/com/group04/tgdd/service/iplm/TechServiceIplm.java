package com.group04.tgdd.service.iplm;

import com.group04.tgdd.dto.DetailSpecs;
import com.group04.tgdd.dto.ProTechReq;
import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.model.Product;
import com.group04.tgdd.model.ProductTechnical;
import com.group04.tgdd.model.Technical;
import com.group04.tgdd.repository.ProductRepo;
import com.group04.tgdd.repository.ProductTechnicalRepo;
import com.group04.tgdd.repository.TechnicalRepo;
import com.group04.tgdd.service.TechnicalService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechServiceIplm implements TechnicalService {

    private final TechnicalRepo technicalRepo;
    private final ProductRepo productRepo;
    private final ProductTechnicalRepo productTechnicalRepo;

    @Override
    public List<Technical> getAll() {
        return technicalRepo.findAll();
    }

    @Override
    public Technical saveTech(Technical technical) {
        return technicalRepo.save(technical);
    }

    @Override
    public void deleteTech(Long techId) {
        technicalRepo.deleteById(techId);
    }

    @Override
    public void saveAllProductTech(List<ProTechReq> proTechReqs) {
        try {
            proTechReqs.forEach(proTechReq -> {
                saveProductTech(proTechReq);
            });
        }catch (Exception e){
            throw new AppException(400, "Failed");
        }
    }

    @Override
    public ProductTechnical saveProductTech(ProTechReq proTechReq) {
        Technical technical = technicalRepo.getReferenceById(proTechReq.getTechId());
        Product product = productRepo.getReferenceById(proTechReq.getProductId());
        ProductTechnical productTechnical = new ProductTechnical(null, proTechReq.getInfo(), technical,product);
        try {
            productTechnicalRepo.save(productTechnical);
        }catch (Exception e){
            ProductTechnical productTechnicalTemp =
                    productTechnicalRepo.findProductTechnicalByProductAndTechnical(product,technical);
            productTechnicalTemp.setInfo(proTechReq.getInfo());
            productTechnicalRepo.save(productTechnicalTemp);
        }
        return productTechnical;
    }

    @Override
    public void deleteProductTech(Long id) {
        boolean check = productTechnicalRepo.existsById(id);
        if (!check) throw new AppException(404,"Product Tech not found");
        productTechnicalRepo.deleteById(id);
    }

    @Override
    public List<DetailSpecs> getAllProductTech(Long productId) {
        Product product = productRepo.getReferenceById(productId);
        List<ProductTechnical> productTechnicals= productTechnicalRepo.findAllByProduct(product);
        List<DetailSpecs> detailSpecs = new ArrayList<>();
        productTechnicals.forEach(productTechnical -> {
            detailSpecs.add(new DetailSpecs(productTechnical.getId(),productTechnical.getTechnical().getName(),productTechnical.getInfo()));
        });
        return detailSpecs;
    }
}
