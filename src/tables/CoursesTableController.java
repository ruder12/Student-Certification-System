/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tables;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import GekoStudent.BDGeko;
import javafx.event.ActionEvent;

/**
 * FXML Controller class
 *
 * @author ruder palencia
 */
public class CoursesTableController implements Initializable {

    @FXML
    private TableView tableCourses;
   private ObservableList<ObservableList> data;

    private Connection conn;
    private BDGeko handler;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = new BDGeko();

        populateTable();

    }

    @FXML
    private void actiondelete(ActionEvent event) {
   
       

    }

    private void populateTable() {
        try {
            conn = handler.getConnection();
            String query = "SELECT code_curso,name_curso,horas_curso,cursofecha FROM cursos";

            ResultSet rst = conn.createStatement().executeQuery(query);
             data = FXCollections.observableArrayList();
            tableCourses.getColumns().clear();
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
 
                col.setPrefWidth(140);
                tableCourses.getColumns().addAll(col);
            }
            while (rst.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int k = 1; k <= rst.getMetaData().getColumnCount(); k++) {
                    row.add(rst.getString(k));
                }
                data.add(row);
            }
            tableCourses.setItems(data);
        } catch (SQLException ex) {
            Logger.getLogger(CoursesTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
}
