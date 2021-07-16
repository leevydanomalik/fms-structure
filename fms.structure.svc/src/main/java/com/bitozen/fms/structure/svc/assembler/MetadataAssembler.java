package com.bitozen.fms.structure.svc.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bitozen.fms.structure.dto.MetadataCreateDTO;
import com.bitozen.fms.structure.dto.MetadataDTO;
import com.bitozen.fms.structure.svc.helper.EnterpriseStructureHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MetadataAssembler {

	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	private EnterpriseStructureHelper helper;
	
	public MetadataDTO toMetadata(MetadataCreateDTO dto) {
		MetadataDTO meta = new MetadataDTO();
		meta.setEs(dto.getEs());
		meta.setId(dto.getId());
		meta.setTag(dto.getTag());
		meta.setType(helper.findBizparByKey(dto.getType()));
		meta.setVersion(dto.getVersion());
		return meta;
	}
}
