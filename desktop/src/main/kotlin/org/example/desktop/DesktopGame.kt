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
 */
class DesktopGame : ApplicationAdapter() {
    
    // --- Dependencias del Core ---
    private lateinit var juego: GameLogicService // La interfaz del motor de juego (DIP)
    private lateinit var renderService: GdxRenderService // Implementación concreta de renderizado
    private lateinit var inputService: GdxInputService // Implementación concreta de entrada

    // --- Objetos de LibGDX ---
    private lateinit var shapeRenderer: ShapeRenderer // Para dibujar formas (jugador, plataformas)
    private lateinit var batch: SpriteBatch // Para dibujar texto (UI)
    private lateinit var font: BitmapFont // Para la fuente del texto
    
    // --- CAMBIO BTG-013: Fuente para habilidad ---
    private lateinit var abilityFont: BitmapFont

    /**
     * Método 'create' de LibGDX. Se llama una sola vez al iniciar.
     * Aquí es donde se inicializan todos los objetos.
     */
    override fun create() {
        // 1. Inicializar objetos de LibGDX
        shapeRenderer = ShapeRenderer()
        batch = SpriteBatch()
        font = BitmapFont()
        
        // --- CAMBIO BTG-013: Fuente de habilidad (color) ---
        abilityFont = BitmapFont()
        abilityFont.color = Color.YELLOW // Esta línea ahora funciona gracias al import

        // 2. Crear las implementaciones concretas de los servicios
        renderService = GdxRenderService(shapeRenderer) // Le pasamos el ShapeRenderer
        inputService = GdxInputService()
        
        // 3. Crear el motor del juego (del 'core')
        juego = MiJuego()

        // 4. Configurar el juego
        juego.loadLevel("level1.txt") // Carga el nivel
        inputService.start() // Inicia el servicio de input
    }

    /**
     * Método 'render' de LibGDX. Es el bucle principal, se llama en cada fotograma.
     */
    override fun render() {
        
        // --- Lógica de Actualización (Update) ---
        
        if (juego.getGameState() == GameState.Playing) {
            val actions = inputService.getActions()
            juego.update(actions, Gdx.graphics.deltaTime)
            
        } else if (juego.getGameState() == GameState.GameOver) {
            val actions = inputService.getActions()
            juego.update(actions, Gdx.graphics.deltaTime)
        }
        
        // --- Lógica de Dibujado (Render) ---
        
        val worldState = juego.getWorldState()
        renderService.renderWorld(worldState)

        // 3. Dibujar la UI (texto)
        batch.begin()
        
        if (juego.getGameState() == GameState.GameOver) {
            font.color = Color.RED // Esta línea ahora funciona
            font.draw(batch, "GAME OVER", 300f, 350f)
            font.draw(batch, "Presiona SHIFT para reiniciar", 250f, 300f)
        } else {
            // Información de estado general
            font.color = Color.WHITE // Esta línea ahora funciona
            font.draw(batch, "Dimensión Actual: ${worldState.currentDimension}", 10f, 580f)
            font.draw(batch, juego.getGameInfo(), 10f, 560f)
            font.draw(batch, "Vidas: ${juego.getLives()}", 10f, 540f)

            // --- CAMBIO BTG-013: UI de Habilidades ---
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
     * Se llama al cerrar la aplicación. Libera los recursos.
     */
    override fun dispose() {
        shapeRenderer.dispose()
        batch.dispose()
        font.dispose()
        
        // --- CAMBIO BTG-013 ---
        abilityFont.dispose()
        
        inputService.stop()
    }
}