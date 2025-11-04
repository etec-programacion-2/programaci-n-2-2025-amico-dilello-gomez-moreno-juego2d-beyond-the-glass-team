package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.Player
import org.example.core.RenderService
import org.example.core.WorldState
import org.example.core.Vector2D

/**
 * Implementación CONCRETA de la interfaz 'RenderService' (del 'core')
 * usando el 'ShapeRenderer' de LibGDX.
 *
 * Recibe el 'WorldState' y lo "traduce" a dibujos en pantalla.
 *
 * @param shapeRenderer El objeto de LibGDX que dibuja formas (rectángulos, círculos).
 */
class GdxRenderService(private val shapeRenderer: ShapeRenderer) : RenderService {

    // --- Colores predefinidos ---
    private val ghostColor = Color(1f, 0f, 0f, 0.3f) // Rojo transparente
    private val solidColor = Color.RED // Rojo sólido
    private val attackHitboxColor = Color(1f, 1f, 0f, 0.4f) // Amarillo transparente
    
    // --- CAMBIO BTG-013: Color para coleccionables ---
    private val collectibleColor = Color.YELLOW
    
    // Variable para el efecto de parpadeo (blink)
    private var blink: Boolean = false

    /**
     * Método central de dibujado. Recibe el 'WorldState' y dibuja todo.
     */
    override fun renderWorld(worldState: WorldState) {
        // 1. Limpiar la pantalla con un color de fondo oscuro
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // 2. Activar 'blending' (para transparencias)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        
        // 3. Iniciar el ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // 4. Dibujar Plataformas
        worldState.platforms.forEach { platform ->
            if (platform.position.y == 20f) {
                shapeRenderer.color = Color.ROYAL
            } else if (platform.tangibleInDimension == worldState.currentDimension) {
                shapeRenderer.color = Color.WHITE
            } else {
                shapeRenderer.color = Color.DARK_GRAY
            }
            shapeRenderer.rect(platform.position.x, platform.position.y, platform.size.x, platform.size.y)
        }

        // 5. Dibujar Jugador
        blink = !blink // Alterna 'blink' en cada fotograma
        if (worldState.playerInvincible && blink) {
            // Si está invencible Y 'blink' es true, no se dibuja (efecto parpadeo)
        } else {
            val player = worldState.player
            shapeRenderer.color = Color.GREEN
            shapeRenderer.rect(player.position.x, player.position.y, player.size.x, player.size.y)
        }

        // 6. Dibujar Hitbox de Ataque (Feedback Visual)
        if (worldState.isPlayerAttacking) {
            val hitboxPos = worldState.player.position.copy()
            val hitboxSize = Player.ATTACK_HITBOX
            if (worldState.playerFacingDirection > 0) { // Derecha
                hitboxPos.x += worldState.player.size.x
            } else { // Izquierda
                hitboxPos.x -= hitboxSize.x
            }
            shapeRenderer.color = attackHitboxColor
            shapeRenderer.rect(hitboxPos.x, hitboxPos.y, hitboxSize.x, hitboxSize.y)
        }

        // 7. Dibujar Enemigos
        worldState.enemies.forEach { enemy ->
            if (enemy.dimension == worldState.currentDimension) {
                shapeRenderer.color = solidColor
            } else {
                shapeRenderer.color = ghostColor
            }
            shapeRenderer.rect(enemy.position.x, enemy.position.y, enemy.size.x, enemy.size.y)
        }
        
        // --- CAMBIO BTG-013: Dibujar Coleccionables ---
        shapeRenderer.color = collectibleColor
        // Filtra los que ya han sido recogidos
        worldState.collectibles.filter { !it.isCollected }.forEach { collectible ->
            shapeRenderer.rect(collectible.position.x, collectible.position.y, collectible.size.x, collectible.size.y)
        }

        // 8. Finalizar el dibujado de formas
        shapeRenderer.end()
        // Desactivar 'blending'
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    // --- Métodos de la interfaz no utilizados (necesarios por el contrato) ---
    override fun drawSprite(sprite: Any, x: Float, y: Float) {}
    override fun render() {}
}