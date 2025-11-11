package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.Player
import org.example.core.RenderService
import org.example.core.WorldState
import org.example.core.AssetManager
import org.example.core.Dimension

/**
 * (ACTUALIZADO: Usa el nuevo Animation.kt de 2 columnas y el hitbox de 32x32.
 * Vuelve a dibujar TODOS los hitboxes).
 */
class GdxRenderService(
    private val batch: SpriteBatch,
    private val shapeRenderer: ShapeRenderer,
    private val assetLoader: GdxAssetLoader,
    private val camera: OrthographicCamera
) : RenderService {

    // --- Colores ---
    private val ghostColor = Color(0.2f, 0.2f, 0.8f, 0.3f)
    private val groundColorA = Color.GREEN
    private val groundColorB = Color.CYAN
    
    // --- Colores para Hitboxes ---
    private val playerHitboxColor = Color(0f, 1f, 0f, 0.3f)     // Verde
    private val enemyHitboxColor = Color(1f, 0f, 0f, 0.3f)      // Rojo
    private val collectibleHitboxColor = Color(1f, 1f, 0f, 0.3f) // Amarillo
    private val attackHitboxColor = Color(1f, 0.5f, 0f, 0.4f)   // Naranja
    
    private var blink: Boolean = false
    
    private val texBackground: Texture?
    
    // --- Animaciones ---
    private val animPlayerIdle: Animation
    private val animPlayerMove: Animation
    private val animPlayerJump: Animation 
    private val animPlayerAttackGround: Animation
    private val animPlayerAttackAir: Animation    
    private val animEnemyBasic: Animation 

    private var currentAnimation: Animation

    init {
        texBackground = assetLoader.getTexture(AssetManager.World.BACKGROUND)
        
        // --- SOLUCIÓN: USAR EL NUEVO Animation.kt (2 columnas) ---
        val numColumns = 2 // ¡La clave de tu animación!

        // player idle = 4 FPS (4 frames)
        animPlayerIdle = Animation(
            assetLoader.getTexture(AssetManager.Player.IDLE)!!, 
            totalFrames = 4, numColumns = numColumns, frameDuration = 1.0f / 4.0f // 0.25f
        )
        
        // player move = 4 FPS (6 frames)
        animPlayerMove = Animation(
            assetLoader.getTexture(AssetManager.Player.MOVE)!!, 
            totalFrames = 6, numColumns = numColumns, frameDuration = 1.0f / 4.0f // 0.25f
        )
        
        // player jump = 3 FPS (2 frames: subida y caida)
        animPlayerJump = Animation(
            assetLoader.getTexture(AssetManager.Player.JUMP)!!, 
            totalFrames = 2, numColumns = numColumns, frameDuration = 1.0f / 3.0f // 0.333f
        )
        animPlayerJump.isLooping = false 
        
        // player attack ground = 6 FPS (3 frames)
        animPlayerAttackGround = Animation(
            assetLoader.getTexture(AssetManager.Player.ATTACK_GROUND)!!, 
            totalFrames = 3, numColumns = numColumns, frameDuration = 1.0f / 6.0f // 0.166f
        )
        animPlayerAttackGround.isLooping = false
        
        // player attack air = 6 FPS (3 frames)
        animPlayerAttackAir = Animation(
            assetLoader.getTexture(AssetManager.Player.ATTACK_AIR)!!, 
            totalFrames = 3, numColumns = numColumns, frameDuration = 1.0f / 6.0f // 0.166f
        )
        animPlayerAttackAir.isLooping = false
        
        // (Enemigo, no solicitado pero se queda con el valor lento)
        animEnemyBasic = Animation(
            assetLoader.getTexture(AssetManager.Enemy.BASIC_MOVE)!!, 
            totalFrames = 4, numColumns = numColumns, frameDuration = 0.3f
        )
        
        currentAnimation = animPlayerIdle
    }

    override fun renderWorld(worldState: WorldState, deltaTime: Float) {
        // 1. Limpiar la pantalla
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // 2. Dibujar Sprites (Texturas)
        batch.projectionMatrix = camera.combined
        batch.begin()
        
        texBackground?.let {
            batch.draw(it, 0f, 0f, 800f, 600f)
        }

        // --- DIBUJAR JUGADOR ---
        blink = !blink
        if (!(worldState.playerInvincible && blink)) {
            val player = worldState.player
            
            val newAnimation: Animation = when {
                player.isAttacking && player.isOnGround -> {
                    if (currentAnimation != animPlayerAttackGround) animPlayerAttackGround.reset()
                    animPlayerAttackGround
                }
                player.isAttacking && !player.isOnGround -> {
                    if (currentAnimation != animPlayerAttackAir) animPlayerAttackAir.reset()
                    animPlayerAttackAir
                }
                !player.isOnGround -> animPlayerJump
                player.velocity.x != 0f -> animPlayerMove
                else -> animPlayerIdle
            }
            
            currentAnimation = newAnimation
            currentAnimation.update(deltaTime)

            val frame: TextureRegion = when {
                currentAnimation == animPlayerJump -> {
                    if (player.velocity.y > 0) animPlayerJump.getFrame(0) // Frame 0 = Subir
                    else animPlayerJump.getFrame(1) // Frame 1 = Caer
                }
                else -> currentAnimation.getFrame()
            }

            val desiredFlip = (player.facingDirection < 0)
            if (frame.isFlipX != desiredFlip) {
                frame.flip(true, false)
            }

            // --- SOLUCIÓN: DIBUJO 32x32 ---
            // El hitbox y el sprite ahora coinciden perfectamente (32x32)
            batch.draw(frame, player.position.x, player.position.y, player.size.x, player.size.y)
        }
        
        batch.end() // Terminamos el batch de sprites

        // 3. Dibujar Formas (Hitboxes, Fantasmas, UI de Formas)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        // --- SOLUCIÓN: DIBUJAR TODOS LOS HITBOXES ---

        // A. Hitbox del Jugador (Se dibuja sobre el sprite)
        val player = worldState.player
        shapeRenderer.color = playerHitboxColor
        shapeRenderer.rect(player.position.x, player.position.y, player.size.x, player.size.y)

        // B. Hitboxes de Enemigos (solo los vivos y en la dimensión actual)
        shapeRenderer.color = enemyHitboxColor
        worldState.enemies
            .filter { it.dimension == worldState.currentDimension } // Solo los de esta dimensión
            .forEach { enemy ->
                shapeRenderer.rect(enemy.position.x, enemy.position.y, enemy.size.x, enemy.size.y)
            }
        
        // C. Hitboxes de Coleccionables
        shapeRenderer.color = collectibleHitboxColor
        worldState.collectibles
            .filter { !it.isCollected } // (Oculta los ya recogidos)
            .forEach { collectible ->
                shapeRenderer.rect(collectible.position.x, collectible.position.y, collectible.size.x, collectible.size.y)
            }

        // D. Lógica de Plataformas (Suelo sólido y Fantasmas)
        worldState.platforms.forEach { platform ->
            if (platform.tangibleInDimension == worldState.currentDimension) {
                // TANGIBLE: Dibujar suelo sólido (si es suelo)
                if (platform.position.y < 50f) {
                    shapeRenderer.color = if (worldState.currentDimension == Dimension.A) groundColorA else groundColorB
                    shapeRenderer.rect(platform.position.x, platform.position.y, platform.size.x, platform.size.y)
                }
            } else {
                // INTANGIBLE: Dibujar fantasma
                shapeRenderer.color = ghostColor
                shapeRenderer.rect(platform.position.x, platform.position.y, platform.size.x, platform.size.y)
            }
        }
        
        // E. Lógica de Fantasmas de Enemigos
        worldState.enemies
            .filter { it.dimension != worldState.currentDimension }
            .forEach { enemy ->
                shapeRenderer.color = ghostColor
                shapeRenderer.rect(enemy.position.x, enemy.position.y, enemy.size.x, enemy.size.y)
            }

        // F. Hitbox de Ataque del Jugador (Efecto)
        if (worldState.isPlayerAttacking) {
            val hitboxPos = player.position.copy()
            val hitboxSize = Player.ATTACK_HITBOX
            if (worldState.playerFacingDirection > 0) {
                hitboxPos.x += player.size.x // Se posiciona después del cuerpo (32x32)
            } else {
                hitboxPos.x -= hitboxSize.x
            }
            shapeRenderer.color = attackHitboxColor
            shapeRenderer.rect(hitboxPos.x, hitboxPos.y, hitboxSize.x, hitboxSize.y)
        }

        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    override fun drawSprite(sprite: Any, x: Float, y: Float) {}
    override fun render() {}
}