package com.yigu.commom.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Administrator on 2016/2/17.
 */
public class StringUtil {

    /**
     * 判断字符串是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        int length;

        if ((s == null) || (s.length() == 0) || s.equals("null")) {
            return true;
        }
        length = s.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 数组转换字符串
     *
     * @param list
     * @param ch
     * @return
     */
    public static String arrayToString(String[] list, String ch) {
        StringBuilder sb = new StringBuilder();

        for (String str : list) {
            if (sb.length() != 0)
                sb.append(ch);
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * list转String
     *
     * @param list
     * @param ch
     * @return
     */
    public static String listToString(List<String> list, String ch) {
        StringBuilder sb = new StringBuilder();

        for (String str : list) {
            if (sb.length() != 0)
                sb.append(ch);
            sb.append(str);
        }
        return sb.toString();
    }

//    /**
//     * 列表转String
//     *
//     * @param list
//     * @return
//     */
//    public static String listToUrlString(List<String> list) {
//        StringBuilder sb = new StringBuilder();
//
//        for (String str : list) {
//            if (sb.length() != 0)
//                sb.append("&");
//            sb.append(urlEncode(str));
//        }
//        return sb.toString();
//    }
//
//    /**
//     * URL
//     *
//     * @param strIn
//     * @return
//     */
//    public static String urlEncode(String strIn) {
//        if (isEmpty(strIn)) {
//            return "";
//        }
//        String strOut = null;
//        try {
//            strOut = java.net.URLEncoder.encode(strIn, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return strOut;
//    }


    /**
     * 替换、过滤特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "【").replaceAll("】", "】").replaceAll("！", "!");//替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 价格格式化
     *
     * @param price
     * @return
     */
    public static String formatPrice(Integer price) {
        if (price == null)
            return "0";
        return NumberFormat.getNumberInstance().format(price);
    }

    // 过滤特殊字符
    public static String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 格式化，两个小数点
     * @param price
     * @return
     */
    public static String numberFormat(long price){
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(price);
    }

    /**
     * 格式化，姓名
     * @param name
     * @return
     */
    public static String nameFormat(String name){
        String str = "";
        if(TextUtils.isEmpty(name))
            str="";
        else{
            if(name.length()<=2)
                str = name;
            else
                str = name.substring(name.length()-2,name.length());
        }
        return str;
    }

}
