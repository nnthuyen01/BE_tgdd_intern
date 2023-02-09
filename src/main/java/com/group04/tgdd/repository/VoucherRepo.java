package com.group04.tgdd.repository;

import com.group04.tgdd.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepo extends JpaRepository<Voucher, Long> {

    @Query("SELECT v FROM Voucher as v WHERE v.code = :code ")
    List<Voucher> findByCode(String code);
    void deleteVoucherByCode(String code);

}
