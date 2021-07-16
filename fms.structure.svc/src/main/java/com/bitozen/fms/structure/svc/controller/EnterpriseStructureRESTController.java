package com.bitozen.fms.structure.svc.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bitozen.fms.common.dto.DeleteRequestDTO;
import com.bitozen.fms.common.dto.GenericResponseDTO;
import com.bitozen.fms.common.dto.GetListRequestDTO;
import com.bitozen.fms.common.dto.share.EnterpriseStructureOptimizeDTO;
import com.bitozen.fms.common.type.ProjectType;
import com.bitozen.fms.common.util.LogOpsUtil;
import com.bitozen.fms.structure.dto.EnterpriseStructureNodeCreateDTO;
import com.bitozen.fms.structure.dto.EnterpriseStructureNodeDTO;
import com.bitozen.fms.structure.svc.hystrix.EnterpriseStructureHystrixService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EnterpriseStructureRESTController {

	@Autowired
	private HttpServletRequest httpRequest;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private EnterpriseStructureHystrixService service;
	
	@RequestMapping(
			value = "/get.es.dummy", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<EnterpriseStructureNodeCreateDTO> getDummy() {
		 return ResponseEntity.status(HttpStatus.OK).body(new EnterpriseStructureNodeCreateDTO().getInstance());
	 }
	 
	 @RequestMapping(value = "/get.es.by.id/{esID}",
	    method = RequestMethod.GET,
	    produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<GenericResponseDTO<EnterpriseStructureNodeDTO>> getESByID(@PathVariable("esID") String esID) {
			try {
	            log.info(objectMapper.writeValueAsString(
	                    LogOpsUtil.getLogOps(ProjectType.CRUD, "ES", EnterpriseStructureRESTController.class.getName(),
	                            httpRequest.getRequestURL().toString(),
	                            new Date(), "Rest", "getESByID",
	                            "SYSTEM",
	                            esID)));
	        } catch (JsonProcessingException ex) {
	            log.info(ex.getMessage());
	        }
			GenericResponseDTO<EnterpriseStructureNodeDTO> response = service.getESByID(esID);
			return ResponseEntity.status(HttpStatus.OK).body(response);
	 }
	 
	 @RequestMapping(value = "/get.es.optimize.by.id/{esID}",
	    method = RequestMethod.GET,
	    produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<GenericResponseDTO<EnterpriseStructureOptimizeDTO>> getESOptimizeByID(@PathVariable("esID") String esID) {
			try {
	            log.info(objectMapper.writeValueAsString(
	                    LogOpsUtil.getLogOps(ProjectType.CRUD, "ES", EnterpriseStructureRESTController.class.getName(),
	                            httpRequest.getRequestURL().toString(),
	                            new Date(), "Rest", "getESOptimizeByID",
	                            "SYSTEM",
	                            esID)));
	        } catch (JsonProcessingException ex) {
	            log.info(ex.getMessage());
	        }
			GenericResponseDTO<EnterpriseStructureOptimizeDTO> response = service.getESOptimizeByID(esID);
			return ResponseEntity.status(HttpStatus.OK).body(response);
	 }
	 
	 @RequestMapping(value = "/get.all.es",
	            method = RequestMethod.POST,
	            produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<GenericResponseDTO<List<EnterpriseStructureNodeDTO>>> getAllES(@RequestBody GetListRequestDTO dto) {
		 try {
	            log.info(objectMapper.writeValueAsString(
	                    LogOpsUtil.getLogOps(ProjectType.CRUD, "ES", EnterpriseStructureRESTController.class.getName(),
	                            httpRequest.getRequestURL().toString(),
	                            new Date(), "Rest", "getAllES",
	                            "ES",
	                            dto)));
	        } catch (JsonProcessingException ex) {
	            log.info(ex.getMessage());
	        }
	        GenericResponseDTO<List<EnterpriseStructureNodeDTO>> response = service.getESAll(dto);
	        return ResponseEntity.status(HttpStatus.OK).body(response);
	 }
	 
	 @RequestMapping(value = "/post.es",
	            method = RequestMethod.POST,
	            consumes = MediaType.APPLICATION_JSON_VALUE,
	            produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<GenericResponseDTO<EnterpriseStructureNodeDTO>> postES(@RequestBody EnterpriseStructureNodeCreateDTO dto) {
		 try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "ES", EnterpriseStructureRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Create",
                            dto.getEsCreational()== null? "SYSTEM": dto.getEsCreational().getCreatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
		GenericResponseDTO<EnterpriseStructureNodeDTO> response = service.postES(dto);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	 }
	 
	 @RequestMapping(value = "/update.es",
	            method = RequestMethod.PUT,
	            consumes = MediaType.APPLICATION_JSON_VALUE,
	            produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<GenericResponseDTO<EnterpriseStructureNodeDTO>> putES(@RequestBody EnterpriseStructureNodeCreateDTO dto) {
		 try {
	            log.info(objectMapper.writeValueAsString(
	                    LogOpsUtil.getLogOps(ProjectType.CRUD, "ES", EnterpriseStructureRESTController.class.getName(),
	                            httpRequest.getRequestURL().toString(),
	                            new Date(), "Rest", "Update",
	                            dto.getEsCreational()== null? "SYSTEM": dto.getEsCreational().getModifiedBy(),
	                            dto)));
	        } catch (JsonProcessingException ex) {
	            log.info(ex.getMessage());
	        }
	        GenericResponseDTO<EnterpriseStructureNodeDTO> response = service.putES(dto);
	        return ResponseEntity.status(HttpStatus.OK).body(response);
	 }
	 
	 @RequestMapping(value = "/delete.es",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<GenericResponseDTO<EnterpriseStructureNodeDTO>> deleteVehicle(@RequestBody DeleteRequestDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "ES", EnterpriseStructureRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Delete",
                            dto.getRequestBy() == null ? "SYSTEM": dto.getRequestBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<EnterpriseStructureNodeDTO> response = service.deleteES(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
     } 
}
