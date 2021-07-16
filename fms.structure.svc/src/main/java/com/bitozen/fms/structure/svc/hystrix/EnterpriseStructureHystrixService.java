package com.bitozen.fms.structure.svc.hystrix;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import com.bitozen.fms.common.dto.DeleteRequestDTO;
import com.bitozen.fms.common.dto.GenericResponseDTO;
import com.bitozen.fms.common.dto.GetListRequestDTO;
import com.bitozen.fms.common.dto.share.EnterpriseStructureOptimizeDTO;
import com.bitozen.fms.common.type.ProjectType;
import com.bitozen.fms.common.util.LogOpsUtil;
import com.bitozen.fms.structure.domain.EnterpriseStructure;
import com.bitozen.fms.structure.domain.repository.EnterpriseStructureRepository;
import com.bitozen.fms.structure.dto.EnterpriseStructureNodeCreateDTO;
import com.bitozen.fms.structure.dto.EnterpriseStructureNodeDTO;
import com.bitozen.fms.structure.svc.assembler.EnterpriseStructureDTOAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@PropertySource("classpath:error-message.properties")
@Slf4j
public class EnterpriseStructureHystrixService {

	 @Autowired
	 private ObjectMapper objectMapper;
	 
	 @Autowired
	 private EnterpriseStructureRepository repository;
	 
	 @Autowired
	 private EnterpriseStructureDTOAssembler assembler;
	 
	 @Value("${data.integrity.exception.message}")
	 private String INTEGRITY_ERROR_MSG;

     @Value("${data.not.found.message}")
     private String DATA_NOT_FOUND_MSG;
     
