package org.fwoxford.service;

import org.fwoxford.BioBankApp;
import org.fwoxford.service.dto.response.StockOutApplyReportDTO;
import org.fwoxford.service.dto.response.StockOutRequirementReportDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
@Transactional
public class ReportExportingServiceIntTest {

    private final Logger log = LoggerFactory.getLogger(ReportExportingServiceIntTest.class);

    @Autowired
    private ReportExportingService reportExportingService;

    @Test
    public void testMakeStockOutRequirementReport() {
        StockOutApplyReportDTO dto = new StockOutApplyReportDTO();
        try {
            dto.setApplyNumber("123456");
            StockOutRequirementReportDTO req = new StockOutRequirementReportDTO();
            req.setCountOfSample(1000);
            req.setMemo("Test");
            req.setCountOfStockOutSample(900);
            dto.setRequirements(new ArrayList<>());
            dto.getRequirements().add(req);
            dto.getRequirements().add(req);
            dto.getRequirements().add(req);

            ByteArrayOutputStream stream = reportExportingService.makeStockOutRequirementReport(dto);
            File dir = new File(".");

            System.out.println(dir.getCanonicalPath());
            System.out.println(dir.getAbsolutePath());

            OutputStream ofs = null;
            ofs = new FileOutputStream(dir.getCanonicalPath() + "/" + stream.hashCode() + ".xlsx");
            stream.writeTo(ofs);

            ofs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        assertThat(allManagedUsers.getContent().stream()
//            .noneMatch(user -> Constants.ANONYMOUS_USER.equals(user.getLogin())))
//            .isTrue();
    }
}
