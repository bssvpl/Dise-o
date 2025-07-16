package com.proy.diseno.Service;

import com.proy.diseno.Model.Curso;
import com.proy.diseno.Repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CursoService implements CommandLineRunner {
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen cursos
        if (cursoRepository.count() == 0) {
            inicializarCursos();
        }
    }
    
    private void inicializarCursos() {
        List<Curso> cursos = Arrays.asList(
            new Curso(
                "Literatura Infantil", 
                "Explora el maravilloso mundo de la literatura infantil. Aprende sobre cuentos clásicos, fábulas y cómo fomentar el amor por la lectura en los niños.",
                "#FF6B6B", 
                "fas fa-book-open", 
                "Literatura", 
                "Principiante", 
                20
            ),
            new Curso(
                "Lenguaje y Comunicación", 
                "Mejora tus habilidades de comunicación oral y escrita. Aprende técnicas de expresión, redacción y presentación efectiva.",
                "#4ECDC4", 
                "fas fa-comments", 
                "Lenguaje", 
                "Intermedio", 
                25
            ),
            new Curso(
                "Historia Universal", 
                "Viaja a través del tiempo y descubre los eventos más importantes de la historia mundial. Desde la antigüedad hasta la era moderna.",
                "#45B7D1", 
                "fas fa-landmark", 
                "Historia", 
                "Principiante", 
                30
            ),
            new Curso(
                "Matemáticas Básicas", 
                "Domina los fundamentos de las matemáticas. Aritmética, geometría y resolución de problemas de manera divertida y práctica.",
                "#96CEB4", 
                "fas fa-calculator", 
                "Matemáticas", 
                "Principiante", 
                22
            ),
            new Curso(
                "Ciencias Naturales", 
                "Explora los misterios de la naturaleza. Biología, química y física explicadas de manera sencilla y entretenida.",
                "#FFEAA7", 
                "fas fa-flask", 
                "Ciencias", 
                "Intermedio", 
                28
            ),
            new Curso(
                "Arte y Creatividad", 
                "Desarrolla tu creatividad a través del arte. Pintura, dibujo, manualidades y expresión artística para todas las edades.",
                "#DDA0DD", 
                "fas fa-palette", 
                "Arte", 
                "Principiante", 
                18
            ),
            new Curso(
                "Tecnología Digital", 
                "Introducción al mundo digital. Computación básica, internet seguro y herramientas tecnológicas para el aprendizaje.",
                "#98D8C8", 
                "fas fa-laptop", 
                "Tecnología", 
                "Principiante", 
                24
            ),
            new Curso(
                "Desarrollo Personal", 
                "Fortalece tu autoestima, habilidades sociales y valores. Aprende a ser la mejor versión de ti mismo.",
                "#F7DC6F", 
                "fas fa-heart", 
                "Desarrollo Personal", 
                "Intermedio", 
                16
            )
        );
        
        cursoRepository.saveAll(cursos);
        System.out.println("Se han inicializado " + cursos.size() + " cursos en la base de datos.");
    }
} 