package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import org.example.core.GameLogicService
import org.example.core.GameState
import org.example.core.MiJuego
import org.example.core.Player // (Importar Player para la constante de BTG-013)

class DesktopGame : ApplicationAdapter() {
    
    private lateinit var juego: GameLogicService
    private lateinit var renderService: GdxRenderService
    private lateinit var inputService: GdxInputService
    private lateinit var assetLoader: GdxAssetLoader // (BTG-014)
    
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont

    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport

    companion object {
        const val V_WIDTH = 800f
        const val V_HEIGHT = 600f
    }

    override fun create() {
        shapeRenderer = ShapeRenderer()
        batch = SpriteBatch()
        font = BitmapFont()
        camera = OrthographicCamera()
        
        viewport = FitViewport(V_WIDTH, V_HEIGHT, camera)
        camera.setToOrtho(false, V_WIDTH, V_HEIGHT)

        // --- ORDEN DE CARGA CORREGIDO (BTG-014) ---
        // 1. Cargar Assets
        assetLoader = GdxAssetLoader()
        assetLoader.loadAssets()

        // 2. Inicializar Servicios (con assets ya cargados)
        renderService = GdxRenderService(batch, shapeRenderer, assetLoader, camera)
        inputService = GdxInputService()
        juego = MiJuego()
        // --- Fin BTG-014 ---

        juego.loadLevel("level1.txt")
        inputService.start()
    }

    override fun render() {
        val deltaTime = Gdx.graphics.deltaTime
        
        // 1. Lógica del Juego
        if (juego.getGameState() == GameState.Playing) {
            val actions = inputService.getActions()
            juego.update(actions, deltaTime)
        } else if (juego.getGameState() == GameState.GameOver) {
            val actions = inputService.getActions()
            juego.update(actions, deltaTime) // MiJuego maneja el reinicio
        }
        
        // 2. Renderizado del Mundo
        // (El renderService usa la cámara del mundo (camera.combined) internamente)
        val worldState = juego.getWorldState()
        renderService.renderWorld(worldState, deltaTime) // Pasamos deltaTime para animaciones

        // 3. Renderizado de UI (encima de todo)
        
        // --- SOLUCIÓN AL ERROR DE COMPILACIÓN ---
        // Reseteamos la cámara del batch a las coordenadas de la pantalla
        // (El método antiguo y compatible)
        batch.projectionMatrix.setToOrtho2D(0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        
        batch.begin()
        
        if (juego.getGameState() == GameState.GameOver) {
            font.color = com.badlogic.gdx.graphics.Color.RED
            // (Ajuste de coordenadas de UI para la nueva cámara)
            font.draw(batch, "GAME OVER", viewport.screenWidth / 2f - 40f, viewport.screenHeight / 2f + 50f)
            font.draw(batch, "Presiona SHIFT para reiniciar", viewport.screenWidth / 2f - 100f, viewport.screenHeight / 2f)
        } else {
            font.color = com.badlogic.gdx.graphics.Color.WHITE
            // (Coordenadas de UI ajustadas para la nueva cámara)
            font.draw(batch, "Dimension Actual: ${worldState.currentDimension}", 10f, viewport.screenHeight - 20f)
            font.draw(batch, "Vidas: ${juego.getLives()}", 10f, viewport.screenHeight - 40f)

            // --- UI DE BTG-013 (LA QUE FALTABA) ---
            font.draw(batch, "Fragmentos: ${juego.getPlayerFragments()} / ${Player.FRAGMENTS_TO_UNLOCK}", 10f, viewport.screenHeight - 60f)
            if (juego.canPlayerDoubleJump()) {
                font.color = com.badlogic.gdx.graphics.Color.YELLOW
                font.draw(batch, "¡DOBLE SALTO DESBLOQUEADO!", 10f, viewport.screenHeight - 80f)
            }
        }
        
        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        // (Añadido 'true' para centrar la cámara)
        viewport.update(width, height, true) 
    }

    override fun dispose() {
        shapeRenderer.dispose()
        batch.dispose()
        font.dispose()
        assetLoader.dispose() // (BTG-014)
    }
}