package com.group04.tgdd.service.iplm;

import com.group04.tgdd.model.Color;
import com.group04.tgdd.repository.ColorRepo;
import com.group04.tgdd.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ColorIplm implements ColorService {

    private final ColorRepo colorRepo;
    @Override
    public Color findById(Long Id) {
        Optional<Color> color = colorRepo.findById(Id);
        return color.orElse(null);
    }

    @Override
    public List<Color> findAll() {
        return colorRepo.findAll();
    }

    @Override
    public Color save(Color color) {
        return colorRepo.save(color);
    }

    @Override
    public Color updateColor(Color color) {
        Color colorUpdate = findById(color.getId());
        if (colorUpdate!=null) {
            colorUpdate.setName(color.getName());
            return colorUpdate;
        }
        else return null;
    }

    @Override
    public boolean deleteColor(Long colorId) {
        boolean check = colorRepo.existsById(colorId);
        if (check){
            colorRepo.deleteById(colorId);
            return true;
        }else {
            return false;
        }
    }
}
