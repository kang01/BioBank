package org.fwoxford.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.fwoxford.domain.StockOutHandover;
import org.fwoxford.service.dto.response.*;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.fwoxford.service.BBISReportType.StockOutCheckResult;
import static org.fwoxford.service.BBISReportType.StockOutRequirement;
import static org.fwoxford.service.BBISReportType.StockOutTakeBox;

@Service
@Transactional
public class ReportExportingService {
    private final Logger log = LoggerFactory.getLogger(ReportExportingService.class);

    @Autowired
    private HttpServletRequest request;

    public XSSFWorkbook openReportTemplate(BBISReportType type, InputStream stream) throws IOException {

        ServletContext ctx = request.getServletContext();
        switch (type){
            case StockOutRequirement:
            case StockOutCheckResult:
            case StockOutHandover:
            case StockOutTakeBox:
                if (stream == null){
                    stream = ctx.getResourceAsStream("/content/templates/" + type.getTemplateFilePath());
                }
                break;
            default:
                return null;
        }

        // 打开 Excel 文件
        XSSFWorkbook workbook = new XSSFWorkbook(stream);

        return workbook;
    }

    private XSSFCell getCell(XSSFSheet sheet, String pos){
        String colPos = "";
        String rowPos = "";
        int colIndex = 0;
        int rowIndex = 0;
        int nColStep = 26;

        for(char c : pos.toCharArray()){
            if (c >= 'A' && c <= 'Z'){
                colPos += c;
                colIndex = colIndex * nColStep + (c-'A'+1);
            } else if (c >= '0' && c <= '9'){
                rowPos += c;
                rowIndex = rowIndex * 10 + (c-'0');
            }
        }

        rowIndex--;colIndex--;
        rowIndex = rowIndex < 0 ? 0 : rowIndex;
        colIndex = colIndex < 0 ? 0 : colIndex;

        XSSFRow dataRow = sheet.getRow(rowIndex);
        if (dataRow == null){
            dataRow = sheet.createRow(rowIndex);
        }
        XSSFCell cell = dataRow.getCell(colIndex);
        if (cell == null){
            cell = dataRow.createCell(colIndex);
        }

        return cell;
    }

    private XSSFCell getCell(XSSFSheet sheet, String colPos, int rowPos){
        int colIndex = 0;
        int rowIndex = rowPos;
        int nColStep = 26;

        for(char c : colPos.toCharArray()){
            if (c >= 'A' && c <= 'Z'){
                colPos += c;
                colIndex = colIndex * nColStep + (c-'A'+1);
            }
        }

        rowIndex--;colIndex--;
        rowIndex = rowIndex < 0 ? 0 : rowIndex;
        colIndex = colIndex < 0 ? 0 : colIndex;

        XSSFRow dataRow = sheet.getRow(rowIndex);
        if (dataRow == null){
            dataRow = sheet.createRow(rowIndex);
        }
        XSSFCell cell = dataRow.getCell(colIndex);
        if (cell == null){
            cell = dataRow.createCell(colIndex);
        }

        return cell;
    }

    private ByteArrayOutputStream makeBarcodeImage(String code) throws IOException {

        if (code == null || code.length() == 0){
            return null;
        }

        int dpi = 150;
        ByteArrayOutputStream barCodeImage = new ByteArrayOutputStream();
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(barCodeImage, "image/png", dpi,
            BufferedImage.TYPE_BYTE_BINARY, false, 0);

        Code128Bean barCodeGenerator = new Code128Bean();
        barCodeGenerator.setModuleWidth(UnitConv.in2mm(1.0f/dpi));
        barCodeGenerator.doQuietZone(false);
        barCodeGenerator.generateBarcode(canvas, code);
        double imageWidth = canvas.getDimensions().getWidth();
        double imageHeight = canvas.getDimensions().getHeight();
        canvas.finish();

        return barCodeImage;
    }

    private Integer insertImageToCell(XSSFCell cell, int picIndex){
        XSSFSheet sheet = cell.getSheet();
        XSSFWorkbook workbook = sheet.getWorkbook();

        List<XSSFPictureData> data = workbook.getAllPictures();
        if (data == null || data.size() == 0 || data.size() < picIndex || data.get(picIndex) == null){
            return null;
        }

        Drawing drawing = sheet.createDrawingPatriarch();
        CreationHelper helper = workbook.getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();

        anchor.setCol1(cell.getColumnIndex());
        anchor.setRow1(cell.getRowIndex());
        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);

