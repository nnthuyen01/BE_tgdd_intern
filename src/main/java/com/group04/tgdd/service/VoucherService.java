package com.group04.tgdd.service;

import com.group04.tgdd.dto.VoucherReq;
import com.group04.tgdd.model.Voucher;

import java.util.List;

public interface VoucherService {
    Voucher save(VoucherReq voucherReq);

    List<Voucher> get(String code);

    void delete(String code);

    Voucher update(VoucherReq voucherReq);

    List<Voucher> getByCategory(Long categoryId);

    Voucher addCategoryToVoucher(Long voucherId, Long categoryId);
}
