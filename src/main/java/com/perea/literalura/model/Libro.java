package com.perea.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String idiomas;
    private Double descargas;

    @ManyToOne
    private Autor autor;

    public Libro(){}

    public Libro(DatosLibro datosLibro) {

        this.titulo = datosLibro.titulo();
        if (datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty()) {
            this.idiomas = datosLibro.idiomas().get(0);
        } else {
            this.idiomas = "desconocido";
        }
        this.descargas = datosLibro.numeroDeDescargas();

        if (datosLibro.autor() != null && !datosLibro.autor().isEmpty()) {
            DatosAutor datosAutor = datosLibro.autor().get(0);
            this.autor = new Autor(datosAutor); // suponiendo que Autor tiene un constructor similar
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Double getDescargas() {
        return descargas;
    }

    public void setDescargas(Double descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "titulo='" + titulo + '\'' +
                ", idiomas='" + idiomas + '\'' +
                ", descargas=" + descargas +
                ", autor=" + autor +
                '}';
    }
}
