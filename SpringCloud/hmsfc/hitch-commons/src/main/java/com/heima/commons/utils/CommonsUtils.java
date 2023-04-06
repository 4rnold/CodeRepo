package com.heima.commons.utils;


import com.heima.commons.domin.po.PO;
import com.heima.commons.domin.vo.VO;
import com.heima.commons.utils.reflect.ReflectUtils;
import com.heima.commons.valuation.BasicValuation;
import com.heima.commons.valuation.FuelCostValuation;
import com.heima.commons.valuation.StartPriceValuation;
import com.heima.commons.valuation.Valuation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.*;

public class CommonsUtils {

    private static final Valuation valuation = new FuelCostValuation(new StartPriceValuation(new BasicValuation(null)));

    private static final NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());

    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(8, 7);

    private static final Random random = new Random();

    static {
        numberFormat.setMaximumFractionDigits(1);
        numberFormat.setGroupingUsed(false);
    }

    public static String getHeaderValues(Map<String, List<String>> headerMap, String key) {
        if (null != headerMap && !headerMap.isEmpty() && StringUtils.isNotEmpty(key)) {
            List<String> headerValues = headerMap.get(key);
            return LocalCollectionUtils.toString(headerValues);
        }
        return null;
    }

    public static String getWorkerID() {
        return String.valueOf(idWorker.nextId());
    }

    /**
     * md5 进行加密
     *
     * @param orginStr
     * @return
     */
    public static String encodeMD5(String orginStr) {
        return DigestUtils.md5Hex(orginStr);
    }


    /**
     * 计算文件签名
     *
     * @param file
     * @return
     */
    public static String fileSignature(byte[] file) {
        return DigestUtils.md5Hex(file);
    }


    public static String floatToStr(float ft) {
        return numberFormat.format(ft);
    }

    public static VO toVO(PO po) {
        VO vo = ReflectUtils.newInstance(po.getVO());
        BeanUtils.copyProperties(po, vo);
        return vo;
    }


    public static List<VO> toVO(List<PO> poList) {
        List<VO> voList = null;
        if (null != poList && !poList.isEmpty()) {
            voList = new ArrayList<>();
            for (PO po : poList) {
                voList.add(toVO(po));
            }
        }
        return voList;
    }

    public static <T> T toPO(VO vo) {
        PO po = ReflectUtils.newInstance(vo.getPO());
        BeanUtils.copyProperties(vo, po);
        return (T) po;
    }

    public static <T> List<T> toPO(List<VO> voList) {
        List<T> poList = null;
        if (null != voList && !voList.isEmpty()) {
            voList = new ArrayList<>();
            for (VO vo : voList) {
                poList.add(toPO(vo));
            }
        }
        return poList;
    }

    /**
     * 计算价格
     *
     * @param unitM
     * @return
     */
    public static float valuationPrice(int unitM) {
        float km = unitM / 1000;
        return valuation.calculation(km);
    }

    /**
     * 输入流转换为xml字符串
     *
     * @param inputStream
     * @return
     */
    public static String streamToString(InputStream inputStream) throws
            IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inputStream.close();
        return new String(outSteam.toByteArray());
    }

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }


    /**
     * 延时
     *
     * @param delay
     */
    public static void delay(long delay) {
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
