package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
	private static final String SELECT_LIBROS_QUERY = "select * from LIBROS";
	private static final String UPDATE_LIBRO_QUERY = "update LIBROS SET copias = ? WHERE isbn = ?";
	private static final String DELETE_LIBRO_QUERY = "delete from LIBROS WHERE isbn = ?";
	private static final String SEARCH_LIBRO_QUERY = "select * from LIBROS WHERE isbn = ?";

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private PreparedStatement preparedStatement;

	/**
	 * Constructor: inicializa conexiÃ³n
	 * 
	 * @throws AccesoDatosException
	 */

	public Libros() throws AccesoDatosException {
		try {
			// Obtenemos la conexiÃ³n
			this.connection = new Utilidades().getConnection();
			this.statement = null;
			this.resultSet = null;
			this.preparedStatement = null;

			statement = connection.createStatement();
			statement.executeUpdate(CREATE_TABLE_LIBROS);
		} catch (IOException e) {
			// Error al leer propiedades
			// En una aplicaciÃ³n real, escribo en el log y delego
			System.err.println(e.getMessage());
			throw new AccesoDatosException("OcurriÃ³ un error al acceder a los datos");
		} catch (SQLException sqle) {
			// En una aplicaciÃ³n real, escribo en el log y delego
			// System.err.println(sqle.getMessage());
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("OcurriÃ³ un error al acceder a los datos");
		} finally {
			liberar();
			cerrar();
		}
		System.out.println("Conectado a la BD libros.");
		System.out.println("Tabla libros creada.");
	}

	/**
	 * MÃ©todo para cerrar la conexiÃ³n
	 * 
	 * @throws AccesoDatosException
	 */
	public void cerrar() {

		if (connection != null) {
			Utilidades.closeConnection(connection);
		}

	}

	/**
	 * MÃ©todo para liberar recursos
	 * 
	 * @throws AccesoDatosException
	 */
	private void liberar() {
		try {
			// Liberamos todos los recursos pase lo que pase
			// Al cerrar un stmt se cierran los resultset asociados. PodÃ­amos omitir el
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
			// En una aplicaciÃ³n real, escribo en el log, no delego porque
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

		List<Libro> libros = new ArrayList<Libro>();

		try {
			conectar();
			// Creación de la sentencia
			statement = connection.createStatement();
			// Ejecución de la consulta y obtención de resultados en un
			// ResultSet
			resultSet = statement.executeQuery(SELECT_LIBROS_QUERY);

			// Recuperación de los datos del ResultSet
			while (resultSet.next()) {
				Libro nuevoLibro = new Libro(resultSet.getInt("isbn"), resultSet.getString("titulo"),
						resultSet.getString("autor"), resultSet.getString("editorial"), resultSet.getInt("paginas"),
						resultSet.getInt("copias"));

				libros.add(nuevoLibro);
			}

		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			// System.err.println(sqle.getMessage());
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		} finally {
			liberar();
			cerrar();
		}

		return libros;

	}

	/**
	 * Actualiza el numero de copias para un libro
	 * 
	 * @param isbn
	 * @param copias
	 * @throws AccesoDatosException
	 */

	public void actualizarCopias(Libro libro) throws AccesoDatosException {
		try {
			conectar();
			// Creación de la sentencia
			preparedStatement = connection.prepareStatement(UPDATE_LIBRO_QUERY);
			preparedStatement.setInt(1, libro.getCopias());
			preparedStatement.setInt(2, libro.getISBN());

			// Ejecución del borrado
			preparedStatement.executeUpdate();
			System.out.println("Las copias del libro: " + libro.getTitulo() + " se han actualizado correctamente.");

		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");

		} finally {
			liberar();
			cerrar();
		}

	}

	/**
	 * AÃ±ade un nuevo libro a la BD
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
			// Ejecución de la inserción
			preparedStatement.executeUpdate();
			System.out.println("Libro añadido correctamente.");
		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
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
		try {
			conectar();
			// Creación de la sentencia
			preparedStatement = connection.prepareStatement(DELETE_LIBRO_QUERY);
			preparedStatement.setInt(1, libro.getISBN());

			// Ejecución del borrado
			preparedStatement.executeUpdate();
			System.out.println("\nEl libro: " + libro.getTitulo() + " se han borrado correctamente.");

		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");

		} finally {
			liberar();
			cerrar();
		}
	}

	/**
	 * Devulve los nombres de los campos de BD
	 * 
	 * @return
	 * @throws AccesoDatosException
	 */

	public String[] getCamposLibro() throws AccesoDatosException {

		ResultSetMetaData rsmd = null;
		String[] campos = null;
		try {
			conectar();
			preparedStatement = connection.prepareStatement(SELECT_LIBROS_QUERY);

			resultSet = preparedStatement.executeQuery();
			rsmd = resultSet.getMetaData();
			int columns = rsmd.getColumnCount();
			campos = new String[columns];
			for (int i = 0; i < columns; i++) {
				// Los indices de las columnas comienzan en 1
				campos[i] = rsmd.getColumnLabel(i + 1);
			}
			return campos;

		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");

		} finally {
			liberar();
		}
	}

	public void obtenerLibro(int ISBN) throws AccesoDatosException {
		try {
			conectar();
			// Creación de la sentencia
			preparedStatement = connection.prepareStatement(SEARCH_LIBRO_QUERY);
			preparedStatement.setInt(1, ISBN);
			// Ejecución de la consulta y obtención de resultados en un
			// ResultSet
			resultSet = preparedStatement.executeQuery();

			// Recuperación de los datos del ResultSet
			while (resultSet.next()) {
				Libro libro = new Libro(resultSet.getInt("isbn"), resultSet.getString("titulo"),
						resultSet.getString("autor"), resultSet.getString("editorial"), resultSet.getInt("paginas"),
						resultSet.getInt("copias"));

				System.out.println("\n" + libro.getISBN() + ", " + libro.getTitulo() + ", " + libro.getAutor() + ", "
						+ libro.getEditorial() + ", " + libro.getPaginas() + ", " + libro.getCopias());
			}

		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		} finally {
			liberar();
			cerrar();
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

}
