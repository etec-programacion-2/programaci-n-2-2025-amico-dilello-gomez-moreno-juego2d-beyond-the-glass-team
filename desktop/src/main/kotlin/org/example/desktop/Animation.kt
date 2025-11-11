package org.example.desktop

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import kotlin.math.ceil

/**
 * (NUEVO SISTEMA DE ANIMACIÓN)
 * Gestiona una animación a partir de una hoja de sprites (Sprite Sheet)
 * que utiliza una cuadrícula de 2 columnas.
 *
 * @param texture La hoja de sprites completa.
 * @param totalFrames El número total de fotogramas en esta animación.
 * @param numColumns El número de columnas en la hoja (¡lo has fijado en 2!).
 * @param frameDuration El tiempo (en segundos) que dura cada fotograma.
 */
class Animation(
    texture: Texture,
    private val totalFrames: Int,
    numColumns: Int,
    private val frameDuration: Float
) {
    private val frames: Array<TextureRegion>
    private var currentFrameTime: Float = 0f
    private var currentFrameIndex: Int = 0
    var isLooping: Boolean = true

    init {
        // --- SOLUCIÓN: LÓGICA DE CORTE DE SPRITESHEET (32x32, 2 columnas) ---
        
        // 1. Calcular número de filas
        val numRows = ceil(totalFrames.toFloat() / numColumns.toFloat()).toInt()

        // 2. Calcular tamaño de cada fotograma
        // (Asumimos que la textura total tiene 'numColumns' de ancho y 'numRows' de alto)
        val frameWidth = texture.width / numColumns
        val frameHeight = texture.height / numRows

        // 3. Dividir la textura en regiones (frames)
        val regions = TextureRegion.split(texture, frameWidth, frameHeight)
        
        // 4. Aplanar la cuadrícula 2D (regions) en un array 1D (frames)
        frames = Array(totalFrames) { i ->
            val row = i / numColumns
            val col = i % numColumns
            regions[row][col]
        }
    }

    /**
     * Avanza la animación por 'deltaTime'.
     */
    fun update(deltaTime: Float) {
        currentFrameTime += deltaTime
        if (currentFrameTime > frameDuration) {
            currentFrameTime = 0f
            currentFrameIndex++
            if (currentFrameIndex >= totalFrames) {
                if (isLooping) {
                    currentFrameIndex = 0
                } else {
                    currentFrameIndex = totalFrames - 1 // Quedarse en el último frame
                }
            }
        }
    }

    /**
     * Obtiene el fotograma actual de la animación.
     */
    fun getFrame(): TextureRegion {
        return frames[currentFrameIndex]
    }

    /**
     * Obtiene un fotograma específico (para animaciones no cíclicas como saltar).
     */
    fun getFrame(index: Int): TextureRegion {
        return frames[index.coerceIn(0, totalFrames - 1)]
    }

    /**
     * Reinicia la animación al primer fotograma.
     */
    fun reset() {
        currentFrameIndex = 0
        currentFrameTime = 0f
    }
}