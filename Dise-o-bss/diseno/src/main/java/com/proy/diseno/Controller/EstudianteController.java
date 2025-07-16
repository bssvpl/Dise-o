package com.proy.diseno.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.proy.diseno.Model.Estudiante;
import com.proy.diseno.Repository.EstudianteRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class EstudianteController {
    
    private static final Logger logger = LoggerFactory.getLogger(EstudianteController.class);

    @Autowired
    private EstudianteRepository estudianteRepository;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "registro";
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "login";
    }

    @PostMapping("/registro")
    public String registrarEstudiante(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam int edad,
            @RequestParam String username,
            @RequestParam String password,
            RedirectAttributes redirectAttributes) {
        
        try {
            logger.debug("Intentando registrar estudiante con username: {}", username);
            
            // Validaciones básicas
            if (nombre == null || nombre.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El nombre no puede estar vacío");
                return "redirect:/registro";
            }
            
            if (apellido == null || apellido.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El apellido no puede estar vacío");
                return "redirect:/registro";
            }
            
            if (edad < 1 || edad > 120) {
                redirectAttributes.addFlashAttribute("error", "La edad debe estar entre 1 y 120 años");
                return "redirect:/registro";
            }
            
            if (username == null || username.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El nombre de usuario no puede estar vacío");
                return "redirect:/registro";
            }
            
            if (password == null || password.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "La contraseña no puede estar vacía");
                return "redirect:/registro";
            }

            // Verificar si el username ya existe
            if (estudianteRepository.findByUsername(username) != null) {
                logger.debug("El username {} ya está en uso", username);
                redirectAttributes.addFlashAttribute("error", "El nombre de usuario ya está en uso");
                return "redirect:/registro";
            }

            Estudiante estudiante = new Estudiante();
            estudiante.setNombre(nombre.trim());
            estudiante.setApellido(apellido.trim());
            estudiante.setEdad(edad);
            estudiante.setUsername(username.trim());
            estudiante.setPassword(password); // En producción, esto debería estar encriptado

            estudianteRepository.save(estudiante);
            logger.debug("Estudiante registrado exitosamente: {}", username);
            
            redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso. Por favor inicia sesión.");
            return "redirect:/login";
            
        } catch (DataIntegrityViolationException e) {
            logger.error("Error de integridad de datos al registrar estudiante: ", e);
            redirectAttributes.addFlashAttribute("error", "El nombre de usuario ya está en uso");
            return "redirect:/registro";
        } catch (Exception e) {
            logger.error("Error al registrar estudiante: ", e);
            redirectAttributes.addFlashAttribute("error", "Error al registrar. Por favor intente nuevamente.");
            return "redirect:/registro";
        }
    }

    @PostMapping("/login")
    public String iniciarSesion(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            logger.debug("Intentando iniciar sesión con username: {}", username);
            
            Estudiante estudiante = estudianteRepository.findByUsername(username);
            
            if (estudiante != null && estudiante.getPassword().equals(password)) {
                session.setAttribute("estudiante", estudiante);
                logger.debug("Sesión iniciada exitosamente para: {}", username);
                return "redirect:/";
            }

            logger.debug("Credenciales inválidas para: {}", username);
            redirectAttributes.addFlashAttribute("error", "Usuario o contraseña incorrectos");
            return "redirect:/login";
            
        } catch (Exception e) {
            logger.error("Error al iniciar sesión: ", e);
            redirectAttributes.addFlashAttribute("error", "Error al iniciar sesión. Por favor intente nuevamente.");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
} 