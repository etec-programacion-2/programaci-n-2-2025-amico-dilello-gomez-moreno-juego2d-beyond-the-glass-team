package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.example.core.GameEngine
import org.example.core.MiJuego
import org.example.core.RenderService
import org.example.core.InputService
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.Input

// La clase principal del juego de escritorio, que se ejecuta dentro del bucle de LibGDX.
class DesktopGame : ApplicationAdapter() {
    private lateinit var gameEngine: GameEngine
    private lateinit var juego: MiJuego
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont

    // Se llama una vez al inicio del juego para inicializar recursos.
    override fun create() {
        batch = SpriteBatch()
        font = BitmapFont()

        val renderService = GdxRenderService(batch, font)
        val inputService = GdxInputService()
        juego = MiJuego()

        // Se inyectan las dependencias en el motor del juego.
        gameEngine = GameEngine(renderService, inputService, juego)
        juego.startGame("Jugador Desktop")
    }

    // Se llama en cada frame para actualizar y dibujar el juego.
    override fun render() {
        // Se usa el servicio de renderizado del motor para dibujar.
        val renderService = (gameEngine::class.members.find { it.name == "renderService" }?.call(gameEngine) as? RenderService)
        renderService?.render()

        // Se dibuja la información del juego directamente.
        batch.begin()
        font.draw(batch, juego.getGameInfo(), 10f, 550f)

        // Se usa el servicio de entrada para detectar si se presiona la tecla de escape.
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
        batch.end()
    }

    // Se llama cuando el juego se cierra para liberar recursos.
    override fun dispose() {
        batch.dispose()
        font.dispose()
        juego.stopGame()
    }
}