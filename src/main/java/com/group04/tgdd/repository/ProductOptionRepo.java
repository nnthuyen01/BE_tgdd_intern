package com.group04.tgdd.repository;

import com.group04.tgdd.model.Product;
import com.group04.tgdd.model.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionRepo extends JpaRepository<ProductOption,Long> {
    List<ProductOption> findAllByProduct(Product product);

    List<ProductOption> findProductOptionByProductId(Long id);

}
