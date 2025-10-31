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

class DesktopGame : ApplicationAdapter() {
    
    private lateinit var juego: GameLogicService
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
        juego = MiJuego()

        juego.loadLevel("level1.txt")
        inputService.start()
    }

    override fun render() {
        
        // CORRECCIÓN: Usar el objeto de la sealed class
        if (juego.getGameState() == GameState.Playing) {
            val actions = inputService.getActions()
            juego.update(actions, Gdx.graphics.deltaTime)
        } else if (juego.getGameState() == GameState.GameOver) {
            val actions = inputService.getActions()
            juego.update(actions, Gdx.graphics.deltaTime)
        }
        
        val worldState = juego.getWorldState()
        renderService.renderWorld(worldState)

        batch.begin()
        
        // CORRECCIÓN: Usar el objeto de la sealed class
        if (juego.getGameState() == GameState.GameOver) {
            font.color = com.badlogic.gdx.graphics.Color.RED
            font.draw(batch, "GAME OVER", 300f, 350f)
            font.draw(batch, "Presiona SHIFT para reiniciar", 250f, 300f)
        } else {
            font.color = com.badlogic.gdx.graphics.Color.WHITE
            font.draw(batch, "Dimensión Actual: ${worldState.currentDimension}", 10f, 580f)
            font.draw(batch, juego.getGameInfo(), 10f, 560f)
            font.draw(batch, "Vidas: ${juego.getLives()}", 10f, 540f)
        }
        
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
