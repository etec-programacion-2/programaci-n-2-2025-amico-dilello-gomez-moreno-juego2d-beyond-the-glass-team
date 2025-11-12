package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.GameAction
import org.example.core.GameLogicService
import org.example.core.GameState
import org.example.core.MiJuego
import com.badlogic.gdx.graphics.Color // --- ERROR SOLUCIONADO --- (Esta es la línea que faltaba)
import com.badlogic.gdx.graphics.Texture // --- (NUEVO) IMPORT PARA EL FILTRO DE TEXTURA ---

/**
 * Clase principal del módulo 'desktop'. Es el 'ApplicationAdapter' de LibGDX.
 * Actúa como el "puente" entre LibGDX y el 'core' (MiJuego).
 * Gestiona el bucle de renderizado de LibGDX.
 *
 * ---
 * @see "Issue BTG-001: Módulo 'desktop'."
 * @see "Issue BTG-002: Utiliza las interfaces 'GameLogicService', 'RenderService', 'InputService'."
 * @see "Issue BTG-008: Implementa el bucle de juego (método render)."
 * ---
 */
class DesktopGame : ApplicationAdapter() {
    
    // --- Dependencias del Core ---
    // (SOLID: D) Dependemos de la ABSTRACCIÓN 'GameLogicService', no de 'MiJuego'.
    private lateinit var juego: GameLogicService 
    private lateinit var renderService: GdxRenderService // Implementación concreta de renderizado
    private lateinit var inputService: GdxInputService // Implementación concreta de entrada

    // --- Objetos de LibGDX ---
    private lateinit var shapeRenderer: ShapeRenderer // Para dibujar formas (jugador, plataformas)
    private lateinit var batch: SpriteBatch // Para dibujar texto (UI)
    private lateinit var font: BitmapFont // Para la fuente del texto
    
    // --- Relacionado con BTG-013: Fuente para habilidad ---
    private lateinit var abilityFont: BitmapFont
    
    // (NUEVO) Fuente para las pantallas de estado
    private lateinit var titleFont: BitmapFont

    /**
     * Método 'create' de LibGDX. Se llama una vez al iniciar.
     * Aquí se inicializan todos los objetos.
     */
    override fun create() {
        // --- Inicialización de objetos LibGDX ---
        shapeRenderer = ShapeRenderer()
        batch = SpriteBatch()
        
        // --- (CAMBIO) Arreglo de fuente pixelada ---
        // Al crear un BitmapFont por defecto, le decimos que use
        // filtros "Linear" (suavizado) en lugar de "Nearest" (pixelado)
        // al escalar la textura de la fuente.
        
        font = BitmapFont()
        font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        
        // (NUEVO) Fuente más grande para títulos
        titleFont = BitmapFont()
        titleFont.data.setScale(2.0f) // Hace la fuente 2x más grande
        titleFont.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) // Aplica filtro
        
