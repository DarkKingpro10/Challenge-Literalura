package com.je.literalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.je.literalura.models.Autor;

import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombreAutor")
    Optional<Autor> findByAutor(@Param("nombreAutor") String nombreAutor);
}
