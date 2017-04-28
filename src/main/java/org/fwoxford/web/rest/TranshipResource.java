package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.fwoxford.domain.Tranship;
import org.fwoxford.domain.User;
import org.fwoxford.service.TranshipService;
import org.fwoxford.service.UserService;
import org.fwoxford.service.dto.TranshipDTO;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.dto.response.TranshipResponse;
import org.fwoxford.service.mapper.TranshipMapper;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Tranship.
 */
@RestController
@RequestMapping("/api")
public class TranshipResource {

    private final Logger log = LoggerFactory.getLogger(TranshipResource.class);

    private static final String ENTITY_NAME = "tranship";

    private final TranshipService transhipService;
    @Autowired
    private UserService userService;

    @Autowired
    private TranshipMapper transhipMapper;

    public TranshipResource(TranshipService transhipService) {
        this.transhipService = transhipService;
    }

    /**
     * POST  /tranships : Create a new tranship.
     *
     * @param transhipDTO the transhipDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transhipDTO, or with status 400 (Bad Request) if the tranship has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tranships")
    @Timed
    public ResponseEntity<TranshipDTO> createTranship(@Valid @RequestBody TranshipDTO transhipDTO) throws URISyntaxException {
//        log.debug("REST request to save Tranship : {}", transhipDTO);
//        if (transhipDTO.getId() != null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tranship cannot already have an ID")).body(null);
//        }
//        TranshipDTO result = transhipService.save(transhipDTO);
//        return ResponseEntity.created(new URI("/api/tranships/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
//            .body(result);
        log.debug("REST request to save Tranship : {}", transhipDTO);
        if (transhipDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tranship cannot already have an ID")).body(null);
        }
        TranshipDTO result = transhipService.insertTranship(transhipDTO);
        return ResponseEntity.created(new URI("/res/api/tranships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tranships : Updates an existing tranship.
     *
     * @param transhipDTO the transhipDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transhipDTO,
     * or with status 400 (Bad Request) if the transhipDTO is not valid,
     * or with status 500 (Internal Server Error) if the transhipDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tranships")
    @Timed
    public ResponseEntity<TranshipDTO> updateTranship(@Valid @RequestBody TranshipDTO transhipDTO) throws URISyntaxException {
        log.debug("REST request to update Tranship : {}", transhipDTO);
        if (transhipDTO.getId() == null) {
            return createTranship(transhipDTO);
        }
        TranshipDTO result = transhipService.insertTranship(transhipDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transhipDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tranships : get all the tranships.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tranships in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tranships")
    @Timed
    public ResponseEntity<List<TranshipDTO>> getAllTranships(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Tranships");
        Page<TranshipDTO> page = transhipService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tranships");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tranships/:id : get the "id" tranship. 根据转运ID查询转运和冻存盒的信息
     *
     * @param id the id of the transhipDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transhipDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tranships/{id}")
    @Timed
    public ResponseEntity<TranshipByIdResponse> getTranship(@PathVariable Long id) {
        log.debug("REST request to get Tranship : {}", id);
        TranshipByIdResponse transhipByIdResponse = transhipService.findTranshipAndFrozenBox(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transhipByIdResponse));
    }

    @GetMapping("/tranships/id/{id}")
    @Timed
    public ResponseEntity<TranshipDTO> getTranshipById(@PathVariable Long id) {
        log.debug("REST request to get Tranship : {}", id);
        TranshipDTO transhipDTO = transhipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transhipDTO));
    }

    /**
     * DELETE  /tranships/:id : delete the "id" tranship.
     *
     * @param id the id of the transhipDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tranships/{id}")
    @Timed
    public ResponseEntity<Void> deleteTranship(@PathVariable Long id) {
        log.debug("REST request to delete Tranship : {}", id);
        transhipService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /tranships : get all the tranships. 获取转运记录
     *
     * @param input the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tranships in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/res/tranships", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
    public DataTablesOutput<TranshipResponse> getPageTranship(@RequestBody DataTablesInput input) {
        DataTablesOutput<Tranship> transhipsTablesOutput = transhipService.findAllTranship(input);
        List<Tranship> tranships =  transhipsTablesOutput.getData();
        List<User> userList = userService.findAll();
        for(Tranship t :tranships){
            for(User u :userList){
                if(t.getReceiverId()!=null&&t.getReceiverId().equals(u.getId())){
                    t.setReceiver(u.getLastName()+u.getFirstName());
                }
            }
        }
        //构造返回列表
        List<TranshipResponse> transhipDTOS = transhipMapper.transhipsToTranshipTranshipResponse(tranships);

        //构造返回分页数据
        DataTablesOutput<TranshipResponse> responseDataTablesOutput = new DataTablesOutput<>();
        responseDataTablesOutput.setDraw(transhipsTablesOutput.getDraw());
        responseDataTablesOutput.setError(transhipsTablesOutput.getError());
        responseDataTablesOutput.setData(transhipDTOS);
        responseDataTablesOutput.setRecordsFiltered(transhipsTablesOutput.getRecordsFiltered());
        responseDataTablesOutput.setRecordsTotal(transhipsTablesOutput.getRecordsTotal());

        return responseDataTablesOutput;
    }

    /**
     * 添加转运记录
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/tranships/new-empty")
    @Timed
    public ResponseEntity<TranshipDTO> initTranship() throws URISyntaxException {
        log.debug("REST request to create Tranship first");
        TranshipDTO result = transhipService.initTranship();
        return ResponseEntity.created(new URI("/res/api/tranships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * 修改保存转运记录单
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/tranships/update-object")
    @Timed
    public ResponseEntity<TranshipDTO> saveTranships(@Valid @RequestBody TranshipDTO transhipDTO) throws URISyntaxException {
        log.debug("REST request to update Tranship : {}", transhipDTO);
        if (transhipDTO.getId() == null) {
            return initTranship();
        }
        TranshipDTO result = transhipService.save(transhipDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transhipDTO.getId().toString()))
            .body(result);
    }

    /**
     * 作废转运记录
     * @param transhipCode
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/tranships/invalid/{transhipCode}")
    @Timed
    public ResponseEntity<TranshipDTO> invalidTranship(@PathVariable String transhipCode) throws URISyntaxException {
        log.debug("REST request to save StockIn : {}", transhipCode);
        TranshipDTO result = transhipService.invalidTranship(transhipCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 判断转运单号是否已经存在  ----true：已经存在，false:不存在
     * @param trackNumber
     * @return
     */
    @RequestMapping(value = "/tranships/isRepeat/{trackNumber}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    public Boolean isRepeatFrozenBoxCode(@PathVariable String trackNumber) {
        Boolean flag =  transhipService.isRepeatTrackNumber(trackNumber);
        return flag;
    }
}
