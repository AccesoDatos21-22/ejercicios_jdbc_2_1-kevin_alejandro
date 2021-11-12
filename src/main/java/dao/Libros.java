package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import modelo.AccesoDatosException;
import modelo.Libro;
import utils.Utilidades;

/**
 * @descrition
 * @author Alejandro y Kevin
 * @date 23/10/2021
 * @version 1.0
 * @license GPLv3
 */

public class Libros {

	// Consultas a realizar en BD
	private static final String CREATE_TABLE_LIBROS = "create table if not exists LIBROS (isbn integer not null, titulo varchar(50) not null, autor varchar(50) not null, editorial varchar(25) not null, paginas integer not null, copias integer not null, constraint isbn_pk primary key (isbn));";
	private static final String INSERT_LIBRO_QUERY = "insert into LIBROS values (?,?,?,?,?,?)";

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private PreparedStatement preparedStatement;

	/**
	 * Constructor: inicializa conexión
	 * 
	 * @throws AccesoDatosException
	 */

	public Libros() throws AccesoDatosException {
		try {
			// Obtenemos la conexión
			this.connection = new Utilidades().getConnection();
			this.statement = null;
			this.resultSet = null;
			this.preparedStatement = null;

			statement = connection.createStatement();
			statement.executeUpdate(CREATE_TABLE_LIBROS);
		} catch (IOException e) {
			// Error al leer propiedades
			// En una aplicación real, escribo en el log y delego
			System.err.println(e.getMessage());
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			// System.err.println(sqle.getMessage());
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		} finally {
			liberar();
			cerrar();
		}
		System.out.println("Conectado a la BD libros.");
		System.out.println("Tabla libros creada.");
	}

	/**
	 * Método para cerrar la conexión
	 * 
	 * @throws AccesoDatosException
	 */
	public void cerrar() {

		if (connection != null) {
			Utilidades.closeConnection(connection);
		}

	}

	/**
	 * Método para liberar recursos
	 * 
	 * @throws AccesoDatosException
	 */
	private void liberar() {
		try {
			// Liberamos todos los recursos pase lo que pase
			// Al cerrar un stmt se cierran los resultset asociados. Podíamos omitir el
			// primer if. Lo dejamos por claridad.
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log, no delego porque
			// es error al liberar recursos
			Utilidades.printSQLException(sqle);
		}
	}

	/**
	 * Metodo que muestra por pantalla los datos de la tabla cafes
	 * 
	 * @param con
	 * @throws SQLException
	 */

	public List<Libro> verCatalogo() throws AccesoDatosException {

		return null;

	}

	/**
	 * Actualiza el numero de copias para un libro
	 * 
	 * @param isbn
	 * @param copias
	 * @throws AccesoDatosException
	 */

	public void actualizarCopias(Libro libro) throws AccesoDatosException {

	}

	/**
	 * Añade un nuevo libro a la BD
	 * 
	 * @param isbn
	 * @param titulo
	 * @param autor
	 * @param editorial
	 * @param paginas
	 * @param copias
	 * @throws AccesoDatosException
	 */
	public void anadirLibro(Libro libro) throws AccesoDatosException {
		try {
			conectar();
			preparedStatement = connection.prepareStatement(INSERT_LIBRO_QUERY);
			preparedStatement.setInt(1, libro.getISBN());
			preparedStatement.setString(2, libro.getTitulo());
			preparedStatement.setString(3, libro.getAutor());
			preparedStatement.setString(4, libro.getEditorial());
			preparedStatement.setInt(5, libro.getPaginas());
			preparedStatement.setInt(6, libro.getCopias());
			// Ejecuci�n de la inserci�n
			preparedStatement.executeUpdate();
			System.out.println("Libro a�adido correctamente.");
		} catch (SQLException sqle) {
			// En una aplicaci�n real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurri� un error al acceder a los datos");
		} finally {
			liberar();
			cerrar();
		}

	}

	/**
	 * Borra un libro por ISBN
	 * 
	 * @param isbn
	 * @throws AccesoDatosException
	 */

	public void borrar(Libro libro) throws AccesoDatosException {
	}

	/**
	 * Devulve los nombres de los campos de BD
	 * 
	 * @return
	 * @throws AccesoDatosException
	 */

	public String[] getCamposLibro() throws AccesoDatosException {

		return null;
	}

	public void obtenerLibro(int ISBN) throws AccesoDatosException {

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

}
