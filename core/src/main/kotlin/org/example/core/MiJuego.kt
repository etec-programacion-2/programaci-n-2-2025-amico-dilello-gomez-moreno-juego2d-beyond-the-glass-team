package org.example.core

/**
 * Implementación de la lógica del juego (SOLID: Inversión de Dependencias).
 * Esta es la clase "Directora" que implementa 'GameLogicService'.
 * Coordina todos los servicios (Física, Combate, IA) y maneja el estado del juego.
 *
 * ---
 * @see "Issue BTG-002: Implementa la interfaz 'GameLogicService'."
 * @see "Issue BTG-008: Gestiona el bucle de juego ('update') y el estado ('gameState')."
 * ---
 */
class MiJuego : GameLogicService {

    // --- ESTADO DEL JUEGO ---
    // (Relacionado con BTG-006)
    private var player: Player = Player(position = Vector2D(0f, 0f), size = Vector2D(32f, 64f))
    // (Relacionado con BTG-007)
    private var levelData: LevelData? = null // Datos cargados del nivel (plataformas, enemigos)
    // (Relacionado con BTG-008, BTG-012)
    private var gameState: GameState = GameState.Playing // Estado actual (Playing, GameOver)
    // (Relacionado con BTG-012)
    private var lives: Int = 3
    private var invincibilidadTimer: Float = 0f // Temporizador para cuando el jugador es golpeado
    // (Relacionado con BTG-009)
    private var currentDimension: Dimension = Dimension.A // Dimensión actual
    private var switchKeyWasPressed = false // Controla el cooldown del cambio de dimensión
    private var originalPlayerStart: Vector2D = Vector2D(0f, 0f)

    // (Relacionado con BTG-013) Control de "tecla recién presionada" para el salto
    private var jumpKeyWasPressed = false

    // --- SERVICIOS (SOLID: S) ---
    // (POO: Encapsulamiento) Los servicios son privados y 'MiJuego' delega tareas.
    // (Relacionado con BTG-010, BTG-006)
    private val physicsService: PhysicsService = PhysicsService()
    // (Relacionado con BTG-010, BTG-011)
    private val enemyPhysicsService: EnemyPhysicsService = EnemyPhysicsService()
    // (Relacionado con BTG-012)
    private val combatService: CombatService = CombatService()
    // (Relacionado con BTG-013)
    private val collectionService: CollectionService = CollectionService()
    // (Relacionado con BTG-007)
    private val levelLoader: LevelLoader = LevelLoader()
    
    // --- PATRÓN OBSERVADOR (BTG-013) ---
    private val subject: Subject = Subject()
    private lateinit var abilityUnlocker: AbilityUnlocker // Se inicializa en loadLevel

    /**
     * Carga un nivel e inicializa el estado del juego.
     * Implementación de la interfaz 'GameLogicService'.
     *
     * @param levelName El nombre del archivo (ej. "level1.txt").
     */
    override fun loadLevel(levelName: String) {
        // (Relacionado con BTG-007)
        levelData = levelLoader.loadLevel(levelName)
        originalPlayerStart = levelData!!.playerStart.copy()
        resetPlayerPosition()
        
        // (Relacionado con BTG-012)
        lives = 3
        gameState = GameState.Playing

        // --- Configuración del Patrón Observador (BTG-013) ---
        // 1. Crear el observador
        abilityUnlocker = AbilityUnlocker(player, 3)
        // 2. Registrar el observador en el sujeto
        subject.addObserver(abilityUnlocker)
    }

    /**
     * Resetea la posición del jugador al inicio del nivel.
     * (POO: Encapsulamiento) Lógica interna reutilizada.
     */
    private fun resetPlayerPosition() {
        player.position = originalPlayerStart.copy()
        player.velocity = Vector2D(0f, 0f)
        // (Relacionado con BTG-012)
        invincibilidadTimer = 1.5f // Da 1.5s de inmunidad al reaparecer
    }

