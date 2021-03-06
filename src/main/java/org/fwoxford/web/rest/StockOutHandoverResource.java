package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import org.fwoxford.service.StockOutHandoverService;
import org.fwoxford.service.dto.response.StockOutHandoverForDataTableEntity;
import org.fwoxford.service.dto.response.StockOutHandoverSampleReportDTO;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutHandoverDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing StockOutHandover.
 */
@RestController
@RequestMapping("/api")
public class StockOutHandoverResource {

    private final Logger log = LoggerFactory.getLogger(StockOutHandoverResource.class);

    private static final String ENTITY_NAME = "stockOutHandover";

    private final StockOutHandoverService stockOutHandoverService;

    public StockOutHandoverResource(StockOutHandoverService stockOutHandoverService) {
        this.stockOutHandoverService = stockOutHandoverService;
    }

    /**
     * POST  /stock-out-handovers : Create a new stockOutHandover.
     *
     * @param stockOutHandoverDTO the stockOutHandoverDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutHandoverDTO, or with status 400 (Bad Request) if the stockOutHandover has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-handovers")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> createStockOutHandover(@Valid @RequestBody StockOutHandoverDTO stockOutHandoverDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutHandover : {}", stockOutHandoverDTO);
        if (stockOutHandoverDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutHandover cannot already have an ID")).body(null);
        }
        StockOutHandoverDTO result = stockOutHandoverService.save(stockOutHandoverDTO);
        return ResponseEntity.created(new URI("/api/stock-out-handovers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-handovers : Updates an existing stockOutHandover.
     *
     * @param stockOutHandoverDTO the stockOutHandoverDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutHandoverDTO,
     * or with status 400 (Bad Request) if the stockOutHandoverDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutHandoverDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-handovers")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> updateStockOutHandover(@Valid @RequestBody StockOutHandoverDTO stockOutHandoverDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutHandover : {}", stockOutHandoverDTO);
        if (stockOutHandoverDTO.getId() == null) {
            return createStockOutHandover(stockOutHandoverDTO);
        }
        StockOutHandoverDTO result = stockOutHandoverService.save(stockOutHandoverDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutHandoverDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-handovers : get all the stockOutHandovers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutHandovers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-handovers")
    @Timed
    public ResponseEntity<List<StockOutHandoverDTO>> getAllStockOutHandovers(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutHandovers");
        Page<StockOutHandoverDTO> page = stockOutHandoverService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-handovers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-handovers/:id : get the "id" stockOutHandover.
     *
     * @param id the id of the stockOutHandoverDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutHandoverDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-handovers/{id}")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> getStockOutHandover(@PathVariable Long id) {
        log.debug("REST request to get StockOutHandover : {}", id);
        StockOutHandoverDTO stockOutHandoverDTO = stockOutHandoverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutHandoverDTO));
    }

    /**
     * DELETE  /stock-out-handovers/:id : delete the "id" stockOutHandover.
     *
     * @param id the id of the stockOutHandoverDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-handovers/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutHandover(@PathVariable Long id) {
        log.debug("REST request to delete StockOutHandover : {}", id);
        stockOutHandoverService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 根据任务创建交接单
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/stock-out-handovers/task/{id}")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> createStockOutHandover(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to save StockOutHandover : {}", id);
        StockOutHandoverDTO result = stockOutHandoverService.saveByTask(id);
        return ResponseEntity.created(new URI("/api/stock-out-handovers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 获取出库交接列表
     * @param input
     * @return
     */

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-handovers", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutHandoverForDataTableEntity> getPageStockOutHandOver(@RequestBody DataTablesInput input) {
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        input.addColumn("id",true,true,null);
        input.addOrder("id",true);
        DataTablesOutput<StockOutHandoverForDataTableEntity> result = stockOutHandoverService.getPageDataStockOutHandOver(input);
        return result;
    }


    /**
     * 打印交接单
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/stock-out-handovers/print/{id}",method = RequestMethod.GET,
        produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Timed
    public ResponseEntity printStockOutHandover(@PathVariable Long id) throws URISyntaxException {

        try {
            ByteArrayOutputStream result = stockOutHandoverService.printStockOutHandover(id);
            byte[] fileInByte = result.toByteArray();
            final HttpHeaders headers = new HttpHeaders();
            String fileReportName = "交接单.xlsx";
            headers.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.set("Content-disposition", "attachment; filename="+ URLEncoder.encode(fileReportName, "UTF-8"));

//            File dir = new File(".");
//            OutputStream ofs = null;
//            ofs = new FileOutputStream(dir.getCanonicalPath() + "/" + result.hashCode() + ".xlsx");
//            result.writeTo(ofs);
//
//            ofs.close();

            return new ResponseEntity(fileInByte, headers, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * 样本交接保存
     * @param ids
     * @param stockOutHandoverDTO
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-handovers/stockOutBox/{ids}/complete")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> completeStockOutHandover(@PathVariable List<Long> ids,@Valid @RequestBody StockOutHandoverDTO stockOutHandoverDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutHandover : {}", stockOutHandoverDTO);
        StockOutHandoverDTO result = stockOutHandoverService.completeStockOutHandover(ids,stockOutHandoverDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutHandoverDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/stock-out-handovers/{id}/details")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> getStockOutHandoverDetail(@PathVariable Long id) {
        log.debug("REST request to get StockOutHandover : {}", id);
        StockOutHandoverDTO stockOutHandoverDTO = stockOutHandoverService.getStockOutHandoverDetail(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutHandoverDTO));
    }

    /**
     * 分页查询交接样本详情
     * @param input
     * @param id
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-handovers/{id}/samples", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutHandoverSampleReportDTO> getPageStockOutHandoverSample(@RequestBody DataTablesInput input, @PathVariable Long id) {
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        input.addColumn("id",true,true,null);
        input.addOrder("id",true);
        DataTablesOutput<StockOutHandoverSampleReportDTO> result =  stockOutHandoverService.getPageStockOutHandoverSample(id, input);
        return result;
    }

    /**
     * 作废交接
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/stock-out-handovers/{id}/invalid")
    @Timed
    public ResponseEntity<StockOutHandoverDTO> invalidStockOutHandover(@PathVariable Long id , @RequestBody StockOutHandoverDTO stockOutHandoverDTO) throws URISyntaxException {

        StockOutHandoverDTO result = stockOutHandoverService.invalidStockOutHandover(id,stockOutHandoverDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
