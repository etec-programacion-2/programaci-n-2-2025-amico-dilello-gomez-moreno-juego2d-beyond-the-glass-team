package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.MiJuego

/**
 * La clase principal del juego de escritorio. Ahora es la única responsable
 * de orquestar el bucle de juego, sin intermediarios.
 */
class DesktopGame : ApplicationAdapter() {
    // ELIMINADO: Ya no se necesita una referencia a GameEngine.

    // Las referencias a los servicios y la lógica se mantienen.
    private lateinit var juego: MiJuego
    private lateinit var renderService: GdxRenderService
    private lateinit var inputService: GdxInputService
    private lateinit var shapeRenderer: ShapeRenderer

    override fun create() {
        shapeRenderer = ShapeRenderer()
        
        renderService = GdxRenderService(shapeRenderer)
        inputService = GdxInputService()
        juego = MiJuego()

        juego.loadLevel("level1.txt")
        inputService.start()
    }

    override fun render() {
        // El bucle de juego es ahora más directo:
        val action = inputService.getAction()
        juego.update(action, Gdx.graphics.deltaTime)
        val worldState = juego.getWorldState()
        renderService.renderWorld(worldState)

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
    }

    override fun dispose() {
        shapeRenderer.dispose()
        inputService.stop()
    }
}
