
package modules;


import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ruderney palencia
 */
public class BackUpsController implements Initializable {

    @FXML
    private MaterialDesignIconView iconClose;
    @FXML
    private JFXButton btnBackUp;
    @FXML
    private JFXButton btnRestore;
    @FXML
    private JFXButton format;

    Modelo_Excel me;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void closeStage(MouseEvent event) {
        btnRestore.getScene().getWindow().hide();
    }

    @FXML
    private void backUpDB(ActionEvent event) {
        String nom="";
         nom = JOptionPane.showInputDialog("Por favor Ingrese el Nombre de la Base de datos");
        if (!nom.equals("")) {
            exportDB(nom);
        }
            
    }
    @FXML
    private void formatxls(ActionEvent event) {
        int r = JOptionPane.showConfirmDialog(null, "Confirmar Descarga de Formato xls");
        if (r!=0) {
            System.out.println("se ha cancelado la descarga del formato" + r);
            JOptionPane.showMessageDialog(null, "Se ha Cancelado la Descarga del Formato");
        }else{
            FileChooser file = new FileChooser();
            String url ="FormatoEstudiantes";
            File fil = file.showSaveDialog(null);
            if (fil!=null) {
                url = fil.getAbsolutePath();
                 me = new Modelo_Excel();
                 me.formatoxls(url+".xls");
                System.out.println("ruta: "+url);
                System.out.println("se ha descargado el formato "+ r);
            }
//           
        
        }
    }

    @FXML
    private void restoreDB(ActionEvent event) {
    }
    private String obtenerFecha() {
        Calendar fecha = new GregorianCalendar();
        int año = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH);
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        String Fehca = dia + "-" + (mes + 1) + "-" + año;
        return Fehca;
    }
     public void exportDB(String nombreDB) {
        me = new Modelo_Excel();
        //nombre de usuario de la base de datos
        String mysqluser = "root";
        //password del usuario
        String mysqlpassword = "";
        //nombre de la base de datos
        String mysqldb = "studentbd";
        //ruta donde se guardará el backup
        String path = "Respaldos/" + nombreDB + "_" + obtenerFecha() + ".sql";
        File existe = new File(path);
        if (existe.exists()) {
            if (JOptionPane.showConfirmDialog(null, "YA EXISTE UN RESPALDO CON ESE NOMBRE\n¿DESEA REEMPLAZARLO?", "RESTAURAR RESPALDO", JOptionPane.YES_NO_OPTION, 0) == JOptionPane.YES_OPTION) {
               
               me.Backup(mysqluser, mysqldb, path);
            }else{
                System.out.println("opcion dos");
            }
        } else {
              me.Backup(mysqluser, mysqldb, path);
        }
    }
    
}
