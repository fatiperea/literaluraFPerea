package com.perea.literalura.service;

import com.perea.literalura.model.Autor;
import com.perea.literalura.model.DatosAutor;
import com.perea.literalura.model.DatosLibro;
import com.perea.literalura.model.Libro;
import com.perea.literalura.repository.AutorRepository;
import com.perea.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> buscarLibrosPorIdioma(String idioma) {
        return libroRepository.findByIdiomaIgnoreCase(idioma);
    }

}





