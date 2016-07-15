
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Rony on 5/29/2016.
 */
public class Database {
     private static Server server = null;
     private static Connection conexion=null;
     private static Database database=null;

    public Database() throws SQLException {
    }
    //conexion = DriverManager.getConnection("jdbc:h2:tcp:~/practica2", "sa", "1234");

    public static Database getDatabase() throws SQLException{
        if(database==null || server==null){
            database= new Database();
            try {
                server = Server.createTcpServer("-tcpAllowOthers").start();
                Class.forName("org.h2.Driver");
                conexion = DriverManager.
                        getConnection("jdbc:h2:~/practica2", "sa", "1234");
                System.out.println("Connection Established: "
                        + conexion.getMetaData().getDatabaseProductName() + "/" + conexion.getCatalog());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return database;
    }




    public void insert(String[] params) throws SQLException {

        String insert="INSERT INTO ESTUDIANTE(MATRICULA,NOMBRE,APELLIDOS,TELEFONO) VALUES(";
        for(int i=0;i<params.length;i++){
            if(i!=params.length-1)
                insert+="'"+params[i]+"'"+",";
            else
                insert+="'"+params[i]+"'"+");";
        }
        conexion.createStatement().execute(insert);
        conexion.commit();
    }

    public void create() throws SQLException {

        String insert="CREATE TABLE IF NOT EXISTS ESTUDIANTE(\n" +
                "  MATRICULA VARCHAR2(8),\n" +
                "  NOMBRE VARCHAR2(100),\n" +
                "  APELLIDOS VARCHAR2(100),\n" +
                "  TELEFONO VARCHAR2(50),\n" +
                "  PRIMARY KEY (MATRICULA)\n" +
                ")";

        conexion.createStatement().execute(insert);
        conexion.commit();
    }

    public void delete(String Matricula) throws SQLException {
        String delete="DELETE FROM ESTUDIANTE WHERE MATRICULA="+"'"+Matricula+"'";
        conexion.createStatement().execute(delete);
        conexion.commit();
    }

    public void update(String[] estudiante) throws SQLException {
        String update=String.format("UPDATE ESTUDIANTE SET NOMBRE = '%s', APELLIDOS = '%s', TELEFONO = '%s'" +
                        " WHERE MATRICULA = '%s'",
                estudiante[1], estudiante[2], estudiante[3], estudiante[0]);
        conexion.createStatement().execute(update);
        conexion.commit();
    }

    public ArrayList<Estudiante> select() throws SQLException {
        ArrayList<Estudiante> lista = new ArrayList<Estudiante>();
        String select="SELECT * FROM ESTUDIANTE";
        ResultSet r= conexion.createStatement().executeQuery(select);
        while(r.next()){
            Estudiante e = new Estudiante();
            e.setMatricula(r.getString("matricula"));
            e.setNombre(r.getString("nombre"));
            e.setApellidos(r.getString("apellidos"));
            e.setTelefono(r.getString("telefono"));
            lista.add(e);
        }
        return lista;
    }
    public void close() throws SQLException {
        conexion.close();
    }

}