        Picture picture = drawing.createPicture(anchor, picIndex);
        picture.resize();

        return picIndex;
    }

    private Integer insertImageToCell(XSSFCell cell, byte[] image){
        XSSFSheet sheet = cell.getSheet();
        XSSFWorkbook workbook = sheet.getWorkbook();
        Drawing drawing = sheet.createDrawingPatriarch();
        CreationHelper helper = workbook.getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();

        anchor.setCol1(cell.getColumnIndex());
        anchor.setRow1(cell.getRowIndex());
        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);

        Integer picIndex = workbook.addPicture(image, Workbook.PICTURE_TYPE_PNG);
        Picture picture = drawing.createPicture(anchor, picIndex);
        picture.resize();

        return picIndex;
    }

    public ByteArrayOutputStream makeStockOutRequirementReport(StockOutApplyReportDTO applyDTO){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ByteArrayOutputStream barCodeImage = makeBarcodeImage(applyDTO.getApplyNumber());
            Integer barCodePicIndex = null;

            XSSFWorkbook workbook = openReportTemplate(StockOutRequirement, null);
            // 找到Sheet
            XSSFSheet summarySheet = workbook.getSheetAt(0);

            // 申请单编号
            XSSFCell cellNO = getCell(summarySheet, "G2");
            if (applyDTO.getApplyNumber() != null && applyDTO.getApplyNumber().length() > 0){
//                cellNO.setCellValue(applyDTO.getApplyNumber());
                barCodePicIndex = insertImageToCell(cellNO, barCodeImage.toByteArray());
                barCodeImage.close();
            }

            // 委托方
            XSSFCell cellRequireCompany = getCell(summarySheet, "B4");
            cellRequireCompany.setCellValue(applyDTO.getApplyCompany());

            // 需求时间
            XSSFCell cellRequireDate = getCell(summarySheet, "B5");
            if (applyDTO.getStartDate() != null && applyDTO.getEndDate() != null){
                String date = applyDTO.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
                    + " ~ " + applyDTO.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                cellRequireDate.setCellValue(date);
            }

            // 需求样本数量
            XSSFCell cellRequireQty = getCell(summarySheet, "E5");
            if (applyDTO.getCountOfSample() != null){
                cellRequireQty.setCellValue(applyDTO.getCountOfSample());
            }

            // 用途
            XSSFCell cellUsage = getCell(summarySheet, "A7");
            cellUsage.setCellValue(applyDTO.getPurposeOfSample());

            // 出库数量
            XSSFCell cellStockOutQty = getCell(summarySheet, "E8");
            if (applyDTO.getCountOfStockOutSample() != null) {
                cellStockOutQty.setCellValue(applyDTO.getCountOfStockOutSample());
            }

            // 相关项目
            XSSFCell cellProjects = getCell(summarySheet, "A10");
            if (applyDTO.getProjects() != null && applyDTO.getProjects().size() > 0){
                String projects = String.join("; \t", applyDTO.getProjects());
                cellProjects.setCellValue(projects);
            }

            // 申请日期
            XSSFCell cellRequireDate2 = getCell(summarySheet, "H11");
            if (applyDTO.getApplicationDate() != null){
                String date = applyDTO.getApplicationDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                cellRequireDate2.setCellValue(date);
            }

            // 委托人
            XSSFCell cellRequirePeople = getCell(summarySheet, "H12");
            cellRequirePeople.setCellValue(applyDTO.getApplicantName());

            // 记录日期
            XSSFCell cellRecordDate = getCell(summarySheet, "J11");
            if (applyDTO.getRecordDate() != null){
                String date = applyDTO.getRecordDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                cellRecordDate.setCellValue(date);
            }

            // 记录人
            XSSFCell cellRecordPeople = getCell(summarySheet, "J12");
            cellRecordPeople.setCellValue(applyDTO.getRecorderName());


            XSSFSheet detailSheet = workbook.getSheetAt(1);
            List<StockOutRequirementReportDTO> requirements = applyDTO.getRequirements();
            if (requirements == null){
                requirements = new ArrayList<>();
            } else {
                if (applyDTO.getApplyNumber() != null && applyDTO.getApplyNumber().length() > 0){
                    cellNO = getCell(detailSheet, "G2");
//                    cellNO.setCellValue(applyDTO.getApplyNumber());

                    barCodePicIndex = insertImageToCell(cellNO, barCodePicIndex);
                }
            }

            int no = 1;
            int startRowPos = 4;
            CellCopyPolicy policy = new CellCopyPolicy();
            for(StockOutRequirementReportDTO r : requirements){

                // 序号
                XSSFCell cellOneRequireNO = getCell(detailSheet, "B", startRowPos);
                cellOneRequireNO.setCellValue(no++);

                // 需求名称
                XSSFCell cellOneRequireName = getCell(detailSheet, "D", startRowPos);
                cellOneRequireName.setCellValue(r.getRequirementName());

                // 需求数量
                XSSFCell cellOneRequireQty = getCell(detailSheet, "D", startRowPos+1);
                if (r.getCountOfSample() != null) {
                    cellOneRequireQty.setCellValue(r.getCountOfSample());
                }

                // 出库数量
                XSSFCell cellOneStockOutQty = getCell(detailSheet, "G", startRowPos+1);
                if (r.getCountOfStockOutSample() != null) {
                    cellOneStockOutQty.setCellValue(r.getCountOfStockOutSample());
                }

                // 差额
                XSSFCell cellOneGapQty = getCell(detailSheet, "J", startRowPos+1);
                if (r.getCountOfSample() != null && r.getCountOfStockOutSample() != null) {
                    cellOneGapQty.setCellValue(r.getCountOfSample() - r.getCountOfStockOutSample());
                }

                // 备注
                XSSFCell cellOneMemo = getCell(detailSheet, "A", startRowPos+3);
                cellOneMemo.setCellValue(r.getMemo());

                // 移动到下一个空表
                startRowPos += 4;

                // 复制一个空表
                detailSheet.copyRows(startRowPos-1, startRowPos + 3, startRowPos + 3, policy);

            }

            // 输出Excel文档到 Output Stream
            workbook.write(outputStream);
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream;
    }

    public ByteArrayOutputStream makeStockOutRequirementCheckReport(StockOutRequirementDetailReportDTO requirementDTO){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ByteArrayOutputStream barCodeImage = makeBarcodeImage(requirementDTO.getRequirementNO());
            Integer barCodePicIndex = null;

            XSSFWorkbook workbook = openReportTemplate(StockOutCheckResult, null);
            // 找到Sheet
            XSSFSheet summarySheet = workbook.getSheetAt(0);

            // 需求编号
            XSSFCell cellNO = getCell(summarySheet, "I2");
            if (requirementDTO.getRequirementNO() != null && requirementDTO.getRequirementNO().length() > 0){
//                cellNO.setCellValue(applyDTO.getApplyNumber());
                barCodePicIndex = insertImageToCell(cellNO, barCodeImage.toByteArray());
                barCodeImage.close();
            }

            // 委托方
            String requirement = "";
            if (requirementDTO.getCountOfSample() != null){
                requirement += String.format("样本 %d 只; ", requirementDTO.getCountOfSample());
            }
            if (requirementDTO.getSampleType() != null && requirementDTO.getSampleType().length() > 0){
                requirement += String.format("%s 类型; ", requirementDTO.getSampleType());
            }
            if (requirementDTO.getSex() != null && requirementDTO.getSex().length() > 0){
                requirement += String.format("性别 %s; ", requirementDTO.getSex());
            }
            if (requirementDTO.getAges() != null && requirementDTO.getAges().length() > 0){
                requirement += String.format("年龄 %s; ", requirementDTO.getAges());
            }
            if (requirementDTO.getDiseaseType() != null && requirementDTO.getDiseaseType().length() > 0){
                requirement += String.format("%s 疾病; ", requirementDTO.getDiseaseType());
            }
            if (requirementDTO.getProjects() != null && requirementDTO.getProjects().length() > 0){
                requirement += String.format("可用项目 %s ", requirementDTO.getProjects());
            }
            if (requirementDTO.getMemo() != null && requirementDTO.getMemo().length() > 0){
                requirement += String.format("\r\n%s。 ", requirementDTO.getMemo());
            }
            // 需求信息
            XSSFCell cellRequirement = getCell(summarySheet, "A4");
            cellRequirement.setCellValue(requirement);

            List<StockOutSampleCheckResultDTO> results = requirementDTO.getCheckResults();
            if (results == null){
                results = new ArrayList<>();
            }

            int no = 1;
            int startRowPos = 6;
            XSSFCell startCell = getCell(summarySheet, "A", startRowPos);
            XSSFCellStyle style = startCell.getCellStyle();
            for(StockOutSampleCheckResultDTO r : results){

                // 序号
                XSSFCell cellOneRequireNO = getCell(summarySheet, "A", startRowPos);
                cellOneRequireNO.setCellStyle(style);
                cellOneRequireNO.setCellValue(no++);

                // 样本编码
                XSSFCell cellOneSampleCode = getCell(summarySheet, "B", startRowPos);
                cellOneSampleCode.setCellStyle(style);
                cellOneSampleCode.setCellValue(r.getSampleCode());

                // 样本类型
                XSSFCell cellOneSampleType = getCell(summarySheet, "C", startRowPos);
                cellOneSampleType.setCellStyle(style);
                cellOneSampleType.setCellValue(r.getSampleType());

                // 性别
                XSSFCell cellOneSex = getCell(summarySheet, "D", startRowPos);
                cellOneSex.setCellStyle(style);
                cellOneSex.setCellValue(r.getSex());

                // 年龄
                XSSFCell cellOneAge = getCell(summarySheet, "E", startRowPos);
                cellOneAge.setCellStyle(style);
                cellOneAge.setCellValue(r.getAge());

                // 使用次数
                XSSFCell cellOneTiems = getCell(summarySheet, "F", startRowPos);
                cellOneTiems.setCellStyle(style);
                if (r.getTimes() != null){
                    cellOneTiems.setCellValue(r.getTimes());
                }

                // 疾病类型
                XSSFCell cellOneDiseaseType = getCell(summarySheet, "G", startRowPos);
                cellOneDiseaseType.setCellStyle(style);
                cellOneDiseaseType.setCellValue(r.getDiseaseType());

                // 项目编码
                XSSFCell cellOneProject = getCell(summarySheet, "H", startRowPos);
                cellOneProject.setCellStyle(style);
                cellOneProject.setCellValue(r.getProjectCode());

                // 状态
                XSSFCell cellOneStatus = getCell(summarySheet, "I", startRowPos);
                cellOneStatus.setCellStyle(style);
                cellOneStatus.setCellValue(r.getStatus());

                // 批注
                XSSFCell cellOneMemo = getCell(summarySheet, "J", startRowPos);
                cellOneMemo.setCellStyle(style);
                cellOneMemo.setCellValue(r.getMemo());

                startRowPos++;
            }

            // 输出Excel文档到 Output Stream
            workbook.write(outputStream);
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream;
    }

    public ByteArrayOutputStream makeStockOutHandoverReport(StockOutHandoverReportDTO handoverDTO){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ByteArrayOutputStream barCodeImage = makeBarcodeImage(handoverDTO.getHandOverNumber());
            ByteArrayOutputStream barCodeImageForApplication = makeBarcodeImage(handoverDTO.getApplicationNumber());
            Integer barCodePicIndex = null;
            Integer barCodePicIndexForApplication = null;

            XSSFWorkbook workbook = openReportTemplate(BBISReportType.StockOutHandover, null);
            // 找到Sheet
            XSSFSheet summarySheet = workbook.getSheetAt(0);

            // 交接单编号
            XSSFCell cellNO = getCell(summarySheet, "G2");
            if (handoverDTO.getHandOverNumber() != null && handoverDTO.getHandOverNumber().length() > 0){
                barCodePicIndex = insertImageToCell(cellNO, barCodeImage.toByteArray());
                barCodeImage.close();
            }

            // 申请单编号
            XSSFCell cellApplyNO = getCell(summarySheet, "B5");
            if (handoverDTO.getApplicationNumber() != null && handoverDTO.getApplicationNumber().length() > 0){
                barCodePicIndexForApplication = insertImageToCell(cellApplyNO, barCodeImageForApplication.toByteArray());
                barCodeImageForApplication.close();
            }

            // 出库计划编号
            XSSFCell cellPlanCode = getCell(summarySheet, "I5");
            cellPlanCode.setCellValue(handoverDTO.getPlanNumber());

            // 出库任务编号
            XSSFCell cellTaskCode = getCell(summarySheet, "I6");
            cellTaskCode.setCellValue(handoverDTO.getTaskNumber());

            // 接收方
            XSSFCell cellReceiverCompany = getCell(summarySheet, "B8");
            cellReceiverCompany.setCellValue(handoverDTO.getReceiverCompany());

            // 联系电话
            XSSFCell cellReceiverContact = getCell(summarySheet, "I8");
            cellReceiverContact.setCellValue(handoverDTO.getReceiverContact());

            // 接收人
            XSSFCell cellReceiver = getCell(summarySheet, "B9");
            cellReceiver.setCellValue(handoverDTO.getReceiver());

            // 交付人
            XSSFCell cellDeliver = getCell(summarySheet, "E9");
            cellDeliver.setCellValue(handoverDTO.getDeliver());

            // 交接时间
            XSSFCell cellHandoverDate = getCell(summarySheet, "I9");
            cellHandoverDate.setCellValue(handoverDTO.getHandoverDate());

            // 样本数量
            XSSFCell cellSampleQty = getCell(summarySheet, "B10");
            if (handoverDTO.getCountOfSample() != null){
                cellSampleQty.setCellValue(handoverDTO.getCountOfSample());
            }

            // 盒子数量
            XSSFCell cellBoxQty = getCell(summarySheet, "E10");
            if (handoverDTO.getCountOfBox() != null){
                cellBoxQty.setCellValue(handoverDTO.getCountOfBox());
            }

            // 备注
            XSSFCell cellMemo = getCell(summarySheet, "A12");
            cellMemo.setCellValue(handoverDTO.getMemo());

            // 接收日期
            XSSFCell cellReceiveDate = getCell(summarySheet, "H13");
            cellReceiveDate.setCellValue(handoverDTO.getReceiveDate());

            // 接收人
            XSSFCell cellReceiver2 = getCell(summarySheet, "A14");
            cellReceiver2.setCellValue(handoverDTO.getReceiver());

            // 交付日期
            XSSFCell cellDeliverDate = getCell(summarySheet, "J13");
            cellDeliverDate.setCellValue(handoverDTO.getDeliverDate());

            // 交付人
            XSSFCell cellRequirePeople = getCell(summarySheet, "J14");
            cellRequirePeople.setCellValue(handoverDTO.getDeliver());

            XSSFSheet detailSheet = workbook.getSheetAt(1);
            List<StockOutHandoverSampleReportDTO> samples = handoverDTO.getSamples();
            if (samples == null){
                samples = new ArrayList<>();
            } else {
                if (handoverDTO.getHandOverNumber() != null && handoverDTO.getHandOverNumber().length() > 0){
                    cellNO = getCell(detailSheet, "H2");
                    barCodePicIndex = insertImageToCell(cellNO, barCodePicIndex);
                }
            }

            int no = 1;
            int startRowPos = 5;
            XSSFCell startCell = getCell(summarySheet, "A", startRowPos);
            XSSFCellStyle style = startCell.getCellStyle();
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            for(StockOutHandoverSampleReportDTO r : samples){

                // 序号
                XSSFCell cellOneRequireNO = getCell(detailSheet, "A", startRowPos);
                cellOneRequireNO.setCellStyle(style);
                cellOneRequireNO.setCellValue(no++);

                // 临时盒编码
                XSSFCell cellOneBoxCode = getCell(detailSheet, "B", startRowPos);
                cellOneBoxCode.setCellStyle(style);
                cellOneBoxCode.setCellValue(r.getBoxCode());

                // 位置
                XSSFCell cellOneLocation = getCell(detailSheet, "C", startRowPos);
                cellOneLocation.setCellStyle(style);
                cellOneLocation.setCellValue(r.getLocation());

                // 样本编码
                XSSFCell cellOneSampleCode = getCell(detailSheet, "D", startRowPos);
                cellOneSampleCode.setCellStyle(style);
                cellOneSampleCode.setCellValue(r.getSampleCode());

                // 类型
                XSSFCell cellOneSampleType = getCell(detailSheet, "E", startRowPos);
                cellOneSampleType.setCellStyle(style);
                cellOneSampleType.setCellValue(r.getSampleType());

                // 性别
                XSSFCell cellOneSex = getCell(detailSheet, "F", startRowPos);
                cellOneSex.setCellStyle(style);
                cellOneSex.setCellValue(r.getSex());

                // 年龄
                XSSFCell cellOneAge = getCell(detailSheet, "G", startRowPos);
                cellOneAge.setCellStyle(style);
                cellOneAge.setCellValue(r.getAge());

                // 疾病
                XSSFCell cellOneDiseaseType = getCell(detailSheet, "H", startRowPos);
                cellOneDiseaseType.setCellStyle(style);
                cellOneDiseaseType.setCellValue(r.getDiseaseType());

                // 项目编码
                XSSFCell cellOneProjectCode = getCell(detailSheet, "I", startRowPos);
                cellOneProjectCode.setCellStyle(style);
                cellOneProjectCode.setCellValue(r.getProjectCode());

                startRowPos++;
            }

            // 输出Excel文档到 Output Stream
            workbook.write(outputStream);
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream;
    }

    public ByteArrayOutputStream makeStockOutTakeBoxReport(List<StockOutTakeBoxReportDTO> takeBoxDTOs){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            XSSFWorkbook workbook = openReportTemplate(StockOutTakeBox, null);
            // 找到Sheet
            XSSFSheet detailSheet = workbook.getSheetAt(0);

            int no = 1;
            int startRowPos = 3;
            XSSFCell startCell = getCell(detailSheet, "A", startRowPos);
            XSSFCellStyle style = startCell.getCellStyle();
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);

            for(StockOutTakeBoxReportDTO r : takeBoxDTOs){
                // 序号
                XSSFCell cellOneNO = getCell(detailSheet, no % 2 == 0 ? "I" : "A", startRowPos);
                cellOneNO.setCellStyle(style);
                cellOneNO.setCellValue(no);

                // 设备
                XSSFCell cellOneEquipment = getCell(detailSheet, no % 2 == 0 ? "J" : "B", startRowPos);
                cellOneEquipment.setCellStyle(style);
                cellOneEquipment.setCellValue(r.getEquipmentCode());

                // 区域
                XSSFCell cellOneArea = getCell(detailSheet, no % 2 == 0 ? "K" : "C", startRowPos);
                cellOneArea.setCellStyle(style);
                cellOneArea.setCellValue(r.getAreaCode());

                // 架子
                XSSFCell cellOneShelf = getCell(detailSheet, no % 2 == 0 ? "L" : "D", startRowPos);
                cellOneShelf.setCellStyle(style);
                cellOneShelf.setCellValue(r.getShelfCode());

                // 位置
                XSSFCell cellOneLocation = getCell(detailSheet, no % 2 == 0 ? "M" : "E", startRowPos);
                cellOneLocation.setCellStyle(style);
                cellOneLocation.setCellValue(r.getShelfLocation());

                // 盒编码
                XSSFCell cellOneBox = getCell(detailSheet, no % 2 == 0 ? "N" : "F", startRowPos);
                cellOneBox.setCellStyle(style);
                cellOneBox.setCellValue(r.getBoxCode());

                // 盒编码
                XSSFCell cellOneCheck = getCell(detailSheet, no % 2 == 0 ? "O" : "G", startRowPos);
                cellOneCheck.setCellStyle(style);
                cellOneCheck.setCellValue("□");

                startRowPos += no % 2 == 0 ? 1 : 0;
                no++;
            }

            // 输出Excel文档到 Output Stream
            workbook.write(outputStream);
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream;
    }

    public HashSet<String[]> readRequiredSamplesFromExcelFile(InputStream stream){

        HashSet<String[]> sampleSet = new HashSet<>();

        try {
            XSSFWorkbook workbook = workbook = new XSSFWorkbook(stream);
            XSSFSheet samplesSheet = workbook.getSheetAt(0);
            int sampleCodeColIndex = 0;
            int sampleTypeColIndex = 1;
            int startRowIndex = 1;
            int countOfEmptyRow = 0;
            List<Integer> emptyRowIndexos = new ArrayList<Integer>();
            for(int i = startRowIndex; i < 65536 && countOfEmptyRow >= 10; ++i){
                XSSFRow dataRow = samplesSheet.getRow(i);
                XSSFCell cell = dataRow.getCell(sampleCodeColIndex);
                String sampleCode = cell.getRawValue();
                cell = dataRow.getCell(sampleTypeColIndex);
                String sampleType = cell.getRawValue();
                if (sampleCode == null || sampleType == null || sampleCode.length() == 0 || sampleType.length() == 0){
                    if (emptyRowIndexos.size() == 0 || i-1 == emptyRowIndexos.get(emptyRowIndexos.size() - 1)){
                        countOfEmptyRow++;
                    }
                    emptyRowIndexos.add(i);
                } else {
                    sampleSet.add(new String[] {sampleCode, sampleType});
                }
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sampleSet;
    }
}
