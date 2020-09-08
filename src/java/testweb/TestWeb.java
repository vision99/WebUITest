package testweb;

import java.sql.SQLException;

public class TestWeb {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        CleanCache.cleanCapexYear();
//        MsConn.showDrivers();
//        try {
//            Connection conn = MsConn.getConn();
//            
//            System.out.println(conn.getMetaData().getUserName());
//            DatabaseMetaData meta = conn.getMetaData();
//            ResultSet res = meta.getProcedures(null, null, null);
//            System.out.println("List of procedures: ");
//            while (res.next()) {
////                if(res.getString("PROCEDURE_NAME").compareToIgnoreCase(str)){
//                System.out.println(/*" " + res.getString("PROCEDURE_CAT") + "," + res.getString("PROCEDURE_SCHEM")
//                        + "," + */res.getString("PROCEDURE_NAME"));
////                }
//            }
//            conn.close();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
    }

}
