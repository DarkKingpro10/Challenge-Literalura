package com.je.literalura.dto;

import com.je.literalura.models.Language;

public record LibroDTO(
        String titulo,
        Language lenguaje,
        Long contador_descargas
){}