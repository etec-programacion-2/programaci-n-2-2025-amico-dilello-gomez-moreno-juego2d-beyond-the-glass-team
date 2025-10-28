package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.RenderService
import org.example.core.WorldState

class GdxRenderService(private val shapeRenderer: ShapeRenderer) : RenderService {

    private val ghostColor = Color(1f, 0f, 0f, 0.3f)
    private val solidColor = Color.RED
    private var blink: Boolean = false

    override fun renderWorld(worldState: WorldState) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // Dibujar Plataformas
        worldState.platforms.forEach { platform ->
            if (platform.position.y == 20f) {
                shapeRenderer.color = Color.ROYAL
            } else if (platform.tangibleInDimension == worldState.currentDimension) {
                shapeRenderer.color = Color.ROYAL
            } else {
                shapeRenderer.color = Color.DARK_GRAY
            }
            shapeRenderer.rect(platform.position.x, platform.position.y, platform.size.x, platform.size.y)
        }

        // --- LÓGICA BTG-012: Invencibilidad ---
        // El "blink" (parpadeo) se alterna
        blink = !blink
        if (worldState.playerInvincible && blink) {
            // No dibujar al jugador (efecto de parpadeo)
        } else {
            // Dibujar Jugador
            val player = worldState.player
            shapeRenderer.color = Color.GREEN
            shapeRenderer.rect(player.position.x, player.position.y, player.size.x, player.size.y)
        }

        // Dibujar Enemigos
        worldState.enemies.forEach { enemy ->
            if (enemy.dimension == worldState.currentDimension) {
                shapeRenderer.color = solidColor
            } else {
                shapeRenderer.color = ghostColor
            }
            shapeRenderer.rect(enemy.position.x, enemy.position.y, enemy.size.x, enemy.size.y)
        }

        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    override fun drawSprite(sprite: Any, x: Float, y: Float) {}
    override fun render() {}
}
