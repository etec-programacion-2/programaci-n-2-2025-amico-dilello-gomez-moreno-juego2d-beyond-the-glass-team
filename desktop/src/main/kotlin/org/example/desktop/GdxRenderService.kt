package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.RenderService
import org.example.core.WorldState

class GdxRenderService(private val shapeRenderer: ShapeRenderer) : RenderService {

    override fun renderWorld(worldState: WorldState) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        worldState.platforms.forEach { platform ->
            
            // --- INICIO DE LA SOLUCIÓN VISUAL ---
            // Regla especial para el suelo: si la plataforma está en y=20, siempre es azul.
            if (platform.position.y == 0f) {
                shapeRenderer.color = Color.ROYAL
            }
            // Lógica normal para el resto de plataformas
            else if (platform.tangibleInDimension == worldState.currentDimension) {
                shapeRenderer.color = Color.ROYAL
            } else {
                shapeRenderer.color = Color.DARK_GRAY
            }
            // --- FIN DE LA SOLUCIÓN VISUAL ---
            
            shapeRenderer.rect(platform.position.x, platform.position.y, platform.size.x, platform.size.y)
        }

        val player = worldState.player
        shapeRenderer.color = Color.GREEN
        shapeRenderer.rect(player.position.x, player.position.y, player.size.x, player.size.y)

        shapeRenderer.end()
    }

    override fun drawSprite(sprite: Any, x: Float, y: Float) {}
    override fun render() {}
}