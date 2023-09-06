package com.bwongo.core.user_mgt.model.dto;

import com.bwongo.core.base.model.dto.CountryResponseDto;
import com.bwongo.core.base.model.enums.GenderEnum;
import com.bwongo.core.base.model.enums.IdentificationType;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
public record UserMetaResponseDto(
         Long id,
         Date createdOn,
         Date modifiedOn,
         UserResponseDto createdBy,
         UserResponseDto modifiedBy,
         String firstName,
         String lastName,
         String middleName,
         String phoneNumber,
         String phoneNumber2,
         String imagePath,
         String displayName,
         GenderEnum gender,
         Date birthDate,
         String email,
         CountryResponseDto country,
         IdentificationType identificationType,
         String identificationNumber,
         String identificationPath,
         Boolean nonVerifiedEmail,
         Boolean nonVerifiedPhoneNumber
) {
}