    /**
     * El bucle de actualización principal del juego.
     * Implementación de la interfaz 'GameLogicService'.
     *
     * ---
     * @see "Issue BTG-008: Bucle de Juego y Gestión de Estado."
     * ---
     *
     * @param actions El Set de acciones abstractas del usuario.
     * @param deltaTime El tiempo pasado desde el último fotograma.
     */
    override fun update(actions: Set<GameAction>, deltaTime: Float) {
        // La lógica del juego solo se ejecuta si estamos en estado 'Playing'
        if (gameState == GameState.Playing) {
            
            // 1. --- MANEJO DE ENTRADA (Input) ---
            handleInput(actions)

            // 2. --- ACTUALIZACIÓN DE TEMPORIZADORES ---
            updateTimers(deltaTime)

            // 3. --- LÓGICA DE FÍSICA (Jugador y Enemigos) ---
            // (Relacionado con BTG-010)
            val allPlatforms = levelData?.platforms ?: emptyList()
            // (Relacionado con BTG-006, BTG-009, BTG-013)
            physicsService.update(player, allPlatforms, currentDimension, deltaTime)
            
            // (Relacionado con BTG-011)
            levelData?.enemies?.filter { it.isAlive }?.forEach { enemy ->
                enemyPhysicsService.update(enemy, allPlatforms, deltaTime)
            }

            // 4. --- LÓGICA DE IA (Enemigos) ---
            // (Relacionado con BTG-011)
            levelData?.enemies?.filter { it.isAlive }?.forEach { enemy ->
                enemy.updateAI(allPlatforms)
            }

            // 5. --- LÓGICA DE COLISIONES (Combate y Coleccionables) ---
            handleCollisions()

        } else if (gameState == GameState.GameOver) {
            // (Relacionado con BTG-012) Si es Game Over, solo escucha la tecla de reinicio
            if (GameAction.SWITCH_DIMENSION in actions) {
                loadLevel("level1.txt") // Reinicia el juego
            }
        }
    }

    /**
     * Procesa las acciones del usuario.
     * (POO: Encapsulamiento) Lógica extraída de 'update'.
     */
    private fun handleInput(actions: Set<GameAction>) {
        // --- Movimiento (BTG-006) ---
        player.velocity.x = 0f
        if (GameAction.MOVE_LEFT in actions) {
            player.velocity.x = -Player.MOVE_SPEED
            player.facingDirection = -1f
        }
        if (GameAction.MOVE_RIGHT in actions) {
            player.velocity.x = Player.MOVE_SPEED
            player.facingDirection = 1f
        }

        // --- Salto (BTG-006) y Doble Salto (BTG-013) ---
        val jumpPressed = GameAction.JUMP in actions
        if (jumpPressed && !jumpKeyWasPressed) { // Detectar "flanco de subida" (solo al presionar)
            if (player.isOnGround) {
                // Salto normal
                player.velocity.y = Player.JUMP_STRENGTH
                player.isOnGround = false
                player.hasDoubleJumped = false // Resetea el doble salto
            } else if (player.canDoubleJump && !player.hasDoubleJumped) {
                // Doble salto (BTG-013)
                player.velocity.y = Player.JUMP_STRENGTH * 0.9f // Un poco menos fuerte
                player.hasDoubleJumped = true
            }
        }
        jumpKeyWasPressed = jumpPressed // Guarda el estado para el próximo fotograma

        // --- Cambio de Dimensión (BTG-009) ---
        val switchPressed = GameAction.SWITCH_DIMENSION in actions
        if (switchPressed && !switchKeyWasPressed) { // Detectar "flanco de subida"
            currentDimension = if (currentDimension == Dimension.A) Dimension.B else Dimension.A
        }
        switchKeyWasPressed = switchPressed // Guarda el estado

        // --- Ataque (BTG-012) ---
        if (GameAction.ATTACK in actions && player.attackTimer <= 0f) { // Solo si el cooldown terminó
            player.isAttacking = true
            player.attackTimer = Player.ATTACK_COOLDOWN // Inicia el temporizador de cooldown
            player.hitEnemiesThisAttack.clear() // Limpia la lista de enemigos golpeados
        }
    }

    /**
     * Actualiza todos los temporizadores del juego.
     * (POO: Encapsulamiento) Lógica extraída de 'update'.
     */
    private fun updateTimers(deltaTime: Float) {
        // --- Temporizador de Invencibilidad (BTG-012) ---
        if (invincibilidadTimer > 0f) {
            invincibilidadTimer -= deltaTime
        }

        // --- Temporizador de Ataque (BTG-012) ---
        if (player.attackTimer > 0f) {
            player.attackTimer -= deltaTime
            
            // Duración del hitbox activo
            if (player.isAttacking && player.attackTimer <= (Player.ATTACK_COOLDOWN - Player.ATTACK_DURATION)) {
                player.isAttacking = false // El hitbox desaparece
            }

            if (player.attackTimer <= 0f) {
                player.isAttacking = false // Asegura que se apague
            }
        }
    }

