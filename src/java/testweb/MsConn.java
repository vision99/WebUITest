
package testweb;


import java.sql.*;
import java.util.Enumeration;

public class MsConn {
    static public void showDrivers(){
        Enumeration<Driver> en = DriverManager.getDrivers();
        Driver dr = null;
//        System.out.println(en);
        while(en.hasMoreElements()){
            dr = en.nextElement();
            System.out.println(dr.toString());
        }
    }
    static Connection getConn(String host) throws ClassNotFoundException, SQLException{
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://"+host+";databaseName=PxProcess";// 
        Connection con = DriverManager.getConnection(url,"login","pass");
        return con ;
//89.175.165.157        ;databaseName=STRATEGY2_PROPHIX_FactDb
    }
}
