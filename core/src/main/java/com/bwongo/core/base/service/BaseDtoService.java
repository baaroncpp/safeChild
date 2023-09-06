package com.bwongo.core.base.service;

import com.bwongo.core.base.model.dto.CountryResponseDto;
import com.bwongo.core.base.model.jpa.TCountry;
import org.springframework.stereotype.Service;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/15/23
 **/
@Service
public class BaseDtoService {

    public CountryResponseDto countryToDto(TCountry country){

        return new CountryResponseDto(
            country.getId(),
            country.getCreatedOn(),
            country.getModifiedOn(),
            country.getName(),
            country.getIsoAlpha2(),
            country.getIsoAlpha3(),
            country.getCountryCode()
        );
    }
}
