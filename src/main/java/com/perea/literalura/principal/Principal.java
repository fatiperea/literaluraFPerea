package com.perea.literalura.principal;

import com.perea.literalura.model.Datos;
import com.perea.literalura.model.DatosLibro;
import com.perea.literalura.service.ConsumoAPI;
import com.perea.literalura.service.ConvertirDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";

    private ConsumoAPI consumoAPI = new ConsumoAPI();

    private ConvertirDatos conversor = new ConvertirDatos();

    private Scanner teclado = new Scanner(System.in);

    private List<DatosLibro> datosLibro= new ArrayList<>();

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
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibros();
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

    private DatosLibro getDatosLibro(){

        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE+"?search=" + tituloLibro.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if(libroBuscado.isPresent()){
            System.out.println("Libro Encontrado ");
            System.out.println(libroBuscado.get());
        }else {
            System.out.println("Libro no encontrado");
        }
        return libroBuscado.get();
    }

    private void buscarLibroPorTitulo() {
        DatosLibro datos = getDatosLibro();
        System.out.println("Libro Encontrado ");
        System.out.println(datos);
        datosLibro.add(datos);
    }

    private void listarLibros() {

        System.out.println("Lista de libros buscados: ");

        //repositorioLibro.findAll().forEach(System.out::println);

        datosLibro.forEach(System.out::println);
    }
}
