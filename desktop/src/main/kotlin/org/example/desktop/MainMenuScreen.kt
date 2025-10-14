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
            }
        })
        
        settingsButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                println("Ajustes Presionado - Cambiando a SettingsScreen...")
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
        table.setFillParent(true) // Ocupa toda la pantalla
        
        // --- PARÁMETROS DE TAMAÑO Y POSICIÓN ---
        val LOGO_WIDTH = 900f     // Ancho fijo para el logo 
        val LOGO_HEIGHT = 600f    // Alto fijo para el logo
        val TOP_OFFSET_PAD = 10f  // Espacio del borde superior. Lo hemos bajado para subir el logo.
        
        val BUTTON_WIDTH = 400f   // Ancho de los botones laterales (Jugar)
        val BUTTON_HEIGHT = 100f  // Alto de los botones laterales
        
        val EXIT_BUTTON_WIDTH = BUTTON_WIDTH * 2 + 30f // Para hacer más ancho el botón "Salir"
        // ---------------------------------------

        // ==============================================
        // FILA 1: LOGO (OCUPA 2 COLUMNAS)
        // ==============================================
        table.add(logo)
            .width(LOGO_WIDTH)
            .height(LOGO_HEIGHT)
            .padTop(TOP_OFFSET_PAD) 
            .padBottom(5f)         
            .colspan(2) // Permite que el logo se centre sobre las 2 columnas siguientes
            .row()

        // ==============================================
        // FILA 2: BOTONES DE ACCIÓN (JUGAR Y AJUSTES) EN LA MISMA FILA
        // ==============================================
        // Asegurándonos de que las columnas no se estiren más de lo necesario
        table.defaults().pad(5f).expandX().fillX()

        table.add(playButton)
            .width(BUTTON_WIDTH)
            .height(BUTTON_HEIGHT)
            .padRight(10f) // Separa 10px a la derecha del botón "Jugar"
        
        table.add(settingsButton)
            .width(BUTTON_WIDTH)
            .height(BUTTON_HEIGHT)
            .padLeft(10f)  // Separa 10px a la izquierda del botón "Ajustes"
            .row()

        // ==============================================
        // FILA 3: BOTÓN "SALIR" EN UNA FILA SEPARADA
        // ==============================================
        table.add(exitButton)
            .width(EXIT_BUTTON_WIDTH)
            .height(BUTTON_HEIGHT)
            .padTop(10f) // Separación de los botones superiores
            .colspan(2)  // El botón "Salir" ocupa el ancho completo
            .row()

        // ==============================================
        // FILA 4: CELDA DE EXPANSIÓN
        // ==============================================
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