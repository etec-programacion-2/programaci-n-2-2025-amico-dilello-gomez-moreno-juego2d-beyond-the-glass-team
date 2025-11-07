package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.GameLogicService
import org.example.core.GameState
import org.example.core.MiJuego
// --- CAMBIO: Imports para el Viewport ---
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

/**
 * (ACTUALIZADO: Corrección de Viewport y Orden de Carga)
 * Clase principal del módulo 'desktop'.
 */
class DesktopGame : ApplicationAdapter() {
    
    // --- Dependencias del Core ---
    private lateinit var juego: GameLogicService
    private lateinit var renderService: GdxRenderService
    private lateinit var inputService: GdxInputService

    // --- Gestión de Assets ---
    private lateinit var assetLoader: GdxAssetLoader
    private lateinit var batch: SpriteBatch
    
    // --- Objetos de LibGDX ---
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var uiBatch: SpriteBatch // Batch separado para UI (no usa la cámara)
    private lateinit var font: BitmapFont
    private lateinit var abilityFont: BitmapFont

    // --- Cámara y Viewport ---
    private lateinit var camera: OrthographicCamera
    // --- CAMBIO: Añadir Viewport ---
    private lateinit var viewport: Viewport
    private val WORLD_WIDTH = 800f
    private val WORLD_HEIGHT = 600f

    /**
     * Método 'create' de LibGDX.
     */
    override fun create() {
        // 1. Inicializar objetos de LibGDX
        shapeRenderer = ShapeRenderer()
        font = BitmapFont()
        abilityFont = BitmapFont()
        abilityFont.color = Color.YELLOW
        
        batch = SpriteBatch()
        uiBatch = SpriteBatch()
        
        // 2. Configurar Cámara y Viewport
        camera = OrthographicCamera()
        // --- CAMBIO: Usamos FitViewport ---
        // El viewport gestionará la cámara y el tamaño del mundo.
        viewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera)
        // (No necesitamos camera.setToOrtho(), el viewport lo hace)
        
        // --- CAMBIO CRÍTICO: Orden de Carga ---
        
        // 3. Cargar Assets
        // ¡DEBEMOS cargar los assets ANTES de crear el RenderService
        // que depende de ellos!
        assetLoader = GdxAssetLoader()
        assetLoader.loadAssets() // Carga real y bloqueante

        // 4. Crear las implementaciones de los servicios
        // Ahora que los assets están cargados, el RenderService
        // puede obtenerlos en su constructor sin problemas.
        renderService = GdxRenderService(batch, shapeRenderer, assetLoader, camera)
        inputService = GdxInputService()
        
        // 5. Crear el motor del juego (del 'core')
        juego = MiJuego()

        // 6. Configurar el juego
        juego.loadLevel("level1.txt")
        inputService.start()
    }

    /**
     * --- CAMBIO: Añadir método 'resize' ---
     * Se llama cuando la ventana se crea o cambia de tamaño.
     * Es ESENCIAL para que el Viewport funcione.
     */
    override fun resize(width: Int, height: Int) {
        // Actualiza el viewport y centra la cámara
        viewport.update(width, height, true)
    }

    /**
     * Método 'render' de LibGDX. Es el bucle principal.
     */
    override fun render() {
        
        // (Ya no necesitamos camera.update(), el viewport lo maneja)

        // --- Lógica de Actualización (Update) ---
        if (juego.getGameState() == GameState.Playing) {
            val actions = inputService.getActions()
            juego.update(actions, Gdx.graphics.deltaTime)
        } else if (juego.getGameState() == GameState.GameOver) {
            val actions = inputService.getActions()
            juego.update(actions, Gdx.graphics.deltaTime)
        }
        
        // --- Lógica de Dibujado (Render) ---
        
        // 1. Renderizado del mundo (sprites y formas)
        val worldState = juego.getWorldState()
        // Le pasamos el 'viewport' para que pueda aplicarlo
        renderService.renderWorld(worldState)

        // 2. Dibujar la UI (texto)
        // El 'uiBatch' usa su propia proyección (default)
        // para dibujar siempre en píxeles de pantalla.
        uiBatch.begin()
        if (juego.getGameState() == GameState.GameOver) {
            font.color = Color.RED
            font.draw(uiBatch, "GAME OVER", 300f, 350f)
            font.draw(uiBatch, "Presiona SHIFT para reiniciar", 250f, 300f)
        } else {
            font.color = Color.WHITE
            font.draw(uiBatch, "Dimensión Actual: ${worldState.currentDimension}", 10f, 580f)
            font.draw(uiBatch, juego.getGameInfo(), 10f, 560f)
            font.draw(uiBatch, "Vidas: ${juego.getLives()}", 10f, 540f)
            val player = juego.getPlayer()
            font.draw(uiBatch, "Fragmentos: ${player.energyFragments} / 3", 10f, 520f)
            if (player.canDoubleJump) {
                abilityFont.draw(uiBatch, "¡DOBLE SALTO DESBLOQUEADO!", 10f, 500f)
            }
        }
        uiBatch.end()
    }

    /**
     * Se llama al cerrar la aplicación. Libera los recursos.
     */
    override fun dispose() {
        assetLoader.dispose()
        batch.dispose()
        uiBatch.dispose()
        shapeRenderer.dispose()
        font.dispose()
        abilityFont.dispose()
        inputService.stop()
    }
}