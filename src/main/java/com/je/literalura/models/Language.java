package com.je.literalura.models;

public enum Language {
	Frances("fr"),
    Finlandes("fi"),
    Ingles("en"),
    Español("es"),
    Portugues("pt"),
    Ruso("ru"),
    Aleman("de"),
    Japones("ja"),
    Turco("tr"),
    Chino("zh");

    private String codigo;

    Language(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static Language isValid(String codigo) {
        for (Language lenguaje : Language.values()) {
            if (lenguaje.getCodigo().equalsIgnoreCase(codigo)) {
                return lenguaje;
            }
        }
        throw new IllegalArgumentException("Categoria no encontrada: " + codigo);
    }

    public static Language fromCode(String codigo) {
        for (Language lenguaje : Language.values()) {
            if (lenguaje.getCodigo().equalsIgnoreCase(codigo)) {
                return lenguaje;
            }
        }
        throw new IllegalArgumentException("Lenguaje no valido: " + codigo);
    }
}
