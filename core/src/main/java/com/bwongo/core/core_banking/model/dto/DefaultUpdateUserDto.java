package com.bwongo.core.core_banking.model.dto;

import nl.strohalm.cyclos.webservices.model.RegistrationFieldValueVO;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/3/23
 **/
public record DefaultUpdateUserDto(
        String fullName,
        String username,
        String email,
        List<RegistrationFieldValueVO> customFields
) {
}
