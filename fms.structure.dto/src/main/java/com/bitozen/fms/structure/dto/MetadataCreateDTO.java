package com.bitozen.fms.structure.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MetadataCreateDTO implements Serializable {

    private String id;
    private String es;
    private String tag;
    private String type;
    private String version;

    @JsonIgnore
    public MetadataCreateDTO getInstance() {
        return new MetadataCreateDTO(
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                "es",
                "tag",
                "TEST_4",
                "version"
        );
    }

}
