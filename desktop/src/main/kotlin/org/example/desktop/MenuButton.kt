//clase universal para crear botones

package org.example.desktop 

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

/**
 * clase reutilizable para crear botones a partir de una ruta de imagen (Texture).
 *
 * @param texturePath la ruta del archivo de imagen (relativa a la carpeta 'assets').
 */
class MenuButton(texturePath: String) : ImageButton(getStyle(texturePath)) {

    //se necesita de una función auxiliar para construir el 'Style' requerido por ImageButton
    companion object {
        private fun getStyle(path: String): ImageButtonStyle {
            val texture = Texture(path) //carga la imagen
            val region = TextureRegion(texture) //crea una región de la textura
            val drawable = TextureRegionDrawable(region) //convierte a un objeto dibujable
            
            //crea el estilo del boton
            val style = ImageButtonStyle()
            style.imageUp = drawable // 'imageUp' es la imagen cuando el botón NO está presionado
            
            // 💡 Consejo: Para un efecto visual, podrías cargar otra imagen (ej: 'play_button_pressed.png')
            // y asignarla a style.imageDown.
            
            return style
        }
    }
}