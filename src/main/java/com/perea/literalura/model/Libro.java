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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro(){}

    public Libro(DatosLibro libro) {

        this.titulo = libro.titulo();
        if (libro.idiomas() != null && !libro.idiomas().isEmpty()) {
            this.idiomas = libro.idiomas().get(0);
        } else {
            this.idiomas = "desconocido";
        }
        this.descargas = libro.numeroDeDescargas();

        if (libro.autor() != null && !libro.autor().isEmpty()) {
            DatosAutor datosAutor = libro.autor().get(0);

            this.autor = new Autor(datosAutor);
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
        return
                "titulo='" + titulo + '\'' +
                ", idiomas='" + idiomas + '\'' +
                ", descargas=" + descargas +
                ", autor=" + autor ;
    }
}
