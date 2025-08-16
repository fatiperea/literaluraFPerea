package com.perea.literalura.repository;

import com.perea.literalura.model.Autor;
import com.perea.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    List<Autor> findByFechaNacimientoLessThanEqualAndFechaMuerteGreaterThanEqualOrFechaMuerteIsNull(int añoNacimiento, int añoMuerte);
}

/* agregar en autor service
}

 */