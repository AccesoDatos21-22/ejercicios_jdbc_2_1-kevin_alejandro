package main;

import java.util.Scanner;

import dao.Cafes;
import dao.Libros;
import modelo.AccesoDatosException;
import modelo.Libro;

public class Main {

	private static Scanner sn = new Scanner(System.in);

	public static void main(String[] args) throws AccesoDatosException {
		int opc = 0;

		do {
			System.out.println("\n ***** Seleccione una Opción *****");
			System.out.println("2. Buscar cafés por Proveedor");
			System.out.println("3. Conectar con la BD de libros, crear la tabla de libros e insertar libros.");
			System.out.println("6. Ver catálogo de libros");
			System.out.println("8. Salir.");

			opc = sn.nextInt();

			switch (opc) {
			case 2:
				try {
					Cafes cafes = new Cafes();
					cafes.insertar("Cafetito", 150, 1.0f, 100, 1000);
					cafes.insertar("Cafe tacilla", 150, 2.0f, 100, 1000);
					cafes.verTabla();
					// cafes.buscar("tacilla");
					// cafes.cafesPorProveedor(150);
					// cafes.borrar("Cafe tacilla");
					// cafes.verTabla();

				} catch (AccesoDatosException e) {
					e.printStackTrace();
				} /**/
				break;
			case 3:
				Libros libros = new Libros();

				Libro libro1 = new Libro(12345, "Sistemas Operativos", "Tanembaun", "Informática", 156, 3);
				libros.anadirLibro(libro1);

				Libro libro2 = new Libro(12453, "Minix", "Stallings", "Informática", 345, 4);
				libros.anadirLibro(libro2);

				Libro libro3 = new Libro(1325, "Linux", "Richard Stallman", "FSF", 168, 10);
				libros.anadirLibro(libro3);

				Libro libro4 = new Libro(1725, "Java", "Juan Garcia", "Programación", 245, 9);
				libros.anadirLibro(libro4);

				break;

			case 6:
				Libros libros6 = new Libros();
				for (Libro libro : libros6.verCatalogo()) {
					System.out.println(libro.toString());
				}

				Libro libroEditado = libros6.verCatalogo().get(2);
				libroEditado.setCopias(5);
				libros6.actualizarCopias(libroEditado);
				

				for (Libro libro : libros6.verCatalogo()) {
					System.out.println(libro.toString());
				}
				
				Libro libroBorrado = libros6.verCatalogo().get(0);
				libros6.borrar(libroBorrado);
				
				for (Libro libro : libros6.verCatalogo()) {
					System.out.println(libro.toString());
				}
				
				for (String campo : libros6.getCamposLibro()) {
					System.out.println("\nCampo: " + campo);
				}
				
				libros6.obtenerLibro(1725);
				break;

			case 8:
				System.out.println("Adiós.");
				break;
			default:
				System.out.println("Opción no válida.");
				break;
			}
		} while (opc != 8);
	}
}