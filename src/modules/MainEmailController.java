/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import GekoStudent.BDGeko;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * FXML Controller class
 *
 * @author ruder palencia
 */
public class MainEmailController implements Initializable {

    private BDGeko con;
    private Connection conn;
    @FXML
    private JFXComboBox<String> comboCurso;
    @FXML
    private FontAwesomeIconView closemail;

    @FXML
    private FontAwesomeIconView buscar;
    @FXML
    private Label labeldatos;
    @FXML
    private JFXButton enviarAll;
    @FXML
    private Label labelmensaje;
    @FXML
    private JFXComboBox<String> txtaño;
    @FXML
    private JFXComboBox<String> txtmes;
    @FXML
    private JFXComboBox<String> diploma;

    private String part1 = "";
    private int datos = 0;
    private String[] correosBD = null;
    private String correos = "";
    private EnviarMail m;
    cargarsplas dialog;
    boolean estado= false;
    Label msj= new Label("  Espere Por favor...");
    private  String id;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = new BDGeko();
        vercursos();
   
        txtaño.getItems().removeAll(txtaño.getItems());
        txtaño.getItems().addAll("2018", "2019", "2020", "2021", "2022");
        txtaño.getSelectionModel().select("");

        txtmes.getItems().removeAll(txtmes.getItems());
        txtmes.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        txtmes.getSelectionModel().select("");
        
