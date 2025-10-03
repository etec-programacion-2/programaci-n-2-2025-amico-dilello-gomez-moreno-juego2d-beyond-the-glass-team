package org.example.core

import java.io.InputStream
import java.lang.IllegalArgumentException

/**
 * SRP: Responsable único de leer un recurso de archivo y construir LevelData.
 * Se usa getResourceAsStream para robustez al cargar desde un JAR.
 */
class LevelLoader {
    fun loadLevel(fileName: String): LevelData {
        // ... (Implementación completa con correcciones de URI y trim() - se mantiene igual) ...
        val platforms = mutableListOf<Platform>()
        val enemies = mutableListOf<Enemy>()
        val collectibles = mutableListOf<Collectible>()
        var playerStart: Vector2D? = null

        // Uso de Stream para leer recursos dentro de un JAR.
        val inputStream: InputStream? = LevelLoader::class.java.classLoader.getResourceAsStream(fileName)
        
        if (inputStream == null) {
            throw IllegalArgumentException("Archivo de nivel no encontrado: $fileName. Debe estar en 'core/src/main/resources'.")
        }

        inputStream.bufferedReader().use { reader ->
            reader.lineSequence().forEach { line ->
                val cleanLine = line.trim()
                if (cleanLine.startsWith("#") || cleanLine.isBlank()) return@forEach

                val parts = cleanLine.split(',')
                val type = parts[0].uppercase().trim()
                
                try {
                    when (type) {
                        "P_START" -> { 
                            playerStart = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat())
                        }
                        "PLATFORM" -> { 
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
