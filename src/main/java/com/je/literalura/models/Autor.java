package com.je.literalura.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private Integer ano_nacimiento;
    private Integer ano_muerte;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

	public Autor() {
	}

	public Autor(AutorRecord autorRecord) {
		// this.id = autorRecord.id();
		this.nombre = autorRecord.nombre();
		this.ano_nacimiento = autorRecord.ano_nacimiento();
		this.ano_muerte = autorRecord.ano_muerte();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getAno_nacimiento() {
		return ano_nacimiento;
	}

	public void setAno_nacimiento(Integer ano_nacimiento) {
		this.ano_nacimiento = ano_nacimiento;
	}

	public Integer getAno_muerte() {
		return ano_muerte;
	}

	public void setAno_muerte(Integer ano_muerte) {
		this.ano_muerte = ano_muerte;
	}

	public List<Libro> getLibros() {
		return libros;
	}

	public void setLibros(List<Libro> libros) {
		this.libros = libros;
	}

	@Override
	public String toString() {
		return "Autor{" + "id=" + id + ", nombre='" + nombre + '\'' + ", ano_nacimiento=" + ano_nacimiento
				+ ", ano_muerte=" + ano_muerte + ", libros=" + libros + '}';
	}
}
