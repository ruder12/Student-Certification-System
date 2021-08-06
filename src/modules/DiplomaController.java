/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import GekoStudent.BDGeko;
import com.jfoenix.controls.JFXComboBox;
import java.io.File;
import java.sql.Statement;
import javafx.scene.paint.Color;
import net.sf.jasperreports.engine.*;

/**
 * FXML Controller class
 *
 * @author ruderney palencia
 */
public class DiplomaController implements Initializable {

    @FXML
    private TableView tablediplomas;
    @FXML
    private JFXTextField txtDI;
    @FXML
    private JFXComboBox txtaño;
    @FXML
    private JFXComboBox txtmes;
    @FXML
    private MaterialDesignIconView iconClose;
    @FXML
    private JFXButton btnSearch;
    @FXML
    private JFXButton btnEnviarEmail;
    @FXML
    private JFXButton btnGenerarDiploma;
    @FXML
    private Label labelerror;
    @FXML
    private JFXComboBox combodiplomas;

    private BDGeko con;
    private Connection cn;
    private EnviarMail m;
    cargarsplas dialog;
    boolean estado= false;
    Label msj= new Label("  Espere Por favor...");

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = new BDGeko();
        txtaño.getItems().removeAll(txtaño.getItems());
        txtaño.getItems().addAll("2018", "2019", "2020", "2021", "2022");
        txtaño.getSelectionModel().select("");

        txtmes.getItems().removeAll(txtmes.getItems());
        txtmes.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        txtmes.getSelectionModel().select("");
        
