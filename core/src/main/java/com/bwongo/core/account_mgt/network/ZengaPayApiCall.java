package com.bwongo.core.account_mgt.network;

import com.bwongo.core.account_mgt.model.dto.CollectionRequestDto;
import com.bwongo.core.account_mgt.model.dto.CollectionResponseDto;
import com.bwongo.core.account_mgt.model.dto.StatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
@Service
@RequiredArgsConstructor
public class ZengaPayApiCall {

    @Value("${zenga-pay.url.collections}")
    private String apiCollectionUrl;

    @Value("${zenga-pay.url.status}")
    private String apiStatusUrl;

    @Value("${zenga-pay.access-token}")
    private String accessToken;

    private final WebClient.Builder webClientBuilder;

    public CollectionResponseDto initiatePayment(CollectionRequestDto collectionRequestDto){
        return webClientBuilder
                .build()
                .post()
                .uri(apiCollectionUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(collectionRequestDto)
                .retrieve()
                .bodyToMono(CollectionResponseDto.class)
                .block();
    }

    public StatusResponseDto getPaymentStatus(String transactionReference){
        return webClientBuilder
                .build()
                .get()
                .uri(apiStatusUrl + transactionReference)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StatusResponseDto.class)
                .block();
    }
}
