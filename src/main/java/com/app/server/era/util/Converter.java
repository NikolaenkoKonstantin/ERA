package com.app.server.era.util;

import com.app.server.era.DTO.DimensionDTO;
import com.app.server.era.model.Dimension;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Converter {
    private final ModelMapper modelMapper;

    public Dimension convertToDimension(DimensionDTO dto){
        return modelMapper.map(dto, Dimension.class);
    }
}
