package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import net.sf.json.JSONArray;
import org.fwoxford.domain.StockOutFiles;
import org.fwoxford.service.StockOutFilesService;
import org.fwoxford.service.StockOutRequiredSampleService;
import org.fwoxford.service.dto.StockOutFilesDTO;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.StockOutRequiredSampleDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing StockOutRequiredSample.
 */
@RestController
@RequestMapping("/api")
public class StockOutRequiredSampleResource {

    private final Logger log = LoggerFactory.getLogger(StockOutRequiredSampleResource.class);

    private static final String ENTITY_NAME = "stockOutRequiredSample";

    private final StockOutRequiredSampleService stockOutRequiredSampleService;

    @Autowired
    StockOutFilesService stockOutFilesService;

    public StockOutRequiredSampleResource(StockOutRequiredSampleService stockOutRequiredSampleService) {
        this.stockOutRequiredSampleService = stockOutRequiredSampleService;
    }

    /**
     * POST  /stock-out-required-samples : Create a new stockOutRequiredSample.
     *
     * @param stockOutRequiredSampleDTO the stockOutRequiredSampleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOutRequiredSampleDTO, or with status 400 (Bad Request) if the stockOutRequiredSample has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-out-required-samples")
    @Timed
    public ResponseEntity<StockOutRequiredSampleDTO> createStockOutRequiredSample(@Valid @RequestBody StockOutRequiredSampleDTO stockOutRequiredSampleDTO) throws URISyntaxException {
        log.debug("REST request to save StockOutRequiredSample : {}", stockOutRequiredSampleDTO);
        if (stockOutRequiredSampleDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stockOutRequiredSample cannot already have an ID")).body(null);
        }
        StockOutRequiredSampleDTO result = stockOutRequiredSampleService.save(stockOutRequiredSampleDTO);
        return ResponseEntity.created(new URI("/api/stock-out-required-samples/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-out-required-samples : Updates an existing stockOutRequiredSample.
     *
     * @param stockOutRequiredSampleDTO the stockOutRequiredSampleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOutRequiredSampleDTO,
     * or with status 400 (Bad Request) if the stockOutRequiredSampleDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOutRequiredSampleDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-out-required-samples")
    @Timed
    public ResponseEntity<StockOutRequiredSampleDTO> updateStockOutRequiredSample(@Valid @RequestBody StockOutRequiredSampleDTO stockOutRequiredSampleDTO) throws URISyntaxException {
        log.debug("REST request to update StockOutRequiredSample : {}", stockOutRequiredSampleDTO);
        if (stockOutRequiredSampleDTO.getId() == null) {
            return createStockOutRequiredSample(stockOutRequiredSampleDTO);
        }
        StockOutRequiredSampleDTO result = stockOutRequiredSampleService.save(stockOutRequiredSampleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOutRequiredSampleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-out-required-samples : get all the stockOutRequiredSamples.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOutRequiredSamples in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stock-out-required-samples/stockOutRequirement/{id}")
    @Timed
    public ResponseEntity<List<StockOutRequiredSampleDTO>> getAllStockOutRequiredSamplesByRequirementId(@ApiParam Pageable pageable,@PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockOutRequiredSamples");
        Page<StockOutRequiredSampleDTO> page = stockOutRequiredSampleService.getAllStockOutRequiredSamplesByRequirementId(pageable,id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-out-required-samples");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-out-required-samples/:id : get the "id" stockOutRequiredSample.
     *
     * @param id the id of the stockOutRequiredSampleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOutRequiredSampleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-out-required-samples/{id}")
    @Timed
    public ResponseEntity<StockOutRequiredSampleDTO> getStockOutRequiredSample(@PathVariable Long id) {
        log.debug("REST request to get StockOutRequiredSample : {}", id);
        StockOutRequiredSampleDTO stockOutRequiredSampleDTO = stockOutRequiredSampleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockOutRequiredSampleDTO));
    }

    /**
     * DELETE  /stock-out-required-samples/:id : delete the "id" stockOutRequiredSample.
     *
     * @param id the id of the stockOutRequiredSampleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-out-required-samples/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOutRequiredSample(@PathVariable Long id) {
        log.debug("REST request to delete StockOutRequiredSample : {}", id);
        stockOutRequiredSampleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 查询已经上传的样本需求
     * @param input
     * @param id
     * @return
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/stock-out-required-samples/stockOutRequirement/{id}", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<StockOutRequiredSampleDTO> getPageStockOutRequiredSampleByRequired(@RequestBody DataTablesInput input, @PathVariable Long id) {
        input.getColumns().forEach(u->{
            if(u.getData()==null||u.getData().equals(null)||u.getData()==""){
                u.setSearchable(false);
            }
        });
        input.addColumn("id",true,true,null);
        input.addOrder("id",true);
        return stockOutRequiredSampleService.getPageStockOutRequiredSampleByRequired(input,id);
    }

    /**
     * 获取上传的出库需求详情--返回类型是JSONArray
     * @param requirementId 需求ID
     * @return
     */
    @RequestMapping(value = "/stock-out-required-samples/requirement/{requirementId}",method = RequestMethod.GET,
        produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
//    public JSONArray getRequiredSamples(@PathVariable Long requirementId) {
//        return stockOutRequiredSampleService.getRequiredSamples(requirementId);
//    }
    @Timed
    public ResponseEntity getRequiredSamples(@PathVariable Long requirementId) throws URISyntaxException {

        try {
            StockOutFilesDTO stockOutFiles = stockOutFilesService.findByRequirement(requirementId);
            byte[] fileInByte = stockOutFiles.getFiles();
            final HttpHeaders headers = new HttpHeaders();
            String fileReportName = stockOutFiles.getFileName();
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
}
