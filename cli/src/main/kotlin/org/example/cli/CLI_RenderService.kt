// Archivo: CLI_RenderService.kt

class CLI_RenderService : RenderService {
    override fun drawSprite(sprite: Any, x: Float, y: Float) {
        println("Dibujando sprite $sprite en ($x, $y) en la consola.")
    }

    override fun render() {
        // Lógica para limpiar la consola y mostrar el nuevo frame.
        println("--- Renderizando nuevo frame de la consola ---")
    }
}