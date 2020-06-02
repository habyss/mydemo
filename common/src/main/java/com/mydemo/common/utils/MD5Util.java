package com.mydemo.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * The type Md 5 util.
 *
 * @author kun.han
 */
public class MD5Util {
    private final static String[] STR_DIGITS = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    /**
     * Instantiates a new Md 5 util.
     */
    public MD5Util() {
    }

    /**
     * @param bByte return Hexadecimal
     * @return string
     */

    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return STR_DIGITS[iD1] + STR_DIGITS[iD2];
    }


    /**
     * @param bByte 转换字节数组为16进制字串
     * @return string
     */
    private static String byteToString(byte[] bByte) {
        StringBuilder sBuffer = new StringBuilder();
        for (byte b : bByte) {
            sBuffer.append(byteToArrayString(b));
        }
        return sBuffer.toString().toUpperCase();
    }


    /**
     * @param bByte 转换字节数组为16进制字串
     * @return string
     */
    private static String byteToLowerString(byte[] bByte) {
        StringBuilder sBuffer = new StringBuilder();
        for (byte b : bByte) {
            sBuffer.append(byteToArrayString(b));
        }
        return sBuffer.toString();
    }

    /**
     * Gets md 5 str.
     *
     * @param strObj the str obj
     * @return the md 5 str
     */
    public static String getMd5Str(String strObj) {
        String resultString = null;
        try {
            resultString = strObj;
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToLowerString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    /**
     * Get md 5 code string.
     *
     * @param strObj the str obj
     * @return the string
     */
    public static String getMd5Code(String strObj) {
        String resultString = null;
        try {
            resultString = strObj;
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
}
