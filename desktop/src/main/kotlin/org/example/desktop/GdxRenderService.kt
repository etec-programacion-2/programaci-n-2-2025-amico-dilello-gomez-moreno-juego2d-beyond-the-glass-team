package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.example.core.RenderService

// Implementación de RenderService para el entorno de escritorio con LibGDX.
class GdxRenderService(private val batch: SpriteBatch, private val font: BitmapFont) : RenderService {
    override fun drawSprite(sprite: Any, x: Float, y: Float) {
        // Lógica para dibujar sprites con LibGDX.
    }

    // Limpia la pantalla y dibuja elementos de la UI.
    override fun render() {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        font.draw(batch, "Beyond the Glass - Desktop", 10f, 580f)
        batch.end()
    }
}