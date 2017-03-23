package org.fwoxford.domain.response;


import org.fwoxford.domain.FrozenBoxType;
import org.fwoxford.domain.SampleType;
import org.fwoxford.service.FrozenBoxService;
import org.fwoxford.service.dto.AbstractAuditingDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the FrozenBox entity.
 */
public class FrozenBoxResponse {

    private Long id;
    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;

    @NotNull
    @Size(max = 20)
    private String frozenBoxRows;

    @NotNull
    @Size(max = 20)
    private String frozenBoxColumns;

    @NotNull
    @Size(max = 100)
    private String equipmentCode;

    @NotNull
    @Size(max = 100)
    private String areaCode;

    @NotNull
    @Size(max = 100)
    private String supportRackCode;

    @NotNull
    @Size(max = 20)
    private String isSplit;

    @Size(max = 255)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String status;

    private FrozenBoxType frozenBoxType;

    private SampleType sampleType;

    private List<FrozenTubeResponse> frozenTubeResponseList;

}
