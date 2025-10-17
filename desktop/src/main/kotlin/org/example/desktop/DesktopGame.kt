package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.GameEngine
import org.example.core.MiJuego

/**
 * La clase principal del juego de escritorio, ahora adaptada a la nueva arquitectura.
 */
class DesktopGame : ApplicationAdapter() {
    // CAMBIO: Ya no necesitamos un GameEngine complejo aquí, la lógica está en MiJuego.
    private lateinit var juego: MiJuego
    private lateinit var renderService: GdxRenderService
    private lateinit var inputService: GdxInputService
    
    // CAMBIO: Usamos ShapeRenderer en lugar de SpriteBatch.
    private lateinit var shapeRenderer: ShapeRenderer

    override fun create() {
        // Inicializamos los componentes nuevos.
        shapeRenderer = ShapeRenderer()
        
        // Creamos las implementaciones de los servicios para desktop.
        renderService = GdxRenderService(shapeRenderer)
        inputService = GdxInputService()
        juego = MiJuego() // Nuestra lógica principal del juego.

        // Cargamos el nivel al iniciar.
        juego.loadLevel("level1.txt")
        
        // Iniciamos el servicio de input si es necesario (según la nueva interfaz).
        inputService.start()
    }

    override fun render() {
        // Este es el nuevo bucle de juego, más limpio.
        
        // 1. OBTENER ENTRADA
        val action = inputService.getAction()

        // 2. ACTUALIZAR LÓGICA
        // Pasamos la acción y el tiempo delta a la lógica del juego.
        juego.update(action, Gdx.graphics.deltaTime)

        // 3. RENDERIZAR ESTADO
        // Obtenemos el estado actual del mundo y se lo pasamos al servicio de renderizado.
        val worldState = juego.getWorldState()
        renderService.renderWorld(worldState)

        // Lógica de salida del juego.
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
    }

    override fun dispose() {
        // Liberamos los recursos.
        shapeRenderer.dispose()
        inputService.stop()
    }
}