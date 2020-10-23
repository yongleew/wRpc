package cn.wu.wRpc.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WRpcConstants {

    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");
    public static final Pattern COMMA_COLON_PATTERN = Pattern.compile("\\s*[:]+\\s*");
    public static final String PROTOCOL_INJVM = "injvm";
    public static final String PROTOCOL_WRPC = "wrpc";
    public static final int PROTOCOL_WRPC_PORT = 8088;

    public static void main(String[] args) {
        String s = "a\b";

        Pattern compile = Pattern.compile("\\\\");
        String[] split = compile.split(s);
        for (String s1 : split) {

            System.out.println(s1);
        }
    }
}
