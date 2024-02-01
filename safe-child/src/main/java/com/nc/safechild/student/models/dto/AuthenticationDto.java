package com.nc.safechild.student.models.dto;

import com.nc.safechild.base.utils.Validate;
import static com.nc.safechild.base.utils.MessageConstants.*;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/27/23
 **/
public record AuthenticationDto(
        String username,
        String pin
) {
    public void validate(){
        Validate.notEmpty(username, NULL_USERNAME);
        Validate.notEmpty(pin, NULL_PIN);
    }
}
