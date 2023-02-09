package com.group04.tgdd.service.iplm;

import com.group04.tgdd.model.Manufacturer;
import com.group04.tgdd.repository.ManufacturerRepo;
import com.group04.tgdd.service.ManufacturerService;
import lombok.RequiredArgsConstructor;

// import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ManufacturerIplm implements ManufacturerService {

    private final ManufacturerRepo manufacturerRepo;


    @Override
    public Manufacturer findById(Long id) {
        var payment = manufacturerRepo.findById(id);
        return payment.orElse(null);
    }

    @Override
    public List<Manufacturer> findAll() {
        return manufacturerRepo.findAll();
    }

    @Override
    public Manufacturer save(Manufacturer manufacturer) {
        return manufacturerRepo.save(manufacturer);
    }

    @Override
    public Manufacturer updateManufacturer(Manufacturer manufacturer) {
        Manufacturer manufacturerUpdate = findById(manufacturer.getId());
        if (manufacturerUpdate!=null) {
            manufacturerUpdate.setName(manufacturer.getName());
            return manufacturerUpdate;
        }
        else return null;
    }

    @Override
    public boolean deleteManufacturer(Long manufacturerId) {
        boolean check = manufacturerRepo.existsById(manufacturerId);
        if (check){
            manufacturerRepo.deleteById(manufacturerId);
            return true;
        }else {
            return false;
        }
    }
}
