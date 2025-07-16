package com.proy.diseno.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proy.diseno.Model.Estudiante;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Estudiante findByUsername(String username);
    
    @Modifying
    @Query(value = "DELETE FROM estudiante_cursos WHERE estudiante_id = :estudianteId AND curso_id = :cursoId", nativeQuery = true)
    void desinscribirDeCurso(@Param("estudianteId") Long estudianteId, @Param("cursoId") Long cursoId);
    
    @Modifying
    @Query(value = "INSERT INTO estudiante_cursos (estudiante_id, curso_id) VALUES (:estudianteId, :cursoId)", nativeQuery = true)
    void inscribirEnCurso(@Param("estudianteId") Long estudianteId, @Param("cursoId") Long cursoId);
} 