        diploma.getItems().removeAll(txtmes.getItems());
        diploma.getItems().addAll("CertificadoDefault", "CertificadoRP");
        diploma.getSelectionModel().select("");
    }

    @FXML
    private void closeStage(MouseEvent event) {
        closemail.getScene().getWindow().hide();
    }

    private void vercursos() {
        try {
            String query = "SELECT code_curso,name_curso FROM cursos";
            conn = con.getConnection();
            try (Statement statement = conn.createStatement();
                    ResultSet rs = statement.executeQuery(query)) {
                while (rs.next()) {
                    comboCurso.getItems().addAll(rs.getString("code_curso") + "-" + rs.getString("name_curso"));
//                String codi= rs.getString("code_curso");

                }
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainEmailController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void consultar(MouseEvent event) throws SQLException {

        String[] parts = comboCurso.getSelectionModel().getSelectedItem().trim().split("-");
        part1 = parts[0]; // parte del codigo
        String part2 = parts[1]; // parte del nombre del curso
        System.out.println("parte uno select "+part1+" parte dos"+ part2 );
        String sql = "SELECT id,correo,COUNT(id) AS datos FROM student WHERE code_curso='" + part1.trim() + "'";
        conn = con.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        rs.next();

        datos = rs.getInt("datos");

        labeldatos.setText("Este es el Total de Estudiantes En Este Curso: " + datos);
    } 

    @FXML
    private void Enviar(ActionEvent event) throws SQLException {
        if ("".equals(comboCurso.getSelectionModel().getSelectedItem()) && "".equals(txtaño.getSelectionModel().getSelectedItem()) && "".equals(txtmes.getSelectionModel().getSelectedItem())) {
            System.out.println("complete todos los campos");

        } else {
             java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
          dialog = new cargarsplas(null, true,msj.getText());
          dialog.setLocationRelativeTo(null);
          dialog.setVisible(true);
          }
       });
             
         Enviardatos();
       
        
        if (estado) {
            dialog.dispose();
        }else{
              System.out.println("error el estado esta falso");
          }
        }
    }
    private void Enviardatos(){
    
                String sql = "SELECT cc,correo FROM student WHERE code_curso=?";
                PreparedStatement pst;
                ResultSet rs;
             try{     
                conn = con.getConnection();
               
                pst = conn.prepareStatement(sql);
                pst.setString(1, part1);
                rs = pst.executeQuery();
               
              // int contador = 1; 

                while (rs.next()) {
                    correos = rs.getString("correo");
                    id = rs.getString("cc");
                    String f = correos + " ";

                    correosBD = f.split(" ");
                    for (String correosBD1 : correosBD) {
    
                        if (correosBD != null) {
 
                             MainEmailAll(id, txtaño.getSelectionModel().getSelectedItem(), 
                                    txtmes.getSelectionModel().getSelectedItem(),
                                    diploma.getSelectionModel().getSelectedItem());
                          
                           

                        } else {
                            //   System.out.println("no hay correos selecionados");
                            labelmensaje.setText("no hay correos selecionados");
                            
                        }
                    }
//                System.out.println("correo: " + correosBD);
                    System.out.println("aqui termina el ciclo");
                    System.out.println(" ");

                }
                }catch(SQLException e){
                    System.out.println("error "+e);
                }
           
            estado=true;
    }
    
 private void MainEmailAll(String txtDI, String txtano, String txtmes,String diploma) {
        m = new EnviarMail();
          ResultSet rs;
          Statement st;
        String query = "SELECT student.cc AS cc,student.name_complet AS nombre,student.correo AS correo,cursos.name_curso AS curso,cursos.horas_curso AS horas FROM student INNER JOIN cursos ON (student.code_curso = cursos.code_curso AND student.fecha_actual = cursos.cursofecha) WHERE student.cc='"+ txtDI.trim() + "'"; 

        String correostudent = "", cc = "", nombre = "",horas="",curso="";
        try {
            conn = con.getConnection();
            if (conn != null) {
                st = conn.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    cc = rs.getString("cc");
                    nombre = rs.getString("nombre");
                    correostudent = rs.getString("correo");
                    curso = rs.getString("curso");
                    horas = rs.getString("horas");
                }
                if ("".equals(correostudent)) {
                    labelmensaje.setText("El correo de este Usuario no Existe");
                } else {
                    if (!correostudent.equals("")) {

                        if (!txtDI.isEmpty()) {
                            if (!"".equals(txtano) && !"".equals(txtmes) && !"".equals(diploma)) {
                                String asunto = "Certificacion de Estudios Cruz Roja Santander";
                                String cuerpomjs = codehtml(nombre);

                                m.enviarConGMail(correostudent, asunto, cuerpomjs, pdf(diploma,nombre,cc,curso,horas).getAbsoluteFile());
                                labelmensaje.setText("Correos Enviados");

                            } else {
                              labelmensaje.setText("Por Favor Rellene el Campo AÑO, MES");
                            }

                        } else {

                         labelmensaje.setText("Por Favor Rellene el Campo ID");
                        }

                    } else {

                    labelmensaje.setText("Este correo no contiene los dominios de Gmail"
                                + " tampoco de cruzrojacolombiana.org");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("error: " + e);
        }

    }

    private File pdf(String diploma,String nombre,String cc,String curso,String horas) {
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        con = new BDGeko();
        File ruta = null;

        String url;
        String fecha = txtaño.getSelectionModel().getSelectedItem() + " " + txtmes.getSelectionModel().getSelectedItem();
        try {
            conn = con.getConnection();
             
            if (conn != null) {
           
    
                //se carga el reporte
                URL in = this.getClass().getResource("/Certificaciones/"+diploma+".jasper");
                jasperReport = (JasperReport) JRLoader.loadObject(in);
                Map parametro = new HashMap();
                parametro.put("certi", diploma.trim());
                parametro.put("name_complet", nombre);
                parametro.put("cc", cc);
                parametro.put("curso", curso);
                parametro.put("horas", horas);            
                parametro.put("fecha", fecha);
                //se procesa el archivo jasper
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, new JREmptyDataSource());
                //se crea el archivo PDF
                url = "diplomas/"+cc.trim() + ".pdf";
                ruta = new File(url);
                JasperExportManager.exportReportToPdfFile(jasperPrint, ruta.getAbsoluteFile().getPath());
                
                System.out.println("ruta arhivo: " + ruta.getAbsolutePath());

            }else{
                
                    System.out.println("no hay coincidencia en la consulta de estudiantes codigo no es igual o fecha");
                }
            
      
      
   
        
        } catch (JRException ex) {
            System.err.println("Error iReport: " + ex.getMessage());
        }

        return ruta;
    }
    public String codehtml(String nombre){
        String html="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
                                        + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                                        + " \n"
                                        + "<head>\n"
                                        + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n"
                                        + "  <title>Cruz Roja Santander</title>\n"
                                        + " \n"
                                        + "</head>\n"
                                        + "\n"
                                        + "<body yahoo bgcolor=\"#f6f8f1\" style=\"margin: 0; padding: 0; min-width: 100%!important; @media only screen and (max-width: 550px), screen and (max-device-width: 550px) {\n"
                                        + "  body[yahoo] .hide {display: none!important;}\n"
                                        + "  body[yahoo] .buttonwrapper {background-color: transparent!important;}\n"
                                        + "  body[yahoo] .button {padding: 0px!important;}\n"
                                        + "  body[yahoo] .button a {background-color: #e05443; padding: 15px 15px 13px!important;}\n"
                                        + "  body[yahoo] .unsubscribe {display: block; margin-top: 20px; padding: 10px 50px; background: #2f3942; border-radius: 5px; text-decoration: none!important; font-weight: bold;}\n"
                                        + "  } \">\n"
                                        + "<table width=\"100%\" bgcolor=\"#f6f8f1\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n"
                                        + "<tr>\n"
                                        + "  <td>\n"
                                        + "   \n"
                                        + "    <table bgcolor=\"#ffffff\"  align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; max-width: 600px;\">\n"
                                        + "      <tr>\n"
                                        + "        <td bgcolor=\"#FFFFFF\"  style=\"padding: 5px 5px 5px 5px;\">\n"
                                        + "          \n"
                                        + "        </td>\n"
                                        + "      </tr>\n"
                                        + "      <tr>\n"
                                        + "        <td  style=\"padding: 30px 30px 30px 30px;border-bottom: 1px solid #f2eeed; \">\n"
                                        + "          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                                        + "            <tr>\n"
                                        + "              <td style=\" color: #153643; font-family: sans-serif; padding: 0 0 15px 0; font-size: 24px; line-height: 28px; font-weight: bold;\">\n"
                                        + "                Bienvenido, Su Certificado de Estudio Esta Listo\n" + "Sr/a " + nombre
                                        + "              </td>\n"
                                        + "            </tr>\n"
                                        + "            <tr>\n"
                                        + "              <td style=\"color: #153643; font-family: sans-serif; font-size: 16px; line-height: 22px;\" >\n"
                                        + "                Es un honor compartir este importante momento de tu vida. La Certificacion es definitivamente una conquista, pero todavía hay muchas cosas que te esperan: "
                                        + "               ¡metas, desafíos y oportunidades! Cualquier cosa es posible cuando usted tiene confianza en sí mismo.\n"
                                        + "              </td>\n"
                                        + "            </tr>\n"
                                        + "          </table>\n"
                                        + "        </td>\n"
                                        + "      </tr>\n"
                                        + "      <tr>\n"
                                        + "        <td style=\"padding: 30px 30px 30px 30px;border-bottom: 1px solid #f2eeed; \">\n"
                                        + "          <table width=\"115\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">  \n"
                                        + "            <tr>\n"
                                        + "              <td height=\"115\" style=\"padding: 0 20px 20px 0;\">\n"
                                        + "                <img style=\"height: auto;\" src=\"https://i.postimg.cc/hP6ZkC8c/fondo-2.png\" width=\"115\" height=\"115\" border=\"0\" alt=\"\" />\n"
                                        + "              </td>\n"
                                        + "            </tr>\n"
                                        + "          </table>\n"
                                        + "     \n"
                                        + "          <table class=\"col380\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%; max-width: 380px;\">  \n"
                                        + "            <tr>\n"
                                        + "              <td>\n"
                                        + "                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                                        + "                  <tr>\n"
                                        + "                    <td style=\"color: #153643; font-family: sans-serif; font-size: 16px; line-height: 22px;\">\n"
                                        + "                       Deseo que este logro no sea un punto de llegada sino de inicio para una carrera brillante. Felicitaciones .\n"
                                        + "                    </td>\n"
                                        + "                  </tr>\n"
                                        + "                  <tr>\n"
                                        + "                    <td style=\"padding: 20px 0 0 0;\">\n"
                                        + "                      <table class=\"buttonwrapper\" bgcolor=\"#e05443\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                                        + "                        <tr>\n"
                                        + "                          <td style=\"text-align: center; font-size: 18px; font-family: sans-serif; font-weight: bold; padding: 0 30px 0 30px; color: #ffffff; text-decoration: none;\" height=\"45\">\n"
                                        + "                            <a href=\"https://iecruzrojasantander.edu.co/\">Visitanos</a>\n"
                                        + "                          </td>\n"
                                        + "                        </tr>\n"
                                        + "                      </table>\n"
                                        + "                    </td>\n"
                                        + "                  </tr>\n"
                                        + "                </table>\n"
                                        + "              </td>\n"
                                        + "            </tr>\n"
                                        + "          </table>\n"
                                        + "  \n"
                                        + "        </td>\n"
                                        + "      </tr>\n"
                                        + "      <tr>\n"
                                        + "        <td style=\"padding: 30px 30px 30px 30px;border-bottom: 1px solid #f2eeed; \">\n"
                                        + "          <img style=\"height: auto;\" src=\"https://i.postimg.cc/XY514wxF/Logo-cruz-roja.png\" width=\"100%\" border=\"0\" alt=\"\" />\n"
                                        + "        </td>\n"
                                        + "      </tr>\n"
                                        + "      <tr>\n"
                                        + "        <td style=\"padding: 30px 30px 30px 30px; color: #153643; font-family: sans-serif; font-size: 16px; line-height: 22px;\">\n"
                                        + "          Este Mensaje se a generado Automatico por el sistema, no responda ni envie informacion importante por seguridad <strong>Muchas Gracias, Felicitaciones.</strong>\n"
                                        + "        </td>\n"
                                        + "      </tr>\n"
                                        + "      <tr>\n"
                                        + "        <td style=\"padding: 20px 30px 15px 30px;\" bgcolor=\"#E74C3C\">\n"
                                        + "          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                                        + "            <tr>\n"
                                        + "              <td align=\"center\" style=\"font-family: sans-serif; font-size: 14px; color: #ffffff; \">\n"
                                        + "                &reg; Bucaramanga, CruzRojaSantander 2021<br/>\n"
                                        + "              </td>\n"
                                        + "            </tr>\n"
                                        + "            <tr>\n"
                                        + "              <td align=\"center\" style=\"padding: 20px 0 0 0; color: #ffffff; text-decoration: underline;\">\n"
                                        + "                <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                                        + "                  <tr>\n"
                                        + "                    <td width=\"37\" style=\"text-align: center; padding: 0 10px 0 10px;\">\n"
                                        + "                      <a href=\"https://es-la.facebook.com/cruzrojasantander/\">\n"
                                        + "                        <img style=\"height: auto;\" src=\"https://i.postimg.cc/pLLkZ3R5/facebook-social-fb-7213.png\" width=\"37\" height=\"37\" alt=\"Facebook\" border=\"0\" />\n"
                                        + "                      </a>\n"
                                        + "                    </td>\n"
                                        + "                   \n"
                                        + "                  </tr>\n"
                                        + "                </table>\n"
                                        + "              </td>\n"
                                        + "            </tr>\n"
                                        + "          </table>\n"
                                        + "        </td>\n"
                                        + "      </tr>\n"
                                        + "    </table>\n"
                                        + "\n"
                                        + "    </td>\n"
                                        + "  </tr>\n"
                                        + "</table>\n"
                                        + "\n"
                                        + "</body>\n"
                                        + "</html>";
        return html;
    }

}
