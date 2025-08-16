package com.perea.literalura.repository;

import com.perea.literalura.model.Autor;
import com.perea.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    //List<Autor> findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqualOrFallecimientoIsNull(int nacimiento, int fallecimiento);


        @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :año AND (a.fechaMuerte IS NULL OR a.fechaMuerte >= :año)")
        List<Autor> listarAutoresVivosPorAnio(@Param("año") int anio);


}

/* agregar en autor service
}

 */