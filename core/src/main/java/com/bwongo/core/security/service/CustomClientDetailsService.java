package com.bwongo.core.security.service;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.SetUtils;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.security.models.ClientApp;
import com.bwongo.core.user_mgt.model.jpa.TAppClient;
import com.bwongo.core.user_mgt.repository.TAppClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bwongo.core.security.utils.MsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/10/23
 **/
@Service
@Primary
@RequiredArgsConstructor
public class CustomClientDetailsService implements ClientDetailsService {

    private final TAppClientRepository appClientRepository;
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Optional<TAppClient> appClient = appClientRepository.findByName(clientId);

        Validate.isTrue(appClient.isPresent(), ExceptionType.INSUFFICIENT_AUTH, APP_CLIENT_NOT_FOUND, clientId);

        TAppClient client = null;
        if(appClient.isPresent()){
            client = appClient.get();
        }

        Validate.isTrue(client !=null && client.getEnabled(), ExceptionType.INSUFFICIENT_AUTH, APP_CLIENT_NOT_ENABLED,clientId);

        return getClientDetails(client);
    }

    private ClientDetails getClientDetails(TAppClient appClient){

        ClientApp details = new ClientApp();
        BeanUtils.copyProperties(appClient,details);
        details.setAuthorizedGrantTypes(SetUtils.getSetFromStringWithSeparator(appClient.getGrantTypes()));
        details.setRegisteredRedirectUri(null);
        details.setScopes(SetUtils.getSetFromStringWithSeparator(appClient.getScope()));
        details.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(appClient.getAuthorities()));
        return details;

    }
}
