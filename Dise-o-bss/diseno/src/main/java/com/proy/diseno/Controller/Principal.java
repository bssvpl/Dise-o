package com.proy.diseno.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Principal {
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/cursos")
    public String cursos() {
        return "cursos";
    }

    @GetMapping("/prueba")
    @ResponseBody
    public String prueba() {
        return "¡Spring Boot funciona!";
    }
}
