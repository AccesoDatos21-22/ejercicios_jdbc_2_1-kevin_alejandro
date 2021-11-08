package daoImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InvalidPropertiesFormatException;

import dao.CafesDAO;
import modelo.AccesoDatosException;
import utils.Utilidades;

public class CafesDAOImpl implements CafesDAO {

	public void verTabla() throws AccesoDatosException {
		// TODO Auto-generated method stub
		Utilidades utilidades = null;
		try {
			utilidades = new Utilidades();
			String nula = "";
			Statement sentencia = utilidades.getConnection().createStatement();
			ResultSet res = sentencia.executeQuery("SELECT * FROM cafes");
			ResultSetMetaData rsmd = res.getMetaData();
			int nColumnas = rsmd.getColumnCount();

			System.out.println("Columnas recuperadas " + nColumnas);
			for (int i = 1; i <= nColumnas; i++) {
				System.out.println("Columna " + i + ":");
				System.out.println("	Nombre : " + rsmd.getColumnName(i));
				System.out.println("	Tipo : " + rsmd.getColumnTypeName(i));
				if (rsmd.isNullable(i) == 0)
					nula = "NO";
				else
					nula = "SI";
				System.out.println("	Puede ser nula? :" + nula);
				System.out.println("	Máximo ancho de la columna: " + rsmd.getColumnDisplaySize(i));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void buscar(String nombre) throws AccesoDatosException {
		// TODO Auto-generated method stub

	}

	public void insertar(String nombre, int provid, float precio, int ventas, int total) throws AccesoDatosException {
		// TODO Auto-generated method stub

	}

	public void borrar(String nombre) throws AccesoDatosException {
		// TODO Auto-generated method stub

	}

	public void cafesPorProveedor(int provid) throws AccesoDatosException {
		// TODO Auto-generated method stub

	}

}
