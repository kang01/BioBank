package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.jhipster.web.util.ResponseUtil;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.fwoxford.service.StockListService;
import org.fwoxford.service.dto.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing StockIn.
 */
@RestController
@RequestMapping("/api")
public class StockListResource {

    private final Logger log = LoggerFactory.getLogger(StockListResource.class);

    private static final String ENTITY_NAME = "stockList";

    @Autowired
    private StockListService stockListService;
    /**
     * 查询冻存位置清单
     * @param input
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-list/frozen-position", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<FrozenPositionListAllDataTableEntity> getPageStockFrozenPositionList(@RequestBody DataTablesInput input,
                                                                                                 @RequestParam(value = "searchForm",required = false) String searchForm ) {
        JSONObject jsonObject = JSONObject.fromObject(searchForm);
        FrozenPositionListSearchForm search = (FrozenPositionListSearchForm) JSONObject.toBean(jsonObject, FrozenPositionListSearchForm.class);
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        return stockListService.getPageStockFrozenPositionList(input,search);
    }

    /**
     * 冻存盒清单
     * @param input
     * @param searchForm
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-list/frozen-box", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<FrozenBoxListAllDataTableEntity> getPageStockFrozenBoxList(@RequestBody DataTablesInput input,
                                                                                       @RequestParam(value = "searchForm",required = false) String searchForm ) {
        JSONObject jsonObject = JSONObject.fromObject(searchForm);
        FrozenBoxListSearchForm search = (FrozenBoxListSearchForm) JSONObject.toBean(jsonObject, FrozenBoxListSearchForm.class);
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        return stockListService.getPageStockFrozenBoxList(input,search);
    }

    /**
     * 样本清单
     * @param input
     * @param searchForm
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-list/frozen-tube", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<FrozenTubeListAllDataTableEntity> getPageStockFrozenTubeList(@RequestBody DataTablesInput input,
                                                                                         @RequestParam(value = "searchForm",required = false) String searchForm ) {
        JSONObject jsonObject = JSONObject.fromObject(searchForm);
        FrozenTubeListSearchForm search = (FrozenTubeListSearchForm) JSONObject.toBean(jsonObject, FrozenTubeListSearchForm.class);
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        return stockListService.getPageStockFrozenTubeList(input,search);
    }

    /**
     * 查询样本历史详情
     * @param frozenTubeId
     * @return
     */
    @GetMapping("/stock-list/frozen-tube-history-detail/{frozenTubeId}")
    @Timed
    public ResponseEntity<List<FrozenTubeHistory>> getFrozenTubeHistoryDetail(@PathVariable Long frozenTubeId) {
        log.debug("REST request to get FrozenTube : {}", frozenTubeId);
        List<FrozenTubeHistory> frozenTubeHistories = stockListService.findFrozenTubeHistoryDetail(frozenTubeId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frozenTubeHistories));
    }
}
