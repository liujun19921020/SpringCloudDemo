package com.lj.commonsutils.helper;

import com.lj.commonsutils.excel.*;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel 帮助API
 */
public class ExcelHelper {

    private static Logger logger = LoggerFactory.getLogger(ExcelHelper.class);

    /**
     * 默认的开始读取的行位置为第一行（索引值为0）
     */
    private final static int READ_START_POS = 0;

    /**
     * 默认结束读取的行位置为最后一行（索引值=0，用负数来表示倒数第n行）
     */
    private final static int READ_END_POS = 0;

    /**
     * 默认Excel内容的开始比较列位置为第一列（索引值为0）
     */
    private final static int COMPARE_POS = 0;

    /**
     * 默认多文件合并的时需要做内容比较（相同的内容不重复出现）
     */
    private final static boolean NEED_COMPARE = true;

    /**
     * 默认多文件合并的新文件遇到名称重复时，进行覆盖
     */
    private final static boolean NEED_OVERWRITE = true;

    /**
     * 默认只操作一个sheet
     */
    private final static boolean ONLY_ONE_SHEET = true;

    /**
     * 默认读取第一个sheet中（只有当ONLY_ONE_SHEET = true时有效）
     */
    private final static int SELECTED_SHEET = 0;

    /**
     * 默认从第一个sheet开始读取（索引值为0）
     */
    private final static int READ_START_SHEET= 0;

    /**
     * 默认在最后一个sheet结束读取（索引值=0，用负数来表示倒数第n行）
     */
    private final static int READ_END_SHEET = 0;

    /**
     * 默认打印各种信息
     */
    private final static boolean PRINT_MSG = true;


    /**
     * Excel文件路径
     */
    private String excelPath = "data.xlsx";

    /**
     * 设定开始读取的位置，默认为0
     */
    private int startReadPos = READ_START_POS;

    /**
     * 设定结束读取的位置，默认为0，用负数来表示倒数第n行
     */
    private int endReadPos = READ_END_POS;

    /**
     * 设定开始比较的列位置，默认为0
     */
    private int comparePos = COMPARE_POS;

    /**
     *  设定汇总的文件是否需要替换，默认为true
     */
    private boolean isOverWrite = NEED_OVERWRITE;

    /**
     *  设定是否需要比较，默认为true(仅当不覆写目标内容是有效，即isOverWrite=false时有效)
     */
    private boolean isNeedCompare = NEED_COMPARE;

    /**
     * 设定是否只操作第一个sheet
     */
    private boolean onlyReadOneSheet = ONLY_ONE_SHEET;

    /**
     * 设定操作的sheet在索引值
     */
    private int selectedSheetIdx =SELECTED_SHEET;

    /**
     * 设定操作的sheet的名称
     */
    private String selectedSheetName = "";

    /**
     * 设定开始读取的sheet，默认为0
     */
    private int startSheetIdx = READ_START_SHEET;

    /**
     * 设定结束读取的sheet，默认为0，用负数来表示倒数第n行
     */
    private int endSheetIdx = READ_END_SHEET;

    /**
     * 设定是否打印消息
     */
    private boolean printMsg = PRINT_MSG;



    public ExcelHelper(){

    }

    public ExcelHelper(String excelPath){
        this.excelPath = excelPath;
    }

    /**
     * 用来验证excel与Vo中的类型是否一致 <br>
     * Map<栏位类型,只能是哪些Cell类型>
     */
    private static Map<Class<?>, CellType[]> validateMap = new HashMap<>();


    /**
     *
     * @param datalist
     * @param out
     */
    public static void exportExcel(String[][] datalist, OutputStream out) {
        exportExcel(datalist,out,true);
    }

    /**
     * static blocks
     */
    static {
        validateMap.put(String[].class, new CellType[]{CellType.STRING});
        validateMap.put(Double[].class, new CellType[]{CellType.NUMERIC});
        validateMap.put(String.class, new CellType[]{CellType.STRING});
        validateMap.put(Double.class, new CellType[]{CellType.NUMERIC});
        validateMap.put(Date.class, new CellType[]{CellType.NUMERIC, CellType.STRING});
        validateMap.put(Integer.class, new CellType[]{CellType.NUMERIC});
        validateMap.put(Float.class, new CellType[]{CellType.NUMERIC});
        validateMap.put(Long.class, new CellType[]{CellType.NUMERIC});
        validateMap.put(Boolean.class, new CellType[]{CellType.BOOLEAN});
    }


    /**
     * 动态创建多个sheet
     * @param sheetName
     * @param sheetHeader
     * @param sheetData
     * @param fileRealPath
     * @throws Exception
     */
    public static void createExcelSheet(List<String> sheetName, List<List> sheetHeader,List<List<List>> sheetData, String fileRealPath)
            throws Exception {
        HSSFWorkbook workBook = new HSSFWorkbook();

        int sheetnum = sheetName.size();

        for(int m=0; m<sheetnum; m++){
            HSSFSheet sheet = workBook.createSheet();
            String msg = sheetName.get(m).toString();
            String str = new String(msg.getBytes("UTF-8"),"UTF-8");
            workBook.setSheetName(m,str);
            HSSFHeader header = sheet.getHeader();
            header.setCenter("sheet");
            HSSFRow headerRow = sheet.createRow(0);

            HSSFCellStyle headstyle = workBook.createCellStyle();
            HSSFFont headfont = workBook.createFont();
            headfont.setColor(Font.COLOR_RED);
            headfont.setBold(true);

            headstyle.setFont(headfont);
            for (int i = 0; i < sheetHeader.get(m).size(); i++) {
                HSSFCell headerCell = headerRow.createCell(i);
                headerCell.setCellStyle(headstyle);
                // 设置cell的值
                headerCell.setCellValue(sheetHeader.get(m).get(i).toString());
                headerCell.setCellStyle(headstyle);
            }

            int rowIndex = 1;
            for (int i = 0; i < sheetData.get(m).size(); i++) {
                List<String> list2 = sheetData.get(m).get(i);
                HSSFRow row = sheet.createRow(rowIndex);
                for (int q = 0; q < list2.size(); q++) {
                    // 创建第i个单元格
                    HSSFCell cell = row.createCell(q);
                    cell.setCellValue(String.valueOf(list2.get(q)).replace("未知", ""));
                    sheet.setColumnWidth(q, (80 * 50));
                }
                rowIndex++;
            }

        }
        FileOutputStream fos = new FileOutputStream(fileRealPath);
        workBook.write(fos);
        fos.close();
    }

