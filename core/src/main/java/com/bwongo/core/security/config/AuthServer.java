package com.bwongo.core.security.config;

import com.bwongo.core.security.utils.CustomAccessTokenConverter;
import com.bwongo.core.security.utils.CustomOauth2RequestFactory;
import com.bwongo.core.security.utils.UserDetailsTokenEnhancer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/5/23
 **/
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthServer extends AuthorizationServerConfigurerAdapter {

    @Value("${app.security.jwt.keystore-password}")
    private String keyStorePassword;

    @Value("${app.security.jwt.keystore-location}")
    private String keyStoreLocation;

    @Value("${app.security.jwt.key-alias}")
    private String keyAlias;

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final ClientDetailsService clientDetailsService;
    private final AuthenticationManager authenticationManager;

    @Bean
    public OAuth2RequestFactory requestFactory(){
        var requestFactory = new CustomOauth2RequestFactory(clientDetailsService);
        requestFactory.setCheckUserScopes(true);
        return requestFactory;
    }

    @Bean
    JwtAccessTokenConverter jwtAccessTokenConverter() {
        var converter = new CustomAccessTokenConverter();
        converter.setKeyPair(new KeyStoreKeyFactory(new ClassPathResource(keyStoreLocation),
                keyStorePassword.toCharArray()).getKeyPair(keyAlias));
        return converter;
    }

    @Bean
    public JwtTokenStore tokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter(){
        return new TokenEndpointAuthenticationFilter(authenticationManager, requestFactory());
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();

        tokenEnhancerChain.setTokenEnhancers(List.of(new UserDetailsTokenEnhancer(), jwtAccessTokenConverter()));

        endpoints
                .tokenEnhancer(tokenEnhancerChain)
                .tokenStore(tokenStore())
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET,
                        HttpMethod.POST,
                        HttpMethod.PUT,
                        HttpMethod.PATCH,
                        HttpMethod.HEAD,
                        HttpMethod.OPTIONS,
                        HttpMethod.DELETE);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }
}

