package org.fwoxford.service;

/**
 * Created by zhuyu on 2017/5/14.
 */

public enum BBISReportType {
    StockOutRequirement(1, "report-stock-out-application.xlsx"),
    StockOutCheckResult(2, "report-stock-out-check-result.xlsx"),
    StockOutTakeBox(3, "report-stock-out-take-box.xlsx"),
    StockOutHandover(4, "report-stock-out-take-over.xlsx");

    private int index;

    private String templateFilePath;

    BBISReportType(int idx, String filePath) {
        this.index = idx;
        this.templateFilePath = filePath;
    }

    public int getIndex() {
        return index;
    }

    public String getTemplateFilePath() {
        return templateFilePath;
    }
}
