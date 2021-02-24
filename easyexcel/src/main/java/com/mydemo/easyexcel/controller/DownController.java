package com.mydemo.easyexcel.controller;

import cn.hutool.core.util.URLUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydemo.common.result.JsonResult;
import com.mydemo.easyexcel.entity.DemoData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * The type Down controller.
 *
 * @author kun.han on 2020/5/29 13:25
 */
@RestController
@RequestMapping("down")
public class DownController {

    /**
     * The Object mapper.
     */
    @Resource
    ObjectMapper objectMapper;
    // String path = request.getSession().getServletContext().getRealPath("/") + "tmp/";
    /**
     * 普通使用bean来进行创建表格
     *
     * @param response the response
     * @throws IOException the io exception
     */
    @GetMapping("normalExcelHasBean")
    public void normalExcelHasBean(HttpServletResponse response) throws IOException {
        setResponse(response);

        EasyExcel.write(response.getOutputStream(), DemoData.class).sheet("模板").doWrite(getData());
    }


    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @param response the response
     * @throws IOException the io exception
     * @since 2.1.1
     */
    @GetMapping("downloadFailedUsingJson")
    public void downloadFailedUsingJson(HttpServletResponse response) throws IOException {
        try {
            setResponse(response);
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), DemoData.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(getData());
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>(2);
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(objectMapper.writeValueAsString(map));
        }
    }

    /**
     * 根据参数只导出指定列
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 根据自己或者排除自己需要的列
     * <p>
     * 3. 直接写即可
     *
     * @param response the response
     * @throws IOException the io exception
     * @since 2.1.1
     */
    @GetMapping("excludeOrIncludeWrite")
    public void excludeOrIncludeWrite(HttpServletResponse response) throws IOException {
        setResponse(response);

        // 根据用户传入字段 假设我们要忽略 date
        Set<String> excludeColumnFiledNames = new HashSet<>();
        excludeColumnFiledNames.add("name");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(response.getOutputStream(), DemoData.class).excludeColumnFiledNames(excludeColumnFiledNames).sheet("模板")
                .doWrite(getData());

        // 根据用户传入字段 假设我们只要导出 date
        Set<String> includeColumnFiledNames = new HashSet<>();
        includeColumnFiledNames.add("date");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(response.getOutputStream(), DemoData.class).includeColumnFiledNames(includeColumnFiledNames).sheet("模板")
                .doWrite(getData());
    }

    /**
     * 重复多次写入
     * <p>
     * 1. 创建excel对应的实体对象 参照
     * <p>
     * 2. 使用 ExcelProperty注解指定复杂的头
     * <p>
     * 3. 直接调用二次写入即可
     *
     * @param response the response
     * @throws IOException the io exception
     */
    @GetMapping("repeatedWrite")
    public void repeatedWrite(HttpServletResponse response) throws IOException {
        setResponse(response);

        // 方法1 如果写到同一个sheet
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), DemoData.class).build();
        // 这里注意 如果同一个sheet只要创建一次
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
        for (int i = 0; i < 5; i++) {
            // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
            List<DemoData> data = getData();
            excelWriter.write(data, writeSheet);
        }
        /// 千万别忘记finish 会帮忙关闭流
        excelWriter.finish();


        // 方法2 如果写到不同的sheet 同一个对象
        // 这里 指定文件
        excelWriter = EasyExcel.write(response.getOutputStream(), DemoData.class).build();
        // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
        for (int i = 0; i < 5; i++) {
            // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
            writeSheet = EasyExcel.writerSheet(i, "模板" + i).build();
            // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
            List<DemoData> data = getData();
            excelWriter.write(data, writeSheet);
        }
        /// 千万别忘记finish 会帮忙关闭流
        excelWriter.finish();



        // 方法3 如果写到不同的sheet 不同的对象
        // 这里 指定文件
        excelWriter = EasyExcel.write(response.getOutputStream()).build();
        // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
        for (int i = 0; i < 5; i++) {
            // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。
            // 这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class 实际上可以一直变
            writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(DemoData.class).build();
            // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
            List<DemoData> data = getData();
            excelWriter.write(data, writeSheet);
        }
        /// 千万别忘记finish 会帮忙关闭流
        excelWriter.finish();
    }


    /**
     * 已有的模板中填充数据.
     *
     * @param response the response
     * @throws IOException the io exception
     */
    public void fillExcel(HttpServletResponse response) throws IOException {
        setResponse(response);

        // 模板注意 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
        // 填充list 的时候还要注意 模板中{.} 多了个点 表示list
        InputStream inStream = getInputStream("http://mytianimg.oss-cn-shanghai.aliyuncs.com/1613980440706.xlsx");
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(inStream).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();

        // 这里注意 入参用了forceNewRow 代表在写入list的时候不管list下面有没有空行 都会创建一行，然后下面的数据往后移动。默认 是false，会直接使用下一行，如果没有则创建。
        // forceNewRow 如果设置了true,有个缺点 就是他会把所有的数据都放到内存了，所以慎用
        // 简单的说 如果你的模板有list,且list不是最后一行，下面还有数据需要填充 就必须设置 forceNewRow=true 但是这个就会把所有数据放到内存 会很耗内存
        // FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();

        // 横向填充
        // FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();


        // excelWriter.fill(data(), fillConfig, writeSheet);
        // excelWriter.fill(data(), fillConfig, writeSheet);

        // 填充
        excelWriter.fill(getData(), writeSheet);
        excelWriter.finish();
    }

    private InputStream getInputStream(String stringUrl) throws IOException {
        URL url = URLUtil.url(stringUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        return conn.getInputStream();
    }

    private void setResponse(HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
    }


    /**
     * Get data list.
     * 示例获取data
     *
     * @return the list
     */
    public List<DemoData> getData(){
        List<DemoData> dataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DemoData test = DemoData.builder().name("test").phone("15224970925").build();
            dataList.add(test);
        }
        return dataList;
    }
}
