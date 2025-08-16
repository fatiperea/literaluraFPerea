package com.perea.literalura.service;

import com.perea.literalura.model.Autor;
import com.perea.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    /*public List<Autor> buscarAutoresVivosEn(int año) {
        return autorRepository.findByFechaNacimientoLessThanEqualAndFechaMuerteGreaterThanEqualOrFechaMuerteIsNull(año, año);

    }*/





}
