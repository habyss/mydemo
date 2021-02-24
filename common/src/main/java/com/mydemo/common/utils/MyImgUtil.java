package com.mydemo.common.utils;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.mydemo.common.constant.ConstantMsg;
import com.mydemo.common.result.BaseAO;
import com.mydemo.common.result.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @author kun.han on 2021/2/24 9:08
 */
public class MyImgUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyImgUtil.class);

    /**
     * Gets qr code.
     *
     * @param originalUrl 二维码信息
     * @param request     the request
     * @param width       二维码宽
     * @param height      二维码高
     * @param margin      the margin
     * @param imgIconUrl  中间小图标
     * @return the qr code
     */
    public BaseAO getQrCode(String originalUrl, Integer width, Integer height, Integer margin,
                            String imgIconUrl, HttpServletRequest request) {

        Map<String, Object> resultMap = new HashMap<>(1);

        // 二维码
        String path = request.getSession().getServletContext().getRealPath("/") + "tmp/";
        File qrFile = new File(path + File.separator + System.currentTimeMillis() + ".png");
        if (!qrFile.getParentFile().exists()) {
            boolean dirResult = qrFile.getParentFile().mkdirs();
            LOGGER.info("======= qrCode file {}=======", dirResult);
        }

        QrConfig qrConfig = QrConfig.create()
                .setWidth(width)
                .setHeight(height);
        if (StringUtils.hasText(imgIconUrl)) {
            qrConfig.setImg(ImgUtil.read(URLUtil.url(imgIconUrl)));
        }
        if (Objects.nonNull(margin)) {
            qrConfig.setMargin(margin);
        }
        // 二维码生成
        QrCodeUtil.generate(originalUrl, qrConfig, qrFile);

        try {

            // // 二维码上传
            // 阿里oss
            // String imgBucketName = SysConf.CONF.get(ConstantMsg.IMG_BUCKETNAME);
            // String qrTag = OSSUtil.putFile(imgBucketName, qrFile.getName(), qrFile);
            //
            // if (StringUtil.isBlank(qrTag)) {
            //     return JsonResult.failureMap(ConstantMsg.FIND_FAILURE);
            // }
            // String qrUrl = SysConf.CONF.get(ConstantMsg.IMG_DOWNLOAD_URL) + qrFile.getName();
            resultMap.put("qrCode", "qrUrl");

            return JsonResult.successMap(ConstantMsg.SUCCESS_FIND, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boolean delete = qrFile.delete();
            LOGGER.info("======= qrCode/fullFile file {}=======", delete);
        }
        return JsonResult.failureMap(ConstantMsg.FAILURE_FIND);
    }

    /**
     * Gets qr code and pic.
     *
     * @param originalPicUrl the original pic url
     * @param qrCodeUrl      the qr code url
     * @param xPos           x – 修正值。 默认在中间，偏移量相对于中间偏移
     * @param yPos           y – 修正值。 默认在中间，偏移量相对于中间偏移
     * @param request        the request
     * @return the qr code and pic
     */
    @PostMapping("anon/getCombPic")
    public BaseAO getQrCodeAndPic(String originalPicUrl, String qrCodeUrl, Integer xPos, Integer yPos,
                                  HttpServletRequest request) {

        Map<String, Object> resultMap = new HashMap<>(1);

        // 获取原图
        BufferedImage originalImg = ImgUtil.read(URLUtil.url(originalPicUrl));
        // 获取二维码
        BufferedImage qrImg = ImgUtil.read(URLUtil.url(qrCodeUrl));

        // 合成后的图片 占位
        String path = request.getSession().getServletContext().getRealPath("/") + "tmp/";
        File combFile = new File(path + File.separator + System.currentTimeMillis() + ".png");
        if (!combFile.getParentFile().exists()) {
            boolean dirResult = combFile.getParentFile().mkdirs();
            LOGGER.info("======= combFile file {}=======", dirResult);
        }
        // 组合图片
        ImgUtil.pressImage(originalImg, combFile, qrImg, xPos, yPos, 1.0f);

        try {
            // // 组合图上传
            // 阿里oss
            // String imgBucketName = SysConf.CONF.get(ConstantMsg.IMG_BUCKETNAME);
            // String tag = OSSUtil.putFile(imgBucketName, combFile.getName(), combFile);
            //
            // if (StringUtil.isBlank(tag)) {
            //     return JsonResult.failureMap(ConstantMsg.FIND_FAILURE);
            // }
            // String combUrl = SysConf.CONF.get(ConstantMsg.IMG_DOWNLOAD_URL) + combFile.getName();
            resultMap.put("combImgUrl", "combUrl");

            return JsonResult.successMap(ConstantMsg.SUCCESS_FIND, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boolean delete = combFile.delete();
            LOGGER.info("======= qrCode/combFile file {}=======", delete);
        }
        return JsonResult.failureMap(ConstantMsg.FAILURE_FIND);
    }
}
