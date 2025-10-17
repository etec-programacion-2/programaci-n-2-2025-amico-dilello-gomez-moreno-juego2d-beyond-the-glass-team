package org.example.desktop

import com.badlogic.gdx.Game
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport

class MainMenuScreen : ScreenAdapter() {

    private val stage = Stage(ScreenViewport())
    
    private val LOGO_PATH = "assets/menu/definitivo/menu_principal/buttons/imgs/beyond_the_glass_logo_menu.png"
    private val PLAY_BUTTON_PATH = "assets/menu/definitivo/menu_principal/buttons/imgs/play_button.png"
    private val SETTINGS_BUTTON_PATH = "assets/menu/definitivo/menu_principal/buttons/imgs/settings_button.png" 
    private val EXIT_BUTTON_PATH = "assets/menu/definitivo/menu_principal/buttons/imgs/exit_button.png"

    override fun show() {
        Gdx.input.inputProcessor = stage

        val playButton = MenuButton(PLAY_BUTTON_PATH)
        val settingsButton = MenuButton(SETTINGS_BUTTON_PATH)
        val exitButton = MenuButton(EXIT_BUTTON_PATH)
        val logo = MenuButton(LOGO_PATH)

        // Lógica de los botones (ClickListener)
        playButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                println("Jugar Presionado - Cambiando a GameScreen...")
                val game = Gdx.app.applicationListener as Game
                
                // 2. Llama a setScreen() para cambiar a la pantalla del juego
                // NOTA: Debes reemplazar 'GameScreen()' con el nombre real de tu clase de juego.
                game.setScreen(DesktopGame())
            }
        })
        
        settingsButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                println("Ajustes Presionado - Cambiando a SettingsScreen...")
                val game = Gdx.app.applicationListener as Game
                
                // 3. Llama a setScreen() para cambiar a la pantalla de ajustes
                // NOTA: Debes reemplazar 'SettingsScreen()' con el nombre real de tu clase de ajustes.
                game.setScreen(SettingsScreenMainMenu(game)) 
            }
        })

        exitButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                println("Saliendo del juego...")
                Gdx.app.exit() // Cierra la aplicación
            }
        })

        logo.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                println("Logo clickeado")
            }
        })

        // Configuración de la Tabla (Table) para organizar los botones
        val table = Table()
                table.setFillParent(true) 
                
                // --- PARÁMETROS DE TAMAÑO Y POSICIÓN ---
                val LOGO_WIDTH = 900f     
                val LOGO_HEIGHT = 600f    
                val TOP_OFFSET_PAD = 10f  // Ajustado para acercar el logo al borde superior
                
                val BUTTON_WIDTH = 400f   
                val BUTTON_HEIGHT = 100f  
                val EXIT_BUTTON_WIDTH = BUTTON_WIDTH * 2 + 30f
        // ---------------------------------------

        // Desactivamos defaults para que no estire las celdas automáticamente
        table.defaults().reset() 

        // FILA 1: LOGO
        table.add(logo)
            .width(LOGO_WIDTH)
            .height(LOGO_HEIGHT)
            .padTop(TOP_OFFSET_PAD) 
            .padTop(40f)
            .colspan(2) 
            .row()

        // ==============================================
        // FILA 2: TABLA ANIDADA (JUGAR Y AJUSTES) - SOLUCIÓN DE CERCANÍA
        // ==============================================
        val actionButtonsTable = Table()

        // Botón Jugar (Columna 1 de la tabla interna)
        actionButtonsTable.add(playButton)
            .width(BUTTON_WIDTH)
            .height(BUTTON_HEIGHT)
            .padRight(5f)
            .padLeft(157f)

        // Botón Ajustes (Columna 2 de la tabla interna)
        actionButtonsTable.add(settingsButton)
            .width(BUTTON_WIDTH)
            .height(BUTTON_HEIGHT)
            .padLeft(-217f) // Separación de 5px a la izquierda
            .padRight(100f)
            .row()
        
        // Insertamos la tabla anidada en la tabla principal
        // Esto centra el grupo Jugar/Ajustes como una única unidad.
        table.add(actionButtonsTable)
            .colspan(2)
            .row()

        // ==============================================
        // FILA 3: BOTÓN "SALIR" EN UNA FILA SEPARADA
        // ==============================================
        table.add(exitButton)
            .width(EXIT_BUTTON_WIDTH)
            .height(BUTTON_HEIGHT)
            .padTop(20f) // Más separación del bloque superior
            .padBottom(230f) // Esto empuja todo el contenido hacia arriba
            .colspan(2)
            .row()

        // FILA 4: CELDA DE EXPANSIÓN (Mantiene el contenido agrupado en la parte superior)
        table.add().expandY().colspan(2).row()
        
        stage.addActor(table)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true) 
    }

    override fun dispose() {
        stage.dispose()
    }
}