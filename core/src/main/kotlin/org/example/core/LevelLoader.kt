package org.example.core

import java.io.InputStream
import java.lang.IllegalArgumentException

/**
 * Clase encargada de leer un archivo de nivel y construir las entidades del juego.
 * Usa Streams para robustez al cargar recursos y 'trim()' para robustez al parsear datos.
 */
class LevelLoader {

    fun loadLevel(fileName: String): LevelData {
        val platforms = mutableListOf<Platform>()
        val enemies = mutableListOf<Enemy>()
        val collectibles = mutableListOf<Collectible>()
        var playerStart: Vector2D? = null

        val inputStream: InputStream? = LevelLoader::class.java.classLoader.getResourceAsStream(fileName)
        
        if (inputStream == null) {
            throw IllegalArgumentException("Archivo de nivel no encontrado: $fileName. Asegúrate de que esté en 'core/src/main/resources'.")
        }

        inputStream.bufferedReader().use { reader ->
            reader.lineSequence().forEach { line ->
                val cleanLine = line.trim()
                if (cleanLine.startsWith("#") || cleanLine.isBlank()) return@forEach // Ignora comentarios

                val parts = cleanLine.split(',')
                val type = parts[0].uppercase().trim() // Trim en el tipo por si acaso
                
                try {
                    when (type) {
                        "P_START" -> { 
                            // P_START, X, Y
                            // *** CORRECCIÓN: Usar trim() en todos los valores numéricos ***
                            playerStart = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat())
                        }
                        "PLATFORM" -> { 
                            // PLATFORM, X, Y, Ancho, Alto, Dimensión (A/B)
                            // *** CORRECCIÓN: Usar trim() en el valor del Enum ***
                            val dim = Dimension.valueOf(parts[5].trim().uppercase())
                            platforms.add(
                                Platform(
                                    position = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat()),
                                    size = Vector2D(parts[3].trim().toFloat(), parts[4].trim().toFloat()),
                                    tangibleInDimension = dim
                                )
                            )
                        }
                        "ENEMY" -> { 
                            // ENEMY, X, Y, Ancho, Alto, Dimensión (A/B)
                            // *** CORRECCIÓN: Usar trim() en todos los valores ***
                            val dim = Dimension.valueOf(parts[5].trim().uppercase())
                            enemies.add(
                                Enemy(
                                    position = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat()),
                                    size = Vector2D(parts[3].trim().toFloat(), parts[4].trim().toFloat()),
                                    currentDimension = dim
                                )
                            )
                        }
                        "COLLECTIBLE" -> { 
                            // COLLECTIBLE, X, Y, Ancho, Alto, Valor
                            // *** CORRECCIÓN: Usar trim() en todos los valores ***
                            collectibles.add(
                                Collectible(
                                    position = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat()),
                                    size = Vector2D(parts[3].trim().toFloat(), parts[4].trim().toFloat()),
                                    value = parts[5].trim().toInt()
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    println("Error al parsear la línea: $cleanLine. Error: ${e.message}")
                }
            }
        }

        require(playerStart != null) { "El archivo de nivel debe definir P_START." }

        return LevelData(
            playerStart = playerStart!!,
            platforms = platforms,
            enemies = enemies,
            collectibles = collectibles
        )
    }
}