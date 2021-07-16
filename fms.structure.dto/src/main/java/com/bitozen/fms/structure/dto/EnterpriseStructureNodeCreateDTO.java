package com.bitozen.fms.structure.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bitozen.fms.common.dto.CreationalSpecificationDTO;
import com.bitozen.fms.common.dto.GenericAccessTokenDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EnterpriseStructureNodeCreateDTO implements Serializable{

	private String esID;
	private String esName;
	private String esType;
	private List<String> esChildren;
	private String esParent;
	private MetadataCreateDTO esMetadata;
	private GenericAccessTokenDTO esToken;
	private CreationalSpecificationDTO esCreational;
	
	@JsonIgnore
    public EnterpriseStructureNodeCreateDTO getInstance() {
		return new EnterpriseStructureNodeCreateDTO(
				UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                "NAMA ES",
                "TEST_4",
                new ArrayList<>(),
                null,
                new MetadataCreateDTO().getInstance(),
                new GenericAccessTokenDTO().getInstance(),
                new CreationalSpecificationDTO().getInstance()
				);
	}
}
