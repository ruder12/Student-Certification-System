
package GekoStudent;

import com.jfoenix.controls.JFXRippler;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Ruder palencia
 */
public class MainController implements Initializable {

    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane menuAbout;
    @FXML
    private AnchorPane menuCargarExcel;
    @FXML
    private AnchorPane menuCourse;
    @FXML
    private AnchorPane menuStudent;
    @FXML
    private AnchorPane menuReports;
    @FXML
    private AnchorPane menuDatabase;
    @FXML
    private AnchorPane menuEnviarMail;
    @FXML
    private AnchorPane menuConfig;
    
    @FXML
    private VBox groupTraining;
    @FXML
    private VBox groupReports;
    @FXML
    private VBox groupRegistrarCargar;
    @FXML
    private VBox groupcorreos;
    @FXML
    private VBox groupAbout;

    public static AnchorPane staticPane;
    public static StackPane mainRootPane;
  
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        staticPane = menuStudent;
        mainRootPane = rootPane;
        setUpRipples();
    }

    @FXML
    public void showAbout() {
        setStage("/modules/About.fxml");
    }

    @FXML
    private void showCompanies(MouseEvent event) {
        setStage("/modules/CargarExcel.fxml");
//
    }

    @FXML
    private void showCourse(MouseEvent event) {
        setStage("/modules/Course.fxml");
        //LLAMAMOS EL MODAL DEL CURSO
    }

    @FXML
    private void showStudent(MouseEvent event) throws IOException {
        setStage("/modules/Student.fxml");
        //funcines estudintes manual
    }

    private void setUpRipples() {
        JFXRippler fXRippler = new JFXRippler(menuAbout, JFXRippler.RipplerMask.RECT, JFXRippler.RipplerPos.FRONT);
        JFXRippler fXRippler2 = new JFXRippler(menuDatabase, JFXRippler.RipplerMask.RECT, JFXRippler.RipplerPos.FRONT);
        groupAbout.getChildren().addAll(fXRippler2, fXRippler);

        JFXRippler fXRippler3 = new JFXRippler(menuStudent, JFXRippler.RipplerMask.RECT, JFXRippler.RipplerPos.FRONT);
        JFXRippler fXRippler4 = new JFXRippler(menuCargarExcel, JFXRippler.RipplerMask.RECT, JFXRippler.RipplerPos.FRONT);
        groupRegistrarCargar.getChildren().addAll(fXRippler3, fXRippler4);

        JFXRippler fXRippler5 = new JFXRippler(menuReports, JFXRippler.RipplerMask.RECT, JFXRippler.RipplerPos.FRONT);
        JFXRippler fXRippler6 = new JFXRippler(menuCourse, JFXRippler.RipplerMask.RECT, JFXRippler.RipplerPos.FRONT);
        groupReports.getChildren().addAll(fXRippler5, fXRippler6);
        
         JFXRippler fXRippler7 = new JFXRippler(menuEnviarMail, JFXRippler.RipplerMask.RECT, JFXRippler.RipplerPos.FRONT);
        JFXRippler fXRippler8 = new JFXRippler(menuConfig, JFXRippler.RipplerMask.RECT, JFXRippler.RipplerPos.FRONT);
        groupcorreos.getChildren().addAll(fXRippler7, fXRippler8);
        

    }


    private void setStage(String fxml) {
        try {
            //dim overlay on new stage opening
            Region veil = new Region();
            veil.setPrefSize(1100, 650);
            veil.setStyle("-fx-background-color:rgba(0,0,0,0.3)");
            Stage newStage = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource(fxml));

            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);
            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initStyle(StageStyle.TRANSPARENT);

            newStage.getScene().getRoot().setEffect(new DropShadow());
            newStage.showingProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    rootPane.getChildren().add(veil);
                } else if (rootPane.getChildren().contains(veil)) {
                    rootPane.getChildren().removeAll(veil);
                }

            });
            newStage.centerOnScreen();
            newStage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showReports(MouseEvent event) {
        //llamo a la caja de certificaciones
        setStage("/modules/Diploma.fxml");
    }

    

    @FXML
    private void showBackUps(MouseEvent event) {
        setStage("/modules/BackUps.fxml");
    }

    @FXML
    private void showenviarEmail(MouseEvent event) {
      setStage("/modules/MainEmail.fxml");
    }
  
  
}