     @SneakyThrows
     @HystrixCommand(fallbackMethod = "defaultGetESByIDFallback")
	 @Cacheable(value = "getESByIDCache", key = "#p0")
     public GenericResponseDTO<EnterpriseStructureNodeDTO> getESByID(String esID) {
		 try {
			 Optional<EnterpriseStructure> data = repository.findOneByEsID(esID);
			 if (!data.isPresent()) {
					log.info(objectMapper.writeValueAsString(LogOpsUtil.getErrorResponse(ProjectType.CRUD, "ES", new Date(), "Rest", "204", "DATA NOT FOUND")));
					return new GenericResponseDTO().noDataFoundResponse();
				}
				log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(ProjectType.CRUD, "Vehicle", new Date(),
						"Rest", new GenericResponseDTO().successResponse().getCode(), new GenericResponseDTO().successResponse().getMessage())));
				return new GenericResponseDTO().successResponse(assembler.toDTO(data.get()));
		 } catch (Exception e) {
				log.info(e.getMessage());
				try {
					log.info(objectMapper.writeValueAsString(LogOpsUtil.getErrorResponse(ProjectType.CRUD, "ES", new Date(), "Rest",
									String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
				} catch (JsonProcessingException ex) {
					log.info(ex.getMessage());
				}
				return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
			}
	 }
	 
	 public GenericResponseDTO<EnterpriseStructureNodeDTO> defaultGetESByIDFallback(String esID, Throwable e) throws IOException {
		 return new GenericResponseDTO<EnterpriseStructureNodeDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later" : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	 }
	 
	 @HystrixCommand(fallbackMethod = "defaultGetESOptimizeByIDFallback")
	 @Cacheable(value = "getESOptimizeByIDCache", key = "#p0")
	 public GenericResponseDTO<EnterpriseStructureOptimizeDTO> getESOptimizeByID(String esID) {
		 try {
			 Optional<EnterpriseStructure> data = repository.findOneByEsID(esID);
			 if (!data.isPresent()) {
					log.info(objectMapper.writeValueAsString(LogOpsUtil.getErrorResponse(ProjectType.CRUD, "ES", new Date(), "Rest", "204", "DATA NOT FOUND")));
					return new GenericResponseDTO().noDataFoundResponse();
				}
				log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(ProjectType.CRUD, "Vehicle", new Date(),
						"Rest", new GenericResponseDTO().successResponse().getCode(), new GenericResponseDTO().successResponse().getMessage())));
				return new GenericResponseDTO().successResponse(assembler.toDTOOptimize(data.get()));
		 } catch (Exception e) {
				log.info(e.getMessage());
				try {
					log.info(objectMapper.writeValueAsString(LogOpsUtil.getErrorResponse(ProjectType.CRUD, "ES", new Date(), "Rest",
									String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
				} catch (JsonProcessingException ex) {
					log.info(ex.getMessage());
				}
				return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
			}
	 }
	 
	 public GenericResponseDTO<EnterpriseStructureOptimizeDTO> defaultGetESOptimizeByIDFallback(String esID, Throwable e) throws IOException {
		 return new GenericResponseDTO<EnterpriseStructureOptimizeDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later" : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	 }
	 
	 @HystrixCommand(fallbackMethod = "defaultGetESAllFallback")
	 @Cacheable(value = "getESAllCache", key = "#p0")
	 public GenericResponseDTO<List<EnterpriseStructureNodeDTO>> getESAll(GetListRequestDTO dto) {
		 try {
				Pageable page = PageRequest.of(dto.getOffset(), dto.getLimit(), Sort.by("esCreational.createdDate").descending());
				String param = String.valueOf(dto.getParams().get("param"));
				Page<EnterpriseStructure> datas = repository.findAllForWeb(String.valueOf(".*").concat(param).concat(".*"), page);
	            if (datas == null) {
	                log.info(objectMapper.writeValueAsString(
	                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "ES", new Date(), "Rest", "204", "DATA NOT FOUND")));
	                return new GenericResponseDTO().noDataFoundResponse();
	            }
	            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
	                    ProjectType.CRUD, "ES", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
	                    new GenericResponseDTO().successResponse().getMessage())));
	            return new GenericResponseDTO().successResponse(assembler.toDTOs(datas.getContent()));
		 } catch (Exception e) {
	            log.info(e.getMessage());
	            try {
	                log.info(objectMapper.writeValueAsString(
	                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "ES", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
	            } catch (JsonProcessingException ex) {
	                log.info(ex.getMessage());
	            }
	            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), e.getLocalizedMessage());
	        }
	 }
	 
	 public GenericResponseDTO<List<EnterpriseStructureNodeDTO>> defaultGetESAllFallback(GetListRequestDTO dto, Throwable e) throws IOException {
		 return new GenericResponseDTO<List<EnterpriseStructureNodeDTO>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later" : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	 }
	 
	 @HystrixCommand(fallbackMethod = "defaultPostESFallback")
	 @Caching(
	        evict = { 
	            @CacheEvict(value = "getESByIDCache", allEntries = true),
	            @CacheEvict(value = "getESOptimizeByIDCache", allEntries = true),
	            @CacheEvict(value = "getESAllCache", allEntries = true)
	        })
	 public GenericResponseDTO<EnterpriseStructureNodeDTO> postES(EnterpriseStructureNodeCreateDTO dto) {
		 try {
			 Optional<EnterpriseStructure> data = repository.findOneByEsID(dto.getEsID());
			 if (data.isPresent()) {
					log.info(objectMapper.writeValueAsString(LogOpsUtil.getErrorResponse(ProjectType.CRUD, "ES", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getEsID().toUpperCase()))));
					return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getEsID()));
				}
				repository.save(assembler.toDomainCreate(dto));
				log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(ProjectType.CRUD, "ES", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(), new GenericResponseDTO().successResponse().getMessage())));
				return new GenericResponseDTO().successResponse();
		 } catch (Exception e) {
				log.info(e.getMessage());
				try {
					log.info(objectMapper .writeValueAsString(LogOpsUtil.getErrorResponse(ProjectType.CRUD, "ES", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
				} catch (JsonProcessingException ex) {
					log.info(ex.getMessage());
				}
				if (e instanceof TransactionSystemException) {
					if (((TransactionSystemException) e).getRootCause() instanceof ConstraintViolationException) {
						Set<ConstraintViolation<?>> data = ((ConstraintViolationException) ((TransactionSystemException) e).getRootCause()).getConstraintViolations();
						String msg = new ArrayList<>(data).get(0).getMessage();
						return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), msg);
					}
				}
				return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
		}
	 }
	 
	 public GenericResponseDTO<EnterpriseStructureNodeDTO> defaultPostESFallback(EnterpriseStructureNodeCreateDTO dto, Throwable e) throws IOException {
		 return new GenericResponseDTO<EnterpriseStructureNodeDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later" : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	 }
	 
	 @HystrixCommand(fallbackMethod = "defaultPutESFallback")
	 @Caching(
	        evict = { 
	            @CacheEvict(value = "getESByIDCache", allEntries = true),
	            @CacheEvict(value = "getESOptimizeByIDCache", allEntries = true),
	            @CacheEvict(value = "getESAllCache", allEntries = true)
	        })
	 public GenericResponseDTO<EnterpriseStructureNodeDTO> putES(EnterpriseStructureNodeCreateDTO dto) {
		 try {
			 Optional<EnterpriseStructure> data = repository.findOneByEsID(dto.getEsID());
			 if (!data.isPresent()) {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "ES", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getEsID()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getEsID()));
			 }
			 
			 if(dto.getEsChildren() != null && !(dto.getEsChildren().equals(""))) {
					assembler.updateChild(dto.getEsID(), dto.getEsChildren(), data.get().getEsChildren());
			 }
			 
			 if(dto.getEsParent() != null && !(dto.getEsParent().equals("")) && !(dto.getEsParent().equals(data.get().getEsParent()))) {
				 assembler.updateParent(dto.getEsID(), data.get().getEsParent(), dto.getEsParent());
			 }
			 
			 repository.save(assembler.update(data.get(), dto));
				log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(ProjectType.CRUD, "ES", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(), new GenericResponseDTO().successResponse().getMessage())));
				return new GenericResponseDTO().successResponse();
		 } catch (Exception e) {
			log.info(e.getMessage());
			try {
				log.info(objectMapper .writeValueAsString(LogOpsUtil.getErrorResponse(ProjectType.CRUD, "ES", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
			} catch (JsonProcessingException ex) {
				log.info(ex.getMessage());
			}
			if (e instanceof TransactionSystemException) {
				if (((TransactionSystemException) e).getRootCause() instanceof ConstraintViolationException) {
					Set<ConstraintViolation<?>> data = ((ConstraintViolationException) ((TransactionSystemException) e).getRootCause()).getConstraintViolations();
					String msg = new ArrayList<>(data).get(0).getMessage();
					return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), msg);
				}
			}
			return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
		}
	 }
	 
	 private GenericResponseDTO<EnterpriseStructureNodeDTO> defaultPutESFallback(EnterpriseStructureNodeCreateDTO dto, Throwable e) throws IOException {
		 return new GenericResponseDTO<EnterpriseStructureNodeDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
					e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later" : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	 }
	 
	 @HystrixCommand(fallbackMethod = "defaultDeleteESFallback")
	 @Caching(
	        evict = { 
	            @CacheEvict(value = "getESByIDCache", allEntries = true),
	            @CacheEvict(value = "getESOptimizeByIDCache", allEntries = true),
	            @CacheEvict(value = "getESAllCache", allEntries = true)
	        })
	 public GenericResponseDTO<EnterpriseStructureNodeDTO> deleteES(DeleteRequestDTO dto) {
		 try {
			 Optional<EnterpriseStructure> data = repository.findOneByEsID(dto.getReferenceID());
			 if (!data.isPresent()) {
				 	String msg = MessageFormat.format(DATA_NOT_FOUND_MSG, dto.getReferenceID().toUpperCase());
					log.info(objectMapper.writeValueAsString(LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Vehicle",
							new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getReferenceID().toUpperCase()))));
					return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), msg);
			 }
			 
			 if(data.get().getEsChildren() != null && !(data.get().getEsChildren().equals(""))) {
				 assembler.deleteChild(dto.getReferenceID(), data.get().getEsChildren());
			 }
			 
			 if(data.get().getEsParent() != null && !(data.get().getEsParent().equals(""))) {
				 assembler.deleteParent(dto.getReferenceID(), data.get().getEsParent());
			 }
			 
			 repository.delete(data.get());
				log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(ProjectType.CRUD, "ES", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(), new GenericResponseDTO().successResponse().getMessage())));
				return new GenericResponseDTO().successResponse();
		 } catch (Exception e) {
			log.info(e.getMessage());
			try {
				log.info(objectMapper .writeValueAsString(LogOpsUtil.getErrorResponse(ProjectType.CRUD, "ES", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
			} catch (JsonProcessingException ex) {
				log.info(ex.getMessage());
			}
			if (e instanceof TransactionSystemException) {
				if (((TransactionSystemException) e).getRootCause() instanceof ConstraintViolationException) {
					Set<ConstraintViolation<?>> data = ((ConstraintViolationException) ((TransactionSystemException) e).getRootCause()).getConstraintViolations();
					String msg = new ArrayList<>(data).get(0).getMessage();
					return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), msg);
				}
			}
			return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
		}
	 }
	 
	 private GenericResponseDTO<EnterpriseStructureNodeDTO> defaultDeleteESFallback(DeleteRequestDTO dto, Throwable e) throws IOException {
		 return new GenericResponseDTO<EnterpriseStructureNodeDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
					e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later" : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	 }
}
