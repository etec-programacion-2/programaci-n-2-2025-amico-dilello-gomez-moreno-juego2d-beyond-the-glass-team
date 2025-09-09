// Archivo: RenderService.kt

interface RenderService {
    /**
     * Dibuja un sprite en una posición específica.
     */
    fun drawSprite(sprite: Any, x: Float, y: Float)

    /**
     * Renderiza el frame actual.
     */
    fun render()

    // Puedes añadir más métodos como drawText, drawRectangle, etc.
}