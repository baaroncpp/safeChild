package com.bwongo.core.base.service;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.DateTimeUtil;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.model.jpa.AuditEntity;
import com.bwongo.core.base.model.jpa.BaseEntity;
import com.bwongo.core.security.models.LoginUser;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
@Slf4j
@Service
public class AuditService {

    public void stampLongEntity(BaseEntity entity) {
        Date date = DateTimeUtil.getCurrentUTCTime();
        if(entity.getId() == null){
            entity.setCreatedOn(date);
        }
        entity.setModifiedOn(DateTimeUtil.getCurrentUTCTime());
    }

    public void stampAuditedEntity(AuditEntity auditEntity) {
        LoginUser user = getLoggedInUser();
        Validate.notNull(this, user, ExceptionType.BAD_REQUEST,"Only a logged in user can make this change");
        Date date = DateTimeUtil.getCurrentUTCTime();
        TUser tUser = new TUser();
        tUser.setId(user.getId());

        if(auditEntity.getId() == null){
            auditEntity.setCreatedOn(date);
            auditEntity.setCreatedBy(tUser);
        }

        auditEntity.setModifiedBy(tUser);
        auditEntity.setModifiedOn(date);
    }

    public LoginUser getLoggedInUser() {
        final var authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated()){
            Map<String,?> decoded = (LinkedHashMap)( (OAuth2AuthenticationDetails)authentication.getDetails()).getDecodedDetails();
            LoginUser user = new LoginUser();
            user.setUsername((String)decoded.get("username"));
            user.setId(Long.valueOf((Integer) decoded.get("id")));
            return user;
        }

        return null;
    }
}
