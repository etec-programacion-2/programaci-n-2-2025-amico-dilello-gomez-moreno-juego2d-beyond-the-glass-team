package org.example.desktop

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.example.core.GameAction
import org.example.core.GameLogicService
import org.example.core.GameState
import org.example.core.MiJuego
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle // (NUEVO) Import para los botones
import com.badlogic.gdx.math.Vector3 // (NUEVO) Import para coordenadas del mouse

/**
 * (MUY MODIFICADO)
 * Clase principal del módulo 'desktop'.
 * Ahora actúa como un "Gestor de Escenas" o "Gestor de UI".
 *
 * (SOLID: S) Su responsabilidad es:
 * 1. Gestionar la lógica de entrada (teclado Y MOUSE).
 * 2. Dibujar la UI correcta para el estado actual (Menú, Juego, Game Over).
 * 3. Delegar el dibujado del *mundo del juego* a 'renderService'.
 *
 * ---
 * @see "Issue BTG-001: Módulo 'desktop'."
 * @see "Issue BTG-002: Utiliza las interfaces 'GameLogicService', 'RenderService', 'InputService'."
 * @see "Issue BTG-008: Implementa el bucle de juego (método render)."
 * ---
 */
class DesktopGame : ApplicationAdapter() {
    
    // --- Dependencias del Core ---
    private lateinit var juego: GameLogicService 
    private lateinit var renderService: GdxRenderService
    private lateinit var inputService: GdxInputService

    // --- Objetos de LibGDX ---
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var abilityFont: BitmapFont
    private lateinit var titleFont: BitmapFont
    
    // --- (NUEVO) Fuentes del Menú ---
    private lateinit var megaTitleFont: BitmapFont // Para el título del juego
    private lateinit var buttonFont: BitmapFont // Para los botones
    
    // --- (NUEVO) Lógica del Menú ---
    private lateinit var jugarButtonRect: Rectangle
    private lateinit var salirButtonRect: Rectangle
    private var mousePos: Vector3 = Vector3() // Para guardar coordenadas del mouse

    /**
     * Método 'create' de LibGDX.
     * (MODIFICADO) Ahora inicializa las fuentes y los botones del menú.
     */
    override fun create() {
        // --- Inicialización de objetos LibGDX ---
        shapeRenderer = ShapeRenderer()
        batch = SpriteBatch()
        
        // --- Fuentes del Juego ---
        font = BitmapFont()
        font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        
        titleFont = BitmapFont()
        titleFont.data.setScale(2.0f)
        titleFont.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        
        abilityFont = BitmapFont()
        abilityFont.color = Color.CYAN
        abilityFont.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        
        // --- (NUEVO) Fuentes del Menú ---
        megaTitleFont = BitmapFont()
        megaTitleFont.data.setScale(3.5f) // Fuente bien grande
        megaTitleFont.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        megaTitleFont.color = Color.WHITE

        buttonFont = BitmapFont()
        buttonFont.data.setScale(2.0f)
        buttonFont.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        // --- (NUEVO) Definición de los botones (Rectangles) ---
        // (x, y, ancho, alto)
        // Colocados en el centro de la pantalla (800x600)
        jugarButtonRect = Rectangle(300f, 300f, 200f, 50f)
        salirButtonRect = Rectangle(300f, 200f, 200f, 50f)

        // --- Inyección de Dependencias ---
        renderService = GdxRenderService(shapeRenderer)
        inputService = GdxInputService()

        // 2. Crear la instancia del 'core'
        juego = MiJuego() 
        
        // (MODIFICADO) YA NO SE LLAMA A 'juego.loadLevel()'.
        // El juego ahora empieza en el estado 'Menu' por defecto.
    }

    /**
     * Método 'render' de LibGDX. Es el bucle principal.
     * (MODIFICADO) Ahora gestiona la Máquina de Estados.
     */
    override fun render() {
        // 1. Obtener el tiempo delta
        val deltaTime = Gdx.graphics.deltaTime

        // 2. (NUEVO) Gestión de Entrada Dependiente del Estado
        // (SOLID: S) DesktopGame es responsable de la entrada de la UI (mouse)
        // e 'inputService' es responsable de la entrada del juego (teclado).
        
        val gameState = juego.getGameState()
        val actions: Set<GameAction>

        if (gameState == GameState.Menu) {
            // Si estamos en el menú, usamos nuestro propio gestor de entrada (mouse)
            actions = handleMenuInput()
        } else {
            // Si estamos jugando, Game Over, etc., usamos el servicio normal (teclado)
            actions = inputService.getActions()
        }

        // 3. (NUEVO) Manejo de 'QUIT' (ESC)
        // Esta es la única acción que 'DesktopGame' maneja directamente.
        // Si el usuario presiona 'QUIT' (ESC) Y estamos en el menú,
        // la aplicación debe cerrarse.
        // Si estamos en el juego, 'MiJuego' lo manejará (y volverá al menú).
        if (gameState == GameState.Menu && GameAction.QUIT in actions) {
            Gdx.app.exit()
            return
        }

        // 4. Actualizar el motor del juego (lógica del 'core')
        // Le pasamos las acciones (sean del mouse o del teclado)
        juego.update(actions, deltaTime)

        // 5. Obtener el estado del mundo (snapshot de datos)
        val worldState = juego.getWorldState()

        // 6. (MODIFICADO) Renderizar el mundo del juego
        // (SOLID: S) Solo dibujamos el mundo si estamos en 'Playing'.
        // En 'Menu', 'GameOver', etc., solo limpiamos la pantalla.
        if (juego.getGameState() == GameState.Playing) {
            renderService.renderWorld(worldState)
        } else {
            // Si no estamos jugando, solo limpiamos la pantalla
            // (El 'renderService' ya no lo hace por nosotros)
            Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
            Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT)
        }