    /**
     * Maneja las colisiones de combate y recolección.
     * (POO: Encapsulamiento) Lógica extraída de 'update'.
     */
    private fun handleCollisions() {
        val allEnemies = levelData?.enemies ?: emptyList()
        val allCollectibles = levelData?.collectibles ?: emptyList()

        // --- Lógica de Combate (BTG-012) ---
        
        // 1. Comprobar si el JUGADOR golpea a un ENEMIGO
        if (player.isAttacking) {
            val hitEnemies = combatService.checkPlayerAttack(player, allEnemies, currentDimension)
            for (enemy in hitEnemies) {
                // Evita golpear al mismo enemigo dos veces en un solo ataque
                if (!player.hitEnemiesThisAttack.contains(enemy)) {
                    enemy.isAlive = false // Enemigo muere
                    player.hitEnemiesThisAttack.add(enemy)
                }
            }
        }

        // 2. Comprobar si un ENEMIGO golpea al JUGADOR
        // (Solo si no es invencible)
        if (invincibilidadTimer <= 0f) {
            if (combatService.checkPlayerDamage(player, allEnemies, currentDimension)) {
                handlePlayerHit()
                // Salir temprano si el jugador fue golpeado
                // para evitar múltiples fuentes de daño en un fotograma.
                return
            }
        }

        // --- Lógica de Coleccionables (BTG-013) ---
        val collectedItems = collectionService.checkCollectibleCollisions(player, allCollectibles)
        for (item in collectedItems) {
            item.isCollected = true // Marca como recogido
            player.energyFragments += item.value // Suma al contador
            
            // --- Notificar al Patrón Observador (BTG-013) ---
            subject.notify(PlayerEvent.ENERGY_COLLECTED)
        }
        
        // --- NUEVA LÓGICA: Comprobar si el jugador se cae del mundo ---
        // (Solo si no es invencible, para evitar bucles de muerte)
        if (invincibilidadTimer <= 0f) {
            // Definimos un "límite de caída" (ej. -100 píxeles)
            // (El piso está en y=0, así que -100 es un margen seguro)
            val deathPlaneY = -100f
            if (player.position.y < deathPlaneY) {
                println("¡Caída fuera del mundo! Recibiendo daño.")
                // Reutiliza la misma lógica de "recibir daño" y respawnear
                handlePlayerHit()
                return // Salir para no procesar más daño este fotograma
            }
        }
    }

    /**
     * Maneja la lógica de cuando el jugador es golpeado.
     * (POO: Encapsulamiento)
     *
     * ---
     * @see "Issue BTG-012: Vidas y Game Over."
     * ---
     */
    private fun handlePlayerHit() {
        lives -= 1 // Pierde una vida
        
        if (lives <= 0) {
            gameState = GameState.GameOver // Se acabó el juego
        } else {
            resetPlayerPosition() // Resetea al inicio del nivel con invencibilidad
        }
    }

    /**
     * Crea el "snapshot" de solo lectura del estado del mundo.
     * Este es el objeto que se envía al RenderService.
     * (POO: Encapsulamiento) Oculta la lógica interna, solo expone datos.
     *
     * ---
     * @see "Issue BTG-002: Arquitectura (WorldState)."
     * @see "Issue BTG-008: Bucle de Juego."
     * ---
     */
    override fun getWorldState(): WorldState {
        return WorldState(
            player = this.player,
            platforms = levelData?.platforms ?: emptyList(),
            // (Relacionado con BTG-012) Solo pasa enemigos vivos al renderizador
            enemies = levelData?.enemies?.filter { it.isAlive } ?: emptyList(),
            
            // (Relacionado con BTG-013) Pasa la lista de coleccionables
            // El renderizador se encargará de filtrar los ya recogidos
            collectibles = levelData?.collectibles ?: emptyList(),
            
            currentDimension = this.currentDimension,
            // (Relacionado con BTG-012)
            playerInvincible = invincibilidadTimer > 0,
            isPlayerAttacking = player.isAttacking,
            playerFacingDirection = player.facingDirection
        )
    }
    
    // --- Getters públicos requeridos por la interfaz 'GameLogicService' ---
    
    override fun getPlayer(): Player = player
    override fun getLevelData(): LevelData? = levelData
    override fun getGameInfo(): String {
        return "Player X: ${player.position.x.toInt()} | Y: ${player.position.y.toInt()} | OnGround: ${player.isOnGround}"
    }
    override fun getGameState(): GameState = gameState
    override fun getLives(): Int = lives
}