        combodiplomas.getItems().removeAll(combodiplomas.getItems());
        combodiplomas.getItems().addAll("CertificadoDefault","CertificadoRP");
        combodiplomas.getSelectionModel().select("");
    }

    @FXML
    private void closeStage(MouseEvent event) {
        iconClose.getScene().getWindow().hide();
    }

    @FXML
    private void searchStudent(ActionEvent event) throws SQLException {
        populateTable();
    }

    private void populateTable() {
        try {
            cn = con.getConnection();
            String query = "SELECT cc,name_complet,code_curso,correo,fecha_actual FROM student WHERE  cc='" + txtDI.getText().trim() + "'";

            ResultSet rst = cn.createStatement().executeQuery(query);
            ObservableList<ObservableList> data = FXCollections.observableArrayList();
            tablediplomas.getColumns().clear();
            int cols = rst.getMetaData().getColumnCount();

            for (int i = 0; i < cols; i++) {
                final int j = i;
                TableColumn col = new TableColumn(rst.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());

                    }
                });

                col.setPrefWidth(150);
                tablediplomas.getColumns().addAll(col);
            }
            while (rst.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int k = 1; k <= rst.getMetaData().getColumnCount(); k++) {
                    row.add(rst.getString(k));
                }
                data.add(row);
            }
            tablediplomas.setItems(data);
        } catch (SQLException ex) {
            Logger.getLogger(DiplomaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void generarDiploma(ActionEvent event) throws SQLException {

        if (!txtDI.getText().isEmpty()) {
            if (!txtmes.getSelectionModel().isEmpty() && !txtaño.getSelectionModel().isEmpty()) {
              String diploma =(String) combodiplomas.getSelectionModel().getSelectedItem();
                verPDF(diploma);
                labelerror.setTextFill(Color.web("#1D1D1D"));
                labelerror.setText("");
            } else {
                labelerror.setTextFill(Color.web("#E50F08"));
                labelerror.setText("Por Favor Rellene el Campo AÑO, MES");
            }

        } else {
            labelerror.setTextFill(Color.web("#E50F08"));
            labelerror.setText("Por Favor Rellene el Campo ID");
        }

    }

    public void verPDF(String diploma) {

        con = new BDGeko();
        String fecha = txtaño.getSelectionModel().getSelectedItem() + " " + txtmes.getSelectionModel().getSelectedItem();
        try {
            cn = con.getConnection();
            String sql = "SELECT student.name_complet AS nombre,cursos.name_curso AS curso,cursos.horas_curso AS horas FROM student INNER JOIN cursos ON student.code_curso = cursos.code_curso WHERE student.cc='"+ txtDI.getText().trim() + "'";
            if (cn != null) {
                 ResultSet rst = cn.createStatement().executeQuery(sql);
                if(rst.next()){
                 String nombre=rst.getString("nombre");
                 String curso=rst.getString("curso");
                 String horas=rst.getString("horas");
             
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/Certificaciones/"+diploma+".jasper"));
                Map parametro = new HashMap();
                parametro.put("certi", diploma.trim());
                parametro.put("name_complet", nombre);
                parametro.put("cc", txtDI.getText());
                parametro.put("curso", curso);
                parametro.put("horas", horas);            
                parametro.put("fecha", fecha);

                JasperPrint jp = JasperFillManager.fillReport(jasperReport, parametro,new JREmptyDataSource());
                JasperViewer jv = new JasperViewer(jp, false);
                jv.setVisible(true);

                jv.setTitle("Reporte Diploma");
                }else{
                    System.out.println("no Existe el Usuario con esta Tipo de Certificacion");
                }
            }
        } catch (JRException ex) {
            System.err.println("Error Report: " + ex.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(DiplomaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static final Logger LOG = Logger.getLogger(DiplomaController.class.getName());

    @FXML
    private void EnviarEmail(ActionEvent event) {
        java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
          dialog = new cargarsplas(null, true,msj.getText());
          dialog.setLocationRelativeTo(null);
          dialog.setVisible(true);
      }
  });
     enviaremail();
  if (estado) {
            dialog.dispose();
        }else{
              System.out.println("error el estado esta falso");
          }
    }
 private void enviaremail(){
    m = new EnviarMail();
        String query = "SELECT cc,name_complet,correo FROM student WHERE  cc='" + txtDI.getText().trim() + "' LIMIT 1";
        ResultSet rs;
        Statement st;
        String correostudent = "", cc = "", nombre = "";
        try {
            cn = con.getConnection();
            if (cn != null) {
                st = cn.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    cc = rs.getString("cc");
                    nombre = rs.getString("name_complet");
                    correostudent = rs.getString("correo");
                }
                if ("".equals(correostudent)) {
                    labelerror.setTextFill(Color.web("#1D1D1D"));
                    labelerror.setText("El correo de este Usuario no Existe");
                }else{ 
                if (!correostudent.equals("")) {

                    if (!txtDI.getText().isEmpty()) {
                        if (txtaño.getSelectionModel().getSelectedItem()!="" && 
                                txtmes.getSelectionModel().getSelectedItem()!="" && combodiplomas.getSelectionModel().getSelectedItem()!="") {
                            String asunto = "Certificacion de Estudios Cruz Roja Santander";
                            String cuerpomjs = codigohtml(nombre);
                            String diploma =(String) combodiplomas.getSelectionModel().getSelectedItem();
                            
                            m.enviarConGMail(correostudent, asunto, cuerpomjs, pdf(diploma.trim()).getAbsoluteFile());
                            labelerror.setTextFill(Color.web("#0AAE0F"));
                            labelerror.setText("Correo Enviado");

                        } else {
                            labelerror.setTextFill(Color.web("#E50F08"));
                            labelerror.setText("Por Favor Rellene el Campo AÑO, MES, Diploma");
                        }

                    } else {
                        labelerror.setTextFill(Color.web("#E50F08"));

                        labelerror.setText("Por Favor Rellene el Campo ID");
                    }

                } else {
                    labelerror.setTextFill(Color.web("#E50F08"));
                    labelerror.setText("Este correo no contiene los dominios de Gmail "
                            + " tampoco de cruzrojacolombiana.org");
                }
            }
            }

        } catch (SQLException e) {
            System.out.println("error: " + e);
        }
        estado=true;
 }
    private File pdf(String diploma) {
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        con = new BDGeko();
        File ruta = null;
        //diplomadefault.jpg"

        String url;
        String fecha = txtaño.getSelectionModel().getSelectedItem() + " " + txtmes.getSelectionModel().getSelectedItem();
        try {
            cn = con.getConnection();
                     String sql = "SELECT student.name_complet AS nombre,cursos.name_curso AS curso,cursos.horas_curso AS horas FROM student INNER JOIN cursos ON student.code_curso = cursos.code_curso WHERE student.cc='"+ txtDI.getText().trim() + "'";
            if (cn != null) {
                 ResultSet rst = cn.createStatement().executeQuery(sql);
                if(rst.next()){
                 String nombre=rst.getString("nombre");
                 String curso=rst.getString("curso");
                 String horas=rst.getString("horas");
           
                //se carga el reporte
                URL in = this.getClass().getResource("/Certificaciones/"+diploma+".jasper");
                jasperReport = (JasperReport) JRLoader.loadObject(in);
                Map parametro = new HashMap();
                parametro.put("certi", diploma.trim());
                parametro.put("name_complet", nombre);
                parametro.put("cc", txtDI.getText());
                parametro.put("curso", curso);
                parametro.put("horas", horas);            
                parametro.put("fecha", fecha);
                //se procesa el archivo jasper
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, new JREmptyDataSource());
                //se crea el archivo PDF
                url = "diplomas/"+txtDI.getText().trim() + ".pdf";
                ruta = new File(url);
                JasperExportManager.exportReportToPdfFile(jasperPrint, ruta.getAbsoluteFile().getPath());
                
                System.out.println("ruta arhivo: " + ruta.getAbsolutePath());

            }else{
                
                    System.out.println("no hay coincidencia en la consulta de estudiantes codigo no es igual o fecha");
                }
            
      
      
   
        }
         
        } catch (SQLException | JRException ex) {
            Logger.getLogger(DiplomaController.class.getName()).log(Level.SEVERE, null, ex);
        }
      return ruta;  
    }
    public String codigohtml(String nombre){
        String codigo="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
" \n" +
"<head>\n" +
"  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
"  <title>Cruz Roja Santander</title>\n" +
" \n" +
"</head>\n" +
"\n" +
"<body yahoo bgcolor=\"#f6f8f1\" style=\"margin: 0; padding: 0; min-width: 100%!important; @media only screen and (max-width: 550px), screen and (max-device-width: 550px) {\n" +
"  body[yahoo] .hide {display: none!important;}\n" +
"  body[yahoo] .buttonwrapper {background-color: transparent!important;}\n" +
"  body[yahoo] .button {padding: 0px!important;}\n" +
"  body[yahoo] .button a {background-color: #e05443; padding: 15px 15px 13px!important;}\n" +
"  body[yahoo] .unsubscribe {display: block; margin-top: 20px; padding: 10px 50px; background: #2f3942; border-radius: 5px; text-decoration: none!important; font-weight: bold;}\n" +
"  } \">\n" +
"<table width=\"100%\" bgcolor=\"#f6f8f1\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
"<tr>\n" +
"  <td>\n" +
"   \n" +
"    <table bgcolor=\"#ffffff\"  align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; max-width: 600px;\">\n" +
"      <tr>\n" +
"        <td bgcolor=\"#FFFFFF\"  style=\"padding: 5px 5px 5px 5px;\">\n" +
"          \n" +
"        </td>\n" +
"      </tr>\n" +
"      <tr>\n" +
"        <td  style=\"padding: 30px 30px 30px 30px;border-bottom: 1px solid #f2eeed; \">\n" +
"          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
"            <tr>\n" +
"              <td style=\" color: #153643; font-family: sans-serif; padding: 0 0 15px 0; font-size: 24px; line-height: 28px; font-weight: bold;\">\n" +
"                Bienvenido, Su Certificado de Estudio Esta Listo\n"+"Sr/a "+nombre+
"              </td>\n" +
"            </tr>\n" +
"            <tr>\n" +
"              <td style=\"color: #153643; font-family: sans-serif; font-size: 16px; line-height: 22px;\" >\n" +
"                Es un honor compartir este importante momento de tu vida. La Certificacion es definitivamente una conquista, pero todavía hay muchas cosas que te esperan: " +
"                ¡metas, desafíos y oportunidades! Cualquier cosa es posible cuando usted tiene confianza en sí mismo..\n" +
"              </td>\n" +
"            </tr>\n" +
"          </table>\n" +
"        </td>\n" +
"      </tr>\n" +
"      <tr>\n" +
"        <td style=\"padding: 30px 30px 30px 30px;border-bottom: 1px solid #f2eeed; \">\n" +
"          <table width=\"115\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">  \n" +
"            <tr>\n" +
"              <td height=\"115\" style=\"padding: 0 20px 20px 0;\">\n" +
"                <img style=\"height: auto;\" src=\"https://i.postimg.cc/hP6ZkC8c/fondo-2.png\" width=\"115\" height=\"115\" border=\"0\" alt=\"\" />\n" +
"              </td>\n" +
"            </tr>\n" +
"          </table>\n" +
"     \n" +
"          <table class=\"col380\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%; max-width: 380px;\">  \n" +
"            <tr>\n" +
"              <td>\n" +
"                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
"                  <tr>\n" +
"                    <td style=\"color: #153643; font-family: sans-serif; font-size: 16px; line-height: 22px;\">\n" +
"                      Deseo que este logro no sea un punto de llegada sino de inicio para una carrera brillante. Felicitaciones .\n" +
"                    </td>\n" +
"                  </tr>\n" +
"                  <tr>\n" +
"                    <td style=\"padding: 20px 0 0 0;\">\n" +
"                      <table class=\"buttonwrapper\" bgcolor=\"#e05443\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
"                        <tr>\n" +
"                          <td style=\"text-align: center; font-size: 18px; font-family: sans-serif; font-weight: bold; padding: 0 30px 0 30px; color: #ffffff; text-decoration: none;\" height=\"45\">\n" +
"                            <a href=\"https://iecruzrojasantander.edu.co/\">Visitanos</a>\n" +
"                          </td>\n" +
"                        </tr>\n" +
"                      </table>\n" +
"                    </td>\n" +
"                  </tr>\n" +
"                </table>\n" +
"              </td>\n" +
"            </tr>\n" +
"          </table>\n" +
"  \n" +
"        </td>\n" +
"      </tr>\n" +
"      <tr>\n" +
"        <td style=\"padding: 30px 30px 30px 30px;border-bottom: 1px solid #f2eeed; \">\n" +
"          <img style=\"height: auto;\" src=\"https://i.postimg.cc/XY514wxF/Logo-cruz-roja.png\" width=\"100%\" border=\"0\" alt=\"\" />\n" +
"        </td>\n" +
"      </tr>\n" +
"      <tr>\n" +
"        <td style=\"padding: 30px 30px 30px 30px; color: #153643; font-family: sans-serif; font-size: 16px; line-height: 22px;\">\n" +
"          Este Mensaje se a generado Automatico por el sistema, no responda ni envie informacion importante por seguridad <strong>Muchas Gracias, Felicitaciones.</strong>\n" +
"        </td>\n" +
"      </tr>\n" +
"      <tr>\n" +
"        <td style=\"padding: 20px 30px 15px 30px;\" bgcolor=\"#44525f\">\n" +
"          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
"            <tr>\n" +
"              <td align=\"center\" style=\"font-family: sans-serif; font-size: 14px; color: #ffffff; \">\n" +
"                &reg; Bucaramanga, CruzRojaSantander 2021<br/>\n" +
"              </td>\n" +
"            </tr>\n" +
"            <tr>\n" +
"              <td align=\"center\" style=\"padding: 20px 0 0 0; color: #ffffff; text-decoration: underline;\">\n" +
"                <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
"                  <tr>\n" +
"                    <td width=\"37\" style=\"text-align: center; padding: 0 10px 0 10px;\">\n" +
"                      <a href=\"https://es-la.facebook.com/cruzrojasantander/\">\n" +
"                        <img style=\"height: auto;\" src=\"https://i.postimg.cc/pLLkZ3R5/facebook-social-fb-7213.png\" width=\"37\" height=\"37\" alt=\"Facebook\" border=\"0\" />\n" +
"                      </a>\n" +
"                    </td>\n" +
"                   \n" +
"                  </tr>\n" +
"                </table>\n" +
"              </td>\n" +
"            </tr>\n" +
"          </table>\n" +
"        </td>\n" +
"      </tr>\n" +
"    </table>\n" +
"\n" +
"    </td>\n" +
"  </tr>\n" +
"</table>\n" +
"\n" +
"</body>\n" +
"</html>";
        return codigo;
    
    }
}
