// Archivo: InputService.kt

interface InputService {
    /**
     * Verifica si una tecla específica está siendo presionada.
     */
    fun isKeyPressed(keyCode: Int): Boolean

    /**
     * Obtiene la posición del cursor del ratón.
     */
    fun getMousePosition(): Pair<Float, Float>

    // Puedes añadir métodos para eventos de clic, scroll, etc.
}
