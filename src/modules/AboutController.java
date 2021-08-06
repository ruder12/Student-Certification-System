/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author ruderney palencia
 */
public class AboutController implements Initializable{

    @FXML
    private MaterialDesignIconView iconClose;
    @FXML
    private Label txtusarapp;
            


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    @FXML
    private void actioapp(MouseEvent event){
        try {
            Desktop.getDesktop().browse(new URI("https://www.youtube.com/channel/UC5ATWkrf8pL62fUHaEAucPA"));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(AboutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void closeStage(MouseEvent event) {
        iconClose.getScene().getWindow().hide();
    }
    
}
