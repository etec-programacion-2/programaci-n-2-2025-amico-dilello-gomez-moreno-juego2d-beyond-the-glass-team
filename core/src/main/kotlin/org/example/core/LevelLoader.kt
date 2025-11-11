package org.example.core

import java.io.InputStream
import java.lang.IllegalArgumentException

/**
 * Servicio dedicado a cargar y parsear archivos de nivel (SOLID: Principio de Responsabilidad Única).
 * Lee un archivo .txt y lo convierte en un objeto LevelData.
 */
class LevelLoader {

    /**
     * Carga un archivo de nivel desde la carpeta 'resources'.
     *
     * @param fileName El nombre del archivo (ej. "level1.txt").
     * @return Un objeto LevelData con todas las entidades.
     * @throws IllegalArgumentException Si el archivo no se encuentra.
     */
    fun loadLevel(fileName: String): LevelData {
        val platforms = mutableListOf<Platform>()
        // La lista es de tipo 'Enemy' (la clase abstracta), pero guardamos
        // instancias de 'BasicEnemy' (POO: Polimorfismo).
        val enemies = mutableListOf<Enemy>()
        val collectibles = mutableListOf<Collectible>()
        var playerStart: Vector2D? = null // Se usa '?' porque aún no se ha leído

        // Obtiene el archivo desde la carpeta 'resources' del classpath
        val inputStream: InputStream? = LevelLoader::class.java.classLoader.getResourceAsStream(fileName)
        
        if (inputStream == null) {
            throw IllegalArgumentException("Archivo de nivel no encontrado: $fileName. Asegúrate de que esté en 'core/src/main/resources'.")
        }

        // 'use' asegura que el 'reader' se cierre automáticamente
        inputStream.bufferedReader().use { reader ->
            // Procesa el archivo línea por línea
            reader.lineSequence().forEach { line ->
                val cleanLine = line.trim()
                // Ignorar líneas vacías o comentarios (que empiezan con #)
                if (cleanLine.startsWith("#") || cleanLine.isBlank()) return@forEach

                val parts = cleanLine.split(',') // Parsea por comas
                val type = parts[0].uppercase().trim() // El tipo de entidad (PLATFORM, ENEMY...)
                
                try {
                    // 'when' (switch) para determinar qué tipo de entidad crear
                    when (type) {
                        "P_START" -> { // Posición inicial del jugador
                            playerStart = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat())
                        }
                        "PLATFORM" -> { // Plataforma
                            // Determina la dimensión (A o B)
                            val dim = if (parts[5].trim().uppercase() == "A") Dimension.A else Dimension.B
                            platforms.add(
                                Platform(
                                    position = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat()),
                                    size = Vector2D(parts[3].trim().toFloat(), parts[4].trim().toFloat()),
                                    tangibleInDimension = dim
                                )
                            )
                        }
                        "ENEMY" -> { // Enemigo (se instancia un BasicEnemy)
                            val dim = if (parts[5].trim().uppercase() == "A") Dimension.A else Dimension.B
                            // Instanciamos BasicEnemy, pero lo guardamos en la List<Enemy> (Polimorfismo)
                            enemies.add(
                                BasicEnemy(
                                    position = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat()),
                                    size = Vector2D(parts[3].trim().toFloat(), parts[4].trim().toFloat()),
                                    dimension = dim
                                )
                            )
                        }
                        "COLLECTIBLE" -> { // Coleccionable
                            collectibles.add(
                                Collectible(
                                    position = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat()),
                                    size = Vector2D(parts[3].trim().toFloat(), parts[4].trim().toFloat()),
                                    value = parts[5].trim().toInt(),
                                    
                                    // --- CAMBIO BTG-013 ---
                                    // Inicializa el coleccionable como "no recogido"
                                    isCollected = false
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    // Manejo de error si una línea está mal formateada
                    println("Error al parsear la línea: $cleanLine. Error: ${e.message}")
                }
            }
        }

        // Es un requisito que el nivel defina P_START
        require(playerStart != null) { "El archivo de nivel debe definir P_START." }

        // Devuelve el objeto LevelData completo
        return LevelData(
            playerStart = playerStart!!, // '!!' es seguro gracias al 'require' anterior
            platforms = platforms,
            enemies = enemies,
            collectibles = collectibles
        )
    }
}