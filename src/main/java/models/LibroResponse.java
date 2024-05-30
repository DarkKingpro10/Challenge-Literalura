package models;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LibroResponse(
        //@JsonAlias("id") Long id,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<AutorRecord> autor,
        @JsonAlias("subjects") List<String> temas,
        @JsonAlias("languages") List<String> lenguaje,
        @JsonAlias("copyright") String derechos_autor,
        @JsonAlias("media_type") String tipo_de_medio,
        @JsonAlias("download_count") Long contador_descargas

){
    @Override
    public List<AutorRecord> autor() {
        return autor == null ? Collections.emptyList() : autor;
    }

    @Override
    public List<String> temas() {
        return temas == null ? Collections.emptyList() : temas;
    }

    @Override
    public List<String> lenguaje() {
        return lenguaje == null ? Collections.emptyList() : lenguaje;
    }
}