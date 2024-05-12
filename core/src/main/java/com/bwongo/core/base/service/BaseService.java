package com.bwongo.core.base.service;

import com.bwongo.commons.models.exceptions.BadRequestException;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.model.dto.CountryResponseDto;
import com.bwongo.core.base.model.dto.DistrictResponseDto;
import com.bwongo.core.base.model.enums.FileDirEnum;
import com.bwongo.core.base.repository.TCountryRepository;
import com.bwongo.core.base.repository.TDistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.bwongo.commons.models.text.StringUtil.randomString;
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

    @Value("${file.root-dir}")
    private String fileRootDir;

    public List<DistrictResponseDto> getAllDistrictByCountryId(Long countryId, Pageable pageable){

        var existingCountry = countryRepository.findById(countryId);
        Validate.isPresent(this, existingCountry, COUNTRY_WITH_ID_NOT_FOUND, countryId);
        final var country = existingCountry.get();

        return districtRepository.findAllByCountry(country, pageable).stream()
                .map(baseDtoService::districtToDto)
                .toList();
    }

    public List<CountryResponseDto> getAllCountries(){
        return countryRepository.findAll().stream()
                .map(baseDtoService::countryToDto)
                .toList();
    }

    public void uploadFile(MultipartFile file, FileDirEnum fileDir) {
        /*try {
            if(file.isEmpty()) {
                throw new BadRequestException(this, "Empty file");
            }
            Path destination = Paths.get(fileRootDir +"/"+ fileDir.getValue()).resolve(generateFileName()).normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), destination);
        } catch(IOException e) {
            throw new BadRequestException(this, e.getMessage());
        }*/

        try {
            Path destination = Paths.get(fileRootDir +"/"+ fileDir.getValue());
            Files.copy(file.getInputStream(), destination.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }


    private String generateFileName() {
        return randomString().substring(0,11);
    }
}
