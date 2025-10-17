package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Game
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport

// NOTA: Debes ajustar esta ruta a una imagen de botón que ya tengas, o usar la misma de "Jugar"
private const val BACK_BUTTON_PATH = "assets/menu/definitivo/menu_principal/buttons/imgs/exit_button.png"

class SettingsScreenMainMenu(private val game: Game) : ScreenAdapter() {
    
    private lateinit var stage: Stage
    private lateinit var font: BitmapFont

    override fun show() {
        // Inicialización básica
        stage = Stage(ScreenViewport())
        Gdx.input.inputProcessor = stage
        
        // Inicialización de la fuente: La forma más básica que no requiere archivos externos
        font = BitmapFont() 
        font.setColor(Color.WHITE) // Establecemos el color de la fuente

        val table = Table()
        table.setFillParent(true)
        stage.addActor(table)

        // Estilo del Label (Título)
        val labelStyle = Label.LabelStyle(font, Color.WHITE)
        
        // Título simple usando Label
        val titleLabel = Label("AJUSTES", labelStyle)
        table.add(titleLabel).pad(100f).row()
        
        // Espacio para los ajustes
        table.add().expandY().row() 

        // Botón de Volver (Usamos MenuButton para evitar dependencia de Skin)
        val backButton = MenuButton(BACK_BUTTON_PATH) 
        
        backButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                println("Volviendo al Menú Principal...")
                // Cambia a la pantalla del menú principal
                game.setScreen(MainMenuScreen()) 
                dispose()
            }
        })
        
        // Añadir el botón Volver al final
        // Puedes ajustar el ancho y alto del botón aquí
        table.add(backButton).width(400f).height(100f).pad(30f).row()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f) // Color de fondo gris oscuro
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        stage.dispose()
        font.dispose() // Es crucial liberar la fuente que creamos en show()
    }
}