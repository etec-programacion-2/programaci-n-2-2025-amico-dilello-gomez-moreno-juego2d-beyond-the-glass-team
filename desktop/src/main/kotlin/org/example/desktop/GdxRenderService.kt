package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.*

// Implementación de RenderService mejorada que usa un ShapeRenderer.
class GdxRenderService(
    private val shapeRenderer: ShapeRenderer
) : RenderService {

    // Este método ahora está obsoleto para nuestro propósito actual.
    override fun drawSprite(sprite: Any, x: Float, y: Float) {
        // Lógica futura para dibujar sprites (imágenes).
    }

    // Nuevo método para renderizar el estado completo del juego.
    fun renderGameState(player: Player?, levelData: LevelData?) {
        // 1. Limpiar la pantalla
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // 2. Dibujar con el ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // Dibujar plataformas
        levelData?.platforms?.forEach { platform ->
            if (player != null && platform.tangibleInDimension == player.currentDimension) {
                shapeRenderer.color = Color.ROYAL
            } else {
                shapeRenderer.color = Color.DARK_GRAY
            }
            shapeRenderer.rect(platform.position.x, platform.position.y, platform.size.x, platform.size.y)
        }

        // Dibujar jugador
        player?.let {
            shapeRenderer.color = Color.GREEN
            shapeRenderer.rect(it.position.x, it.position.y, it.size.x, it.size.y)
        }

        shapeRenderer.end()
    }

    // El método render original de la interfaz ahora está vacío,
    // ya que la lógica principal de renderizado está en renderGameState.
    override fun render() {
        // La limpieza y el dibujado se manejan en renderGameState.
    }
}