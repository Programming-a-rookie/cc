package org.sinosoft.webservice.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CloseConnectUtil {

    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        if (rs != null){
            rs.close();
        }
        if (ps != null){
            ps.close();
        }
        if (conn != null){
            conn.close();
        }
    }
}
