package com.group04.tgdd.repository;

import com.group04.tgdd.model.Product;
import com.group04.tgdd.model.ProductTechnical;
import com.group04.tgdd.model.Technical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductTechnicalRepo extends JpaRepository<ProductTechnical,Long> {
    boolean existsById(Long id);
    List<ProductTechnical> findAllByProduct(Product product);
    ProductTechnical findProductTechnicalByProductAndTechnical(Product product, Technical technical);
}
