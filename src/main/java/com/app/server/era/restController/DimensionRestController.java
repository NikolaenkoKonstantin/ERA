package com.app.server.era.restController;

import com.app.server.era.DTO.DimensionDTO;
import com.app.server.era.model.Dimension;
import com.app.server.era.service.DimensionService;
import com.app.server.era.util.Converter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DimensionRestController {
    private final DimensionService dimService;
    private final Converter converter;

    @PostMapping("/dimension")
    public ResponseEntity<HttpStatus> addDimension(@RequestBody DimensionDTO dto, BindingResult bindingResult){
        dimService.addDimension(dto.getCardNumber(), converter.convertToDimension(dto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
