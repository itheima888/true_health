package com.itheima.service;

import java.util.Map;

/**
 * 运营数据统计报表接口
 */
public interface ReportService {
    /**
     * 运营数据统计报表
     * @return
     */
    Map getBusinessReportData() throws Exception;
}
