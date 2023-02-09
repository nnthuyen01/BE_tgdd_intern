package com.group04.tgdd.service;

import com.group04.tgdd.model.Manufacturer;

import java.util.List;

public interface ManufacturerService {
    Manufacturer findById(Long manufacturerId);
    List<Manufacturer> findAll();
    Manufacturer save(Manufacturer manufacturer);
    Manufacturer updateManufacturer(Manufacturer manufacturer);
    boolean deleteManufacturer(Long id);
}
