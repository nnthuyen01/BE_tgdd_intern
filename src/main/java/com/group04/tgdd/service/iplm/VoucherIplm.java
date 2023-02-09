package com.group04.tgdd.service.iplm;

import com.group04.tgdd.dto.VoucherReq;
import com.group04.tgdd.model.Category;
import com.group04.tgdd.model.Voucher;
import com.group04.tgdd.repository.CategoryRepo;
import com.group04.tgdd.repository.VoucherRepo;
import com.group04.tgdd.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class VoucherIplm implements VoucherService {

    private final VoucherRepo voucherRepo;
    private final CategoryRepo categoryRepo;
    @Override
    public Voucher save(VoucherReq voucherReq) {

        List<Voucher> checkList = voucherRepo.findByCode(voucherReq.getCode());
        if(checkList.size() > 0){
            return null;
        }

        Double promotion = voucherReq.getPromotion().doubleValue();
        String code = voucherReq.getCode();
        LocalDateTime expireTime = voucherReq.getExpireTime();
        List<Long> id_list = voucherReq.getCategoryIdList();
        List<Category> categoryList = categoryList(id_list);

        Voucher voucher = new Voucher();
        voucher.setCode(code);
        voucher.setPromotion(promotion);
        voucher.setExpireTime(expireTime);
        voucher.setCategoryList(categoryList);

        return voucherRepo.save(voucher);
    }

    @Override
    public List<Voucher> get(String code) {
        return voucherRepo.findByCode(code);
    }

    @Override
    public void delete(String code) {
        voucherRepo.deleteVoucherByCode(code);
    }

    @Override
    public Voucher update(VoucherReq voucherReq) {
        Long id = voucherReq.getId();
        Double promotion = voucherReq.getPromotion().doubleValue();
        String code = voucherReq.getCode();
        LocalDateTime expireTime = voucherReq.getExpireTime();
        List<Long> id_list = voucherReq.getCategoryIdList();
        List<Category> categoryList = categoryList(id_list);

        Voucher updatedVoucher = voucherRepo.findById(id).get();

        if (updatedVoucher != null) {

            if (code == null && code.trim().equals(""))
                updatedVoucher.setCode(code.trim().replaceAll("  "," "));
            else
                updatedVoucher.setCode(code);
            if (promotion ==  null)
                updatedVoucher.setPromotion(0d);
            else
                updatedVoucher.setPromotion(promotion);
            if (expireTime == null && expireTime.toString().trim().equals(""))
                updatedVoucher.setExpireTime(null);
            else
                updatedVoucher.setExpireTime(expireTime);

            updatedVoucher.setCategoryList(categoryList);

            return voucherRepo.save(updatedVoucher);

        }
        else
            return null;
    }

    @Override
    public List<Voucher> getByCategory(Long categoryId) {
        List<Voucher> allVouchers = voucherRepo.findAll();
        List<Voucher> returnVouchers = new ArrayList<>();
        for(Voucher currentVoucher: allVouchers){
            for(Category category: currentVoucher.getCategoryList()){
                if(category.getId() == categoryId){
                    returnVouchers.add(currentVoucher);
                }
            }

        }
        return returnVouchers;
    }

    @Override
    public Voucher addCategoryToVoucher(Long voucherId, Long categoryId) {
        /*Find category and Voucher*/
        Category category = categoryRepo.findById(categoryId).get();

        if(category == null){
            return null;
        }
        Voucher updatedVoucher = voucherRepo.findById(voucherId).get();

        if(updatedVoucher == null){
            return null;
        }

        /*Check if the voucher already contains the category*/
        Boolean existed = false;
        for(Category item: updatedVoucher.getCategoryList()){
            if(item.getId() == categoryId){
                existed = true;
            }
        }
        /*If not in the list*/
        if(existed == false){
            updatedVoucher.getCategoryList().add(category);
        }

        return voucherRepo.save(updatedVoucher);

    }

    private List<Category> categoryList(List<Long> category_id_list){
        List<Category> categoryList = new ArrayList<>();
        for(Long id: category_id_list){
            try{
                Category category = categoryRepo.findById(id).get();
                if(category != null){
                    categoryList.add(category);
                }
            }
            catch (Exception e){

            }

        }
        return categoryList;
    }

}
