package org.fwoxford.service.dto;

import java.util.List;

/**
 * Created by gengluying on 2017/4/10.
 */
public class StockInBoxSplitDTO {
    private Long frozenBoxId;
    private Integer isSplit;
    private String status;
    private String frozenBoxCode;
    private Integer countOfSample;
    private String frozenBoxColumns;
    private String frozenBoxRows;
    private SampleTypeDTO sampleType;
    private EquipmentDTO equipment;
    private AreaDTO area;
    private SupportRackDTO shelf;
    private Long equipmentId;
    private Long areaId;
    private Long supportRackId;
    private String stockInCode;
    private String memo;
    private String columnsInShelf;
    private String rowsInShelf;
    private List<StockInTubeDTO> stockInTubeDTOList;
}
