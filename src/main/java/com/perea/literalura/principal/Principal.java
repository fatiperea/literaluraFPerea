package com.perea.literalura.principal;

import com.perea.literalura.model.Datos;
import com.perea.literalura.model.DatosAutor;
import com.perea.literalura.model.DatosLibro;
import com.perea.literalura.service.ConsumoAPI;
import com.perea.literalura.service.ConvertirDatos;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.boot.SpringApplication.exit;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";

    private ConsumoAPI consumoAPI = new ConsumoAPI();

    private ConvertirDatos conversor = new ConvertirDatos();

    private Scanner teclado = new Scanner(System.in);

    private List<DatosLibro> datosLibro= new ArrayList<>();

    private DatosLibro libro;

    private List<DatosAutor> datosAutor= new ArrayList<>();

    private DatosAutor autor;

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
                    listarAutores();
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

        System.out.println("Ingrese el título del libro:");
        var tituloLibro = teclado.nextLine();

        if (tituloLibro.isEmpty() || tituloLibro.length() < 3) {
            System.out.println("El título ingresado es inválido.");
            getDatosLibro();
        }else {

            var json = consumoAPI.obtenerDatos(URL_BASE+"?search=" + tituloLibro.replace(" ","+"));
            var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

            if (datosBusqueda.resultados() == null || datosBusqueda.resultados().isEmpty()) {
                System.out.println("No se lograron resultados");
                //exit();
            }

            Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                    .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                    .findFirst();

            if(libroBuscado.isPresent() && libroBuscado != null){
                System.out.println("Libro Encontrado!");
                libro=libroBuscado.get();
                datosLibro.add(libro);
                System.out.println("Datos del libro: " + libro);


            }else {
                System.out.println("Libro no encontrado!");
            }

        }

        /*var json = consumoAPI.obtenerDatos(URL_BASE+"?search=" + tituloLibro.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        if (datosBusqueda.resultados() == null || datosBusqueda.resultados().isEmpty()) {
            return null;
        }

        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if(libroBuscado.isPresent()){
            System.out.println("Libro Encontrado!");
            libro=libroBuscado.get();

        }else {
            System.out.println("Libro no encontrado!");
        }*/
        return libro;
    }

    private void buscarLibroPorTitulo() {

        getDatosLibro();
        /*DatosLibro datos = getDatosLibro();
        datosLibro.add(libro);
        if (libro != null){
            System.out.println("Datos del libro: " + libro);
            datosLibro.add(libro);
        }/*else {
            System.out.println("Libro no encontrado");
        }*/
        //System.out.println("Datos del libro: ");
        //System.out.println(datos);

        /*
        if (libro != null){
            System.out.println("Libro buscado: " + libro);
            datosLibro.add(datos);
        }else {

            System.out.println("Libro no encontrado");
        }
         */
        //datosLibro.add(datos);
    }

    private void listarLibros() {

        System.out.println("Lista de libros buscados: ");

        //repositorioLibro.findAll().forEach(System.out::println);

        datosLibro.forEach(System.out::println);
    }

    private void listarAutores() {

        List<DatosAutor> autores = datosLibro.stream()
                .map(libro -> libro.autor().isEmpty() ? null : libro.autor().get(0))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("Lista de autores visitados: ");

        autores.forEach(System.out::println);


    }
}
