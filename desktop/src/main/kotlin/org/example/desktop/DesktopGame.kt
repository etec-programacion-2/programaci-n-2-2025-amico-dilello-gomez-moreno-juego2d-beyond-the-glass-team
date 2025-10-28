package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.GameLogicService // <-- Importar la interfaz
import org.example.core.MiJuego

class DesktopGame : ApplicationAdapter() {
    
    // --- CORRECCIÓN SOLID ---
    // Dependemos de la abstracción (GameLogicService)
    private lateinit var juego: GameLogicService
    // ------------------------

    private lateinit var renderService: GdxRenderService
    private lateinit var inputService: GdxInputService
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont

    override fun create() {
        shapeRenderer = ShapeRenderer()
        batch = SpriteBatch()
        font = BitmapFont()

        renderService = GdxRenderService(shapeRenderer)
        inputService = GdxInputService()
        
        // La instanciación de la clase concreta se hace aquí (Composition Root)
        juego = MiJuego()

        juego.loadLevel("level1.txt")
        inputService.start()
    }

    override fun render() {
        val actions = inputService.getActions()
        juego.update(actions, Gdx.graphics.deltaTime)
        
        // Ahora llamamos a los métodos de la interfaz
        val worldState = juego.getWorldState()
        
        renderService.renderWorld(worldState)

        batch.begin()
        font.draw(batch, "Dimensión Actual: ${worldState.currentDimension}", 10f, 580f)
        font.draw(batch, juego.getGameInfo(), 10f, 560f)
        batch.end()

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
    }

    override fun dispose() {
        shapeRenderer.dispose()
        inputService.stop()
        batch.dispose()
        font.dispose()
    }
}
