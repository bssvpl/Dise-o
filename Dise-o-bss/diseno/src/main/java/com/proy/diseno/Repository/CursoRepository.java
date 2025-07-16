package com.proy.diseno.Repository;

import com.proy.diseno.Model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    
    Optional<Curso> findByNombre(String nombre);
    
    List<Curso> findByCategoria(String categoria);
    
    List<Curso> findByNivelDificultad(String nivelDificultad);
    
    @Query("SELECT c FROM Curso c WHERE c.nombre LIKE %:termino% OR c.descripcion LIKE %:termino%")
    List<Curso> buscarPorTermino(@Param("termino") String termino);
    
    @Query("SELECT c FROM Curso c JOIN c.estudiantes e WHERE e.id = :estudianteId")
    List<Curso> findCursosByEstudianteId(@Param("estudianteId") Long estudianteId);
    
    @Query("SELECT c FROM Curso c WHERE c.id NOT IN (SELECT c2.id FROM Curso c2 JOIN c2.estudiantes e WHERE e.id = :estudianteId)")
    List<Curso> findCursosNoInscritosByEstudianteId(@Param("estudianteId") Long estudianteId);
} 