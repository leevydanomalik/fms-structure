package com.bitozen.fms.structure.dto;

import com.bitozen.fms.common.dto.share.BizparOptimizeDTO;
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
public class MetadataDTO implements Serializable {

    private String id;
    private String es;
    private String tag;
    private BizparOptimizeDTO type;
    private String version;

    @JsonIgnore
    public MetadataDTO getInstance() {
        return new MetadataDTO(
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                "es",
                "tag",
                new BizparOptimizeDTO(),
                "version"
        );
    }

}
