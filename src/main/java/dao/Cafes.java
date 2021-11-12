package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InvalidPropertiesFormatException;

import modelo.AccesoDatosException;
import utils.Utilidades;

/**
 * @descrition
 * @author Alex Y Kevin
 * @date 23/10/2021
 * @version 1.0
 * @license GPLv3
 */

public class Cafes {

	private Connection connection;
	private ResultSet resultSet;
	private PreparedStatement preparedStatement;
	private Statement statement;

	private static final String SELECT_CAFES_QUERY = "select CAF_NOMBRE, PROV_ID, PRECIO, VENTAS, TOTAL from CAFES";
	private static final String SEARCH_CAFE_QUERY = "select * from CAFES WHERE CAF_NOMBRE= ?";
	private static final String INSERT_CAFE_QUERY = "insert into CAFES values (?,?,?,?,?)";
	private static final String DELETE_CAFE_QUERY = "delete from CAFES WHERE CAF_NOMBRE = ?";
	private static final String SEARCH_CAFES_PROVEEDOR = "select * from CAFES,PROVEEDORES WHERE CAFES.PROV_ID= ? AND CAFES.PROV_ID=PROVEEDORES.PROV_ID";

	private static final String CREATE_TABLE_PROVEEDORES = "create table if not exists proveedores (PROV_ID integer NOT NULL, PROV_NOMBRE varchar(40) NOT NULL, CALLE varchar(40) NOT NULL, CIUDAD varchar(20) NOT NULL, PAIS varchar(2) NOT NULL, CP varchar(5), PRIMARY KEY (PROV_ID));";

	private static final String CREATE_TABLE_CAFES = "create table if not exists CAFES (CAF_NOMBRE varchar(32) NOT NULL, PROV_ID int NOT NULL, PRECIO numeric(10,2) NOT NULL, VENTAS integer NOT NULL, TOTAL integer NOT NULL, PRIMARY KEY (CAF_NOMBRE), FOREIGN KEY (PROV_ID) REFERENCES PROVEEDORES(PROV_ID));";

	public Cafes() {
		connection = null;
		resultSet = null;
		preparedStatement = null;
		statement = null;

		try {
			conectar();
			statement = connection.createStatement();

			statement.executeUpdate(CREATE_TABLE_PROVEEDORES);

			statement.executeUpdate(CREATE_TABLE_CAFES);

			statement.executeUpdate(
					"insert into proveedores values(49, 'PROVerior Coffee', '1 Party Place', 'Mendocino', 'CA', '95460');");
			statement.executeUpdate(
					"insert into proveedores values(101, 'Acme, Inc.', '99 mercado CALLE', 'Groundsville', 'CA', '95199');");
			statement.executeUpdate(
					"insert into proveedores values(150, 'The High Ground', '100 Coffee Lane', 'Meadows', 'CA', '93966');");
		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			// Utilidades.printSQLException(sqle);

		} finally {
			liberar();
			cerrarConexion();
		}
	}

	/**
	 * Metodo que muestra por pantalla los datos de la tabla cafes
	 * 
	 * @param con
	 * @throws SQLException
	 */
	public void verTabla() throws AccesoDatosException {

		try {
			conectar();
			// Creación de la sentencia
			statement = connection.createStatement();
			// Ejecución de la consulta y obtención de resultados en un
			// ResultSet
			resultSet = statement.executeQuery(SELECT_CAFES_QUERY);

			// Recuperación de los datos del ResultSet
			while (resultSet.next()) {
				String coffeeName = resultSet.getString("CAF_NOMBRE");
				int supplierID = resultSet.getInt("PROV_ID");
				float PRECIO = resultSet.getFloat("PRECIO");
				int VENTAS = resultSet.getInt("VENTAS");
				int total = resultSet.getInt("TOTAL");
				System.out.println(coffeeName + ", " + supplierID + ", " + PRECIO + ", " + VENTAS + ", " + total);
			}

		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			// System.err.println(sqle.getMessage());
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		} finally {
			liberar();
			cerrarConexion();
		}
	}

