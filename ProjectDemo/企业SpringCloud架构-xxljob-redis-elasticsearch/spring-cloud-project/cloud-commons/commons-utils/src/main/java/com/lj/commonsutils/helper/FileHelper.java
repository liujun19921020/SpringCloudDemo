package com.lj.commonsutils.helper;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 文件 帮助API
 */
public class FileHelper {

    private static Logger logger = LoggerFactory.getLogger(ExcelHelper.class);

    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 获取文件名称
     *
     * @param pathandname
     * @return
     */
    public static String getFileName(String pathandname) {
        /**
         * 仅保留文件名不保留后缀
         */
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }

    /**
     * 保留文件名及后缀
     *
     * @param pathandname
     * @return
     */
    public static String getFileNameWithSuffix(String pathandname) {
        if (StringUtils.isNotBlank(pathandname)) {
            pathandname = pathandname.replaceAll("\\\\", "/");
        }
        int start = pathandname.lastIndexOf("/");
        if (start != -1) {
            return pathandname.substring(start + 1);
        } else {
            return null;
        }
    }

    /**
     * 获取完整文件名
     *
     * @param file
     * @return
     */
    public static String getFullFileNameByFile(String file) {
        File file1 = new File(file);
        if (file1.exists())
            return file1.getName();
        return getFileNameWithSuffix(file);
    }

    /**
     * 压缩文件
     * @param srcDir
     * @param outPath
     * @param KeepDirStructure
     * @throws RuntimeException
     */
    public static void toZip(String srcDir, String outPath, boolean KeepDirStructure)
            throws RuntimeException {
        if (!outPath.contains(".zip")) {
            logger.error("压缩失败，压缩文件名称定义有误!");
            return;
        }
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(outPath));
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            long end = System.currentTimeMillis();
            logger.info("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from toZip", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    logger.error("ZipOutputStream流释放异常", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    logger.error("FileOutputStream流释放异常", e);
                }
            }
        }
    }


    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }

    /**
     * 本地文件保存
     * @param destination
     * @param input
     * @throws IOException
     */
    public static void writeToLocal(String destination, InputStream input)
            throws IOException {
        FileOutputStream downloadFile = null;
        try {
            int index;
            byte[] bytes = new byte[1024];
            downloadFile = new FileOutputStream(destination);
            while ((index = input.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
        } finally {

        }
        input.close();
        downloadFile.close();

    }

}
