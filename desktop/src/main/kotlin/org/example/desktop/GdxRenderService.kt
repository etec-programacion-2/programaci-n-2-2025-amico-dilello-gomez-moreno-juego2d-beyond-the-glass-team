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
 * (SOLID: D) Es la implementación que cumple el contrato de 'RenderService'.
 * (SOLID: S) Su ÚNICA responsabilidad es recibir el 'WorldState' y "traducirlo"
 * a dibujos en pantalla. No contiene lógica de juego.
 *
 * ---
 * @see "Issue BTG-002: Diseño de la arquitectura de servicios (RenderService)."
 * ---
 *
 * @param shapeRenderer El objeto de LibGDX que dibuja formas (rectángulos, círculos).
 */
class GdxRenderService(private val shapeRenderer: ShapeRenderer) : RenderService {

    // --- Colores predefinidos ---
    // --- Relacionado con BTG-009: Feedback visual de dimensiones ---
    private val ghostColor = Color(1f, 0f, 0f, 0.3f) // Rojo transparente (intangible)
    private val solidColor = Color.RED // Rojo sólido
    
    // --- Relacionado con BTG-012: Feedback visual de combate ---
    private val attackHitboxColor = Color(1f, 1f, 0f, 0.4f) // Amarillo transparente
    
    // --- Relacionado con BTG-013: Color para coleccionables ---
    private val collectibleColor = Color.YELLOW
    
    // --- (NUEVO) Colores para la Puerta de Salida ---
    private val exitColorLocked = Color.PURPLE
    private val exitColorUnlocked = Color.CYAN
    
    // Variable para el efecto de parpadeo (blink)
    // --- Relacionado con BTG-012: Feedback de invencibilidad ---
    private var blink: Boolean = false

    /**
     * Método central de dibujado. Recibe el 'WorldState' y dibuja todo.
     */
    override fun renderWorld(worldState: WorldState) {
        // 1. Limpiar la pantalla
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // 2. Activar 'blending' (para transparencias)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        
        // 3. Iniciar el dibujado de formas
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // 4. Dibujar Plataformas
        // --- Relacionado con BTG-009: Lógica de renderizado de dimensiones ---
        worldState.platforms.forEach { platform ->
            if (platform.tangibleInDimension == worldState.currentDimension) {
                shapeRenderer.color = Color.WHITE // Sólido
            } else {
                shapeRenderer.color = Color.DARK_GRAY // Fantasma (Intangible)
            }
            shapeRenderer.rect(platform.position.x, platform.position.y, platform.size.x, platform.size.y)
        }

        // 5. Dibujar Jugador
        // --- Relacionado con BTG-012: Feedback de invencibilidad (parpadeo) ---
        if (worldState.playerInvincible) {
            blink = !blink // Alterna el parpadeo
            shapeRenderer.color = if (blink) Color.WHITE else Color.GRAY
        } else {
            shapeRenderer.color = Color.GREEN // Color normal
        }
        shapeRenderer.rect(worldState.player.position.x, worldState.player.position.y, worldState.player.size.x, worldState.player.size.y)

        // 6. Dibujar Hitbox de Ataque (si está atacando)
        // --- Relacionado con BTG-012: Feedback visual de combate ---
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
        // --- Relacionado con BTG-011: Dibujado de enemigos ---
        worldState.enemies.forEach { enemy ->
            if (enemy.dimension == worldState.currentDimension) {
                shapeRenderer.color = solidColor // Sólido en esta dimensión
            } else {
                shapeRenderer.color = ghostColor // Fantasma en esta dimensión
            }
            shapeRenderer.rect(enemy.position.x, enemy.position.y, enemy.size.x, enemy.size.y)
        }
        
        // 8. Dibujar Coleccionables
        // --- Relacionado con BTG-013: Dibujar Coleccionables ---
        shapeRenderer.color = collectibleColor
        // Filtra los que ya han sido recogidos (el 'core' pasa todos)
        worldState.collectibles.filter { !it.isCollected }.forEach { collectible ->
            shapeRenderer.rect(collectible.position.x, collectible.position.y, collectible.size.x, collectible.size.y)
        }

        // 9. (NUEVO) Dibujar Puerta de Salida
        worldState.levelExit?.let { exit ->
            // Elige el color basado en si está desbloqueada
            shapeRenderer.color = if (worldState.isExitUnlocked) {
                exitColorUnlocked
            } else {
                exitColorLocked
            }
            shapeRenderer.rect(exit.position.x, exit.position.y, exit.size.x, exit.size.y)
        }

        // 10. Finalizar el dibujado de formas
        shapeRenderer.end()
        // Desactivar 'blending'
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    // --- Métodos de la interfaz no utilizados (necesarios por el contrato) ---
    override fun drawSprite(sprite: Any, x: Float, y: Float) {}
    override fun render() {}
}