package com.bwongo.commons.models.text;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * @Author bkaaron
 * @Project bega
 * @Date 4/24/23
 **/
public class StringUtil {
    private static final String DEFAULT_SEQUENCE_PREFIX = "00000000000";
    private static final char COLON = ':';
    private static final char PLUS = '+';
    private static final char SINGLE_ZERO = '0';
    private static Logger logger;

    static {
        StringUtil.logger = LogManager.getLogger("Debug_Log");
    }

    public static boolean isEmpty(final String string) {
        return null == string || "".equals(string.trim());
    }


    public static String trimRetNull(final String string) {
        if (null == string) {
            return null;
        }
        return string.trim();
    }

    public static int string2Int(final String intString, final int defaultValue) {
        int intValue;
        try {
            intValue = Integer.parseInt(intString);
        }
        catch (NumberFormatException e) {
            StringUtil.logger.error((Object)("Parse String [" + intString + "] to int failed. Set default value [" + defaultValue + "]"), (Throwable)e);
            intValue = defaultValue;
        }
        return intValue;
    }

    public static double string2Double(final String doubString, final double defaultValue) {
        double doubValue;
        try {
            doubValue = Double.parseDouble(doubString);
        }
        catch (NumberFormatException e) {
            StringUtil.logger.error((Object)("Parse String [" + doubString + "] to double failed. Set default value [" + defaultValue + "]"), (Throwable)e);
            doubValue = defaultValue;
        }
        return doubValue;
    }

    public static String getSequence(final String sequence, final int length) {
        if (isEmpty(sequence)) {
            return "00000000000".substring(0, length - 1) + '1';
        }
        final StringBuilder builder = new StringBuilder(sequence.trim());
        builder.insert(0, "00000000000");
        return builder.substring(builder.length() - length, builder.length());
    }

    public static String getSequenceForDefinedZeros(final String value, final int sequenceLength) {
        var sequence = "";
        if (isEmpty(value)) {

            IntStream.of(sequenceLength).forEach(t -> {
                sequence.concat("0");
            });
            return  sequence;
        }

        var zeroLength = sequenceLength - value.length();
        IntStream.of(zeroLength).forEach(t -> {
            sequence.concat("0");
        });
        return sequence.concat(value);
    }

    public static String trimPhonenumber(String phonenumber) {
        if (isEmpty(phonenumber)) {
            return phonenumber;
        }
        phonenumber = phonenumber.trim();
        final int index = phonenumber.indexOf(58);
        phonenumber = phonenumber.substring(index + 1);
        StringBuilder builder = new StringBuilder(phonenumber);
        for (boolean isPlusChar = builder.charAt(0) == '+'; isPlusChar; isPlusChar = (builder.charAt(0) == '+')) {
            builder = builder.delete(0, 1);
        }
        for (boolean isZeroChar = builder.charAt(0) == '0'; isZeroChar; isZeroChar = (builder.charAt(0) == '0')) {
            builder = builder.delete(0, 1);
        }
        return builder.toString();
    }

    public static String dealNull(final String str) {
        return (null == str) ? "" : str;
    }

    public static String randomString() {
        final String uu = UUID.randomUUID().toString();
        final String[] uus = uu.split("-");
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < uus.length; ++i) {
            builder.append(uus[i]);
        }
        return builder.toString();
    }

    public static String iso88591ToUtf8(final String msg) {
        String message = null;
        try {
            message = new String(msg.getBytes("iso-8859-1"), "UTF-8");
        }
        catch (Exception e) {
            StringUtil.logger.error((Object)"parse string from iso-8859-1 to utf-8 failed.", (Throwable)e);
            message = msg;
        }
        return message;
    }

    public static String convertToString(final List<String> list) {
        if (null == list || list.isEmpty()) {
            return "";
        }
        final StringBuffer stringBuffer = new StringBuffer();
        for (final String value : list) {
            stringBuffer.append(value).append(",");
        }
        if (!list.isEmpty()) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }


    public static boolean checkNull(final Object strSource) {
        boolean bflag = false;
        if (null == strSource) {
            bflag = true;
        }
        return bflag;
    }

    public static boolean checkNullorEmpty(final String strSource) {
        boolean bflag = false;
        if (null == strSource || strSource.trim().length() == 0) {
            bflag = true;
        }
        return bflag;
    }

    @SuppressWarnings({"rawtypes"})
    public static boolean checkNullorEmpty(final List strSource) {
        boolean bflag = false;
        if (null == strSource || strSource.isEmpty()) {
            bflag = true;
        }
        return bflag;
    }

    @SuppressWarnings({"rawtypes"})
    public static boolean checkNullorEmpty(final Map strSource) {
        boolean bflag = false;
        if (null == strSource || strSource.isEmpty()) {
            bflag = true;
        }
        return bflag;
    }

    public static String trim(final String str) {
        String message = str;
        if (!checkNull(message)) {
            message = message.trim();
        }
        return message;
    }

    public boolean isEndCode(final String objSrc, final String code) {
        boolean flag = false;
        if (!checkNull(objSrc) && objSrc.endsWith(code)) {
            flag = true;
        }
        return flag;
    }

    public static String subStr(final String objSrc, final int index) {
        String newStr = objSrc;
        if (!checkNull(newStr) && index < newStr.length()) {
            newStr = newStr.substring(0, index);
        }
        return newStr;
    }

    public static String formatPasswordInAirtimeReq(String request) {
        return request.replaceAll("(\\<authkey>).*.(authkey\\>)","<authkey>***</authkey>");
    }

    public static String getFieldStringValueFromObject(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getField(fieldName);
        return (String)field.get(object);
    }

}
