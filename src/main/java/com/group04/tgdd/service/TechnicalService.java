package com.group04.tgdd.service;

import com.group04.tgdd.dto.DetailSpecs;
import com.group04.tgdd.dto.ProTechReq;
import com.group04.tgdd.model.ProductTechnical;
import com.group04.tgdd.model.Technical;

import java.util.List;

public interface TechnicalService {
    List<Technical> getAll();

    Technical saveTech(Technical technical);

    void deleteTech(Long techId);

    void saveAllProductTech(List<ProTechReq> proTechReqs);

    ProductTechnical saveProductTech(ProTechReq proTechReq);

    void deleteProductTech(Long id);

    List<DetailSpecs> getAllProductTech(Long productId);
}
