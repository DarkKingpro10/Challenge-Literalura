package repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import models.Language;
import models.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("SELECT l FROM libros l JOIN l.autor a WHERE l.titulo = :titulo AND a.nombre = :nombreAutor")
    Optional<Libro> findByTituloAndAutor(@Param("titulo") String titulo, @Param("nombreAutor") String nombreAutor);

    List<Libro> findByLenguaje(Language lenguaje);
}
