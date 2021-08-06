package GekoStudent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BDGeko {

    protected Connection dbconnection;

    public Connection getConnection() {
        String host = "localhost";//192.168.10.179
        String port = "3306";  
        String dbname = "studentbd";
        String user = "root";//admin
        String password = "";//password = "palr612"//  "930926Ruder";
        final String ConnectionString = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        try {
            dbconnection = DriverManager.getConnection(ConnectionString, user, password);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return dbconnection;
    }

}
