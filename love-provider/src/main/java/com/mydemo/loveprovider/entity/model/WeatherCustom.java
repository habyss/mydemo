package com.mydemo.loveprovider.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kun.han on 2019/6/13 14:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherCustom {
    /**
     * date : 2019061311
     * ac_name : 空调开启指数
     * ac_hint : 较少开启
     * ac_des_s : 体感舒适，不需要开启空调。
     * ag_name : 过敏指数
     * ag_hint : 极不易发
     * ag_des_s : 无需担心过敏，可放心外出，享受生活。
     * cl_name : 晨练指数
     * cl_hint : 较不宜
     * cl_des_s : 室外锻炼请携带雨具。
     * co_name : 舒适度指数
     * co_hint : 舒适
     * co_des_s : 白天不冷不热，风力不大。
     * ct_name : 穿衣指数
     * ct_hint : 较舒适
     * ct_des_s : 建议穿薄外套或牛仔裤等服装。
     * dy_name : 钓鱼指数
     * dy_hint : 不宜
     * dy_des_s : 天气不好，有风，不适合垂钓。
     * fs_name : 防晒指数
     * fs_hint : 弱
     * fs_des_s : 涂抹8-12SPF防晒护肤品。
     * gj_name : 逛街指数
     * gj_hint : 较适宜
     * gj_des_s : 有降水，逛街需要带雨具。
     * gl_name : 太阳镜指数
     * gl_hint : 不需要
     * gl_des_s : 白天能见度差不需要佩戴太阳镜
     * gm_name : 感冒指数
     * gm_hint : 少发
     * gm_des_s : 无明显降温，感冒机率较低。
     * hc_name : 划船指数
     * hc_hint : 不适宜
     * hc_des_s : 天气不好，建议选择别的娱乐方式。
     * jt_name : 交通指数
     * jt_hint : 较好
     * jt_des_s : 有降水且路面潮湿，不宜高速行驶。
     * lk_name : 路况指数
     * lk_hint : 潮湿
     * lk_des_s : 有降水，路面潮湿，请小心驾驶。
     * ls_name : 晾晒指数
     * ls_hint : 不宜
     * ls_des_s : 降水可能会淋湿衣物，请选择在室内晾晒。
     * mf_name : 美发指数
     * mf_hint : 适宜
     * mf_des_s : 风力较大容易弄脏头发，注意清洁。
     * nl_name : 夜生活指数
     * nl_hint : 较不适宜
     * nl_des_s : 建议夜生活最好在室内进行。
     * pj_name : 啤酒指数
     * pj_hint : 较适宜
     * pj_des_s : 适量的饮用啤酒，注意不要过量。
     * pk_name : 放风筝指数
     * pk_hint : 不宜
     * pk_des_s : 天气不好，不适宜放风筝。
     * pl_name : 空气污染扩散条件指数
     * pl_hint : 良
     * pl_des_s : 气象条件有利于空气污染物扩散。
     * pp_name : 化妆指数
     * pp_hint : 保湿
     * pp_des_s : 请选用中性保湿型霜类化妆品。
     * tr_name : 旅游指数
     * tr_hint : 适宜
     * tr_des_s : 有降水，温度适宜，不要错过出游机会。
     * uv_name : 紫外线强度指数
     * uv_hint : 最弱
     * uv_des_s : 辐射弱，涂擦SPF8-12防晒护肤品。
     * wc_name : 风寒指数
     * wc_hint : 无
     * wc_des_s : 暂缺
     * xc_name : 洗车指数
     * xc_hint : 不宜
     * xc_des_s : 有雨，雨水和泥水会弄脏爱车。
     * xq_name : 心情指数
     * xq_hint : 较差
     * xq_des_s : 雨水可能会使心绪无端地挂上轻愁。
     * yd_name : 运动指数
     * yd_hint : 较不宜
     * yd_des_s : 有降水，推荐您在室内进行休闲运动。
     * yh_name : 约会指数
     * yh_hint : 较不适宜
     * yh_des_s : 建议尽量不要去室外约会。
     * ys_name : 雨伞指数
     * ys_hint : 带伞
     * ys_des_s : 有降水，带雨伞，短期外出可收起雨伞。
     * zs_name : 中暑指数
     * zs_hint : 无
     * zs_des_s : 气温不高，中暑几率极低。
     */

    private String date;
    private String ac_name;
    private String ac_hint;
    private String ac_des_s;
    private String ag_name;
    private String ag_hint;
    private String ag_des_s;
    private String cl_name;
    private String cl_hint;
    private String cl_des_s;
    private String co_name;
    private String co_hint;
    private String co_des_s;
    private String ct_name;
    private String ct_hint;
    private String ct_des_s;
    private String dy_name;
    private String dy_hint;
    private String dy_des_s;
    private String fs_name;
    private String fs_hint;
    private String fs_des_s;
    private String gj_name;
    private String gj_hint;
    private String gj_des_s;
    private String gl_name;
    private String gl_hint;
    private String gl_des_s;
    private String gm_name;
    private String gm_hint;
    private String gm_des_s;
    private String hc_name;
    private String hc_hint;
    private String hc_des_s;
    private String jt_name;
    private String jt_hint;
    private String jt_des_s;
    private String lk_name;
    private String lk_hint;
    private String lk_des_s;
    private String ls_name;
    private String ls_hint;
    private String ls_des_s;
    private String mf_name;
    private String mf_hint;
    private String mf_des_s;
    private String nl_name;
    private String nl_hint;
    private String nl_des_s;
    private String pj_name;
    private String pj_hint;
    private String pj_des_s;
    private String pk_name;
    private String pk_hint;
    private String pk_des_s;
    private String pl_name;
    private String pl_hint;
    private String pl_des_s;
    private String pp_name;
    private String pp_hint;
    private String pp_des_s;
    private String tr_name;
    private String tr_hint;
    private String tr_des_s;
    private String uv_name;
    private String uv_hint;
    private String uv_des_s;
    private String wc_name;
    private String wc_hint;
    private String wc_des_s;
    private String xc_name;
    private String xc_hint;
    private String xc_des_s;
    private String xq_name;
    private String xq_hint;
    private String xq_des_s;
    private String yd_name;
    private String yd_hint;
    private String yd_des_s;
    private String yh_name;
    private String yh_hint;
    private String yh_des_s;
    private String ys_name;
    private String ys_hint;
    private String ys_des_s;
    private String zs_name;
    private String zs_hint;
    private String zs_des_s;

}
