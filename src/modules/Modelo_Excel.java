
package modules;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Ruder Palencia (Geko)
 */
public class Modelo_Excel {


    public void formatoxls(String url) {
 //Crear libro de trabajo en blanco
        Workbook workbook = new HSSFWorkbook();
        //Crea hoja nueva
        Sheet sheet = workbook.createSheet("Estudiantes");
        //Por cada línea se crea un arreglo de objetos (Object[])
        Map<String, Object[]> datos = new TreeMap<>();
        datos.put("1", new Object[]{"Id","Documento", "Nombre y Apellido", "Codigo Curso","correo"});
        datos.put("2", new Object[]{1,1035452,"Maria diaz",244,"Remen@cruzroja.edu.co"});
        datos.put("3", new Object[]{2,1035652 ,"David fernandez",244,"Allos@cruzroja.edu.co"});
        datos.put("4", new Object[]{3,1035854, "Carlos sandoval",244,"Caritas@cruzroja.edu.co"});
        datos.put("5", new Object[]{4,1035352, "Luisa guitierrez",244,"Vitz@cruzroja.edu.co"});
        //Iterar sobre datos para escribir en la hoja
        Set keyset = datos.keySet();
        int numeroRenglon = 0;
        for (Object key : keyset) {
            Row row = sheet.createRow(numeroRenglon++);
            Object[] arregloObjetos = datos.get(key);
           
            int numeroCelda = 0;
            for (Object obj : arregloObjetos) {
                Cell cell = row.createCell(numeroCelda++);
                 
                if (obj instanceof String) {
                    cell.setCellValue((String) obj);
                } else if (obj instanceof Integer) {
                    cell.setCellValue((Integer) obj);
                }
            }
        }
        try {
            try ( //Se genera el documento
                    FileOutputStream out = new FileOutputStream(new File(url))) {
               if(out!=null){
                workbook.write(out);
                   System.out.println("Archivo generado exitosamente");
               }else{
                   System.out.println("Archivo no generado -Error-");
               }
               
            }
        } catch (IOException e) {
            System.out.println("error: "+e);
        }
    }
   public void Backup(String user,String bd,String ruta_name_bd){
        try {
            String cmd = "mysqldump -u"+user+" -p"+bd+" "+ruta_name_bd;
            Runtime rt = Runtime.getRuntime();
            if (rt.exec(cmd)!=null) {
                System.out.println("se ejecuto correctamente");
            }else{
                System.out.println("error ");
            }

        } catch (IOException ex) {
            Logger.getLogger(Modelo_Excel.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
}
