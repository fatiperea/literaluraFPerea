package com.perea.literalura.principal;

import com.perea.literalura.model.*;
import com.perea.literalura.repository.AutorRepository;
import com.perea.literalura.repository.LibroRepository;
import com.perea.literalura.service.ConsumoAPI;
import com.perea.literalura.service.ConvertirDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";

    private ConsumoAPI consumoAPI = new ConsumoAPI();

    private ConvertirDatos conversor = new ConvertirDatos();

    private  Scanner teclado = new Scanner(System.in);

    //private List<DatosLibro> datosLibro= new ArrayList<>();

    private DatosLibro libro;

    //private List<DatosAutor> datosAutor= new ArrayList<>();

    //private DatosAutor autor;

    private LibroRepository repositorio;

    private AutorRepository autorRepositorio;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {

        this.repositorio=libroRepository;
        this.autorRepositorio=autorRepository;
    }



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

    private String solicitarTitulo() {
        while (true) {

            String titulo = teclado.nextLine().trim();

            if (titulo.isEmpty() || titulo.length() < 3) {
                System.out.println("Título inválido. Intente nuevamente.");
            } else {
                return titulo;
            }
        }
    }

    private void controlDuplicado(String buscado){

        System.out.println("esta duplicado?"+buscado);

        Optional<Libro> libroExistente = repositorio.findByTitulo(buscado);
        if (libroExistente.isPresent()) {
            System.out.println("Libro existente: " + libroExistente.get().getTitulo());
            return;
        }
    }

    private DatosLibro getDatosLibro(){

        System.out.println("Ingrese el título del libro:");
        String tituloLibro =solicitarTitulo();

        //var tituloLibro = teclado.nextLine().trim();

        //if (tituloLibro.isEmpty() || tituloLibro.length() < 3) {
          //  System.out.println("El título ingresado es inválido.");
            //getDatosLibro();

        //}else {

            var json = consumoAPI.obtenerDatos(URL_BASE+"?search=" + tituloLibro.replace(" ","+"));
            Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);

            controlDeserializacion(datosBusqueda);

        controlDuplicado(tituloLibro);

            /*if (datosBusqueda.resultados() == null || datosBusqueda.resultados().isEmpty()) {
                System.out.println("No se lograron resultados");
                //exit();
            }/*else if(repositorio.findByTitulo(tituloLibro).isPresent()){

                DatosLibro datosLibro= datosBusqueda.resultados().get(0);
                System.out.println("Libro Encontrado(existente)!"+datosLibro);

            }else {*/

            Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                    .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                    .findFirst();

            libroVacio(libroBuscado);

            /*if(libroBuscado.isPresent()){
                System.out.println("Libro Encontrado!");
                libro= libroBuscado.get();

            }else {
                System.out.println("Libro no encontrado!");
            }*/

       // }
       // }
        libro= libroBuscado.get();
        /*Libro libroEncontrado= new Libro(libro);
        System.out.println("Datos del libro: " + libroEncontrado);
        repositorio.save(libroEncontrado);*/

        return libro;
    }

    private void controlDeserializacion(Datos datos){

        if (datos.resultados() == null || datos.resultados().isEmpty()) {
            System.out.println("No se lograron resultados");
            return;
            //exit();
        }

    }

    private void libroVacio(Optional<DatosLibro> libroBuscado){

        if (libroBuscado.isEmpty()) {
            System.out.println("Resultados erróneos o no coincidentes");
            return;
        }
    }

    private void buscarLibroPorTitulo() {

        //String busquedaLibro= libro.titulo();
        DatosLibro datosLibro=getDatosLibro();
        Libro libroEncontrado= new Libro(datosLibro);
        System.out.println("Datos del libro: " + libroEncontrado);
        repositorio.save(libroEncontrado);

    }

    private void listarLibros() {

        System.out.println("Lista de libros buscados: ");

        List<Libro> libros=repositorio.findAll();

        libros.forEach(System.out::println);

    }

    private void listarAutores() {

        /*List<DatosAutor> autores = datosLibro.stream()
                .map(libro -> libro.autor().isEmpty() ? null : libro.autor().get(0))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());*/

        System.out.println("Lista de autores visitados: ");

        List<Autor> autores = autorRepositorio.findAll();
        autores.forEach(System.out::println);

    }
}
