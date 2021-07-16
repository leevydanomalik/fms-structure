package com.bitozen.fms.structure.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.bitozen.fms.structure.domain.EnterpriseStructure;

@Repository
public interface EnterpriseStructureRepository extends MongoRepository<EnterpriseStructure, Long>{

	List<EnterpriseStructure> findAll();
	
	Optional<EnterpriseStructure> findOneByEsID(String esID);
	
	@Query(value = "{$or: [{ 'esID' : {$regex: ?0,$options: 'i'}}," +
            " {'esName' : {$regex: ?0,$options: 'i'}}]}")
	Page<EnterpriseStructure> findAllForWeb(String param, Pageable pageable);
	
	long count();
	
	Optional<EnterpriseStructure> findFirstByOrderByIdDesc();
}
