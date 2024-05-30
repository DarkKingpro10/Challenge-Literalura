package dto;

import models.Language;

public record LibroDTO(
        String titulo,
        Language lenguaje,
        Long contador_descargas
){}