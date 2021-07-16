package com.bitozen.fms.structure.dto;

import java.io.Serializable;
import java.util.List;

import com.bitozen.fms.common.dto.CreationalSpecificationDTO;
import com.bitozen.fms.common.dto.GenericAccessTokenDTO;
import com.bitozen.fms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.fms.common.dto.share.EnterpriseStructureOptimizeDTO;

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
public class EnterpriseStructureNodeDTO implements Serializable {

	private String esID;
	private String esName;
	private BizparOptimizeDTO esType;
	private List<EnterpriseStructureOptimizeDTO> esChildren;
	private EnterpriseStructureOptimizeDTO esParent;
	private MetadataDTO esMetadata;
	private GenericAccessTokenDTO esToken;
	private CreationalSpecificationDTO esCreational;
}
