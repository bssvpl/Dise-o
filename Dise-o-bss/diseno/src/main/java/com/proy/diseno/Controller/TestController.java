package com.proy.diseno.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Test endpoint funcionando";
    }
    
    @GetMapping("/test/{id}")
    @ResponseBody
    public String testWithId(@PathVariable Long id) {
        return "Test endpoint con ID: " + id;
    }
} 