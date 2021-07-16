package com.bitozen.fms.structure.common.util;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component("structureRequestUtil")
@PropertySource("classpath:fms.structure.token.properties")
public class RequestUtil {

    private final String HEADER_KEY = "Authorization";

    @Value("${fms.structure.token}")
    private String STRUCTURE_TOKEN;

    public HttpEntity<String> getPreFormattedRequestWithToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.add(HEADER_KEY, STRUCTURE_TOKEN);

        return new HttpEntity<>(httpHeaders);
    }

}
