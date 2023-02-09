package com.group04.tgdd.service.iplm;

import com.group04.tgdd.dto.*;
import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.exception.NotFoundException;
import com.group04.tgdd.model.*;
import com.group04.tgdd.repository.*;
import com.group04.tgdd.service.*;
import com.group04.tgdd.service.Cloudinary.CloudinaryUpload;
import com.group04.tgdd.utils.MoneyConvert;
import lombok.RequiredArgsConstructor;
// import lombok.var;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceIplm implements ProductService {
    private final CategoryRepo categoryRepo;
    private final ManufacturerRepo manufacturerRepo;

    private final ProductRepo productRepo;
    private final ProductOptionRepo productOptionRepo;
    private final ProductOptionService productOptionService;
    private final ProductImageService productImageService;
    private final TechnicalService technicalService;
    private final CloudinaryUpload cloudinaryUpload;

    private final EventRepo eventRepo;
    @Autowired
    private final ModelMapper mapper;
    @PersistenceContext
    private EntityManager entityManager;

    private final String PREFIX_URL_IMAGE = "https://res.cloudinary.com/quangdangcloud/image/upload";

    @Override
    public Product saveNewProduct(ProductReq productReq) {
        Product product = new Product();
        Category category = categoryRepo.getReferenceById(productReq.getCategoryId());
        Category subcategory = null;
        if (productReq.getSubCategoryId() != null) {
            subcategory = categoryRepo.getReferenceById(productReq.getSubCategoryId());
        }
        Manufacturer manufacturer = manufacturerRepo.getReferenceById(productReq.getManufacturerId());
        product = mapper.map(productReq, Product.class);
        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setManufacturer(manufacturer);

        productRepo.save(product);
        Product finalProduct = product;
        if (productReq.getImages() != null) {

            productReq.getImages().forEach(image -> {
                image.setProductId(finalProduct.getId());
            });
            productImageService.saveNewImage(productReq.getImages());
        }

        if (productReq.getProductOptions() != null) {
            List<ProductOpReq> productOptions = productReq.getProductOptions();
            productOptions.forEach(productOpReq -> {
                productOpReq.setProductId(finalProduct.getId());
                productOptionService.saveNewProductOption(productOpReq);
            });
        }

        if (productReq.getTechs() != null) {
            productReq.getTechs().forEach(tech -> {
                tech.setProductId(finalProduct.getId());
            });
            technicalService.saveAllProductTech(productReq.getTechs());
        }
        return product;
    }

    @Override
    public ProductDetailResp findProductById(Long productId) {
        var productOp = productRepo.findById(productId);

        Product product = productOp.orElse(null);
        if (product == null)
            return null;

        List<ProductOption> productOptions = productOptionRepo.findAllByProduct(product);
        List<ProductOptionResp> productOptionResps = new ArrayList<>();
        productOptions.forEach(productOption -> {
            ProductOptionResp productOptionResp = mapper.map(productOption,ProductOptionResp.class);
            productOptionResp.setMarketPrice(MoneyConvert.calculaterPrice(productOption.getPrice(),productOption.getPromotion()));
            productOptionResps.add(productOptionResp);
        });

        List<ProductImage> productImages = product.getProductImages();
        ProductDetailResp productResp = mapper.map(product,ProductDetailResp.class);
        productResp.setProductOptions(productOptionResps);
        productResp.setRate(Math.round(product.getRate()*100.0)/100.0);

        Map<String, List<ProductImage>> groupedByColor =
                productImages.stream().collect(Collectors.groupingBy(productImage -> productImage.getColor().getName()));
        List<ImageProduct> imageProducts = new ArrayList<>();
        for(String color : groupedByColor.keySet()){
            imageProducts.add(new ImageProduct(color,groupedByColor.get(color)));
        }
        productResp.setImages(imageProducts);

        productResp.setDetailSpecs(productTechsToDetailSpecs(product));
        productResp.setEvents(setEventList(product));
        return productResp;
    }

    private List<Event> setEventList(Product product){
        List<Event> returnList = new ArrayList<>();
        List<Product> productList = new ArrayList<>();
        productList.add(product);

        returnList = eventRepo.findEventByProductListInAndExpireTimeGreaterThan(productList, LocalDateTime.now());
        for(Event event: returnList){
            entityManager.detach(event);
            event.setProductList(null);
        }
        return returnList;
    }
    @Override
    public Product updateProduct(ProductReq productReq) {
        var check = productRepo.findById(productReq.getId());
        if (!check.isPresent())
            throw new AppException(404, "Product ID not found");
        Product product = check.get();

        if (productReq.getCategoryId() != null && !productReq.getCategoryId().equals(product.getCategory().getId())) {
            var checkCate = categoryRepo.existsById(productReq.getCategoryId());
            if (checkCate) {
                product.setCategory(categoryRepo.getReferenceById(productReq.getCategoryId()));
            }
        }
        if (productReq.getManufacturerId() != null && !productReq.getManufacturerId().equals(product.getManufacturer().getId())) {
            var checkManu = manufacturerRepo.existsById(productReq.getManufacturerId());
            if (checkManu) {
                product.setManufacturer(manufacturerRepo.getReferenceById(productReq.getManufacturerId()));
            }
        }
        if (productReq.getSubCategoryId() != null && product.getSubcategory()!=null) {
            var checkSubcate = categoryRepo.existsById(productReq.getSubCategoryId());
            if (checkSubcate) {
                product.setSubcategory(categoryRepo.getReferenceById(productReq.getSubCategoryId()));
            }
        }
        productRepo.save(product);
        return product;
    }

    @Override
    public void deleteProductById(Long id) {
        Product prduct = productRepo.findById(id).orElse(null);
        if (prduct == null) {
            throw new AppException(404, "Product ID not found");
        }
        try {
            List<ProductImage> images = prduct.getProductImages();
            for (ProductImage image : images) {

                if (image.getUrlImage().startsWith(PREFIX_URL_IMAGE)) {
                    cloudinaryUpload.deleteImage(image.getUrlImage());
                }
            }
            productRepo.deleteById(id);
        } catch (Exception e) {
            throw new AppException(400, "Can't delete because have order for this product ID");
        }
    }

    @Override
    public List<SearchProductResp> getProductByKeyword(String keyword, Long manufacturerId, Long categoryId, Long subCategoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Manufacturer manufacturer = null;
        if (manufacturerId != 0 && manufacturerRepo.existsById(manufacturerId))
            manufacturer = manufacturerRepo.getReferenceById(manufacturerId);
        Category category = null;
        if (categoryId != 0 && categoryRepo.existsById(categoryId))
            category = categoryRepo.getReferenceById(categoryId);
        Category subcategory = null;
        if (subCategoryId != 0 && categoryRepo.existsById(subCategoryId))
            subcategory = categoryRepo.getReferenceById(subCategoryId);

        List<Product> products = productRepo.findAllByKeyword(keyword.toLowerCase(), category, subcategory, manufacturer, pageable);

        return processResponseProductList(products);
    }


    @Override
    public List<SearchProductResp> search(String keyword, int page, int size) {
        List<Product> products = productRepo.search(keyword, PageRequest.of(page - 1, size));

        return processResponseProductList(products);
    }

    List<SearchProductResp> processResponseProductList(List<Product> productsList) {
        List<SearchProductResp> productRespList = new ArrayList<>();
        for (Product product : productsList) {
            SearchProductResp searchProductRespDTO = mapper.map(product, SearchProductResp.class);
            if (product.getProductImages() != null) {
                searchProductRespDTO.setImage(product.getProductImages().get(0).getUrlImage());
            }
            if (product.getProductOptions() != null) {
                ProductOption productOption = product.getProductOptions().get(0);
                searchProductRespDTO.setOption(productOption);
                searchProductRespDTO.setPromotion(productOption.getPromotion());
                searchProductRespDTO.setPrice(productOption.getPrice());
                if (productOption.getPromotion() == 0) {
                    searchProductRespDTO.setMarketPrice(productOption.getPrice());
                } else {
                    searchProductRespDTO.setMarketPrice(MoneyConvert.calculaterPrice(productOption.getPrice(),productOption.getPromotion()));
                }
            }

            searchProductRespDTO.setDetailSpecs(productTechsToDetailSpecs(product));

            productRespList.add(searchProductRespDTO);
        }
        return productRespList;
    }


    @Override
    public void disableProduct(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(()-> new NotFoundException("Product ID not found"));
        product.setEnable(!product.isEnable());
        productRepo.save(product);
    }

    private List<DetailSpecs> productTechsToDetailSpecs(Product product){
        List<DetailSpecs> detailSpecs = new ArrayList<>();
        List<ProductTechnical> productTechnicals = product.getProductTechnicals();
        productTechnicals.forEach(productTechnical -> {
            detailSpecs.add(new DetailSpecs(productTechnical.getId(),productTechnical.getTechnical().getName(),productTechnical.getInfo()));
        });
        return detailSpecs;
    }

}
