package com.proy.diseno.Model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cursos")
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(name = "color_fondo")
    private String colorFondo;
    
    @Column(name = "icono")
    private String icono;
    
    @Column(name = "categoria")
    private String categoria;
    
    @Column(name = "nivel_dificultad")
    private String nivelDificultad;
    
    @Column(name = "duracion_horas")
    private Integer duracionHoras;
    
    @ManyToMany(mappedBy = "cursosInscritos")
    private Set<Estudiante> estudiantes = new HashSet<>();
    
    // Constructores
    public Curso() {}
    
    public Curso(String nombre, String descripcion, String colorFondo, String icono, String categoria, String nivelDificultad, Integer duracionHoras) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.colorFondo = colorFondo;
        this.icono = icono;
        this.categoria = categoria;
        this.nivelDificultad = nivelDificultad;
        this.duracionHoras = duracionHoras;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getColorFondo() {
        return colorFondo;
    }
    
    public void setColorFondo(String colorFondo) {
        this.colorFondo = colorFondo;
    }
    
    public String getIcono() {
        return icono;
    }
    
    public void setIcono(String icono) {
        this.icono = icono;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getNivelDificultad() {
        return nivelDificultad;
    }
    
    public void setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }
    
    public Integer getDuracionHoras() {
        return duracionHoras;
    }
    
    public void setDuracionHoras(Integer duracionHoras) {
        this.duracionHoras = duracionHoras;
    }
    
    public Set<Estudiante> getEstudiantes() {
        return estudiantes;
    }
    
    public void setEstudiantes(Set<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }
} 