package com.bitozen.fms.structure.svc.configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration{

	 @Value("${spring.data.mongodb.host}")
	    private String MONGO_HOST;
	    @Value("${spring.data.mongodb.port}")
	    private String MONGO_PORT;
	    @Value("${spring.data.mongodb.database}")
	    private String MONGO_DB;

	    @Override
	    public MongoClient mongoClient() {
	        MongoClient mongoClient = MongoClients.create(
	                MongoClientSettings.builder()
	                        .applyToClusterSettings(builder ->
	                                builder.hosts(Arrays.asList(
	                                        new ServerAddress(MONGO_HOST, Integer.valueOf(MONGO_PORT)))))
	                        .build());
	        return mongoClient;
	    }

	    @Override
	    protected String getDatabaseName() {
	        return MONGO_DB;
	    }

	    @Override
	    public Collection getMappingBasePackages() {
	        return Collections.singleton("com.bitozen.fms");
	    }

	    @Bean
	    public MongoTemplate mongoTemplate() {
	        MongoTemplate mt = new MongoTemplate(mongoClient(), getDatabaseName());
	        return mt;
	    }
}
