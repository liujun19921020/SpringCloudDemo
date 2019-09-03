package com.lj.commonsutils.excel;

import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析Excel的类
 */
public class ExcelAnalysis {

    private static Logger logger = LoggerFactory.getLogger(ExcelAnalysis.class);

    //数据存放的对象
    private List<List<String>> data = null;

    //存放每一行的临时对象
    private List<String> row = null;

    //需要触发回调的行数 默认10000
    private int limit = 10000;

    private boolean beforeBack = true;

    //回调接口
    private ExcelAnalysisCallBack excelAnalysisCallBack;

    private final OPCPackage xlsxPackage;

    public ExcelAnalysis(OPCPackage xlsxPackage, ExcelAnalysisCallBack callBack) {
        this.excelAnalysisCallBack = callBack;
        this.xlsxPackage = xlsxPackage;
        data = new ArrayList<>();
    }


    /**
     * 基于事件驱动型Sheet解析的Handler
     * 有效避免了oom的问题，解析效率低一点
     * 不同的业务解析可能需要重新实现不同的Handler
     */
    public class analysisHandler implements XSSFSheetXMLHandler.SheetContentsHandler {

        @Override
        public void startRow(int rowNum) {
            beforeBack = true;
            row = new ArrayList<>();
        }

        @Override
        public void endRow(int rowNum) {
            data.add(row);
            row = null;
            if (beforeBack && rowNum % limit == 0) {
                limitCallBack();//达到一定量时走回调
                beforeBack = false;
            }
        }

        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment xssfComment) {
            row.add(formattedValue);
        }

        @Override
        public void headerFooter(String text, boolean isHeader, String tagName) {

        }

        @Override
        public void endSheet() {
            if (beforeBack){
                limitCallBack();
            }
            logger.info("sheet解析完成");
        }
    }

    public List<List<String>> getData() {
        return data;
    }

    /**
     * 回调
     */
    private void limitCallBack() {
        excelAnalysisCallBack.limitCallBack(data);
        data = new ArrayList<>();
    }

    private void processSheet(
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            XSSFSheetXMLHandler.SheetContentsHandler sheetHandler,
            InputStream sheetInputStream)
            throws IOException, ParserConfigurationException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(
                    styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
        }
    }


    public void process()
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            String sheetName = iter.getSheetName();
            logger.info("正在读取sheet： " + sheetName + " [index=" + index + "]:");
            processSheet(styles, strings, new analysisHandler(), stream);
            logger.info("sheet 读取完成!");
            stream.close();
            ++index;
        }
    }


}
