package org.fwoxford.domain.response;

import org.fwoxford.service.dto.FrozenTubeDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by gengluying on 2017/3/22.
 */
public class FrozenTubeResponse {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String projectCode;

    @NotNull
    @Size(max = 100)
    private String frozenTubeCode;

    @NotNull
    @Size(max = 100)
    private String sampleTempCode;

    @NotNull
    @Size(max = 100)
    private String sampleCode;

    @NotNull
    @Size(max = 100)
    private String frozenTubeTypeCode;

    @NotNull
    @Size(max = 255)
    private String frozenTubeTypeName;

    @NotNull
    @Size(max = 100)
    private String sampleTypeCode;

    @NotNull
    @Size(max = 255)
    private String sampleTypeName;

    @NotNull
    @Max(value = 20)
    private Integer sampleUsedTimesMost;

    @NotNull
    @Max(value = 20)
    private Integer sampleUsedTimes;

    @NotNull
    @Max(value = 20)
    private Integer frozenTubeVolumns;

    @NotNull
    @Size(max = 20)
    private String frozenTubeVolumnsUnit;

    @NotNull
    @Size(max = 20)
    private String tubeRows;

    @NotNull
    @Size(max = 20)
    private String tubeColumns;

    @Size(max = 1024)
    private String memo;

    @Size(max = 20)
    private String errorType;

    @NotNull
    @Size(max = 20)
    private String status;

    private Long frozenTubeTypeId;

    private Long sampleTypeId;

    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
