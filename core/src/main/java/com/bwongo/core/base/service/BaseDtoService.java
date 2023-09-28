package com.bwongo.core.base.service;

import com.bwongo.core.base.model.dto.CountryResponseDto;
import com.bwongo.core.base.model.dto.DistrictResponseDto;
import com.bwongo.core.base.model.dto.LocationResponseDto;
import com.bwongo.core.base.model.jpa.TCountry;
import com.bwongo.core.base.model.jpa.TDistrict;
import com.bwongo.core.base.model.jpa.TLocation;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/15/23
 **/
@Service
public class BaseDtoService {

    public CountryResponseDto countryToDto(TCountry country){

        if(country == null){
            return null;
        }

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

    public DistrictResponseDto districtToDto(TDistrict district){

        if(district == null){
            return null;
        }

        return new DistrictResponseDto(
                district.getId(),
                district.getCreatedOn(),
                district.getModifiedOn(),
                countryToDto(district.getCountry()),
                district.getName(),
                district.getRegion()
        );
    }

    public LocationResponseDto locationToDto(TLocation location){

        if(location == null){
            return null;
        }

        return new LocationResponseDto(
                location.getId(),
                location.getCreatedOn(),
                location.getModifiedOn(),
                location.getLatitude(),
                location.getLongitude()
        );
    }
}
