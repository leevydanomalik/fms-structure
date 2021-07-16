package com.bitozen.fms.structure.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.bitozen.fms.common.dto.CreationalSpecificationDTO;
import com.bitozen.fms.common.dto.GenericAccessTokenDTO;
import com.bitozen.fms.structure.dto.MetadataCreateDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Document(value = "enterprisestructure")
public class EnterpriseStructure {

	@Id
    private Long id;
	private String esID;
	private String esName;
	private String esType;
	private List<String> esChildren;
	private String esParent;
	private MetadataCreateDTO esMetadata;
	private GenericAccessTokenDTO esToken;
	private CreationalSpecificationDTO esCreational;
	
}
