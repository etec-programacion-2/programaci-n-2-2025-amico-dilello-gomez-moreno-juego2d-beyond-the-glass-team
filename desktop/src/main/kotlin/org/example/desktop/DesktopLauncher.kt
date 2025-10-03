package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import org.example.core.* // Importar todo el core

/**
 * Función main: Punto de entrada de la aplicación de escritorio.
 */
fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setTitle("Beyond the Glass")
    config.setWindowedMode(800, 600)
    Lwjgl3Application(DesktopGameAdapter(), config)
}

/**
 * ADAPTADOR PRINCIPAL DE LIBGDX: Une el ciclo de vida de LibGDX (create, render, dispose) 
 * con el GameEngine del CORE.
 */
class DesktopGameAdapter : ApplicationAdapter() {
    
    private lateinit var gameEngine: GameEngine
    private lateinit var juego: MiJuego
    
    // Adaptadores para la inyección de dependencias
    private lateinit var gdxRenderService: GdxRenderService 
    private lateinit var gdxInputService: GdxInputService
    private lateinit var gdxGameStateManager: GdxGameStateManager

    override fun create() {
        // 1. Cargar Nivel (Usamos el LevelLoader del CORE)
        val levelLoader = LevelLoader()
        val levelData = levelLoader.loadLevel("level1.txt") 

        // 2. Inicializar el Juego (MiJuego del CORE)
        juego = MiJuego()
        juego.initializeLevel(levelData)
        juego.startGame("Leoric")
        
        // 3. Inicializar los ADAPTADORES (Implementaciones concretas)
        gdxRenderService = GdxRenderService(juego) 
        gdxInputService = GdxInputService()
        gdxGameStateManager = GdxGameStateManager(gdxRenderService, juego)

        // 4. Inicializar el Motor (GameEngine del CORE)
        // Se inyectan las dependencias (DIP)
        gameEngine = GameEngine(gdxRenderService, gdxInputService, juego, gdxGameStateManager)
    }

    override fun render() {
        // Llama al bucle principal del CORE en cada frame.
        gameEngine.updateFrame()
        
        // Lógica de la plataforma: Salir con ESC
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
    }

    override fun dispose() {
        juego.stopGame()
        gdxRenderService.dispose() 
    }
}