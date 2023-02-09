package com.group04.tgdd.service;

import com.group04.tgdd.model.Color;

import java.util.List;

public interface ColorService {
    Color findById(Long colorId);
    List<Color> findAll();
    Color save(Color color);
    Color updateColor(Color color);
    boolean deleteColor(Long id);
}
