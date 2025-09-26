package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

import org.example.core.GameEngine
import org.example.core.MiJuego

/**
 * La clase principal del juego de escritorio, extiende de ApplicationAdapter de LibGDX.
 * Este archivo solo contiene el punto de entrada y la lógica del bucle principal de LibGDX.
 */
class DesktopGame : ApplicationAdapter() {
    private lateinit var gameEngine: GameEngine
    private lateinit var juego: MiJuego
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    
    // Se llama una vez al inicio del juego para inicializar recursos.
    override fun create() {
        batch = SpriteBatch()
        font = BitmapFont()
        
        // **IMPORTANTE:** Aquí solo se instancian, NO se definen las clases de servicio.
        val renderService = GdxRenderService(batch, font)
        val inputService = GdxInputService()
        juego = MiJuego()
        
        // Se crea el gestor de estados.
        val gameStateManager = GdxGameStateManager(renderService, juego)
        
        // Se inyectan las cuatro dependencias en el motor del juego.
        gameEngine = GameEngine(renderService, inputService, juego, gameStateManager)
        juego.startGame("Jugador Desktop")
    }

    // Se llama en cada frame (LibGDX Game Loop).
    override fun render() {
        // El bucle de LibGDX llama a este método, que a su vez llama a la orquestación del motor.
        gameEngine.updateFrame()

        // Lógica de salida específica de LibGDX (debe ir aquí).
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
        
        // Dibuja la puntuación del juego sobre el renderizado del GameState
        batch.begin()
        font.draw(batch, juego.getGameInfo(), 10f, 550f)
        batch.end()
    }
    
    // Se llama cuando el juego se cierra para liberar recursos.
    override fun dispose() {
        batch.dispose()
        font.dispose()
        gameEngine.stop()
    }
}