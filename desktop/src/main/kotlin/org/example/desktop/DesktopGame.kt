package org.example.desktop 

import com.badlogic.gdx.Game // Importamos la clase Game

/**
 * Clase principal del juego. Extiende de Game para poder manejar múltiples pantallas (menus, niveles, etc.).
 * Nota: Tu DesktopLauncher llama a esta clase para iniciar el juego.
 */
class DesktopGame : Game() { // <-- Asegúrate de usar 'Game'

    // Este método se llama una vez cuando el juego se inicia.
    override fun create() {
        // Creamos una instancia de la pantalla del menú principal
        val mainMenu = MainMenuScreen()
        
        // Le decimos al juego que muestre esa pantalla.
        // Esto hace que el método render() de MainMenuScreen sea el que se ejecute.
        setScreen(mainMenu) 
    }

    // Los métodos render() y dispose() de esta clase (DesktopGame) 
    // generalmente se dejan vacíos o se usan para recursos globales, 
    // ya que la pantalla actual (MainMenuScreen) maneja el dibujo.
}