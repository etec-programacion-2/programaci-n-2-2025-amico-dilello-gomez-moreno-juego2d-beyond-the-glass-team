package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Game
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport

// Asegúrate de que MenuButton.kt existe y está en el mismo paquete!

class MainMenuScreen : ScreenAdapter() {

    // Variables de la clase (pueden estar al inicio)
    private val stage = Stage(ScreenViewport())
    
    // Las rutas de los assets deben ser las que verificaste que SÍ existen.
    private val LOGO_PATH = "assets/menu/definitivo/menu_principal/buttons/imgs/beyond_the_glass_logo_menu.png"
    private val PLAY_BUTTON_PATH = "assets/menu/definitivo/menu_principal/buttons/imgs/play_button.png"
    private val SETTINGS_BUTTON_PATH = "assets/menu/definitivo/menu_principal/buttons/imgs/settings_button.png" // O settings_buttonv3.png si no lo renombraste
    private val EXIT_BUTTON_PATH = "assets/menu/definitivo/menu_principal/buttons/imgs/exit_button.png"

    // **********************************************
    // 1. MÉTODO SHOW(): Se llama cuando la pantalla se muestra (inicialización)
    // **********************************************
    override fun show() {
        Gdx.input.inputProcessor = stage

        val playButton = MenuButton(PLAY_BUTTON_PATH)
        val settingsButton = MenuButton(SETTINGS_BUTTON_PATH)
        val exitButton = MenuButton(EXIT_BUTTON_PATH)
        val logo = MenuButton(LOGO_PATH)

        // Lógica de los botones (ClickListener)
        playButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                // TODO: Cambiar a la pantalla de juego real (GameScreen)
                println("Jugar Presionado")
            }
        })
        
        settingsButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                // TODO: Cambiar a la pantalla de ajustes (SettingsScreen)
                println("Ajustes Presionado")
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
                println("Logo")
            }
        })

        // Configuración de la Tabla (Table) para organizar los botones
        val table = Table()
        table.setFillParent(true) // Ocupa toda la pantalla
        table.center() // Centra el contenido de la tabla

        // Añadir elementos a la tabla (asegúrate de que esto no esté en la línea 45)
        table.add(logo).pad(10f).row()
        table.add(playButton).pad(10f).row()
        table.add(settingsButton).pad(10f).row()
        table.add(exitButton).pad(10f).row()
        stage.addActor(table)
    } // FIN DE show()

    // **********************************************
    // 2. MÉTODO RENDER(): Se llama en cada frame (dibujo)
    // **********************************************
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta)
        stage.draw()
    }

    // **********************************************
    // 3. MÉTODO RESIZE(): Se llama al cambiar el tamaño de la ventana
    //    ¡ESTO ES VITAL para solucionar el estiramiento!
    //    Esta función DEBE estar fuera de show() y render().
    // **********************************************
    override fun resize(width: Int, height: Int) {
        // Llama a update en el Viewport para ajustarse al nuevo tamaño de la ventana
        stage.viewport.update(width, height, true) 
    }

    // **********************************************
    // 4. Otros métodos (como dispose para liberar recursos)
    // **********************************************
    override fun dispose() {
        stage.dispose()
        // Libera las texturas de MenuButton si las cargaste manualmente, para evitar fugas de memoria
    }
}