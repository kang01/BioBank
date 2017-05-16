package org.fwoxford.service.dto.response;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by gengluying on 2017/5/16.
 */
public class FrozenTubeTypeResponse {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String frozenTubeTypeCode;

    @NotNull
    @Size(max = 255)
    private String frozenTubeTypeName;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFrozenTubeTypeCode() {
        return frozenTubeTypeCode;
    }
    public void setFrozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
    }
    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }
    public void setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
    }
}
