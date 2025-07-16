package com.proy.diseno.Model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "estudiantes")
public class Estudiante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String apellido;
    private int edad;
    private String username;
    private String password;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "estudiante_cursos",
        joinColumns = @JoinColumn(name = "estudiante_id"),
        inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private Set<Curso> cursosInscritos = new HashSet<>();
    
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
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public int getEdad() {
        return edad;
    }
    
    public void setEdad(int edad) {
        this.edad = edad;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Set<Curso> getCursosInscritos() {
        return cursosInscritos;
    }
    
    public void setCursosInscritos(Set<Curso> cursosInscritos) {
        this.cursosInscritos = cursosInscritos;
    }
    
    // Métodos de conveniencia
    public void agregarCurso(Curso curso) {
        if (curso != null && !this.cursosInscritos.contains(curso)) {
            this.cursosInscritos.add(curso);
            if (!curso.getEstudiantes().contains(this)) {
                curso.getEstudiantes().add(this);
            }
        }
    }
    
    public void removerCurso(Curso curso) {
        if (curso != null && this.cursosInscritos.contains(curso)) {
            this.cursosInscritos.remove(curso);
            if (curso.getEstudiantes().contains(this)) {
                curso.getEstudiantes().remove(this);
            }
        }
    }
} 