package org.fwoxford.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by gengluying on 2017/6/28.
 */
public class DBUtilForTemp {
    /*
         * 打开数据库
         */
    private static String driver;//连接数据库的驱动
    private static String url;
    private static String username;
    private static String password;

    static {
        driver="oracle.jdbc.driver.OracleDriver";//需要的数据库驱动
        url="jdbc:oracle:" + "thin:@10.24.10.56:1521:xe";//数据库名路径
        username="biobank_temp_0825";
        password="root123";
    }
    public static Connection open()
    {
        try {
            Class.forName(driver);
            return (Connection) DriverManager.getConnection(url,username, password);
        } catch (Exception e) {
            System.out.println("数据库连接失败！");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//加载驱动

        return null;
    }

    /*
     * 关闭数据库
     */
    public static void close(Connection conn)
    {
        if(conn!=null)
        {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
