package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.MiJuego

class DesktopGame : ApplicationAdapter() {
    private lateinit var juego: MiJuego
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
        // Llama a getActions() que devuelve un Set
        val actions = inputService.getActions()
        
        // Pasa el Set a la lógica del juego
        juego.update(actions, Gdx.graphics.deltaTime)
        
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