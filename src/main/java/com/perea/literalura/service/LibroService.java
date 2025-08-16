package com.perea.literalura.service;

import com.perea.literalura.model.Autor;
import com.perea.literalura.model.DatosAutor;
import com.perea.literalura.model.DatosLibro;
import com.perea.literalura.model.Libro;
import com.perea.literalura.repository.AutorRepository;
import com.perea.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibroService {

        @Autowired
        private AutorRepository autorRepository;

        @Autowired
        private LibroRepository libroRepository;

        public void controlDuplicados(DatosLibro datosLibro) {
            String titulo = datosLibro.titulo();

            if (libroRepository.findByTitulo(titulo).isPresent()) {
                System.out.println("Libro existente: " + titulo);
                return;
            }else {

                Libro libro = new Libro(datosLibro);
                libroRepository.save(libro);

            }

            //Optional<Autor> autorExistente = autorRepository.findByNombre(autorNuevo.getNombre());

            // Si existe, lo usamos en lugar del nuevo
            //autorExistente.ifPresent(libro::setAutor);

            // Guardar solo el libro

            //System.out.println("✅ Libro guardado: " + libro.getTitulo());
        }




    /*public void guardarLibro(DatosLibro datosLibro) {
            Libro libro = new Libro();

            if (datosLibro.autor() != null && !datosLibro.autor().isEmpty()) {
                DatosAutor datosAutor = datosLibro.autor().get(0);

                Autor autor = crearAutor(datosAutor);
                libro.setAutor(autor);
            }

            libro.setTitulo(datosLibro.titulo());
            libro.setDescargas(datosLibro.numeroDeDescargas());
            libro.setIdiomas(datosLibro.idiomas().get(0));

            libroRepository.save(libro);
        }
*/
        private Autor crearAutor(DatosAutor datosAutor) {
            Optional<Autor> autorExistente = autorRepository.findByNombre(datosAutor.nombre());

            return autorExistente.orElseGet(() -> new Autor(datosAutor));
        }
    }



