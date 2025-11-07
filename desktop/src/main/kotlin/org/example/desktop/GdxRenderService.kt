package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
// --- CAMBIO: Importar la Cámara ---
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.Player
import org.example.core.RenderService
import org.example.core.WorldState
import org.example.core.AssetManager
import org.example.core.Dimension

/**
 * (ACTUALIZADO: Corrección de Cámara)
 *
 * @param batch El SpriteBatch para dibujar texturas (sprites).
 * @param shapeRenderer El ShapeRenderer para dibujar formas (hitboxes, placeholders).
 * @param assetLoader El cargador que provee las texturas.
 * @param camera La cámara del mundo del juego (Inyectada).
 */
class GdxRenderService(
    private val batch: SpriteBatch,
    private val shapeRenderer: ShapeRenderer,
    private val assetLoader: GdxAssetLoader,
    // --- CAMBIO: Aceptar la cámara ---
    private val camera: OrthographicCamera
) : RenderService {

    // --- Colores predefinidos ---
    private val ghostColor = Color(0.2f, 0.2f, 0.8f, 0.3f) // Tinte azul "fantasma"
    private val attackHitboxColor = Color(1f, 1f, 0f, 0.4f)
    
    private var blink: Boolean = false
    
    // --- Referencias a texturas ---
    private val texBackground: Texture? = assetLoader.getTexture(AssetManager.World.BACKGROUND)
    private val texPlatformA: Texture? = assetLoader.getTexture(AssetManager.World.PLATFORM_A)
    private val texPlatformB: Texture? = assetLoader.getTexture(AssetManager.World.PLATFORM_B)
    private val texCollectible: Texture? = assetLoader.getTexture(AssetManager.Items.ENERGY_FRAGMENT)
    private val texEnemyBasic: Texture? = assetLoader.getTexture(AssetManager.Enemy.BASIC_MOVE)
    private val texPlayerIdle: Texture? = assetLoader.getTexture(AssetManager.Player.IDLE)
    private val texPlayerMove: Texture? = assetLoader.getTexture(AssetManager.Player.MOVE)
    private val texPlayerJump: Texture? = assetLoader.getTexture(AssetManager.Player.JUMP)
    private val texPlayerAttack: Texture? = assetLoader.getTexture(AssetManager.Player.ATTACK)

    /**
     * Método central de dibujado.
     */
    override fun renderWorld(worldState: WorldState) {
        // 1. Limpiar la pantalla
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // 2. Dibujar Sprites (Texturas)
        
        // --- CAMBIO: Aplicar la cámara al SpriteBatch ---
        // Le decimos al batch que dibuje en el sistema de coordenadas de la cámara
        batch.projectionMatrix = camera.combined
        
        batch.begin()
        
        // --- DIBUJAR FONDO ---
        if (texBackground != null) {
            batch.draw(texBackground, 0f, 0f, 800f, 600f)
        }

        // --- DIBUJAR PLATAFORMAS ---
        worldState.platforms.forEach { platform ->
            val tex = if (platform.tangibleInDimension == Dimension.A) texPlatformA else texPlatformB
            if (tex != null) {
                batch.draw(tex, platform.position.x, platform.position.y, platform.size.x, platform.size.y)
            }
        }
        
        // --- DIBUJAR COLECCIONABLES ---
        if (texCollectible != null) {
            worldState.collectibles.filter { !it.isCollected }.forEach { collectible ->
                batch.draw(texCollectible, collectible.position.x, collectible.position.y, collectible.size.x, collectible.size.y)
            }
        }

        // --- DIBUJAR ENEMIGOS ---
        if (texEnemyBasic != null) {
            worldState.enemies.forEach { enemy ->
                // Ajusta esto si tu sprite mira a la derecha por defecto: val flipX = enemy.direction < 0
                val flipX = enemy.direction > 0 
                batch.draw(
                    texEnemyBasic,
                    enemy.position.x, enemy.position.y,
                    enemy.size.x, enemy.size.y,
                    0, 0,
                    texEnemyBasic.width, texEnemyBasic.height,
                    flipX, false
                )
            }
        }

        // --- DIBUJAR JUGADOR ---
        blink = !blink
        if (!(worldState.playerInvincible && blink)) {
            val player = worldState.player
            
            val playerTexture = when {
                player.isAttacking -> texPlayerAttack
                !player.isOnGround -> texPlayerJump
                player.velocity.x != 0f -> texPlayerMove
                else -> texPlayerIdle
            } ?: texPlayerIdle

            val flipX = player.facingDirection < 0

            if (playerTexture != null) {
                batch.draw(
                    playerTexture,
                    player.position.x, player.position.y,
                    player.size.x, player.size.y,
                    0, 0,
                    playerTexture.width, playerTexture.height,
                    flipX, false
                )
            }
        }
        
        batch.end() // Terminamos el batch de sprites

        // 3. Dibujar Formas (Debug y Efectos "Fantasma")
        
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        
        // --- CAMBIO: Aplicar la cámara al ShapeRenderer ---
        // Le decimos también que dibuje en el mismo sistema de coordenadas
        shapeRenderer.projectionMatrix = camera.combined
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // --- DIBUJAR TINTES "FANTASMA" ---
        // (Esto es lo que SÍ estabas viendo)
        shapeRenderer.color = ghostColor
        worldState.platforms
            .filter { it.tangibleInDimension != worldState.currentDimension }
            .forEach { platform ->
                shapeRenderer.rect(platform.position.x, platform.position.y, platform.size.x, platform.size.y)
            }
        
        worldState.enemies
            .filter { it.dimension != worldState.currentDimension }
            .forEach { enemy ->
                shapeRenderer.rect(enemy.position.x, enemy.position.y, enemy.size.x, enemy.size.y)
            }

        // --- DIBUJAR HITBOX DE ATAQUE (Debug) ---
        if (worldState.isPlayerAttacking) {
            val player = worldState.player
            val hitboxPos = player.position.copy()
            val hitboxSize = Player.ATTACK_HITBOX
            if (worldState.playerFacingDirection > 0) {
                hitboxPos.x += player.size.x
            } else {
                hitboxPos.x -= hitboxSize.x
            }
            shapeRenderer.color = attackHitboxColor
            shapeRenderer.rect(hitboxPos.x, hitboxPos.y, hitboxSize.x, hitboxSize.y)
        }

        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    // --- Métodos de la interfaz no utilizados (necesarios por el contrato) ---
    override fun drawSprite(sprite: Any, x: Float, y: Float) {}
    override fun render() {}
}