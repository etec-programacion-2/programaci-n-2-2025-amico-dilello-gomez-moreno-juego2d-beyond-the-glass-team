package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.example.core.MiJuego

class DesktopGame : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var juego: MiJuego
    
    override fun create() {
        batch = SpriteBatch()
        font = BitmapFont()
        
        // Usar la lógica del core
        juego = MiJuego()
        juego.startGame("Jugador Desktop")
        juego.updateScore(200)
    }
    
    override fun render() {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        
        batch.begin()
        
        // Mostrar información del juego del core
        font.draw(batch, "Beyond the Glass - Desktop", 10f, 580f)
        font.draw(batch, juego.getGameInfo(), 10f, 550f)
        font.draw(batch, "Presiona ESC para salir", 10f, 520f)
        
        batch.end()
        
        // Salir con ESC
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
    }
    
    override fun dispose() {
        batch.dispose()
        font.dispose()
        juego.stopGame()
    }
}

fun main() {
    val config = Lwjgl3ApplicationConfiguration().apply {
        setTitle("Beyond the Glass")
        setWindowedMode(800, 600)
        useVsync(true)
    }
    Lwjgl3Application(DesktopGame(), config)
}