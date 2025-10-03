package org.example.desktop

import org.example.core.GameState
import org.example.core.GameStateManager
import org.example.core.RenderService
import org.example.core.MiJuego

/**
 * ADAPTADOR LibGDX para GameStateManager.
 * DIP: Implementa la interfaz de GameStateManager.
 */
class GdxGameStateManager(
    private val renderService: RenderService,
    private val juego: MiJuego
) : GameStateManager {
    
    override fun changeState(newState: GameState) {
        // Aquí se implementarían las transiciones entre pantallas (ej. Pausa, Menú).
    }
    
    override fun update() {
        // En LibGDX, el GameEngine llama a MiJuego.updateGame() directamente.
        // Este método podría usarse para lógica específica de la plataforma.
    }
    
    override fun render() {
        // Llama al renderizador para dibujar el frame actual.
        renderService.render()
    }
}
