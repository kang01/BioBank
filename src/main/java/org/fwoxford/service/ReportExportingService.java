package org.fwoxford.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.fwoxford.service.dto.response.StockOutApplyReportDTO;
import org.fwoxford.service.dto.response.StockOutRequirementDetailReportDTO;
import org.fwoxford.service.dto.response.StockOutRequirementReportDTO;
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

import static org.fwoxford.service.BBISReportType.StockOutRequirement;

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
        XSSFCell cell = dataRow.getCell(colIndex);

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
        XSSFCell cell = dataRow.getCell(colIndex);

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
            int dpi = 150;
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
        return null;
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
