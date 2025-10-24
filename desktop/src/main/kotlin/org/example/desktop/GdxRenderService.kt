package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.RenderService
import org.example.core.WorldState

// Implementación de RenderService para el entorno de escritorio con LibGDX.
class GdxRenderService(private val shapeRenderer: ShapeRenderer) : RenderService {

    // MÉTODO CENTRAL ACTUALIZADO PARA CUMPLIR CON LA NUEVA INTERFAZ
    override fun renderWorld(worldState: WorldState) {
        // Limpia la pantalla
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Prepara el ShapeRenderer para dibujar formas rellenas
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // Dibuja las plataformas
        worldState.platforms.forEach { platform ->
            // Si la plataforma es tangible en la dimensión actual, se dibuja azul.
            // Si no, se dibuja gris oscuro para indicar que no es sólida.
            if (platform.tangibleInDimension == worldState.currentDimension) {
                shapeRenderer.color = Color.ROYAL
            } else {
                shapeRenderer.color = Color.DARK_GRAY
            }
            shapeRenderer.rect(platform.position.x, platform.position.y, platform.size.x, platform.size.y)
        }

        // Dibuja al jugador de color verde
        val player = worldState.player
        shapeRenderer.color = Color.GREEN
        shapeRenderer.rect(player.position.x, player.position.y, player.size.x, player.size.y)

        // Termina el dibujado
        shapeRenderer.end()
    }

    // Los métodos antiguos se mantienen por ahora para cumplir el contrato, aunque renderWorld es el principal.
    override fun drawSprite(sprite: Any, x: Float, y: Float) {
        // Lógica futura para dibujar sprites (imágenes)
    }

    override fun render() {
        // Este método podría usarse para dibujar elementos de UI que no están en el WorldState.
    }
}