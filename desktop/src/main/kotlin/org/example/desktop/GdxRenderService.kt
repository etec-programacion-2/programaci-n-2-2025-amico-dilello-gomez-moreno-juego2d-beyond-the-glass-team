package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.example.core.RenderService
import org.example.core.MiJuego

/**
 * ADAPTADOR LibGDX para RenderService.
 * DIP: Implementa la interfaz de RenderService usando las herramientas de LibGDX (SpriteBatch, etc.).
 */
class GdxRenderService(private val juego: MiJuego) : RenderService {
    private val batch = SpriteBatch()
    private val font = BitmapFont()
    
    override fun drawSprite(sprite: Any, x: Float, y: Float) {
        // Lógica de dibujo de sprites de LibGDX (se implementará más adelante).
    }

    override fun render() {
        // 1. Limpiar la pantalla
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        
        batch.begin()
        
        // 2. Dibujar información de debug y UI
        font.draw(batch, "Beyond the Glass - Desktop", 10f, 580f)
        font.draw(batch, juego.getGameInfo(), 10f, 550f) 
        
        // 3. Dibujar al jugador
        if (juego.isRunning()) {
            val player = juego.getPlayer()
            // Placeholder: Dibuja un texto en la posición actual del jugador
            font.draw(batch, "P", player.position.x, player.position.y) 
        }
        
        batch.end()
    }
    
    fun dispose() {
        batch.dispose()
        font.dispose()
    }
}
