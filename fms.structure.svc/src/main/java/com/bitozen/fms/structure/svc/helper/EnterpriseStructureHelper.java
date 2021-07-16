package com.bitozen.fms.structure.svc.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bitozen.fms.common.dto.GenericResponseDTO;
import com.bitozen.fms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.fms.common.status.ResponseStatus;
import com.bitozen.fms.structure.common.util.RequestUtil;
import com.bitozen.fms.structure.common.util.RestClientUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EnterpriseStructureHelper {

	@Value("${fms.bizpar.url}")
    private String BIZPAR_URL;

    @Value("${fms.bizpar.get.optimize.by.key}")
    private String BIZPAR_GET_OPTIMIZE_BY_KEY_URI;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private RestClientUtil restClientUtil;
    
    public BizparOptimizeDTO findBizparByKey(String bizparKey) {
        if (bizparKey != null && !bizparKey.trim().equalsIgnoreCase("") && !bizparKey.isEmpty()) {
            ResponseEntity<GenericResponseDTO<BizparOptimizeDTO>> data = restClientUtil.restServiceExchange(
                    BIZPAR_URL,
                    BIZPAR_GET_OPTIMIZE_BY_KEY_URI,
                    HttpMethod.GET,
                    requestUtil.getPreFormattedRequestWithToken(),
                    GenericResponseDTO.class,
                    bizparKey);
            if (data.getBody().getStatus().equals(ResponseStatus.S)) {
                return objectMapper.convertValue(data.getBody().getData(), BizparOptimizeDTO.class);
            }
        }
        return new BizparOptimizeDTO();
    } 
}
