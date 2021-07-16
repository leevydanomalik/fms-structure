package com.bitozen.fms.structure.svc.assembler;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bitozen.fms.common.assembler.IObjectAssembler;
import com.bitozen.fms.common.dto.CreationalSpecificationDTO;
import com.bitozen.fms.common.dto.GenericAccessTokenDTO;
import com.bitozen.fms.common.dto.share.BizparOptimizeDTO;
import com.bitozen.fms.common.dto.share.EnterpriseStructureOptimizeDTO;
import com.bitozen.fms.structure.domain.EnterpriseStructure;
import com.bitozen.fms.structure.domain.common.CreationalSpecification;
import com.bitozen.fms.structure.domain.repository.EnterpriseStructureRepository;
import com.bitozen.fms.structure.dto.EnterpriseStructureNodeCreateDTO;
import com.bitozen.fms.structure.dto.EnterpriseStructureNodeDTO;
import com.bitozen.fms.structure.dto.MetadataDTO;
import com.bitozen.fms.structure.svc.helper.EnterpriseStructureHelper;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EnterpriseStructureDTOAssembler implements IObjectAssembler<EnterpriseStructure, EnterpriseStructureNodeDTO>{

	@Autowired
	private EnterpriseStructureRepository repository;
	
	@Autowired
	private EnterpriseStructureHelper helper;
	
	@Autowired
	private MetadataAssembler metaAssembler;
	
	public EnterpriseStructureNodeDTO toDTO(@NotNull EnterpriseStructure domainObject) {
		try {
			return new EnterpriseStructureNodeDTO(
					domainObject.getEsID(),
					domainObject.getEsName(),
					getBizpar(domainObject.getEsType()),
					toDTOChild(domainObject.getEsChildren()),
					domainObject.getEsParent() == null ? new EnterpriseStructureOptimizeDTO() : domainObject.getEsParent().equals("") ? new EnterpriseStructureOptimizeDTO() : toDTOOptimize(repository.findOneByEsID(domainObject.getEsParent()).get()),
					metaAssembler.toMetadata(domainObject.getEsMetadata()),
					domainObject.getEsToken(),
					domainObject.getEsCreational()== null ? new CreationalSpecificationDTO().getInstance() : domainObject.getEsCreational()
					);
		} catch(Exception e) {
			e.getMessage();
		}
		return new EnterpriseStructureNodeDTO();
	}

	public EnterpriseStructureOptimizeDTO toDTOOptimize(@NotNull EnterpriseStructure domain) {
		try {
			return new EnterpriseStructureOptimizeDTO(
					domain.getEsID(),
					domain.getEsName(),
					getBizpar(domain.getEsType())
					);
		} catch(Exception e) {
			e.getMessage();
		}
		return new EnterpriseStructureOptimizeDTO();
	}
	
	public EnterpriseStructure toDomain(EnterpriseStructureNodeDTO dtoObject) {
		return new EnterpriseStructure();
	}
	
	public EnterpriseStructure toDomainCreate(@NotNull EnterpriseStructureNodeCreateDTO dto) {
		try {
			
			if(dto.getEsChildren() != null && !(dto.getEsChildren().isEmpty())) {
				setChild(dto.getEsID(), dto.getEsChildren());
			}
			
			if(dto.getEsParent() != null && !(dto.getEsParent().equals(""))) {
				setParent(dto.getEsID(), dto.getEsParent());
			}
			
			return new EnterpriseStructure(
					repository.findFirstByOrderByIdDesc().get().getId() + 1,
					dto.getEsID(),
					dto.getEsName(),
					dto.getEsType(),
					dto.getEsChildren() == null ? new ArrayList<>() : dto.getEsChildren(),
					dto.getEsParent(),
					dto.getEsMetadata(),
					dto.getEsToken(),
					dto.getEsCreational() == null ? new CreationalSpecificationDTO().getInstance() : dto.getEsCreational()
					);
		} catch(Exception e) {
			log.info(e.getMessage());
		}
		return new EnterpriseStructure();
	}

	public List<EnterpriseStructureNodeDTO> toDTOs(@NotNull Set<EnterpriseStructure> domainObjects) {
		List<EnterpriseStructureNodeDTO> datas = new ArrayList<>();
        domainObjects.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

	public List<EnterpriseStructureNodeDTO> toDTOs(@NotNull List<EnterpriseStructure> domainObjects) {
		List<EnterpriseStructureNodeDTO> datas = new ArrayList<>();
        domainObjects.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

	public List<EnterpriseStructure> toDomains(@NotNull List<EnterpriseStructureNodeDTO> dtoObjects) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public EnterpriseStructure update(@NotNull EnterpriseStructure domain, @NotNull EnterpriseStructureNodeCreateDTO dto) {
		domain.setEsName(dto.getEsName());
		domain.setEsType(dto.getEsType());
		domain.setEsChildren(dto.getEsChildren() == null ? new ArrayList<>() : dto.getEsChildren());
		domain.setEsParent(dto.getEsParent() == null ? "" : dto.getEsParent());
		domain.setEsMetadata(dto.getEsMetadata());
		domain.setEsToken(dto.getEsToken());
		domain.getEsCreational().setModifiedBy(dto.getEsCreational().getModifiedBy() == null ? "SYSTEM" : dto.getEsCreational().getModifiedBy());
		domain.getEsCreational().setModifiedDate(dto.getEsCreational().getModifiedDate() == null ? new Date() : dto.getEsCreational().getModifiedDate());
		return domain;
	}
	
	public BizparOptimizeDTO getBizpar(@NotNull String key) {
		return helper.findBizparByKey(key);
	}
	
	//ES Parent-Children assemblers
	
	public List<EnterpriseStructureOptimizeDTO> toDTOChild(@NotNull List<String> esIDs) {
		List<EnterpriseStructureOptimizeDTO> dtos = new ArrayList<>();
		esIDs.stream().forEach((o) -> {
			dtos.add(toDTOOptimize(repository.findOneByEsID(o).get()));
		});
		return dtos;
	}

	public void setParent(@NotNull String esID, @NotNull String parentID) {
		try {
			Optional<EnterpriseStructure> es = repository.findOneByEsID(parentID);
			if(es.isPresent()) {
				List<String> esChilds = es.get().getEsChildren();
				esChilds.add(esID);
				es.get().setEsChildren(esChilds);
				repository.save(es.get());
			}
		} catch(Exception e) {
			log.info(e.getMessage());
		}
	}
	
	public void setChild(@NotNull String esID, @NotNull List<String> childrenIDs) {
		childrenIDs.stream().forEach((o) -> {
			Optional<EnterpriseStructure> es = repository.findOneByEsID(o);
			if(es.isPresent()) {
				if(es.get().getEsParent() != null && !(es.get().getEsParent().equals(""))) {
					removeChild(es.get().getEsParent(), es.get().getEsID());
				}
				es.get().setEsParent(esID);
			}
			repository.save(es.get());
		});
	}

	public void removeChild(@NotNull String esID, @NotNull String childID) {
		try {
			Optional<EnterpriseStructure> es = repository.findOneByEsID(esID);
			List<String> childs = new ArrayList<>();
			if(es.isPresent()) {
				childs = es.get().getEsChildren();
				childs.remove(childID);
				es.get().setEsChildren(childs);
			}
			repository.save(es.get());
		} catch(Exception e) {
			log.info(e.getMessage());
		}
		
	}
	
	public void updateChild(@NotNull String esID, @NotNull List<String> afterChild, @NotNull List<String> beforeChild) {
		afterChild.stream().forEach((o) -> {
			if(!(beforeChild.contains(o))) {
				Optional<EnterpriseStructure> es = repository.findOneByEsID(o);
				if(es.isPresent()) {
					if(es.get().getEsParent() != null && !(es.get().getEsParent().equals(""))) {
						removeChild(es.get().getEsParent(), es.get().getEsID());
					}
					es.get().setEsParent(esID);
				}
				repository.save(es.get());
			}
		});
		
		beforeChild.stream().forEach((o) -> {
			if(!(afterChild.contains(o))) {
				Optional<EnterpriseStructure> es = repository.findOneByEsID(o);
				if(es.isPresent()) {
					es.get().setEsParent("");
				}
				repository.save(es.get());
			}
		});
	}
	
	public void updateParent(@NotNull String esID, @NotNull String beforeParent, @NotNull String afterParent) {
		try {
			Optional<EnterpriseStructure> es = repository.findOneByEsID(afterParent);
			if(es.isPresent()) {
				List<String> esChilds = es.get().getEsChildren();
				esChilds.add(esID);
				es.get().setEsChildren(esChilds);
				repository.save(es.get());
			}
			
			es = repository.findOneByEsID(afterParent);
			if(es.isPresent()) {
				List<String> esChilds = es.get().getEsChildren();
				esChilds.remove(esID);
				es.get().setEsChildren(esChilds);
				repository.save(es.get());
			}
		} catch(Exception e) {
			log.info(e.getMessage());
		}
	}
	
	public void deleteChild(@NotNull String esID, @NotNull List<String> childIDs) {
		childIDs.stream().forEach((o) -> {
				Optional<EnterpriseStructure> es = repository.findOneByEsID(o);
				if(es.isPresent()) {
					es.get().setEsParent("");
				}
				repository.save(es.get());
		});
	}
	
	public void deleteParent(@NotNull String esID, @NotNull String parentID) {
		try {
			Optional<EnterpriseStructure> es = repository.findOneByEsID(parentID);
			if(es.isPresent()) {
				List<String> esChilds = es.get().getEsChildren();
				esChilds.remove(esID);
				es.get().setEsChildren(esChilds);
				repository.save(es.get());
			}
		} catch(Exception e) {
			log.info(e.getMessage());
		}
	}
}
