package com.proy.diseno.Controller;

import com.proy.diseno.Model.Curso;
import com.proy.diseno.Model.Estudiante;
import com.proy.diseno.Repository.CursoRepository;
import com.proy.diseno.Repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CursoController {
    
    private static final Logger logger = LoggerFactory.getLogger(CursoController.class);
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @GetMapping("/cursos")
    public String mostrarCursos(Model model, HttpSession session) {
        logger.info("Accediendo a /cursos");
        
        Estudiante estudiante = (Estudiante) session.getAttribute("estudiante");
        
        if (estudiante == null) {
            logger.warn("Estudiante no autenticado, redirigiendo a login");
            return "redirect:/login";
        }
        
        logger.info("Estudiante autenticado: {}", estudiante.getUsername());
        
        try {
            // Obtener todos los cursos disponibles
            List<Curso> todosLosCursos = cursoRepository.findAll();
            logger.info("Total de cursos encontrados: {}", todosLosCursos.size());
            
            // Obtener cursos en los que el estudiante está inscrito
            List<Curso> cursosInscritos = cursoRepository.findCursosByEstudianteId(estudiante.getId());
            logger.info("Cursos inscritos del estudiante: {}", cursosInscritos.size());
            
            // Obtener cursos disponibles para inscripción
            List<Curso> cursosDisponibles = cursoRepository.findCursosNoInscritosByEstudianteId(estudiante.getId());
            logger.info("Cursos disponibles para inscripción: {}", cursosDisponibles.size());
            
            model.addAttribute("todosLosCursos", todosLosCursos);
            model.addAttribute("cursosInscritos", cursosInscritos);
            model.addAttribute("cursosDisponibles", cursosDisponibles);
            model.addAttribute("estudiante", estudiante);
            
            return "cursos";
            
        } catch (Exception e) {
            logger.error("Error al cargar cursos: ", e);
            model.addAttribute("error", "Error al cargar los cursos");
            return "cursos";
        }
    }
    
    @GetMapping("/mis-cursos")
    public String misCursos(Model model, HttpSession session) {
        logger.info("Accediendo a /mis-cursos");
        
        Estudiante estudiante = (Estudiante) session.getAttribute("estudiante");
        
        if (estudiante == null) {
            logger.warn("Estudiante no autenticado, redirigiendo a login");
            return "redirect:/login";
        }
        
        logger.info("Estudiante autenticado: {}", estudiante.getUsername());
        
        try {
            // Limpiar la caché de JPA para obtener datos frescos
            estudianteRepository.flush();
            
            // Obtener cursos en los que el estudiante está inscrito usando consulta directa
            List<Curso> cursosInscritos = cursoRepository.findCursosByEstudianteId(estudiante.getId());
            logger.info("Cursos inscritos encontrados: {}", cursosInscritos.size());
            
            // Actualizar la sesión con el estudiante actualizado desde la base de datos
            Estudiante estudianteActualizado = estudianteRepository.findById(estudiante.getId()).orElse(estudiante);
            session.setAttribute("estudiante", estudianteActualizado);
            
            model.addAttribute("cursos", cursosInscritos);
            model.addAttribute("estudiante", estudianteActualizado);
            
            return "mis-cursos";
            
        } catch (Exception e) {
            logger.error("Error al cargar mis cursos: ", e);
            model.addAttribute("error", "Error al cargar tus cursos");
            return "mis-cursos";
        }
    }
    
    @PostMapping("/inscribir-curso/{cursoId}")
    @Transactional
    public String inscribirCurso(@PathVariable Long cursoId, 
                                HttpSession session, 
                                RedirectAttributes redirectAttributes) {
        logger.info("Intentando inscribir estudiante en curso: {}", cursoId);
        
        Estudiante estudiante = (Estudiante) session.getAttribute("estudiante");
        
        if (estudiante == null) {
            return "redirect:/login";
        }
        
        try {
            Curso curso = cursoRepository.findById(cursoId).orElse(null);
            if (curso == null) {
                redirectAttributes.addFlashAttribute("error", "Curso no encontrado");
                return "redirect:/cursos";
            }
            
            // Verificar si ya está inscrito usando consulta directa
            List<Curso> cursosInscritos = cursoRepository.findCursosByEstudianteId(estudiante.getId());
            if (cursosInscritos.stream().anyMatch(c -> c.getId().equals(cursoId))) {
                redirectAttributes.addFlashAttribute("error", "Ya estás inscrito en este curso");
                return "redirect:/cursos";
            }
            
            // Usar consulta SQL nativa para inscribir directamente en la tabla de relación
            estudianteRepository.inscribirEnCurso(estudiante.getId(), cursoId);
            
            // Limpiar la caché de JPA
            estudianteRepository.flush();
            
            // Obtener el estudiante actualizado desde la base de datos
            Estudiante estudianteActualizado = estudianteRepository.findById(estudiante.getId()).orElse(estudiante);
            session.setAttribute("estudiante", estudianteActualizado);
            
            logger.info("Estudiante {} inscrito exitosamente en curso {}", estudiante.getUsername(), curso.getNombre());
            redirectAttributes.addFlashAttribute("mensaje", "Te has inscrito exitosamente en " + curso.getNombre());
            
        } catch (Exception e) {
            logger.error("Error al inscribirse en el curso: ", e);
            redirectAttributes.addFlashAttribute("error", "Error al inscribirse en el curso");
        }
        
        return "redirect:/cursos";
    }
    
    @PostMapping("/desinscribir-curso/{cursoId}")
    @Transactional
    public String desinscribirCurso(@PathVariable Long cursoId, 
                                   HttpSession session, 
                                   RedirectAttributes redirectAttributes) {
        logger.info("Intentando desinscribir estudiante del curso: {}", cursoId);
        
        Estudiante estudiante = (Estudiante) session.getAttribute("estudiante");
        
        if (estudiante == null) {
            return "redirect:/login";
        }
        
        try {
            Curso curso = cursoRepository.findById(cursoId).orElse(null);
            if (curso == null) {
                redirectAttributes.addFlashAttribute("error", "Curso no encontrado");
                return "redirect:/mis-cursos";
            }
            
            // Usar consulta SQL nativa para desinscribir directamente de la tabla de relación
            estudianteRepository.desinscribirDeCurso(estudiante.getId(), cursoId);
            
            // Limpiar la caché de JPA
            estudianteRepository.flush();
            
            // Obtener el estudiante actualizado desde la base de datos
            Estudiante estudianteActualizado = estudianteRepository.findById(estudiante.getId()).orElse(estudiante);
            session.setAttribute("estudiante", estudianteActualizado);
            
            logger.info("Estudiante {} desinscrito exitosamente del curso {}", estudiante.getUsername(), curso.getNombre());
            redirectAttributes.addFlashAttribute("mensaje", "Te has desinscrito de " + curso.getNombre());
            
        } catch (Exception e) {
            logger.error("Error al desinscribirse del curso: ", e);
            redirectAttributes.addFlashAttribute("error", "Error al desinscribirse del curso");
        }
        
        return "redirect:/mis-cursos";
    }
    
    @GetMapping("/test-cursos")
    @ResponseBody
    public String testCursos() {
        logger.info("Endpoint de prueba /test-cursos accedido");
        try {
            long totalCursos = cursoRepository.count();
            return "Controlador de cursos funcionando. Total de cursos en BD: " + totalCursos;
        } catch (Exception e) {
            logger.error("Error en endpoint de prueba: ", e);
            return "Error: " + e.getMessage();
        }
    }
    
    @GetMapping("/test-curso-endpoint")
    @ResponseBody
    public String testCursoEndpoint() {
        logger.info("Endpoint de prueba /test-curso-endpoint accedido");
        return "El endpoint /curso/{cursoId} está disponible. Controlador funcionando correctamente.";
    }
    
    @GetMapping("/curso-simple/{cursoId}")
    @ResponseBody
    public String verContenidoCursoSimple(@PathVariable Long cursoId, HttpSession session) {
        logger.info("Accediendo al contenido simple del curso: {}", cursoId);
        
        Estudiante estudiante = (Estudiante) session.getAttribute("estudiante");
        
        if (estudiante == null) {
            return "No autenticado";
        }
        
        try {
            Curso curso = cursoRepository.findById(cursoId).orElse(null);
            if (curso == null) {
                return "Curso no encontrado";
            }
            
            return "Curso encontrado: " + curso.getNombre() + " - Estudiante: " + estudiante.getNombre();
            
        } catch (Exception e) {
            logger.error("Error: ", e);
            return "Error: " + e.getMessage();
        }
    }
    
    @GetMapping("/curso/{cursoId}")
    public String verContenidoCurso(@PathVariable Long cursoId, 
                                   Model model, 
                                   HttpSession session) {
        logger.info("Accediendo al contenido del curso: {}", cursoId);
        
        Estudiante estudiante = (Estudiante) session.getAttribute("estudiante");
        
        if (estudiante == null) {
            logger.warn("Estudiante no autenticado, redirigiendo a login");
            return "redirect:/login";
        }
        
        try {
            // Obtener el curso
            Curso curso = cursoRepository.findById(cursoId).orElse(null);
            if (curso == null) {
                logger.error("Curso no encontrado: {}", cursoId);
                return "redirect:/mis-cursos";
            }
            
            // Verificar que el estudiante esté inscrito en el curso
            List<Curso> cursosInscritos = cursoRepository.findCursosByEstudianteId(estudiante.getId());
            boolean estaInscrito = cursosInscritos.stream().anyMatch(c -> c.getId().equals(cursoId));
            
            if (!estaInscrito) {
                logger.warn("Estudiante {} no está inscrito en el curso {}", estudiante.getUsername(), curso.getNombre());
                return "redirect:/mis-cursos";
            }
            
            model.addAttribute("curso", curso);
            model.addAttribute("estudiante", estudiante);
            
            logger.info("Mostrando contenido del curso {} para el estudiante {}", curso.getNombre(), estudiante.getUsername());
            return "curso-contenido";
            
        } catch (Exception e) {
            logger.error("Error al cargar el contenido del curso: ", e);
            return "redirect:/mis-cursos";
        }
    }
    
    @GetMapping("/test-desinscripcion/{estudianteId}/{cursoId}")
    @ResponseBody
    @Transactional
    public String testDesinscripcion(@PathVariable Long estudianteId, @PathVariable Long cursoId) {
        logger.info("Probando desinscripción: estudiante {} del curso {}", estudianteId, cursoId);
        try {
            // Verificar inscripción antes
            List<Curso> cursosAntes = cursoRepository.findCursosByEstudianteId(estudianteId);
            boolean estabaInscrito = cursosAntes.stream().anyMatch(c -> c.getId().equals(cursoId));
            
            if (!estabaInscrito) {
                return "El estudiante no estaba inscrito en el curso";
            }
            
            // Desinscribir
            estudianteRepository.desinscribirDeCurso(estudianteId, cursoId);
            estudianteRepository.flush();
            
            // Verificar después
            List<Curso> cursosDespues = cursoRepository.findCursosByEstudianteId(estudianteId);
            boolean sigueInscrito = cursosDespues.stream().anyMatch(c -> c.getId().equals(cursoId));
            
            return String.format("Antes: %s, Después: %s, Desinscripción exitosa: %s", 
                               estabaInscrito, sigueInscrito, !sigueInscrito);
            
        } catch (Exception e) {
            logger.error("Error en prueba de desinscripción: ", e);
            return "Error: " + e.getMessage();
        }
    }
} 