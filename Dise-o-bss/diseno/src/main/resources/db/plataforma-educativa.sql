-- Script SQL para la Plataforma Educativa
-- Base de datos: plataforma_educativa

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS plataforma_educativa;
USE plataforma_educativa;

-- Tabla de estudiantes
CREATE TABLE IF NOT EXISTS estudiantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    edad INT NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Tabla de cursos
CREATE TABLE IF NOT EXISTS cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    color_fondo VARCHAR(7),
    icono VARCHAR(50),
    categoria VARCHAR(50),
    nivel_dificultad VARCHAR(20),
    duracion_horas INT
);

-- Tabla de relación muchos a muchos entre estudiantes y cursos
CREATE TABLE IF NOT EXISTS estudiante_cursos (
    estudiante_id BIGINT,
    curso_id BIGINT,
    fecha_inscripcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (estudiante_id, curso_id),
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE,
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE
);

-- Insertar cursos de ejemplo
INSERT INTO cursos (nombre, descripcion, color_fondo, icono, categoria, nivel_dificultad, duracion_horas) VALUES
('Literatura Infantil', 'Explora el maravilloso mundo de la literatura infantil. Aprende sobre cuentos clásicos, fábulas y cómo fomentar el amor por la lectura en los niños.', '#FF6B6B', 'fas fa-book-open', 'Literatura', 'Principiante', 20),
('Lenguaje y Comunicación', 'Mejora tus habilidades de comunicación oral y escrita. Aprende técnicas de expresión, redacción y presentación efectiva.', '#4ECDC4', 'fas fa-comments', 'Lenguaje', 'Intermedio', 25),
('Historia Universal', 'Viaja a través del tiempo y descubre los eventos más importantes de la historia mundial. Desde la antigüedad hasta la era moderna.', '#45B7D1', 'fas fa-landmark', 'Historia', 'Principiante', 30),
('Matemáticas Básicas', 'Domina los fundamentos de las matemáticas. Aritmética, geometría y resolución de problemas de manera divertida y práctica.', '#96CEB4', 'fas fa-calculator', 'Matemáticas', 'Principiante', 22),
('Ciencias Naturales', 'Explora los misterios de la naturaleza. Biología, química y física explicadas de manera sencilla y entretenida.', '#FFEAA7', 'fas fa-flask', 'Ciencias', 'Intermedio', 28),
('Arte y Creatividad', 'Desarrolla tu creatividad a través del arte. Pintura, dibujo, manualidades y expresión artística para todas las edades.', '#DDA0DD', 'fas fa-palette', 'Arte', 'Principiante', 18),
('Tecnología Digital', 'Introducción al mundo digital. Computación básica, internet seguro y herramientas tecnológicas para el aprendizaje.', '#98D8C8', 'fas fa-laptop', 'Tecnología', 'Principiante', 24),
('Desarrollo Personal', 'Fortalece tu autoestima, habilidades sociales y valores. Aprende a ser la mejor versión de ti mismo.', '#F7DC6F', 'fas fa-heart', 'Desarrollo Personal', 'Intermedio', 16);

-- Insertar algunos estudiantes de ejemplo
INSERT INTO estudiantes (nombre, apellido, edad, username, password) VALUES
('Juan', 'Pérez', 15, 'juan.perez', 'password123'),
('María', 'García', 16, 'maria.garcia', 'password123'),
('Carlos', 'López', 14, 'carlos.lopez', 'password123');

-- Inscribir algunos estudiantes en cursos de ejemplo
INSERT INTO estudiante_cursos (estudiante_id, curso_id) VALUES
(1, 1), -- Juan en Literatura Infantil
(1, 3), -- Juan en Historia Universal
(2, 2), -- María en Lenguaje y Comunicación
(2, 5), -- María en Ciencias Naturales
(3, 4), -- Carlos en Matemáticas Básicas
(3, 7); -- Carlos en Tecnología Digital

