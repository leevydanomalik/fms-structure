package com.bitozen.fms.structure.domain.common;

import com.bitozen.fms.structure.common.util.ZonedDateTimeAttributeConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Convert;

@Embeddable
public class CreationalSpecification implements Serializable {
    private String createdBy;
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    private ZonedDateTime createdDate;
    private String modifiedBy;
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    private ZonedDateTime modifiedDate;

    public CreationalSpecification() {
    }

    public CreationalSpecification(String createdBy, ZonedDateTime createdDate, String modifiedBy, ZonedDateTime modifiedDate) {
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    

    @JsonIgnore
    public CreationalSpecification getInstance(){
        return new CreationalSpecification("SYSTEM", null, null, null);
    }
}
