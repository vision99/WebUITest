package testweb;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class CleanCache {

    public static void cleanCapexYear() throws ClassNotFoundException, SQLException {
        try {
            Connection con = MsConn.getConn(System.getProperty("host.name.db"));
            con.createStatement().execute("EXEC [PxProcess].[dbo].[RefreshRolap] @connection = N'STRATEGY2', @cube_name = N'CAPEX_год';"
                    + "EXEC [PxProcess].[dbo].[RefreshRolap] @connection = N'STRATEGY2', @cube_name = N'CAPEX_год_Отчетный';"
                    + "EXEC [PxProcess].[dbo].[RefreshRolap] @connection = N'STRATEGY2', @cube_name = N'CAPEX_месяц';"
                    + "EXEC [PxProcess].[dbo].[RefreshRolap] @connection = N'STRATEGY2', @cube_name = N'Макропараметры';"
                    + "EXEC [PxProcess].[dbo].[RefreshRolap] @connection = N'STRATEGY2', @cube_name = N'Расчеты_по_проектам';"
                    + "EXEC [PxProcess].[dbo].[RefreshRolap] @connection = N'STRATEGY2', @cube_name = N'Расчеты_по_проектам_Отчетный';"
                    + "EXEC [PxProcess].[dbo].[RefreshRolap] @connection = N'STRATEGY2', @cube_name = N'РеалКом';"
                    + "EXEC [PxProcess].[dbo].[RefreshRolap] @connection = N'STRATEGY2', @cube_name = N'РеалКом_Отчетный';"
                    + "EXEC [PxProcess].[dbo].[RefreshRolap] @connection = N'STRATEGY2', @cube_name = N'Регламент проектов';"
                    + "EXEC [PxProcess].[dbo].[RefreshRolap] @connection = N'STRATEGY2', @cube_name = N'Себестоимость';"
                    + "EXEC [PxProcess].[dbo].[RefreshRolap] @connection = N'STRATEGY2', @cube_name = N'Себестоимость_Отчетный';"
                    + "EXEC [PxProcess].[dbo].[RefreshRolap] @connection = N'STRATEGY2', @cube_name = N'Справочники';"
                    + "EXEC [PxProcess].[dbo].[RefreshRolap] @connection = N'STRATEGY_KT', @cube_name = N'KT_1';"
            );
//            CallableStatement proc
//                     = con.prepareCall("{ call RefreshRolap(?, ?) }");
//            proc.setString(1, "STRATEGY2");
//            proc.setString(2, "CAPEX_год");
//            proc.execute();
            System.out.println("Cache очищен");
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException();
        }
    }
}