	/**
	 * MÃ³todo que busca un cafe por nombre y muestra sus datos
	 *
	 * @param nombre
	 */
	public void buscar(String nombre) throws AccesoDatosException {
		try {
			conectar();
			// Creación de la sentencia
			statement = connection.prepareStatement(SEARCH_CAFE_QUERY);
			preparedStatement.setString(1, nombre);
			// Ejecución de la consulta y obtención de resultados en un
			// ResultSet
			resultSet = preparedStatement.executeQuery();

			// Recuperación de los datos del ResultSet
			if (resultSet.next()) {
				String coffeeName = resultSet.getString("CAF_NOMBRE");
				int supplierID = resultSet.getInt("PROV_ID");
				float PRECIO = resultSet.getFloat("PRECIO");
				int VENTAS = resultSet.getInt("VENTAS");
				int total = resultSet.getInt("TOTAL");
				System.out.println(coffeeName + ", " + supplierID + ", " + PRECIO + ", " + VENTAS + ", " + total);
			}

		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		} finally {
			liberar();
			cerrarConexion();
		}
	}

	/**
	 * MÃ³todo para insertar una fila
	 * 
	 * @param nombre
	 * @param provid
	 * @param precio
	 * @param ventas
	 * @param total
	 * @return
	 */
	public void insertar(String nombre, int provid, float precio, int ventas, int total) throws AccesoDatosException {
		try {
			conectar();
			preparedStatement = connection.prepareStatement(INSERT_CAFE_QUERY);
			preparedStatement.setString(1, nombre);
			preparedStatement.setInt(2, provid);
			preparedStatement.setFloat(3, precio);
			preparedStatement.setInt(4, ventas);
			preparedStatement.setInt(5, total);
			// Ejecución de la inserción
			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		} finally {
			liberar();
			cerrarConexion();
		}
	}

	/**
	 * MÃ³todo para borrar una fila dado un nombre de cafÃ³
	 * 
	 * @param nombre
	 * @return
	 */
	public void borrar(String nombre) throws AccesoDatosException {
		try {
			conectar();
			// Creación de la sentencia
			preparedStatement = connection.prepareStatement(DELETE_CAFE_QUERY);
			preparedStatement.setString(1, nombre);
			// Ejecución del borrado
			preparedStatement.executeUpdate();
			System.out.println("café " + nombre + " ha sido borrado.");

		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");

		} finally {
			liberar();
			cerrarConexion();
		}
	}

	/**
	 * MÃ³todo que busca un cafe por nombre y muestra sus datos
	 *
	 * @param nombre
	 */
	public void cafesPorProveedor(int provid) throws AccesoDatosException {
		try {
			conectar();
			// Creación de la sentencia
			preparedStatement = connection.prepareStatement(SEARCH_CAFES_PROVEEDOR);
			preparedStatement.setInt(1, provid);
			// Ejecución de la consulta y obtención de resultados en un
			// ResultSet
			resultSet = preparedStatement.executeQuery();

			// Recuperación de los datos del ResultSet
			while (resultSet.next()) {
				String coffeeName = resultSet.getString("CAF_NOMBRE");
				int supplierID = resultSet.getInt("PROV_ID");
				float PRECIO = resultSet.getFloat("PRECIO");
				int VENTAS = resultSet.getInt("VENTAS");
				int total = resultSet.getInt("TOTAL");
				String provName = resultSet.getString("PROV_NOMBRE");
				String calle = resultSet.getString("CALLE");
				String ciudad = resultSet.getString("CIUDAD");
				String pais = resultSet.getString("PAIS");
				int cp = resultSet.getInt("CP");
				System.out.println(coffeeName + ", " + supplierID + ", " + PRECIO + ", " + VENTAS + ", " + total
						+ "\n y el proveedor es: " + provName + "," + calle + "," + ciudad + "," + pais + "," + cp);
			}

		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		} finally {
			liberar();
			cerrarConexion();
		}

	}

	public void conectar() {
		try {
			connection = new Utilidades().getConnection();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cerrarConexion() {
		try {
			if (connection != null) {
				connection.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void liberar() {

		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (statement != null) {
				statement.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
