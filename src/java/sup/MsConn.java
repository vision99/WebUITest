
package sup;

import java.sql.*;
import java.util.Enumeration;

public class MsConn {
    static public void showDrivers(){
        Enumeration<Driver> en = DriverManager.getDrivers();
        Driver dr = null;
        while(en.hasMoreElements()){
            dr = en.nextElement();
            System.out.println(dr.toString());
        }
    }
}
