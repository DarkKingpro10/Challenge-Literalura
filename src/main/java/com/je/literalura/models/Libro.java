package com.je.literalura.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "autor_id", referencedColumnName = "id", nullable = false)
    private Autor autor;

    private List<String> temas;

    @Enumerated(EnumType.STRING)
    private Language lenguaje;

    private String derechos_autor;
    private String tipo_de_medio;
    private Long contador_descargas;

    public Libro() {
    }

    public Libro(LibroResponse libroResults) {
       // this.id = libroResults.id();
        this.titulo = libroResults.titulo();
        this.temas = libroResults.temas();
        this.lenguaje = libroResults.lenguaje().isEmpty() ? null : Language.fromCode(libroResults.lenguaje().get(0).toUpperCase());
        this.derechos_autor = libroResults.derechos_autor();
        this.tipo_de_medio = libroResults.tipo_de_medio();
        this.contador_descargas = libroResults.contador_descargas();
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

    public List<String> getTemas() {
        return temas;
    }

    public void setTemas(List<String> temas) {
        this.temas = temas;
    }

    public Language getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(Language lenguaje) {
        this.lenguaje = lenguaje;
    }

    public String getDerechos_autor() {
        return derechos_autor;
    }

    public void setDerechos_autor(String derechos_autor) {
        this.derechos_autor = derechos_autor;
    }

    public String getTipo_de_medio() {
        return tipo_de_medio;
    }

    public void setTipo_de_medio(String tipo_de_medio) {
        this.tipo_de_medio = tipo_de_medio;
    }

    public Long getContador_descargas() {
        return contador_descargas;
    }

    public void setContador_descargas(Long contador_descargas) {
        this.contador_descargas = contador_descargas;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", temas=" + temas +
                ", lenguaje=" + lenguaje +
                ", derechos_autor='" + derechos_autor + '\'' +
                ", tipo_de_medio='" + tipo_de_medio + '\'' +
                ", contador_descargas=" + contador_descargas +
                '}';
    }
}