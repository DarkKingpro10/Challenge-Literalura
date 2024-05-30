package models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

public record LibrosData(@JsonAlias("count") Long contador, @JsonAlias("next") String pag_siguiente,
		@JsonAlias("previous") String pag_anterior, @JsonAlias("results") List<LibroResponse> libro) {
}