package com.perea.literalura.principal;

import com.perea.literalura.service.ConsumoAPI;
import com.perea.literalura.service.ConvertirDatos;

import java.util.Scanner;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";

    private ConsumoAPI consumoAPI = new ConsumoAPI();

    private ConvertirDatos conversor = new ConvertirDatos();

    private Scanner teclado = new Scanner(System.in);

    public void menu() {
        var opcion = -1;
        System.out.println("Bienvenido a Literalura!");
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por título
                    2 - Listar libros
                    3 - Listar autores
                    4 - Listar autores vivos en determinado año
                    5 - Listar libros por idioma
                    5 - Top 5
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    //buscarLibroPorTitulo();
                    break;
                case 2:
                    //listarLibros();
                    break;
                case 3:
                    //listarAutores();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

}
