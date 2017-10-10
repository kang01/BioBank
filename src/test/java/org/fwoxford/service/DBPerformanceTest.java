package org.fwoxford.service;

import org.fwoxford.BioBankApp;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.dto.UserDTO;
import org.fwoxford.service.util.RandomUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
//@Transactional
public class DBPerformanceTest {

    private final Logger log = LoggerFactory.getLogger(ReportExportingServiceIntTest.class);

    @Autowired
    StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;

    @Autowired
    StockOutApplyRepository stockOutApplyRepository;

    @Autowired
    StockOutRequirementRepository stockOutRequirementRepository;

    @Autowired
    FrozenBoxRepository frozenBoxRepository;

    @Autowired
    FrozenTubeRepository frozenTubeRepository;

    @Autowired
    FrozenBoxTypeRepository frozenBoxTypeRepository;

    @Autowired
    FrozenTubeTypeRepository frozenTubeTypeRepository;

    @Autowired
    SampleTypeRepository sampleTypeRepository;


    @Test
    public void testDBWrite5W() {
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(1L);
        if (stockOutApply == null){
            stockOutApply = new StockOutApply();
            stockOutApply.startTime(LocalDate.now())
                .endTime(LocalDate.now())
                .purposeOfSample("TEST")
                .recordTime(LocalDate.now())
//            .recordId("TEST")
//            .parentApplyId("TEST")
//            .approverId("TEST")
//            .approveTime("TEST")
                .invalidReason("TEST")
                .status("TEST")
                .memo("TEST")
                .applyPersonName("TEST")
                .applyCode("TEST");
            stockOutApply = stockOutApplyRepository.saveAndFlush(stockOutApply);
        }

        StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findOne(1L);
        if (stockOutRequirement == null){
            stockOutRequirement = new StockOutRequirement();
            stockOutRequirement.applyCode("TEST")
                .requirementCode("TEST")
                .requirementName("TEST")
                .countOfSample(0)
                .countOfSampleReal(0)
                .sex("TEST")
                .ageMin(0)
                .ageMax(0)
                .diseaseType("TEST")
                .isHemolysis(Boolean.TRUE)
                .isBloodLipid(Boolean.TRUE)
//            .importingFileId(0L)
                .status("TEST")
                .memo("TEST");
            stockOutRequirement.setStockOutApply(stockOutApply);
            stockOutRequirement = stockOutRequirementRepository.saveAndFlush(stockOutRequirement);
        }

        FrozenBox frozenBox = frozenBoxRepository.findOne(1L);
        if (frozenBox == null){
            frozenBox = new FrozenBox();
            frozenBox.frozenBoxCode("TEST")
                .frozenBoxTypeCode("TEST")
                .frozenBoxTypeRows("TEST")
                .frozenBoxTypeColumns("TEST")
                .projectCode("TEST")
                .projectName("TEST")
                .projectSiteCode("TEST")
                .projectSiteName("TEST")
                .equipmentCode("TEST")
                .areaCode("TEST")
                .supportRackCode("TEST")
                .sampleTypeCode("TEST")
                .sampleTypeName("TEST")
                .isSplit(0)
                .rowsInShelf("TEST")
                .columnsInShelf("TEST")
                .memo("TEST")
                .status("TEST")
                .emptyTubeNumber(0)
                .emptyHoleNumber(0)
                .dislocationNumber(0)
                .isRealData(0);
            frozenBox.setFrozenBoxType(frozenBoxTypeRepository.findOne(1L));
            frozenBoxRepository.saveAndFlush(frozenBox);
        }

        FrozenTube frozenTube = frozenTubeRepository.findOne(1L);
        if (frozenTube == null){
            frozenTube = new FrozenTube();
            frozenTube.projectCode("TEST")
                .projectSiteCode("TEST")
                .frozenTubeCode("TEST")
                .sampleTempCode("TEST")
                .sampleCode("TEST")
                .frozenTubeTypeCode("TEST")
                .frozenTubeTypeName("TEST")
                .sampleTypeCode("TEST")
                .sampleTypeName("TEST")
                .sampleUsedTimesMost(0)
                .sampleUsedTimes(0)
                .frozenTubeVolumns(0d)
                .frozenTubeVolumnsUnit("TEST")
                .sampleVolumns(0d)
                .tubeRows("TEST")
                .tubeColumns("TEST")
                .memo("TEST")
                .errorType("TEST")
                .frozenTubeState("TEST")
                .status("TEST")
                .frozenBoxCode("TEST")
                .patientId(1L)
                .dob(ZonedDateTime.now())
                .gender("TEST")
                .diseaseType("TEST")
                .isHemolysis(false)
                .isBloodLipid(false)
                .visitType("TEST")
                .visitDate(ZonedDateTime.now())
                .age(0)
                .sampleStage("TEST");

            frozenTube.setFrozenBox(frozenBox);
            frozenTube.setFrozenTubeType(frozenTubeTypeRepository.findOne(1L));
            frozenTube.setSampleType(sampleTypeRepository.findOne(1L));

            frozenTube = frozenTubeRepository.saveAndFlush(frozenTube);
        }

        ZonedDateTime start = ZonedDateTime.now();
        ArrayList<StockOutReqFrozenTube> tubes = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            StockOutReqFrozenTube tube = new StockOutReqFrozenTube();
            tube.importingSampleId(0L)
                .frozenTubeTypeId(1L)
                .sampleTypeId(1L)
//                .sampleClassificationId(1L)
//                .projectId(1L)
//                .projectSiteId(1L)
                .frozenBox(null)
                .frozenTube(null)
                .stockOutFrozenBox(null)
                .stockOutTask(null)
                .stockOutRequirement(null)
                .status("TEST")
                .memo("TEST")
                .tubeRows("TEST")
                .tubeColumns("TEST")
                .repealReason("TEST")
                .frozenBoxCode("TEST")
//                .frozenBoxCode1D("TEST")
                .projectCode("TEST")
                .projectSiteCode("TEST")
                .frozenTubeCode("TEST")
                .sampleTempCode("TEST")
                .sampleCode("TEST")
                .frozenTubeTypeCode("TEST")
                .frozenTubeTypeName("TEST")
                .sampleTypeCode("TEST")
                .sampleTypeName("TEST")
                .sampleClassificationCode("TEST")
                .sampleClassificationName("TEST")
                .sampleUsedTimesMost(0)
                .sampleUsedTimes(0)
                .frozenTubeVolumns(0d)
                .sampleVolumns(0d)
                .frozenTubeVolumnsUnit("TEST")
                .errorType("TEST")
                .frozenTubeState("TEST")
                ;
            tube.setFrozenBox(frozenBox);
            tube.setFrozenTube(frozenTube);
            tube.setStockOutRequirement(stockOutRequirement);
            tubes.add(tube);
            if (tubes.size() > 50000){
                stockOutReqFrozenTubeRepository.save(tubes);
                tubes.clear();
            }
        }
        if (tubes.size() > 0){
            stockOutReqFrozenTubeRepository.save(tubes);
            tubes.clear();
        }

        ZonedDateTime end = ZonedDateTime.now();

        log.debug("BEGIN\t" + start.toString());
        log.debug("END\t" + end.toString());
        log.debug("DURATION\t" + Duration.between(start,end).getSeconds());

    }


}
