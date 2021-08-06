/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GekoStudent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author casar
 */
public class validarUser {

    private BDGeko con;
    private Connection conn = null;
    private PreparedStatement pst;
    private ResultSet rs;

    public boolean ValidarUSER(String user, String pass) {
        boolean res = false;
        try {
            con = new BDGeko();
            conn = con.getConnection();
            if (conn != null) {
                String sql = "SELECT * FROM usuarios WHERE BINARY user=? AND BINARY pass=?";

                pst = conn.prepareStatement(sql);
                pst.setString(1, user);
                pst.setString(2, pass);

                rs = pst.executeQuery();
                if (rs.next()) {
                    System.out.println("conectado...");
                    res = true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return res;

    }
}
