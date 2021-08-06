/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import GekoStudent.BDGeko;
import GekoStudent.MainController;
import java.time.LocalDate;
import javafx.scene.control.DatePicker;

/**
 * FXML Controller class
 *
 * @author ruderney palencia
 */
public class CourseController implements Initializable {

    @FXML
    private JFXButton btnShowCoursesList;
    @FXML
    private JFXTextField txtCourseName;
    @FXML
    private JFXTextField txtCourseCodigo;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXTextField txthorasCurso;
    private BDGeko handler;
    private Connection conn;
    @FXML
    private MaterialDesignIconView closeIcon;
    @FXML
    private DatePicker cursofecha;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = new BDGeko();
        
        
       
        
    }

  
    @FXML
    private void showCoursesList(ActionEvent event) throws IOException {
        //Dim stage
        Region veil = new Region();
        veil.setPrefSize(1100, 650);
        veil.setStyle("-fx-background-color:rgba(0,0,0,0.3)");
        btnSave.getScene().getWindow().hide();
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        Parent root = FXMLLoader.load(getClass().getResource("/tables/CoursesTable.fxml"));
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (MainController.mainRootPane != null) {
                    MainController.mainRootPane.getChildren().add(veil);
                }
            } else if (MainController.mainRootPane.getChildren().contains(veil)) {
                MainController.mainRootPane.getChildren().remove(veil);
            }

        });

        newStage.show();

    }

    @FXML
    private void saveCourse(ActionEvent event) throws SQLException {
        LocalDate date = cursofecha.getValue();
        conn = handler.getConnection();
        String saveQuery = "INSERT INTO cursos(code_curso,name_curso,horas_curso,cursofecha) VALUES (?,?,?,?)";
        if (txtCourseName.getText().isEmpty() || txtCourseCodigo.getText().isEmpty()) {
            return;
        }
        try (PreparedStatement ps = conn.prepareStatement(saveQuery)) {
            ps.setString(1, txtCourseCodigo.getText().toUpperCase()); 
            ps.setString(2, txtCourseName.getText().toUpperCase());
            ps.setString(3, txthorasCurso.getText());
            ps.setString(4, date.toString());
            int success = ps.executeUpdate();
            if (success == 1) {
                txtCourseName.setText("");
                txtCourseCodigo.setText("");
            }
        }
        conn.close();

    }

    @FXML
    private void closeStage(MouseEvent event) {
        btnSave.getScene().getWindow().hide();
    }

}
