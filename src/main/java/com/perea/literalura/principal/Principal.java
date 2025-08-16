package com.perea.literalura.principal;

import com.perea.literalura.exception.LibroDuplicadoException;
import com.perea.literalura.exception.LibroNoEncontradoException;
import com.perea.literalura.model.*;
import com.perea.literalura.repository.AutorRepository;
import com.perea.literalura.repository.LibroRepository;
import com.perea.literalura.service.ConsumoAPI;
import com.perea.literalura.service.ConvertirDatos;

import java.util.*;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";

    private ConsumoAPI consumoAPI = new ConsumoAPI();

    private ConvertirDatos conversor = new ConvertirDatos();

    private  Scanner teclado = new Scanner(System.in);

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
                    4 - Listar libros por idioma
                    5 - Listar autores vivos en determinado año
                    6 - Top 5
                    
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
                case 4:
                    listarLibrosPorIdioma();
                    break;
                case 5:
                    listarAutoresVivosPorAnio();
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

            String titulo = teclado.nextLine().trim().toLowerCase();

            if (titulo.isEmpty() || titulo.length() < 3) {
                System.out.println("Título inválido. Intente nuevamente.");
            } else {
                return titulo;
            }
        }
    }

    private void controlDuplicado(String buscado){

        Optional<Libro> libroExistente = repositorio.findByTituloContainingIgnoreCase(buscado);
        if (libroExistente.isPresent()) {
            System.out.println("verificando duplicado");
            throw new LibroDuplicadoException("Libro existente: " + libroExistente.get());

        }
    }

    private DatosLibro getDatosLibro() throws LibroDuplicadoException {

        System.out.println("Ingrese el título del libro:");
        String tituloLibro = solicitarTitulo();

            controlDuplicado(tituloLibro);

        var json = consumoAPI.obtenerDatos(URL_BASE+"?search=" + tituloLibro.replace(" ","+"));
        Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        try {
            controlDeserializacion(datosBusqueda);
        } catch (LibroNoEncontradoException e) {
            System.out.println( e.getMessage());
            return null;
        }

        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        try {
            libroVacio(libroBuscado);
        } catch (LibroDuplicadoException | LibroNoEncontradoException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return libroBuscado.get();
    }

    private void controlDeserializacion(Datos datos){

        if (datos.resultados() == null || datos.resultados().isEmpty()) {
            System.out.println("No se lograron resultados");
        }

    }

    private void libroVacio(Optional<DatosLibro> libroBuscado){

        if (libroBuscado.isEmpty()) {
            throw new LibroNoEncontradoException("El título ingresado no coincide con ningún libro de la API.");
        }
    }

    private void buscarLibroPorTitulo() {

        /*try {

            System.out.println("verificando duplicado1");

            DatosLibro datosLibro=getDatosLibro();
            if (datosLibro == null) {
                return;
            }
            Libro libroEncontrado= new Libro(datosLibro);
            System.out.println("Datos del libro: " + libroEncontrado);
            repositorio.save(libroEncontrado);

        }catch (LibroDuplicadoException | LibroNoEncontradoException e) {
        System.out.println(e.getMessage());
        return;*/

            try {
                DatosLibro datos = getDatosLibro(); // ← puede lanzar excepción
                Libro libro = new Libro(datos);
                repositorio.save(libro);
                System.out.println("📘 Libro guardado: " + libro.getTitulo());
            } catch (LibroDuplicadoException e) {
                System.out.println(e.getMessage());
                return; // ← esto es clave: detiene el método y vuelve al menú
            } catch (LibroNoEncontradoException e) {
                System.out.println("❌ " + e.getMessage());
                return;
            }

    }

    private void listarLibros() {

        System.out.println("Lista de libros buscados: ");

        List<Libro> libros=repositorio.findAll();

        libros.forEach(System.out::println);

    }

    private void listarAutores() {

        System.out.println("Lista de autores visitados: ");

        List<Autor> autores = autorRepositorio.findAll();
        autores.forEach(System.out::println);

    }

    private void listarLibrosPorIdioma() {

        System.out.println("Ingrese el idioma para filtrar libros:");

            String idioma = teclado.nextLine().trim();

            List<Libro> libros = repositorio.findByIdiomaIgnoreCase(idioma);

            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros en el idioma: " + idioma);
                return;
            }

            System.out.println("Libros en idioma '" + idioma + "':");
            libros.stream()
                    .map(libro -> "• " + libro.getTitulo() + " — " + libro.getAutor().getNombre())
                    .forEach(System.out::println);

    }

    private void listarAutoresVivosPorAnio() {

        System.out.println("Ingrese el año para buscar autores vivos:");
        String entrada = teclado.nextLine().trim();

        try {
            int anio = Integer.parseInt(entrada);
            List<Autor> autores=autorRepositorio.listarAutoresVivosPorAnio(anio);//autorRepositorio.findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqualOrFallecimientoIsNull(anio)

            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + anio);
                return;
            }

            System.out.println("Autores vivos en el año " + anio + ":");
            autores.stream()
                    .map(a -> "• " + a.getNombre() + " (" + a.getNacimiento() + " - " +
                            (a.getFallecimiento() != null ? a.getFallecimiento() : "actualidad") + ")")
                    .forEach(System.out::println);

        } catch (NumberFormatException e) {
            System.out.println("Año inválido. Ingrese un número entero.");
        }

    }

}
