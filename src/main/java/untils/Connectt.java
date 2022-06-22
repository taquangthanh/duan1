package untils;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;

public class Connectt {
    public Connection con() throws SQLServerException {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser("SA");
        ds.setPassword("Hieu230201.");
        ds.setDatabaseName("duan");
        ds.setServerName("hieupro");
        ds.setPortNumber(1433);
        Connection con = ds.getConnection();
        return con;
    }
}
