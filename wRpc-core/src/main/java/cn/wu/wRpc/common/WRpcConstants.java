package cn.wu.wRpc.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WRpcConstants {

    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");
    public static final Pattern COMMA_COLON_PATTERN = Pattern.compile("\\s*[:]+\\s*");
    public static final String PROTOCOL_INJVM = "injvm";
    public static final String PROTOCOL_WRPC = "wrpc";
    public static final int PROTOCOL_WRPC_PORT = 8088;

    private static String driverName =
            "org.apache.hive.jdbc.HiveDriver";

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