        // --- Relacionado con BTG-013: Fuente especial para habilidad ---
        abilityFont = BitmapFont()
        abilityFont.color = Color.CYAN // Color distintivo para la habilidad
        abilityFont.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) // Aplica filtro

        // --- Inyección de Dependencias (Manual) ---
        
        // 1. Crear los servicios concretos de 'desktop'
        renderService = GdxRenderService(shapeRenderer)
        inputService = GdxInputService()

        // 2. Crear la instancia del 'core'
        // (SOLID: D) DesktopGame solo conoce la interfaz GameLogicService
        juego = MiJuego() 
        
        // 3. Iniciar servicios del 'core' (aunque MiJuego los crea internamente ahora)
        // (En una implementación ideal, 'MiJuego' recibiría 'renderService' e 'inputService' en su constructor)
        inputService.start()

        // 4. Cargar el nivel (NUEVO: ahora empieza desde level1)
        // --- Relacionado con BTG-007: Carga de nivel ---
        juego.loadLevel("level1.txt")
    }

    /**
     * Método 'render' de LibGDX. Es el bucle principal del juego.
     * Se llama en cada fotograma.
     */
    override fun render() {
        // --- Lógica del bucle de juego (Issue BTG-008) ---

        // 1. Obtener el tiempo delta (tiempo desde el último fotograma)
        val deltaTime = Gdx.graphics.deltaTime

        // 2. Obtener acciones del usuario
        // (SOLID: D) 'juego' no sabe de LibGDX, solo recibe GameActions abstractas.
        val actions = inputService.getActions()

        // 3. (NUEVO) Manejo de la acción de SALIR
        // (SOLID: D) Solo el 'desktop' sabe CÓMO salir (Gdx.app.exit).
        // El 'core' solo emitió la acción abstracta 'QUIT'.
        if (GameAction.QUIT in actions) {
            Gdx.app.exit()
            return
        }

        // 4. Actualizar el motor del juego (lógica del 'core')
        juego.update(actions, deltaTime)

        // 5. Obtener el estado del mundo (snapshot de datos)
        // (SOLID: D, POO: Encapsulamiento) No vemos la lógica interna, solo el estado.
        val worldState = juego.getWorldState()

        // 6. Renderizar el estado del mundo (si estamos jugando)
        // (SOLID: S) Delegamos el dibujado al servicio de renderizado.
        if (juego.getGameState() == GameState.Playing) {
            renderService.renderWorld(worldState)
        }

        // 7. Dibujar la UI (texto)
        // (DesktopGame se encarga de la UI, que es específico de la plataforma)
        batch.begin()
        
        // (NUEVO) Máquina de estados para la UI
        when (juego.getGameState()) {
            
            GameState.Playing -> {
                // Información de estado general
                font.color = Color.WHITE
                font.draw(batch, "Dimensión Actual: ${worldState.currentDimension}", 10f, 580f)
                font.draw(batch, juego.getGameInfo(), 10f, 560f)
                
                // --- Relacionado con BTG-012: UI de Vidas ---
                font.draw(batch, "Vidas: ${juego.getLives()}", 10f, 540f)

                // --- Relacionado con BTG-013: UI de Coleccionables y Habilidades ---
                val player = juego.getPlayer()
                // Muestra contador de fragmentos
                font.draw(batch, "Fragmentos: ${player.energyFragments} / 3", 10f, 520f)
                
                // Muestra si la habilidad está desbloqueada
                if (player.canDoubleJump) {
                    abilityFont.draw(batch, "¡DOBLE SALTO DESBLOQUEADO!", 10f, 500f)
                }
            }
            
            GameState.GameOver -> {
                // --- Relacionado con BTG-012: UI de Game Over ---
                // (Corregido) titleFont y font ahora usan filtro Linear
                titleFont.color = Color.RED
                titleFont.draw(batch, "GAME OVER", 280f, 350f)
                font.color = Color.WHITE
                font.draw(batch, "Presiona SHIFT para reiniciar (desde Nivel 1)", 240f, 300f)
                font.draw(batch, "Presiona ESC para salir", 290f, 270f)
            }
            
            is GameState.GameWon -> {
                // --- (NUEVO) UI de Juego Ganado ---
                // (Corregido) titleFont y font ahora usan filtro Linear
                titleFont.color = Color.CYAN
                titleFont.draw(batch, "¡HAS GANADO!", 270f, 350f)
                font.color = Color.WHITE
                font.draw(batch, "¡Completaste todos los niveles!", 270f, 300f)
                font.draw(batch, "Presiona ESC para salir", 290f, 270f)
            }
            
            else -> {
                 // No dibujar nada para 'Menu' o 'Paused' (aún no implementados)
            }
        }
        
        batch.end()
    }

    /**
     * Se llama al cerrar la aplicación. Libera recursos.
     */
    override fun dispose() {
        inputService.stop()
        shapeRenderer.dispose()
        batch.dispose()
        font.dispose()
        abilityFont.dispose()
        titleFont.dispose() // (NUEVO)
    }
}