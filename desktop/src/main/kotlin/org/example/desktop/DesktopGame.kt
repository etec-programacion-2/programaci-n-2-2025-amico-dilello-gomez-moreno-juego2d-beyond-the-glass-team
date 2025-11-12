package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.GameLogicService
import org.example.core.GameState
import org.example.core.MiJuego
import com.badlogic.gdx.graphics.Color // --- ERROR SOLUCIONADO --- (Esta es la línea que faltaba)

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

    /**
     * Método 'create' de LibGDX. Se llama una vez al iniciar.
     * Aquí se inicializan todos los objetos.
     */
    override fun create() {
        // --- Inicialización de objetos LibGDX ---
        shapeRenderer = ShapeRenderer()
        batch = SpriteBatch()
        font = BitmapFont()
        
        // --- Relacionado con BTG-013: Fuente especial para habilidad ---
        abilityFont = BitmapFont()
        abilityFont.color = Color.CYAN // Color distintivo para la habilidad

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

        // 4. Cargar el nivel
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

        // 3. Actualizar el motor del juego (lógica del 'core')
        juego.update(actions, deltaTime)

        // 4. Obtener el estado del mundo (snapshot de datos)
        // (SOLID: D, POO: Encapsulamiento) No vemos la lógica interna, solo el estado.
        val worldState = juego.getWorldState()

        // 5. Renderizar el estado del mundo
        // (SOLID: S) Delegamos el dibujado al servicio de renderizado.
        renderService.renderWorld(worldState)

        // 6. Dibujar la UI (texto)
        // (DesktopGame se encarga de la UI, que es específico de la plataforma)
        batch.begin()
        
        // --- Relacionado con BTG-012: UI de Game Over ---
        if (juego.getGameState() == GameState.GameOver) {
            font.color = Color.RED
            font.draw(batch, "GAME OVER", 300f, 350f)
            font.draw(batch, "Presiona SHIFT para reiniciar", 250f, 300f)
        } else {
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
    }
}