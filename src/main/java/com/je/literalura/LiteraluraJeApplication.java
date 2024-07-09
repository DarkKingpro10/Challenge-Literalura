package com.je.literalura;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.je.literalura.api.API;
import com.je.literalura.dto.AutorDTO;
import com.je.literalura.models.Autor;
import com.je.literalura.models.Language;
import com.je.literalura.models.Libro;
import com.je.literalura.models.LibrosData;
import com.je.literalura.repository.AutorRepository;
import com.je.literalura.repository.LibroRepository;
import com.je.literalura.services.Converter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootApplication
public class LiteraluraJeApplication implements CommandLineRunner{

	@Autowired
	private LibroRepository libroRepositorio;
	@Autowired
	private AutorRepository autorRepositorio;

	private final Scanner scanner = new Scanner(System.in);
	private final String url = "https://gutendex.com/books/";
	private final String content = "{\"count\":0, \"next\":null, \"previous\":null, \"results\":[]}";
	private Converter converter = new Converter();
	private API api = new API();
	private Autor autor = new Autor();
	private List<Libro> libroList = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraJeApplication.class, args);
	}

	public void showMenu() {
		System.out.println("\n\n");
		System.out.println("""

				1 - Listar Libros
				2 - Guardar Libro
				3 - Listar Autores
				4 - Listar autores vivos en determinado año
				5 - Buscar libro por idioma
				6 - Salir
				 """);
		System.out.println("\n");
	}

	public void run(String... args) throws Exception {
		iniciarAPP();
	}
	
	public void iniciarAPP(){
        int opcion = -1;
        while (opcion != 0){
        	showMenu();
            try{
                opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion){
                    case 1:
                    mostrarLibrosEnBaseDatos();
                       break;
                    case 2:
                       obtenerDatosLibros();
                       break;
                    case 3:
                       mostrarAutoresRegistrados();
                       break;
                    case 4:
                       mostrarAutoresVivosEnAño();
                       break;
                    case 5:
                       buscarLibrosLenguaje();
                       break;
                    case 6:
                       System.out.println("Cerrando aplicacion...");
                       break;
                    default:
                       System.out.println("Opcion invalida");
                       break;
               }
            }catch(InputMismatchException ex){
               System.out.println("Entrada invalida. Por favor ingrese un numero del menu ");
               scanner.nextLine();//consumir la entrada valida
            }
        }
    }
	
	private void obtenerDatosLibros() {
        System.out.println("\n\n");
        System.out.println("Escriba el nombre del libro que desea buscar...");
        String nombreLibro = scanner.nextLine().trim();
        String formated = nombreLibro.replace(" ", "+").toLowerCase();

        if (nombreLibro.isEmpty()) {
            System.out.println("El nombre del libro no puede estar vacío");
            return;
        }

        try {

            var json = api.obtenerDatos(url + "?search=" + formated);
            if (json.contains(content)) {
                System.out.println("No se encontraron resultados...");

            } else {

                converter.obtenerDatos(json, LibrosData.class).libro().stream()
                        .findFirst()
                        .ifPresentOrElse(datos -> {
                            String titulo = datos.titulo();
                            String nombreAutor = datos.autor().get(0).nombre();
                            Optional<Libro> libroExiste = libroRepositorio.findByTituloAndAutor(titulo, nombreAutor);

                            if (libroExiste.isPresent()) {
                                System.out.println("El libro ya existe con ese autor.");
                                return;
                            }

                            autor = autorRepositorio.findByAutor(nombreAutor)
                                    .orElseGet(() -> autorRepositorio.save(new Autor(datos.autor().get(0))));

                            try {
                                Libro libro = new Libro(datos);
                                libro.setAutor(autor);

                                libroRepositorio.save(libro);
                                autor.getLibros().add(libro);
                                autorRepositorio.save(autor);
                                System.out.println("\n\n");
                                System.out.println("Libro guardado exitosamente...\n" +
                                        "----------------------LIBRO----------------------" + '\n' +
                                        "Titulo              : " + cortarTitulo(libro.getTitulo()) + '\n' +
                                        "Autor               : " + autor.getNombre() + '\n' +
                                        "Idioma              : " + libro.getLenguaje() + '\n' +
                                        "Numero de descargas : " + libro.getContador_descargas());
                            } catch (Exception ex) {
                                System.out.println("Error al guardar el libro: " + ex.getMessage());
                            }
                        }, () -> {
                            System.out.println("No se encontraron registros");
                        });
            }

        } catch (Exception ex) {
            System.out.println("Error al obtener datos: " + ex.getMessage());
        }

    }

    private void mostrarLibrosEnBaseDatos() {
        libroList = libroRepositorio.findAll();
        libroList.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(libro -> {
                    Autor autorLibro = libro.getAutor();
                    System.out.println(
                            "==============================================================================" + '\n' +
                            "Titulo              : " + cortarTitulo(libro.getTitulo()) + '\n' +
                            "Autor               : " + (autorLibro != null ? autorLibro.getNombre() : "Desconocido") + '\n' +
                            "Idioma              : " + libro.getLenguaje() + '\n' +
                            "Numero de descargas : " + libro.getContador_descargas() + '\n');
                });
    }

    public List<AutorDTO> mostrarAutoresRegistrados() {
        List<Autor> autores = autorRepositorio.findAll();
        List<AutorDTO> autorDTOS = new ArrayList<>();
        for(Autor autor : autores){
            System.out.println(
                    "==============================================================================" + '\n' +
                            "Autor               : " + autor.getNombre() + '\n' +
                            "Fecha nacimiento    : " + autor.getAno_nacimiento() + '\n' +
                            "Fecha fallecimiento : " + autor.getAno_muerte() + '\n');
            System.out.println("Libros: ");
            for(Libro libro : autor.getLibros()){
                System.out.println(" - " + cortarTitulo(libro.getTitulo()));
            }
            AutorDTO autorDTO = new AutorDTO(autor.getNombre(), autor.getAno_nacimiento(), autor.getAno_muerte());
            autorDTOS.add(autorDTO);
        }
        return autorDTOS;
    }

    private void mostrarAutoresVivosEnAño() {

        System.out.println("\n");
        System.out.println("Ingrese al año a consultar...");
        int anoConsulta = scanner.nextInt();
        scanner.nextLine();

        List<Autor> autores = autorRepositorio.findAll();
        boolean autoresEncontrados = false;

        for(Autor autor : autores){
            Integer anoNacimiento = autor.getAno_nacimiento();
            Integer anoMuerte = autor.getAno_muerte();

            if(anoNacimiento != null && anoConsulta >= anoNacimiento && (anoMuerte == null || anoConsulta <= anoMuerte)){
                autoresEncontrados = true;
                System.out.println("==============================================================================" + '\n' +
                        "Nombre              : " + autor.getNombre() + '\n' +
                        "Fecha nacimiento    : " + autor.getAno_nacimiento() + '\n' +
                        "Fecha fallecimiento : " + (anoMuerte != null ? anoMuerte : "N/A"));

                if(autor.getLibros() != null && !autor.getLibros().isEmpty()){
                    System.out.println("Libros: ");
                    for(Libro libro : autor.getLibros()){
                        System.out.println(" - Titulo: " + cortarTitulo(libro.getTitulo()));
                    }
                }else{
                    System.out.println("Autor no tienen libros registrados");
                }                
            }
        }
        if(!autoresEncontrados) System.out.println("Perido no registra datos");
    }

    private void buscarLibrosLenguaje() {
        System.out.println("Ingrese el lenguaje que desea buscar");
        String codigoLenguaje = scanner.nextLine().trim().toLowerCase();
        if(codigoLenguaje.isEmpty()) System.out.println("Lenguaje no puede estar vacio");

        try{
            Language lenguaje = Language.fromCode(codigoLenguaje);
            List<Libro> libros = libroRepositorio.findByLenguaje(lenguaje);
            if(libros.isEmpty()){
                System.out.println("No se encontaron libros en el lenguaje: " + lenguaje);
            }else{
                libros.forEach(libro -> {
                    Autor autor = libro.getAutor();
                    System.out.println(
                            "==============================================================================" + '\n' +
                                    "Titulo           : " + libro.getTitulo() + '\n' +
                                    "Autor            : " + (autor != null ? autor.getNombre() : "Desconocido" ) + '\n' +
                                    "Idioma           : " + libro.getLenguaje() + '\n' +
                                    "Numero descargas : " + libro.getContador_descargas());
                });
            }

        }catch(IllegalArgumentException ex){
            System.out.println("Codigo lenguaje no valido: " + codigoLenguaje);
        }
    }

    private String cortarTitulo(String titulo){
        String[] partes = titulo.split(":");
        String tituloCorto = partes[0].trim();
        return tituloCorto;
    }
}
