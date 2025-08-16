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
    private String idioma;
    private Double descargas;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro(){}

    public Libro(DatosLibro libro) {

        this.titulo = libro.titulo();

        if (libro.idioma() != null && !libro.idioma().isEmpty()) {
            this.idioma = libro.idioma().get(0);
        } else {
            this.idioma = "desconocido";
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

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
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
                ", idiomas='" + idioma + '\'' +
                ", descargas=" + descargas +
                ", autor=" + autor ;
    }
}
