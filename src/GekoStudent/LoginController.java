/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GekoStudent;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author danml
 */
public class LoginController implements Initializable {

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXTextField txtuser;

    @FXML
    private JFXPasswordField txtpass;

    @FXML
    private Label labelerror;
    
    @FXML
    private Label der;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        der.setText("\u00a9"+" Cruz Roja C. Seccional Santander-2021");
        
    }

    @FXML
    private void doLogin() throws IOException {
        validarUser v = new validarUser();
        if (!txtuser.getText().isEmpty() || !txtpass.getText().isEmpty()) {
            if (v.ValidarUSER(txtuser.getText(), txtpass.getText())) {
                btnLogin.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
                Stage mainStage = new Stage();
                Scene scene = new Scene(root);
                mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo-IECR2.png")));
                mainStage.setResizable(false);
                mainStage.setScene(scene);
                mainStage.show();
            } else {
                labelerror.setText("Datos Incorrectos");
            }

        } else {
            labelerror.setText("Verifique Los Datos Ingresados");
        }

    }
    @FXML
    private void enter(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            System.out.println("Usuario Inicio con Enter");
            try {
                doLogin();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

 }
}
