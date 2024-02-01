package com.bwongo.core.base.service;

import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.model.dto.CountryResponseDto;
import com.bwongo.core.base.model.dto.DistrictResponseDto;
import com.bwongo.core.base.repository.TCountryRepository;
import com.bwongo.core.base.repository.TDistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.bwongo.core.base.utils.BasicMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/28/23
 **/
@Service
@RequiredArgsConstructor
public class BaseService {

    private final TDistrictRepository districtRepository;
    private final TCountryRepository countryRepository;
    private final BaseDtoService baseDtoService;

    public List<DistrictResponseDto> getAllDistrictByCountryId(Long countryId, Pageable pageable){

        var existingCountry = countryRepository.findById(countryId);
        Validate.isPresent(this, existingCountry, COUNTRY_WITH_ID_NOT_FOUND, countryId);
        final var country = existingCountry.get();

        return districtRepository.findAllByCountry(country, pageable).stream()
                .map(baseDtoService::districtToDto)
                .collect(Collectors.toList());
    }

    public List<CountryResponseDto> getAllCountries(){
        return countryRepository.findAll().stream()
                .map(baseDtoService::countryToDto)
                .collect(Collectors.toList());
    }
}
