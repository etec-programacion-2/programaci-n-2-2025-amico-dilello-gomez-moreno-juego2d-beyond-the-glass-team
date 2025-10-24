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
    // Ya no hay referencias a GameEngine o GameStateManager

    private lateinit var juego: MiJuego
    private lateinit var renderService: GdxRenderService
    private lateinit var inputService: GdxInputService
    private lateinit var shapeRenderer: ShapeRenderer

    override fun create() {
        shapeRenderer = ShapeRenderer()
        
        // Creamos los servicios y la lógica directamente
        renderService = GdxRenderService(shapeRenderer)
        inputService = GdxInputService()
        juego = MiJuego()

        juego.loadLevel("level1.txt")
        inputService.start()
    }

    override fun render() {
        // El bucle de juego es ahora directo y limpio:
        
        // 1. INPUT
        val action = inputService.getAction()
        
        // 2. UPDATE
        juego.update(action, Gdx.graphics.deltaTime)
        
        // 3. RENDER
        val worldState = juego.getWorldState()
        renderService.renderWorld(worldState)

        // Salida
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
    }

    override fun dispose() {
        shapeRenderer.dispose()
        inputService.stop()
    }
}