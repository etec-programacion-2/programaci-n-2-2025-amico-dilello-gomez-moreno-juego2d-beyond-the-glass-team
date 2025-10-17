package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer // Importar ShapeRenderer
import org.example.core.GameEngine
import org.example.core.LevelLoader
import org.example.core.MiJuego

class DesktopGame : ApplicationAdapter() {
    private lateinit var gameEngine: GameEngine
    private lateinit var juego: MiJuego
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var shapeRenderer: ShapeRenderer // Nuevo

    override fun create() {
        batch = SpriteBatch()
        font = BitmapFont()
        shapeRenderer = ShapeRenderer() // Inicializarlo

        val renderService = GdxRenderService(shapeRenderer) // Pasarlo al servicio
        val inputService = GdxInputService()
        juego = MiJuego()

        // Cargar el nivel
        try {
            val levelLoader = LevelLoader()
            val levelData = levelLoader.loadLevel("level1.txt")
            juego.loadLevel(levelData)
        } catch (e: Exception) {
            e.printStackTrace()
            Gdx.app.exit() // Salir si el nivel no carga
        }

        val gameStateManager = GdxGameStateManager(renderService, juego)
        
        gameEngine = GameEngine(renderService, inputService, juego, gameStateManager)
        juego.startGame("Jugador Desktop")
    }

    override fun render() {
        gameEngine.updateFrame()

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
        
        // Dibuja la puntuación sobre el renderizado del juego
        batch.begin()
        font.draw(batch, juego.getGameInfo(), 10f, Gdx.graphics.height - 20f)
        batch.end()
    }
    
    override fun dispose() {
        batch.dispose()
        font.dispose()
        shapeRenderer.dispose() // Liberar el recurso
    }
}
