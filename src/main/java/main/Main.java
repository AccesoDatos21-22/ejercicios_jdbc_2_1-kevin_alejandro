package main;

import java.util.Scanner;

import dao.Cafes;
import modelo.AccesoDatosException;

public class Main {

	private static Scanner sn = new Scanner(System.in);

	public static void main(String[] args) throws AccesoDatosException {
		int opc = 0;

		do {
			System.out.println("\n ***** Seleccione una Opci�n *****");
			System.out.println("1. Buscar caf�s por Proveedor");
			System.out.println("8. Salir.");

			opc = sn.nextInt();

			switch (opc) {
			case 1:
				System.out.println("Uno");
				try {
					Cafes cafes = new Cafes();
					//cafes.insertar("Cafetito", 150, 1.0f, 100, 1000);
					//cafes.insertar("Cafe tacilla", 150, 2.0f, 100, 1000);
					//cafes.verTabla();
					// cafes.buscar("tacilla");
					cafes.cafesPorProveedor(150);
					// cafes.borrar("Cafe tacilla");
					// cafes.verTabla();

				} catch (AccesoDatosException e) {
					e.printStackTrace();
				}/**/
				break;

			case 8:
				System.out.println("Adi�s.");
				break;
			default:
				System.out.println("Opci�n no v�lida.");
				break;
			}
		} while (opc != 8);
	}
}