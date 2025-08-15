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

    private Scanner teclado = new Scanner(System.in);

    private List<DatosLibro> datosLibro= new ArrayList<>();

    private DatosLibro libro;

    private List<DatosAutor> datosAutor= new ArrayList<>();

    private DatosAutor autor;

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

    private DatosLibro getDatosLibro(){

        System.out.println("Ingrese el título del libro:");
        var tituloLibro = teclado.nextLine().trim();

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
                System.out.println("Datos del libro: " + libroBuscado);
                libro= libroBuscado.get();

            }else {
                System.out.println("Libro no encontrado!");
            }
        }
        Libro libroEncontrado= new Libro(libro);
        repositorio.save(libroEncontrado);
        return libro;
    }

    private void buscarLibroPorTitulo() {

        getDatosLibro();

    }

    private void listarLibros() {

        System.out.println("Lista de libros buscados: ");

        List<Libro> libros=repositorio.findAll();

        libros.forEach(System.out::println);

    }

    private void listarAutores() {

        List<DatosAutor> autores = datosLibro.stream()
                .map(libro -> libro.autor().isEmpty() ? null : libro.autor().get(0))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        System.out.println("Lista de autores visitados: ");

        autores.forEach(System.out::println);

        autores.forEach(datosAutor -> {
            System.out.println(datosAutor);

            // Verificamos si el autor ya existe en la base de datos

            Optional<Autor> autorExistente = autorRepositorio.findByNombre(datosAutor.nombre());

            if (autorExistente.isEmpty()) {
                Autor nuevoAutor = new Autor();
                nuevoAutor.setNombre(datosAutor.nombre());
                autorRepositorio.save(nuevoAutor);
                System.out.println("Autor guardado: " + nuevoAutor.getNombre());
            } else {
                System.out.println("Autor ya existe: " + autorExistente.get().getNombre());
            }
        });


    }
}
