package com.perea.literalura.repository;

import com.perea.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTituloContainingIgnoreCase(String titulo);

    List<Libro> findByIdiomaIgnoreCase(String idioma);

}
