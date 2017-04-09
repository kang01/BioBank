package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.fwoxford.domain.SupportRack;
import org.fwoxford.service.SupportRackService;
import org.fwoxford.service.dto.SupportRackDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * REST controller for managing FrozenPosition.
 */
@RestController
@RequestMapping("/api")
public class FrozenPositionResource {

    private final Logger log = LoggerFactory.getLogger(FrozenPositionResource.class);
    @Autowired
    private SupportRackService supportRackService;
    /**
     * 输入设备编码，返回该设备下的所有架子
     * @param equipmentCode
     * @return
     */
    @GetMapping("/frozen-pos/shelfs/{equipmentCode}")
    @Timed
    public ResponseEntity<List<SupportRackDTO>> getSupportRackList(@PathVariable String equipmentCode) {
        List<SupportRackDTO> supportRackDTOS = supportRackService.getSupportRackList(equipmentCode);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(supportRackDTOS));
    }

    /**
     * 取冻存位置
     * @param equipmentCode
     * @param areaCode
     * @return
     */
    @RequestMapping(value = "/frozen-pos/shelfs/{equipmentCode}/{areaCode}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    public List<SupportRackDTO> getSupportRackListByEquipmentAndArea(@PathVariable String equipmentCode ,@PathVariable String areaCode) {
        List<SupportRackDTO> result = supportRackService.getSupportRackListByEquipmentAndArea(equipmentCode,areaCode);
        return result;
    }

    /**
     * 输入设备编码，返回该设备下所有未装满的架子
     * @param equipmentCode
     * @return
     */
    @RequestMapping(value = "/frozen-pos/incomplete-shelfs/{equipmentCode}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    public List<SupportRackDTO> getIncompleteShelves(@PathVariable String equipmentCode) {
        List<SupportRackDTO> result = supportRackService.getIncompleteShelves(equipmentCode);
        return result;
    }

    /**
     * 输入设备编码和区域编码，返回该设备下的某区域的所有未装满的架子
     * @param equipmentCode
     * @param areaCode
     * @return
     */
    @RequestMapping(value = "/frozen-pos/incomplete-shelfs/{equipmentCode}/{areaCode}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
    public List<SupportRackDTO> getIncompleteShelves(@PathVariable String equipmentCode, @PathVariable String areaCode) {
        List<SupportRackDTO> result =  supportRackService.getIncompleteShelvesByEquipmentAndArea(equipmentCode,areaCode);
        return result;
    }
}
