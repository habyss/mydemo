package com.mydemo.easyexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydemo.common.constant.ConstantMsg;
import com.mydemo.common.result.BaseAO;
import com.mydemo.common.result.JsonResult;
import com.mydemo.easyexcel.entity.DemoData;
import com.mydemo.easyexcel.listener.DemoDataListener;
import com.mydemo.easyexcel.listener.NoModelDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The type Upload controller.
 *
 * @author kun.han on 2020/5/29 9:55
 */
@RestController
@RequestMapping("upload")
public class UploadController {

    /**
     * The Object mapper.
     */
    @Resource
    ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    /**
     * Normal excel no bean base ao.
     * 有返回值
     *
     * @param file the file
     * @return the base ao
     * @throws IOException the io exception
     */
    @PostMapping("normalExcelNoBeanHasBack")
    public BaseAO normalExcelNoBeanHasBack(@RequestParam("file") MultipartFile file) throws IOException {
        // sheetNo 0为第一个sheet -> 依次
        List<Map<Integer, String>> listMap = EasyExcel.read(file.getInputStream()).sheet(0).doReadSync();
        for (Map<Integer, String> data : listMap) {
            // 返回每条数据的键值对 表示所在的列 和所在列的值
            LOGGER.info("读取到数据:{}", objectMapper.writeValueAsString(data));
        }
        System.out.println(listMap.size());
        return JsonResult.successMap(ConstantMsg.SUCCESS_FIND, listMap);
    }

    /**
     * Normal excel has bean base ao.
     * 有返回值
     *
     * @param file the file
     * @return the base ao
     * @throws IOException the io exception
     */
    @PostMapping("normalExcelHasBeanHasBack")
    public BaseAO normalExcelHasBeanHasBack(@RequestParam("file") MultipartFile file) throws IOException {
        // sheetNo 0为第一个sheet -> 依次
        List<DemoData> list = EasyExcel.read(file.getInputStream()).head(DemoData.class).sheet(0).doReadSync();
        for (DemoData data : list) {
            LOGGER.info("读取到数据:{}", objectMapper.writeValueAsString(data));
        }
        System.out.println(list.size());
        return JsonResult.successMap(ConstantMsg.SUCCESS_FIND, list);
    }

    /**
     * Normal excel no bean no back base ao.
     *
     * @param file the file
     * @return the base ao
     * @throws IOException the io exception
     */
    @PostMapping("normalExcelNoBeanNoBack")
    public BaseAO normalExcelNoBeanNoBack(@RequestParam("file") MultipartFile file) throws IOException {
        // sheetNo 0为第一个sheet -> 依次
        EasyExcel.read(file.getInputStream(), new NoModelDataListener()).sheet(0).doRead();
        return JsonResult.successMap(ConstantMsg.SUCCESS_FIND);
    }

    /**
     * Normal excel has bean no back base ao.
     *
     * @param file the file
     * @return the base ao
     * @throws IOException the io exception
     */
    @PostMapping("normalExcelHasBeanNoBack")
    public BaseAO normalExcelHasBeanNoBack(@RequestParam("file") MultipartFile file) throws IOException {
        // sheetNo 0为第一个sheet -> 依次
        EasyExcel.read(file.getInputStream(), DemoData.class, new DemoDataListener(objectMapper)).sheet(0).doRead();
        return JsonResult.successMap(ConstantMsg.SUCCESS_FIND);
    }


    /**
     * 读多个或者全部sheet,这里注意一个sheet不能读取多次，多次读取需要重新读取文件
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoD5ataListener}
     * <p>
     * 3. 直接读即可
     */
    @PostMapping("mutExcelNoBack")
    public BaseAO mutExcelNoBack(@RequestParam("file") MultipartFile file) throws IOException {
        // 读取全部sheet
        // 这里需要注意
        //      DemoDataListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。
        //      然后所有sheet都会往同一个DemoDataListener里面写
        EasyExcel.read(file.getInputStream(), DemoData.class, new DemoDataListener(objectMapper)).doReadAll();
        // 读取部分sheet
        ExcelReader excelReader = EasyExcel.read(file.getInputStream()).build();
        // head 和Listener 自己使用功能必须不同的Listener
        ReadSheet readSheet1 =
                EasyExcel.readSheet(0).registerReadListener(new NoModelDataListener()).build();
        ReadSheet readSheet2 =
                EasyExcel.readSheet(1).head(DemoData.class).registerReadListener(new DemoDataListener(objectMapper)).build();
        // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
        excelReader.read(readSheet1, readSheet2);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();

        return JsonResult.successMap(ConstantMsg.SUCCESS_FIND);
    }

}