        // 7. Dibujar la UI (texto)
        // (DesktopGame se encarga de la UI, que es específico de la plataforma)
        batch.begin()
        
        // (NUEVO) Máquina de estados para la UI
        when (juego.getGameState()) {
            
            GameState.Menu -> {
                // --- DIBUJAR EL MENÚ PRINCIPAL ---
                
                // Título
                megaTitleFont.draw(batch, "Beyond The Glass", 180f, 500f)
                
                // Botones (Texto)
                // Comprueba si el mouse está sobre el botón para resaltarlo
                val mouseX = mousePos.x
                val mouseY = mousePos.y
                
                // Botón Jugar
                if (jugarButtonRect.contains(mouseX, mouseY)) {
                    buttonFont.color = Color.YELLOW // Resaltado
                } else {
                    buttonFont.color = Color.WHITE
                }
                buttonFont.draw(batch, "Jugar", jugarButtonRect.x + 60f, jugarButtonRect.y + 35f)
                
                // Botón Salir
                if (salirButtonRect.contains(mouseX, mouseY)) {
                    buttonFont.color = Color.YELLOW // Resaltado
                } else {
                    buttonFont.color = Color.WHITE
                }
                buttonFont.draw(batch, "Salir", salirButtonRect.x + 60f, salirButtonRect.y + 35f)
            }
            
            GameState.Playing -> {
                // --- DIBUJAR LA UI DEL JUEGO (como antes) ---
                font.color = Color.WHITE
                font.draw(batch, "Dimensión Actual: ${worldState.currentDimension}", 10f, 580f)
                font.draw(batch, juego.getGameInfo(), 10f, 560f)
                font.draw(batch, "Vidas: ${juego.getLives()}", 10f, 540f)

                val player = juego.getPlayer()
                font.draw(batch, "Fragmentos: ${player.energyFragments} / 3", 10f, 520f)
                
                if (player.canDoubleJump) {
                    abilityFont.draw(batch, "¡DOBLE SALTO DESBLOQUEADO!", 10f, 500f)
                }
            }
            
            GameState.GameOver -> {
                // --- DIBUJAR PANTALLA GAME OVER (MODIFICADA) ---
                titleFont.color = Color.RED
                titleFont.draw(batch, "GAME OVER", 280f, 350f)
                font.color = Color.WHITE
                
                // (MODIFICADO) El texto ahora dice "volver al menú"
                font.draw(batch, "Presiona SHIFT para volver al menú", 240f, 300f)
                font.draw(batch, "Presiona ESC para volver al menú", 290f, 270f)
            }
            
            is GameState.GameWon -> {
                // --- DIBUJAR PANTALLA VICTORIA (MODIFICADA) ---
                titleFont.color = Color.CYAN
                titleFont.draw(batch, "¡HAS GANADO!", 270f, 350f)
                font.color = Color.WHITE
                font.draw(batch, "¡Completaste todos los niveles!", 270f, 300f)
                
                // (MODIFICADO) El texto ahora dice "volver al menú"
                font.draw(batch, "Presiona SHIFT para volver al menú", 240f, 270f)
                font.draw(batch, "Presiona ESC para volver al menú", 290f, 240f)
            }
            
            else -> {
                 // No dibujar nada para 'Paused'
            }
        }
        
        batch.end()
    }

    /**
     * (NUEVO) Gestor de entrada DEL MENÚ.
     * Esta función es llamada por 'render' SOLO si el estado es 'Menu'.
     * Comprueba las teclas Y el mouse.
     *
     * @return Un Set de GameActions para enviar a 'MiJuego.update()'.
     */
    private fun handleMenuInput(): Set<GameAction> {
        // 1. Comprobar la entrada del teclado (para la tecla ESC)
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            // El usuario presionó ESC en el menú
            return setOf(GameAction.QUIT)
        }
        
        // 2. Comprobar la entrada del mouse (para los botones)
        
        // Actualizar la posición del mouse (con la Y invertida)
        // 'unproject' traduce las coordenadas de la pantalla (pixeles)
        // a las coordenadas del mundo/cámara (que en nuestro caso son las mismas)
        mousePos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
        // (Esto es necesario si la cámara se mueve, pero es buena práctica)
        // batch.projectionMatrix la usa para saber cómo traducir
        // ...pero para UI simple, podemos solo invertir Y:
        val mouseY = Gdx.graphics.height - Gdx.input.y.toFloat()
        mousePos.set(Gdx.input.x.toFloat(), mouseY, 0f)

        // Comprobar si se acaba de hacer clic
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            
            // ¿Clic en "Jugar"?
            if (jugarButtonRect.contains(mousePos.x, mousePos.y)) {
                return setOf(GameAction.START_GAME) // Envía la acción al 'core'
            }
            
            // ¿Clic en "Salir"?
            if (salirButtonRect.contains(mousePos.x, mousePos.y)) {
                return setOf(GameAction.QUIT) // Envía la acción (DesktopGame la interceptará)
            }
        }
        
        // No se hizo clic en nada
        return emptySet()
    }

    /**
     * Se llama al cerrar la aplicación. Libera recursos.
     */
    override fun dispose() {
        // (MODIFICADO) Añadidas las nuevas fuentes
        shapeRenderer.dispose()
        batch.dispose()
        font.dispose()
        abilityFont.dispose()
        titleFont.dispose()
        megaTitleFont.dispose()
        buttonFont.dispose()
    }
}