    /**
     * 获取cell类型的文字描述
     * @param cellType
     * @return
     */
    private static String getCellTypeByInt(CellType cellType) {
        if(cellType == CellType.BLANK)
            return "Null type";
        else if(cellType == CellType.BOOLEAN)
            return "Boolean type";
        else if(cellType == CellType.ERROR)
            return "Error type";
        else if(cellType == CellType.FORMULA)
            return "Formula type";
        else if(cellType == CellType.NUMERIC)
            return "Numeric type";
        else if(cellType == CellType.STRING)
            return "String type";
        else
            return "Unknown type";
    }

    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param <T>
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                javabean属性的数据类型有基本数据类型及String,Date,String[],Double[]
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     */
    public static <T> void exportExcel(Map<String,String> headers, Collection<T> dataset, OutputStream out) {
        exportExcel(headers, dataset, out, null);
    }

    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param <T>
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                javabean属性的数据类型有基本数据类型及String,Date,String[],Double[]
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    public static <T> void exportExcel(Map<String,String> headers, Collection<T> dataset, OutputStream out,
                                       String pattern) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();

        write2Sheet(sheet, headers, dataset, pattern);
        try {
            workbook.write(out);
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
    }

    /**
     *
     * @param datalist
     * @param out
     * @param autoColumnWidth
     */
    public static void exportExcel(String[][] datalist, OutputStream out,boolean autoColumnWidth) {
        try {
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet();

            for (int i = 0; i < datalist.length; i++) {
                String[] r = datalist[i];
                HSSFRow row = sheet.createRow(i);
                for (int j = 0; j < r.length; j++) {
                    HSSFCell cell = row.createCell(j);
                    //cell max length 32767
                    if (r[j] != null && r[j].length() > 32767) {
                        r[j] = "--此字段过长(超过32767),已被截断--" + r[j];
                        r[j] = r[j].substring(0, 32766);
                    }
                    cell.setCellValue(r[j]);
                }
            }
            //自动列宽
            if(autoColumnWidth) {
                if (datalist.length > 0) {
                    int colcount = datalist[0].length;
                    for (int i = 0; i < colcount; i++) {
                        sheet.autoSizeColumn(i);
                    }
                }
            }
            workbook.write(out);
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
    }


    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于多个sheet
     *
     * @param <T>
     * @param sheets {@link ExcelSheet}的集合
     * @param out    与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     */
    public static <T> void exportExcel(List<ExcelSheet<T>> sheets, OutputStream out) {
        exportExcel(sheets, out, null);
    }

    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于多个sheet
     *
     * @param <T>
     * @param sheets  {@link ExcelSheet}的集合
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    public static <T> void exportExcel(List<ExcelSheet<T>> sheets, OutputStream out, String pattern) {
        if (CollectionUtils.isEmpty(sheets)) {
            return;
        }
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        for (ExcelSheet<T> sheet : sheets) {
            // 生成一个表格
            HSSFSheet hssfSheet = workbook.createSheet(sheet.getSheetName());
            write2Sheet(hssfSheet, sheet.getHeaders(), sheet.getDataset(), pattern);
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
    }

    /**
     * 每个sheet的写入
     *
     * @param sheet   页签
     * @param headers 表头
     * @param dataset 数据集合
     * @param pattern 日期格式
     */
    private static <T> void write2Sheet(HSSFSheet sheet, Map<String,String> headers, Collection<T> dataset,
                                        String pattern) {
        //时间格式默认"yyyy-MM-dd"
        if (StringHelper.isBlank(pattern)){
            pattern = "yyyy-MM-dd";
        }
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        // 标题行转中文
        Set<String> keys = headers.keySet();
        Iterator<String> it1 = keys.iterator();
        String key = "";    //存放临时键变量
        int c= 0;   //标题列数
        while (it1.hasNext()){
            key = it1.next();
            if (headers.containsKey(key)) {
                HSSFCell cell = row.createCell(c);
                HSSFRichTextString text = new HSSFRichTextString(headers.get(key));
                cell.setCellValue(text);
                c++;
            }
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = it.next();
            try {
                if (t instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) t;
                    int cellNum = 0;
                    //遍历列名
                    Iterator<String> it2 = keys.iterator();
                    while (it2.hasNext()){
                        key = it2.next();
                        if (!headers.containsKey(key)) {
                            logger.error("Map 中 不存在 key [" + key + "]");
                            continue;
                        }
                        Object value = map.get(key);
                        HSSFCell cell = row.createCell(cellNum);

                        cellNum = setCellValue(cell,value,pattern,cellNum,null,row);

                        cellNum++;
                    }
                } else {
                    List<FieldForSortting> fields = sortFieldByAnno(t.getClass());
                    int cellNum = 0;
                    for (int i = 0; i < fields.size(); i++) {
                        HSSFCell cell = row.createCell(cellNum);
                        Field field = fields.get(i).getField();
                        field.setAccessible(true);
                        Object value = field.get(t);

                        cellNum = setCellValue(cell,value,pattern,cellNum,field,row);

                        cellNum++;
                    }
                }
            } catch (Exception e) {
                logger.error(e.toString(), e);
            }
        }
        // 设定自动宽度
        for (int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * 设置cell值
     * @param cell
     * @param value
     * @param pattern
     * @param cellNum
     * @param field
     * @param row
     * @return
     */
    private static int setCellValue(HSSFCell cell,Object value,String pattern,int cellNum,Field field,HSSFRow row){
        String textValue = null;
        if (value instanceof Integer) {
            int intValue = (Integer) value;
            cell.setCellValue(intValue);
        } else if (value instanceof Float) {
            float fValue = (Float) value;
            cell.setCellValue(fValue);
        } else if (value instanceof Double) {
            double dValue = (Double) value;
            cell.setCellValue(dValue);
        } else if (value instanceof Long) {
            long longValue = (Long) value;
            cell.setCellValue(longValue);
        } else if (value instanceof Boolean) {
            boolean bValue = (Boolean) value;
            cell.setCellValue(bValue);
        } else if (value instanceof Date) {
            Date date = (Date) value;
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            textValue = sdf.format(date);
        } else if (value instanceof String[]) {
            String[] strArr = (String[]) value;
            for (int j = 0; j < strArr.length; j++) {
                String str = strArr[j];
                cell.setCellValue(str);
                if (j != strArr.length - 1) {
                    cellNum++;
                    cell = row.createCell(cellNum);
                }
            }
        } else if (value instanceof Double[]) {
            Double[] douArr = (Double[]) value;
            for (int j = 0; j < douArr.length; j++) {
                Double val = douArr[j];
                // 值不为空则set Value
                if (val != null) {
                    cell.setCellValue(val);
                }

                if (j != douArr.length - 1) {
                    cellNum++;
                    cell = row.createCell(cellNum);
                }
            }
        } else {
            // 其它数据类型都当作字符串简单处理
            String empty = "";
            if(field != null) {
                ExcelCell anno = field.getAnnotation(ExcelCell.class);
                if (anno != null) {
                    empty = anno.defaultValue();
                }
            }
            textValue = value == null ? empty : value.toString();
        }
        if (textValue != null) {
            HSSFRichTextString richString = new HSSFRichTextString(textValue);
            cell.setCellValue(richString);
        }
        return cellNum;
    }

    /**
     * 把Excel的数据封装成voList
     *
     * @param clazz       vo的Class
     * @param inputStream excel输入流
     * @param pattern     如果有时间数据，设定输入格式。默认为"yyy-MM-dd"
     * @param logs        错误log集合
     * @param arrayCount  如果vo中有数组类型,那就按照index顺序,把数组应该有几个值写上.
     * @return voList
     * @throws RuntimeException
     */
    public static <T> Collection<T> importExcel(Class<T> clazz, InputStream inputStream,
                                                String pattern, ExcelLogs logs, Integer... arrayCount) {
        Workbook workBook;
        try {
            workBook = WorkbookFactory.create(inputStream);
        } catch (Exception e) {
            logger.error("load excel file error",e);
            return null;
        }
        List<T> list = new ArrayList<>();
        Sheet sheet = workBook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        try {
            List<ExcelLog> logList = new ArrayList<>();
            // Map<title,index>
            Map<String, Integer> titleMap = new HashMap<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) {
                    if (clazz == Map.class) {
                        // 解析map用的key,就是excel标题行
                        Iterator<Cell> cellIterator = row.cellIterator();
                        Integer index = 0;
                        while (cellIterator.hasNext()) {
                            String value = cellIterator.next().getStringCellValue();
                            titleMap.put(value, index);
                            index++;
                        }
                    }
                    continue;
                }
                // 整行都空，就跳过
                boolean allRowIsNull = true;
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Object cellValue = getCellValue(cellIterator.next());
                    if (cellValue != null) {
                        allRowIsNull = false;
                        break;
                    }
                }
                if (allRowIsNull) {
                    logger.warn("Excel row " + row.getRowNum() + " all row value is null!");
                    continue;
                }
                StringBuilder log = new StringBuilder();
                if (clazz == Map.class) {
                    Map<String, Object> map = new HashMap<>();
                    for (String k : titleMap.keySet()) {
                        Integer index = titleMap.get(k);
                        Cell cell = row.getCell(index);
                        // 判空
                        if (cell == null) {
                            map.put(k, null);
                        } else {
                            cell.setCellType(CellType.STRING);
                            String value = cell.getStringCellValue();
                            map.put(k, value);
                        }
                    }
                    list.add((T) map);

                } else {
                    T t = clazz.newInstance();
                    int arrayIndex = 0;// 标识当前第几个数组了
                    int cellIndex = 0;// 标识当前读到这一行的第几个cell了
                    List<FieldForSortting> fields = sortFieldByAnno(clazz);
                    for (FieldForSortting ffs : fields) {
                        Field field = ffs.getField();
                        field.setAccessible(true);
                        if (field.getType().isArray()) {
                            Integer count = arrayCount[arrayIndex];
                            Object[] value;
                            if (field.getType().equals(String[].class)) {
                                value = new String[count];
                            } else {
                                // 目前只支持String[]和Double[]
                                value = new Double[count];
                            }
                            for (int i = 0; i < count; i++) {
                                Cell cell = row.getCell(cellIndex);
                                String errMsg = validateCell(cell, field, cellIndex);
                                if (StringHelper.isBlank(errMsg)) {
                                    value[i] = getCellValue(cell);
                                } else {
                                    log.append(errMsg);
                                    log.append(";");
                                    logs.setHasError(true);
                                }
                                cellIndex++;
                            }
                            field.set(t, value);
                            arrayIndex++;
                        } else {
                            Cell cell = row.getCell(cellIndex);
                            String errMsg = validateCell(cell, field, cellIndex);
                            if (StringHelper.isBlank(errMsg)) {
                                Object value = null;
                                // 处理特殊情况,Excel中的String,转换成Bean的Date
                                if (field.getType().equals(Date.class)
                                        && cell.getCellTypeEnum() == CellType.STRING) {
                                    Object strDate = getCellValue(cell);
                                    try {
                                        value = new SimpleDateFormat(pattern).parse(strDate.toString());
                                    } catch (ParseException e) {

                                        errMsg =
                                                MessageFormat.format("the cell [{0}] can not be converted to a date ",
                                                        CellReference.convertNumToColString(cell.getColumnIndex()));
                                    }
                                } else {
                                    value = getCellValue(cell);
                                    // 处理特殊情况,excel的value为String,且bean中为其他,且defaultValue不为空,那就=defaultValue
                                    ExcelCell annoCell = field.getAnnotation(ExcelCell.class);
                                    if (value instanceof String && !field.getType().equals(String.class)
                                            && StringHelper.isNotBlank(annoCell.defaultValue())) {
                                        value = annoCell.defaultValue();
                                    }
                                }
                                field.set(t, value);
                            }
                            if (StringHelper.isNotBlank(errMsg)) {
                                log.append(errMsg);
                                log.append(";");
                                logs.setHasError(true);
                            }
                            cellIndex++;
                        }
                    }
                    list.add(t);
                    logList.add(new ExcelLog(t, log.toString(), row.getRowNum() + 1));
                }
            }
            logs.setLogList(logList);
        } catch (InstantiationException e) {
            throw new RuntimeException(MessageFormat.format("can not instance class:{0}",
                    clazz.getSimpleName()), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(MessageFormat.format("can not instance class:{0}",
                    clazz.getSimpleName()), e);
        }
        return list;
    }

    /**
     * 验证cell类型是否正确
     *
     * @param cell    cell单元格
     * @param field   栏位
     * @param cellNum 第几个
     * @return
     */
    private static String validateCell(Cell cell, Field field, int cellNum) {
        String columnName = CellReference.convertNumToColString(cellNum);
        String result = null;
        CellType[] cellTypeArr = validateMap.get(field.getType());
        if (cellTypeArr == null) {
            result = MessageFormat.format("Unsupported type [{0}]", field.getType().getSimpleName());
            return result;
        }
        ExcelCell annoCell = field.getAnnotation(ExcelCell.class);
        if (cell == null
                || (cell.getCellTypeEnum() == CellType.STRING && StringHelper.isBlank(cell
                .getStringCellValue()))) {
            if (annoCell != null && annoCell.valid().allowNull() == false) {
                result = MessageFormat.format("the cell [{0}] can not null", columnName);
            }
            ;
        } else if (cell.getCellTypeEnum() == CellType.BLANK && annoCell.valid().allowNull()) {
            return result;
        } else {
            List<CellType> cellTypes = Arrays.asList(cellTypeArr);

            //如果类型不在指定范围内，并且没有默认值
            if (!(cellTypes.contains(cell.getCellTypeEnum()))
                    || StringHelper.isNotBlank(annoCell.defaultValue())
                    && cell.getCellTypeEnum() == CellType.STRING) {
                StringBuilder strType = new StringBuilder();
                for (int i = 0; i < cellTypes.size(); i++) {
                    CellType cellType = cellTypes.get(i);
                    strType.append(getCellTypeByInt(cellType));
                    if (i != cellTypes.size() - 1) {
                        strType.append(",");
                    }
                }
                result =
                        MessageFormat.format("the cell [{0}] type must [{1}]", columnName, strType.toString());
            } else {
                // 类型符合验证,但值不在要求范围内的
                // String in
                if (annoCell.valid().in().length != 0 && cell.getCellTypeEnum() == CellType.STRING) {
                    String[] in = annoCell.valid().in();
                    String cellValue = cell.getStringCellValue();
                    boolean isIn = false;
                    for (String str : in) {
                        if (str.equals(cellValue)) {
                            isIn = true;
                        }
                    }
                    if (!isIn) {
                        result = MessageFormat.format("the cell [{0}] value must in {1}", columnName, in);
                    }
                }
                // 数字型
                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                    double cellValue = cell.getNumericCellValue();
                    // 小于
                    if (!Double.isNaN(annoCell.valid().lt())) {
                        if (!(cellValue < annoCell.valid().lt())) {
                            result =
                                    MessageFormat.format("the cell [{0}] value must less than [{1}]", columnName,
                                            annoCell.valid().lt());
                        }
                    }
                    // 大于
                    if (!Double.isNaN(annoCell.valid().gt())) {
                        if (!(cellValue > annoCell.valid().gt())) {
                            result =
                                    MessageFormat.format("the cell [{0}] value must greater than [{1}]", columnName,
                                            annoCell.valid().gt());
                        }
                    }
                    // 小于等于
                    if (!Double.isNaN(annoCell.valid().le())) {
                        if (!(cellValue <= annoCell.valid().le())) {
                            result =
                                    MessageFormat.format("the cell [{0}] value must less than or equal [{1}]",
                                            columnName, annoCell.valid().le());
                        }
                    }
                    // 大于等于
                    if (!Double.isNaN(annoCell.valid().ge())) {
                        if (!(cellValue >= annoCell.valid().ge())) {
                            result =
                                    MessageFormat.format("the cell [{0}] value must greater than or equal [{1}]",
                                            columnName, annoCell.valid().ge());
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 根据annotation的seq排序后的栏位
     *
     * @param clazz
     * @return
     */
    private static List<FieldForSortting> sortFieldByAnno(Class<?> clazz) {
        Field[] fieldsArr = clazz.getDeclaredFields();
        List<FieldForSortting> fields = new ArrayList<>();
        List<FieldForSortting> annoNullFields = new ArrayList<>();
        for (Field field : fieldsArr) {
            ExcelCell ec = field.getAnnotation(ExcelCell.class);
            if (ec == null) {
                // 没有ExcelCell Annotation 视为不汇入
                continue;
            }
            int id = ec.index();
            fields.add(new FieldForSortting(field, id));
        }
        fields.addAll(annoNullFields);
        sortByProperties(fields, true, false, "index");
        return fields;
    }

    /**
     * 按属性排序
     * @param list
     * @param isNullHigh
     * @param isReversed
     * @param props
     */
    private static void sortByProperties(List<? extends Object> list, boolean isNullHigh,
                                         boolean isReversed, String... props) {
        if (CollectionUtils.isNotEmpty(list)) {
            Comparator<?> typeComp = ComparableComparator.getInstance();
            if (isNullHigh == true) {
                typeComp = ComparatorUtils.nullHighComparator(typeComp);
            } else {
                typeComp = ComparatorUtils.nullLowComparator(typeComp);
            }
            if (isReversed) {
                typeComp = ComparatorUtils.reversedComparator(typeComp);
            }

            List<Object> sortCols = new ArrayList<Object>();

            if (props != null) {
                for (String prop : props) {
                    sortCols.add(new BeanComparator(prop, typeComp));
                }
            }
            if (sortCols.size() > 0) {
                Comparator<Object> sortChain = new ComparatorChain(sortCols);
                Collections.sort(list, sortChain);
            }
        }
    }

    /**
     * 还原设定（其实是重新new一个新的对象并返回）
     * @return
     */
    public ExcelHelper RestoreSettings(){
        ExcelHelper instance = new  ExcelHelper(this.excelPath);
        return instance;
    }

    /**
     * 自动根据文件扩展名，调用对应的读取方法
     *
     * @throws IOException
     */
    public List<Row> readExcel() throws IOException{
        return readExcel(this.excelPath);
    }

    /**
     * 自动根据文件扩展名，调用对应的读取方法
     *
     * @param xlsPath
     * @throws IOException
     */
    public List<Row> readExcel(String xlsPath) throws IOException{

        //扩展名为空时，
        if (xlsPath.equals("")){
            throw new IOException("文件路径不能为空！");
        }else{
            File file = new File(xlsPath);
            if(!file.exists()){
                throw new IOException("文件不存在！");
            }
        }

        //获取扩展名
        String ext = xlsPath.substring(xlsPath.lastIndexOf(".")+1);

        try {

            if("xls".equals(ext)){				//使用xls方式读取
                return readExcel_xls(xlsPath);
            }else if("xlsx".equals(ext)){		//使用xlsx方式读取
                return readExcel_xlsx(xlsPath);
            }else{									//依次尝试xls、xlsx方式读取
                out("您要操作的文件没有扩展名，正在尝试以xls方式读取...");
                try{
                    return readExcel_xls(xlsPath);
                } catch (IOException e1) {
                    out("尝试以xls方式读取，结果失败！，正在尝试以xlsx方式读取...");
                    try{
                        return readExcel_xlsx(xlsPath);
                    } catch (IOException e2) {
                        out("尝试以xls方式读取，结果失败！\n请您确保您的文件是Excel文件，并且无损，然后再试。");
                        throw e2;
                    }
                }
            }
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 自动根据文件扩展名，调用对应的写入方法
     * @param rowList
     * @throws IOException
     */
    public void writeExcel(List<Row> rowList) throws IOException{
        writeExcel(rowList,excelPath);
    }

    /**
     * 自动根据文件扩展名，调用对应的写入方法
     *
     * @param rowList
     * @param xlsPath
     * @throws IOException
     */
    public void writeExcel(List<Row> rowList, String xlsPath) throws IOException {

        //扩展名为空时，
        if (xlsPath.equals("")){
            throw new IOException("文件路径不能为空！");
        }

        //获取扩展名
        String ext = xlsPath.substring(xlsPath.lastIndexOf(".")+1);

        try {

            if("xls".equals(ext)){				//使用xls方式写入
                writeExcel_xls(rowList,xlsPath);
            }else if("xlsx".equals(ext)){		//使用xlsx方式写入
                writeExcel_xlsx(rowList,xlsPath);
            }else{									//依次尝试xls、xlsx方式写入
                out("您要操作的文件没有扩展名，正在尝试以xls方式写入...");
                try{
                    writeExcel_xls(rowList,xlsPath);
                } catch (IOException e1) {
                    out("尝试以xls方式写入，结果失败！，正在尝试以xlsx方式读取...");
                    try{
                        writeExcel_xlsx(rowList,xlsPath);
                    } catch (IOException e2) {
                        out("尝试以xls方式写入，结果失败！\n请您确保您的文件是Excel文件，并且无损，然后再试。");
                        throw e2;
                    }
                }
            }
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 修改Excel（97-03版，xls格式）
     * @param rowList
     * @param dist_xlsPath
     * @throws IOException
     */
    public void writeExcel_xls(List<Row> rowList, String dist_xlsPath) throws IOException {
        writeExcel_xls(rowList, excelPath,dist_xlsPath);
    }

    /**
     * 修改Excel（97-03版，xls格式）
     *
     * @param rowList
     * @param src_xlsPath
     * @param dist_xlsPath
     * @throws IOException
     */
    public void writeExcel_xls(List<Row> rowList, String src_xlsPath, String dist_xlsPath) throws IOException {

        // 判断文件路径是否为空
        if (dist_xlsPath == null || dist_xlsPath.equals("")) {
            out("文件路径不能为空");
            throw new IOException("文件路径不能为空");
        }
        // 判断文件路径是否为空
        if (src_xlsPath == null || src_xlsPath.equals("")) {
            out("文件路径不能为空");
            throw new IOException("文件路径不能为空");
        }

        // 判断列表是否有数据，如果没有数据，则返回
        if (rowList == null || rowList.size() == 0) {
            out("文档为空");
            return;
        }

        try {
            HSSFWorkbook wb = null;

            // 判断文件是否存在
            File file = new File(dist_xlsPath);
            if (file.exists()) {
                // 如果复写，则删除后
                if (isOverWrite) {
                    file.delete();
                    // 如果文件不存在，则创建一个新的Excel
                    // wb = new HSSFWorkbook();
                    // wb.createSheet("Sheet1");
                    wb = new HSSFWorkbook(new FileInputStream(src_xlsPath));
                } else {
                    // 如果文件存在，则读取Excel
                    wb = new HSSFWorkbook(new FileInputStream(file));
                }
            } else {
                // 如果文件不存在，则创建一个新的Excel
                // wb = new HSSFWorkbook();
                // wb.createSheet("Sheet1");
                wb = new HSSFWorkbook(new FileInputStream(src_xlsPath));
            }

            // 将rowlist的内容写到Excel中
            writeExcel(wb, rowList, dist_xlsPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改Excel（97-03版，xls格式）
     *
     * @param rowList
     * @param dist_xlsPath
     * @throws IOException
     */
    public void writeExcel_xlsx(List<Row> rowList, String dist_xlsPath) throws IOException {
        writeExcel_xls(rowList, excelPath , dist_xlsPath);
    }

    /**
     * 修改Excel（2007版，xlsx格式）
     *
     * @throws IOException
     */
    public void writeExcel_xlsx(List<Row> rowList, String src_xlsPath, String dist_xlsPath) throws IOException {

        // 判断文件路径是否为空
        if (dist_xlsPath == null || dist_xlsPath.equals("")) {
            out("文件路径不能为空");
            throw new IOException("文件路径不能为空");
        }
        // 判断文件路径是否为空
        if (src_xlsPath == null || src_xlsPath.equals("")) {
            out("文件路径不能为空");
            throw new IOException("文件路径不能为空");
        }

        // 判断列表是否有数据，如果没有数据，则返回
        if (rowList == null || rowList.size() == 0) {
            out("文档为空");
            return;
        }

        try {
            // 读取文档
            XSSFWorkbook wb = null;

            // 判断文件是否存在
            File file = new File(dist_xlsPath);
            if (file.exists()) {
                // 如果复写，则删除后
                if (isOverWrite) {
                    file.delete();
                    // 如果文件不存在，则创建一个新的Excel
                    // wb = new XSSFWorkbook();
                    // wb.createSheet("Sheet1");
                    wb = new XSSFWorkbook(new FileInputStream(src_xlsPath));
                } else {
                    // 如果文件存在，则读取Excel
                    wb = new XSSFWorkbook(new FileInputStream(file));
                }
            } else {
                // 如果文件不存在，则创建一个新的Excel
                // wb = new XSSFWorkbook();
                // wb.createSheet("Sheet1");
                wb = new XSSFWorkbook(new FileInputStream(src_xlsPath));
            }
            // 将rowlist的内容添加到Excel中
            writeExcel(wb, rowList, dist_xlsPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Excel 2007版，xlsx格式
     *
     * @return
     * @throws IOException
     */
    public List<Row> readExcel_xlsx() throws IOException {
        return readExcel_xlsx(excelPath);
    }

    /**
     * 读取Excel 2007版，xlsx格式
     * @return
     * @throws Exception
     */
    public List<Row> readExcel_xlsx(String xlsPath) throws IOException {
        // 判断文件是否存在
        File file = new File(xlsPath);
        if (!file.exists()) {
            throw new IOException("文件名为" + file.getName() + "Excel文件不存在！");
        }

        XSSFWorkbook wb = null;
        List<Row> rowList = new ArrayList<Row>();
        try {
            FileInputStream fis = new FileInputStream(file);
            // 去读Excel
            wb = new XSSFWorkbook(fis);

            // 读取Excel 2007版，xlsx格式
            rowList = readExcel(wb);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rowList;
    }

    /***
     * 读取Excel(97-03版，xls格式)
     *
     * @throws IOException
     */
    public List<Row> readExcel_xls() throws IOException {
        return readExcel_xls(excelPath);
    }

    /***
     * 读取Excel(97-03版，xls格式)
     *
     * @throws Exception
     */
    public List<Row> readExcel_xls(String xlsPath) throws IOException {

        // 判断文件是否存在
        File file = new File(xlsPath);
        if (!file.exists()) {
            throw new IOException("文件名为" + file.getName() + "Excel文件不存在！");
        }
        HSSFWorkbook wb = null;// 用于Workbook级的操作，创建、删除Excel
        List<Row> rowList = new ArrayList<Row>();

        try {
            // 读取Excel
            wb = new HSSFWorkbook(new FileInputStream(file));

            // 读取Excel 97-03版，xls格式
            rowList = readExcel(wb);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rowList;
    }

    /**
     * 获取单元格的内容
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        Object result = "";
        if (cell == null
                || (cell.getCellType() == CellType.STRING && StringHelper.isBlank(cell
                .getStringCellValue()))) {
            return null;
        }
        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING:
                    result = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        result = cell.getDateCellValue();
                    } else {
                        result = cell.getNumericCellValue();
                    }
                    break;
                case BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case FORMULA:
                    result = cell.getCellFormula();
                    try {
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            result = cell.getDateCellValue();
                        } else {
                            result = cell.getNumericCellValue();
                        }
                    } catch (IllegalStateException e) {
                        result = cell.getRichStringCellValue();
                    }
                    break;
                case ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case BLANK:
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }

    /**
     * 通用读取Excel
     * @param wb
     * @return
     */
    private List<Row> readExcel(Workbook wb) {
        List<Row> rowList = new ArrayList<Row>();

        int sheetCount = 1;//需要操作的sheet数量

        Sheet sheet = null;
        if(onlyReadOneSheet){	//只操作一个sheet
            // 获取设定操作的sheet(如果设定了名称，按名称查，否则按索引值查)
            sheet =selectedSheetName.equals("")? wb.getSheetAt(selectedSheetIdx):wb.getSheet(selectedSheetName);
        }else{							//操作多个sheet
            sheetCount = wb.getNumberOfSheets();//获取可以操作的总数量
        }

        // 获取sheet数目
        for(int t=startSheetIdx; t<sheetCount+endSheetIdx;t++){
            // 获取设定操作的sheet
            if(!onlyReadOneSheet) {
                sheet =wb.getSheetAt(t);
            }

            //获取最后行号
            int lastRowNum = sheet.getLastRowNum();

            if(lastRowNum>0){	//如果>0，表示有数据
                out("\n开始读取名为【"+sheet.getSheetName()+"】的内容：");
            }

            Row row = null;
            // 循环读取
            for (int i = startReadPos; i <= lastRowNum + endReadPos; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    rowList.add(row);
                    out("第"+(i+1)+"行：",false);
                    // 获取每一单元格的值
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        String value = getCellValue(row.getCell(j));
                        if (!value.equals("")) {
                            out(value + " | ",false);
                        }
                    }
                    out("");
                }
            }
        }
        return rowList;
    }

    /**
     * 修改Excel，并另存为
     * @param wb
     * @param rowList
     * @param xlsPath
     */
    private void writeExcel(Workbook wb, List<Row> rowList, String xlsPath) {

        if (wb == null) {
            out("操作文档不能为空！");
            return;
        }

        Sheet sheet = wb.getSheetAt(0);// 修改第一个sheet中的值

        // 如果每次重写，那么则从开始读取的位置写，否则果获取源文件最新的行。
        int lastRowNum = isOverWrite ? startReadPos : sheet.getLastRowNum() + 1;
        int t = 0;//记录最新添加的行数
        out("要添加的数据总条数为："+rowList.size());
        for (Row row : rowList) {
            if (row == null) continue;
            // 判断是否已经存在该数据
            int pos = findInExcel(sheet, row);

            Row r = null;// 如果数据行已经存在，则获取后重写，否则自动创建新行。
            if (pos >= 0) {
                sheet.removeRow(sheet.getRow(pos));
                r = sheet.createRow(pos);
            } else {
                r = sheet.createRow(lastRowNum + t++);
            }

            //用于设定单元格样式
            CellStyle newstyle = wb.createCellStyle();

            //循环为新行创建单元格
            for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                Cell cell = r.createCell(i);// 获取数据类型
                cell.setCellValue(getCellValue(row.getCell(i)));// 复制单元格的值到新的单元格
                // cell.setCellStyle(row.getCell(i).getCellStyle());//出错
                if (row.getCell(i) == null) continue;
                copyCellStyle(row.getCell(i).getCellStyle(), newstyle); // 获取原来的单元格样式
                cell.setCellStyle(newstyle);// 设置样式
                // sheet.autoSizeColumn(i);//自动跳转列宽度
            }
        }
        out("其中检测到重复条数为:" + (rowList.size() - t) + " ，追加条数为："+t);

        // 统一设定合并单元格
        setMergedRegion(sheet);

        try {
            // 重新将数据写入Excel中
            FileOutputStream outputStream = new FileOutputStream(xlsPath);
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            out("写入Excel时发生错误！ ");
            e.printStackTrace();
        }
    }

    /**
     * 查找某行数据是否在Excel表中存在，返回行数。
     * @param sheet
     * @param row
     * @return
     */
    private int findInExcel(Sheet sheet, Row row) {
        int pos = -1;

        try {
            // 如果覆写目标文件，或者不需要比较，则直接返回
            if (isOverWrite || !isNeedCompare) {
                return pos;
            }
            for (int i = startReadPos; i <= sheet.getLastRowNum() + endReadPos; i++) {
                Row r = sheet.getRow(i);
                if (r != null && row != null) {
                    String v1 = getCellValue(r.getCell(comparePos));
                    String v2 = getCellValue(row.getCell(comparePos));
                    if (v1.equals(v2)) {
                        pos = i;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pos;
    }

    /**
     * 复制一个单元格样式到目的单元格样式
     * @param fromStyle
     * @param toStyle
     */
    public static void copyCellStyle(CellStyle fromStyle, CellStyle toStyle) {
        toStyle.setAlignment(fromStyle.getAlignment());
        // 边框和边框颜色
        toStyle.setBorderBottom(fromStyle.getBorderBottom());
        toStyle.setBorderLeft(fromStyle.getBorderLeft());
        toStyle.setBorderRight(fromStyle.getBorderRight());
        toStyle.setBorderTop(fromStyle.getBorderTop());
        toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
        toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
        toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
        toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

        // 背景和前景
        toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
        toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

        // 数据格式
        toStyle.setDataFormat(fromStyle.getDataFormat());
        toStyle.setFillPattern(fromStyle.getFillPattern());
        // toStyle.setFont(fromStyle.getFont(null));
        toStyle.setHidden(fromStyle.getHidden());
        toStyle.setIndention(fromStyle.getIndention());// 首行缩进
        toStyle.setLocked(fromStyle.getLocked());
        toStyle.setRotation(fromStyle.getRotation());// 旋转
        toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
        toStyle.setWrapText(fromStyle.getWrapText());

    }

    /**
     * 获取合并单元格的值
     *
     * @param sheet
     * @return
     */
    public void setMergedRegion(Sheet sheet) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            // 获取合并单元格位置
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstRow = ca.getFirstRow();
            if (startReadPos - 1 > firstRow) {// 如果第一个合并单元格格式在正式数据的上面，则跳过。
                continue;
            }
            int lastRow = ca.getLastRow();
            int mergeRows = lastRow - firstRow;// 合并的行数
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            // 根据合并的单元格位置和大小，调整所有的数据行格式，
            for (int j = lastRow + 1; j <= sheet.getLastRowNum(); j++) {
                // 设定合并单元格
                sheet.addMergedRegion(new CellRangeAddress(j, j + mergeRows, firstColumn, lastColumn));
                j = j + mergeRows;// 跳过已合并的行
            }

        }
    }


    /**
     * 打印消息，
     * @param msg 消息内容
     */
    private void out(String msg){
        if(printMsg){
            out(msg,true);
        }
    }
    /**
     * 打印消息，
     * @param msg 消息内容
     * @param tr 换行
     */
    private void out(String msg,boolean tr){
        if(printMsg){
            System.out.print(msg+(tr?"\n":""));
        }
    }

    public String getExcelPath() {
        return this.excelPath;
    }

    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath;
    }

    public boolean isNeedCompare() {
        return isNeedCompare;
    }

    public void setNeedCompare(boolean isNeedCompare) {
        this.isNeedCompare = isNeedCompare;
    }

    public int getComparePos() {
        return comparePos;
    }

    public void setComparePos(int comparePos) {
        this.comparePos = comparePos;
    }

    public int getStartReadPos() {
        return startReadPos;
    }

    public void setStartReadPos(int startReadPos) {
        this.startReadPos = startReadPos;
    }

    public int getEndReadPos() {
        return endReadPos;
    }

    public void setEndReadPos(int endReadPos) {
        this.endReadPos = endReadPos;
    }

    public boolean isOverWrite() {
        return isOverWrite;
    }

    public void setOverWrite(boolean isOverWrite) {
        this.isOverWrite = isOverWrite;
    }

    public boolean isOnlyReadOneSheet() {
        return onlyReadOneSheet;
    }

    public void setOnlyReadOneSheet(boolean onlyReadOneSheet) {
        this.onlyReadOneSheet = onlyReadOneSheet;
    }

    public int getSelectedSheetIdx() {
        return selectedSheetIdx;
    }

    public void setSelectedSheetIdx(int selectedSheetIdx) {
        this.selectedSheetIdx = selectedSheetIdx;
    }

    public String getSelectedSheetName() {
        return selectedSheetName;
    }

    public void setSelectedSheetName(String selectedSheetName) {
        this.selectedSheetName = selectedSheetName;
    }

    public int getStartSheetIdx() {
        return startSheetIdx;
    }

    public void setStartSheetIdx(int startSheetIdx) {
        this.startSheetIdx = startSheetIdx;
    }

    public int getEndSheetIdx() {
        return endSheetIdx;
    }

    public void setEndSheetIdx(int endSheetIdx) {
        this.endSheetIdx = endSheetIdx;
    }

    public boolean isPrintMsg() {
        return printMsg;
    }

    public void setPrintMsg(boolean printMsg) {
        this.printMsg = printMsg;
    }

    /**
     * 导出csv
     */
    public static void ExportCSV(String filePath, List<Object[]> rows) throws Exception {
        if (rows.size() > 100000) {
            logger.error("csv写入超过十万行");
            return;
        }
        FileWriter writer = null;
        CSVPrinter printer = null;
        try {
            writer = new FileWriter(filePath);
            printer = CSVFormat.EXCEL.print(writer);
            for (Object[] cell : rows) {
                //每一行写入
                printer.printRecord(cell);
            }
        } finally {
            //关流
            if (printer != null) {
                printer.flush();
                printer.close();
            }
            if (writer != null) {
                writer.close();
            }

        }
    }

    /**
     * 大文件导入Excel
     * 需要自行实现回调
     */
    public static void ProcessExcel(InputStream inputStream, ExcelAnalysisCallBack callBack) {
        OPCPackage p = null;
        try {
            p = OPCPackage.open(inputStream);
            ExcelAnalysis excelAnalysis = new ExcelAnalysis(p,callBack);
            excelAnalysis.process();
        } catch (Exception e) {
            logger.error("ExcelHelper.ProcessExcel异常 ", e);
        } finally {
            if (p != null) {
                try {
                    p.close();
                } catch (Exception e) {
                    logger.error("关闭异常", e);
                }
            }
        }
    }

}
