package org.example.core

import java.io.InputStream
import java.lang.IllegalArgumentException

class LevelLoader {

    fun loadLevel(fileName: String): LevelData {
        val platforms = mutableListOf<Platform>()
        // La lista ahora es de tipo 'Enemy' (la clase abstracta)
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
                if (cleanLine.startsWith("#") || cleanLine.isBlank()) return@forEach

                val parts = cleanLine.split(',')
                val type = parts[0].uppercase().trim()
                
                try {
                    when (type) {
                        "P_START" -> { 
                            playerStart = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat())
                        }
                        "PLATFORM" -> { 
                            val dim = if (parts[5].trim().uppercase() == "A") Dimension.A else Dimension.B
                            platforms.add(
                                Platform(
                                    position = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat()),
                                    size = Vector2D(parts[3].trim().toFloat(), parts[4].trim().toFloat()),
                                    tangibleInDimension = dim
                                )
                            )
                        }
                        "ENEMY" -> { 
                            val dim = if (parts[5].trim().uppercase() == "A") Dimension.A else Dimension.B
                            // --- CAMBIO ---
                            // Instanciamos BasicEnemy, pero lo guardamos en la List<Enemy>
                            enemies.add(
                                BasicEnemy(
                                    position = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat()),
                                    size = Vector2D(parts[3].trim().toFloat(), parts[4].trim().toFloat()),
                                    dimension = dim
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
