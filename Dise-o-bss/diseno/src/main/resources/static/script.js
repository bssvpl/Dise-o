document.addEventListener('DOMContentLoaded', () => {
    // Elementos del DOM
    const accessibilityBtn = document.getElementById('accessibility-btn');
    const accessibilityPanel = document.getElementById('accessibility-panel');
    const mainContent = document.querySelector('.content');
    const fontSizeInput = document.getElementById('font-size');
    const lineSpacingInput = document.getElementById('line-spacing');
    const letterSpacingInput = document.getElementById('letter-spacing');
    const zoomInput = document.getElementById('zoom');
    const themeToggle = document.getElementById('theme-toggle');
    const readTextBtn = document.getElementById('read-text');
    const textContent = document.querySelector('.text-content');
    const fontFamilySelect = document.getElementById('font-family');
    const mainTextContent = document.getElementById('main-text-content');
    const readMoreBtn = document.getElementById('read-more-btn');
    const h1SizeInput = document.getElementById('h1-size');
    const h1Element = document.querySelector('h1');
    const themeLightBtn = document.getElementById('theme-light');
    const themeDarkBtn = document.getElementById('theme-dark');

    // Función para actualizar la visibilidad de la imagen
    function updateImageVisibility() {
        if (accessibilityPanel.classList.contains('active')) {
            // storyImage.style.display = 'none'; // Eliminar cualquier referencia o lógica relacionada con story-image-container
        } else {
            // storyImage.style.display = 'block'; // Eliminar cualquier referencia o lógica relacionada con story-image-container
        }
    }

    // Al cargar la página, asegúrate de que la visibilidad sea la correcta
    updateImageVisibility();

    // Al hacer click en el botón de accesibilidad, alterna el panel y la imagen
    accessibilityBtn.addEventListener('click', () => {
        accessibilityPanel.classList.toggle('active');
        updateImageVisibility();
    });

    // Control de tipo de fuente
    if (fontFamilySelect && textContent && h1Element) {
        fontFamilySelect.addEventListener('change', (e) => {
            textContent.style.fontFamily = e.target.value;
            h1Element.style.fontFamily = e.target.value;
        });
    }

    // Control de tamaño de fuente
    if (fontSizeInput && textContent && h1Element) {
        fontSizeInput.addEventListener('input', (e) => {
            textContent.style.fontSize = `${e.target.value}px`;
            h1Element.style.fontSize = `${parseFloat(e.target.value) * 1.2}px`;
        });
    }

    // Control de espacio entre líneas
    if (lineSpacingInput && textContent && h1Element) {
        lineSpacingInput.addEventListener('input', (e) => {
            textContent.style.lineHeight = e.target.value;
            h1Element.style.lineHeight = e.target.value;
        });
    }

    // Control de espacio entre letras
    if (letterSpacingInput && textContent && h1Element) {
        letterSpacingInput.addEventListener('input', (e) => {
            textContent.style.letterSpacing = `${e.target.value}px`;
            h1Element.style.letterSpacing = `${e.target.value}px`;
        });
    }

    // Control de zoom tipo lupa real para el main
    if (zoomInput && mainContent) {
        zoomInput.addEventListener('input', (e) => {
            const zoom = e.target.value / 100;
            mainContent.style.fontSize = `${1 * zoom}rem`;
            mainContent.style.padding = `${2.5 * zoom}rem ${2 * zoom}rem ${2 * zoom}rem ${2 * zoom}rem`;
            mainContent.style.width = `${80 * zoom}vw`;
        });
    }

    // Texto a voz
    if (readTextBtn && textContent) {
        readTextBtn.addEventListener('click', () => {
            if ('speechSynthesis' in window) {
                if (window.speechSynthesis.speaking) {
                    window.speechSynthesis.cancel();
                    return;
                }
                const text = textContent.textContent;
                const utterance = new SpeechSynthesisUtterance(text);
                utterance.lang = 'es-ES';
                window.speechSynthesis.speak(utterance);
            } else {
                alert('Tu navegador no soporta la síntesis de voz.');
            }
        });
    }

    // Botones de tema claro y oscuro
    if (themeLightBtn) {
        themeLightBtn.addEventListener('click', () => {
            document.documentElement.setAttribute('data-theme', 'light');
            if (typeof savePreferences === 'function') savePreferences();
        });
    }
    if (themeDarkBtn) {
        themeDarkBtn.addEventListener('click', () => {
            document.documentElement.setAttribute('data-theme', 'dark');
            if (typeof savePreferences === 'function') savePreferences();
        });
    }

    // Guardar preferencias en localStorage
    const savePreferences = () => {
        const preferences = {
            fontSize: fontSizeInput.value,
            lineSpacing: lineSpacingInput.value,
            letterSpacing: letterSpacingInput.value,
            zoom: zoomInput.value,
            theme: document.documentElement.getAttribute('data-theme'),
            panelActive: accessibilityPanel.classList.contains('active'),
            fontFamily: fontFamilySelect.value
        };
        localStorage.setItem('accessibilityPreferences', JSON.stringify(preferences));
    };

    // Cargar preferencias guardadas
    const loadPreferences = () => {
        const savedPreferences = localStorage.getItem('accessibilityPreferences');
        if (savedPreferences) {
            const preferences = JSON.parse(savedPreferences);
            fontSizeInput.value = preferences.fontSize;
            lineSpacingInput.value = preferences.lineSpacing;
            letterSpacingInput.value = preferences.letterSpacing;
            zoomInput.value = preferences.zoom;
            fontFamilySelect.value = preferences.fontFamily || 'Arial, sans-serif';
            document.documentElement.setAttribute('data-theme', preferences.theme);
            
            // Aplicar valores cargados solo al contenido
            textContent.style.fontSize = `${preferences.fontSize}px`;
            textContent.style.fontFamily = preferences.fontFamily || 'Arial, sans-serif';
            textContent.style.lineHeight = preferences.lineSpacing;
            textContent.style.letterSpacing = `${preferences.letterSpacing}px`;
            textContent.style.transform = `scale(${preferences.zoom / 100})`;

            // Restaurar estado del panel solo si estaba activo
            if (preferences.panelActive) {
                accessibilityPanel.classList.add('active');
                mainContent.classList.add('panel-active');
            }
        }
    };

    // Guardar preferencias cuando cambian
    [fontSizeInput, lineSpacingInput, letterSpacingInput, zoomInput, fontFamilySelect].forEach(input => {
        input.addEventListener('change', savePreferences);
    });

    // Guardar preferencias cuando cambia el tema o el panel
    if (accessibilityBtn) {
        accessibilityBtn.addEventListener('click', savePreferences);
    }

    // Cargar preferencias al iniciar
    loadPreferences();

    // Si el panel ya está activo al cargar (por preferencias), oculta la imagen
    // if (accessibilityPanel.classList.contains('active') && storyImage) { // Eliminar cualquier referencia o lógica relacionada con story-image-container
    //     storyImage.style.display = 'none';
    // } else if (storyImage) { // Eliminar cualquier referencia o lógica relacionada con story-image-container
    //     storyImage.style.display = 'block';
    // }

    // Inicialmente truncar el texto
    mainTextContent.classList.add('truncated');
    readMoreBtn.style.display = 'inline-block';
    let expanded = false;

    readMoreBtn.addEventListener('click', (e) => {
        e.preventDefault();
        expanded = !expanded;
        if (expanded) {
            mainTextContent.classList.remove('truncated');
            mainTextContent.style.maxHeight = '1000em';
            readMoreBtn.textContent = 'Leer menos';
        } else {
            mainTextContent.classList.add('truncated');
            mainTextContent.style.maxHeight = '';
            readMoreBtn.textContent = 'Leer más';
        }
    });

    // Control de tamaño de fuente del h1
    if (h1SizeInput && h1Element) {
        h1SizeInput.addEventListener('input', (e) => {
            h1Element.style.fontSize = `${e.target.value}rem`;
        });
    }

    // --- MODO FOCO ROBUSTO Y FUNCIONAL ---
    const focusModeInput = document.getElementById('focus-mode');
    if (focusModeInput) {
        // Detectar el contenedor principal de texto
        let focusContainer = document.querySelector('.content');
        // Si estamos en la página de curso, usar .text-content si existe
        if (document.querySelector('.text-content')) {
            focusContainer = document.querySelector('.text-content');
        }
        if (focusContainer) {
            focusModeInput.addEventListener('change', (e) => {
                const focusables = Array.from(focusContainer.querySelectorAll('h1, p'));
                if (e.target.checked) {
                    focusContainer.classList.add('focus-mode-active');
                    focusables.forEach(el => {
                        el.addEventListener('mouseenter', handleFocus);
                        el.addEventListener('mouseleave', removeFocus);
                    });
                } else {
                    focusContainer.classList.remove('focus-mode-active');
                    focusables.forEach(el => {
                        el.classList.remove('focused');
                        el.removeEventListener('mouseenter', handleFocus);
                        el.removeEventListener('mouseleave', removeFocus);
                    });
                }
            });
        }
    }

    function handleFocus(e) {
        const focusables = Array.from(mainContent.querySelectorAll('h1, p'));
        focusables.forEach(el => el.classList.remove('focused'));
        e.target.classList.add('focused');
        e.target.style.textDecoration = 'underline';
    }
    function removeFocus(e) {
        e.target.classList.remove('focused');
        e.target.style.textDecoration = '';
    }

    // Progreso dinámico en mis-cursos
    document.querySelectorAll('.curso-card[data-curso-id]').forEach(function(card) {
        const cursoId = card.getAttribute('data-curso-id');
        const progresoBar = card.querySelector('.progreso-bar');
        const progresoTexto = card.querySelector('.progreso-texto');
        if (progresoBar && progresoTexto) {
            const leido = localStorage.getItem('curso_leido_' + cursoId) === 'true';
            if (leido) {
                progresoBar.style.width = '100%';
                progresoTexto.textContent = '100% Completado';
            } else {
                progresoBar.style.width = '0%';
                progresoTexto.textContent = '0% Completado';
            }
        }
    });
});