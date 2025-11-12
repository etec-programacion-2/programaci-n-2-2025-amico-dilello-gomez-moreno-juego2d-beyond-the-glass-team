package org.example.core

import java.io.InputStream
import java.lang.IllegalArgumentException

/**
 * Servicio dedicado a cargar y parsear archivos de nivel
 * (SOLID: Principio de Responsabilidad Única).
 * Lee un archivo .txt y lo convierte en un objeto LevelData.
 *
 * ---
 * @see "Issue BTG-007: Implementación de Carga de Niveles."
 * ---
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
        // instancias de 'BasicEnemy' (POO: Polimorfismo / SOLID: Liskov).
        // (Relacionado con BTG-011)
        val enemies = mutableListOf<Enemy>()
        // (Relacionado con BTG-013)
        val collectibles = mutableListOf<Collectible>()
        var playerStart: Vector2D? = null // Se usa '?' porque aún no se ha leído
        
        /** (NUEVO) Variable para la puerta de salida (solo puede haber una). */
        var exitGate: LevelExit? = null

        // Obtiene el archivo desde la carpeta 'resources' del classpath
        val inputStream: InputStream? = LevelLoader::class.java.classLoader.getResourceAsStream(fileName)
        
        if (inputStream == null) {
            throw IllegalArgumentException("Archivo de nivel no encontrado: $fileName. Asegúrate de que esté en 'core/src/main/resources'.")
        }

        // 'use' asegura que el 'reader' se cierre automáticamente
        inputStream.bufferedReader().use { reader ->
            reader.forEachLine { line ->
                // Ignora comentarios y líneas vacías
                val cleanLine = line.trim()
                if (cleanLine.isEmpty() || cleanLine.startsWith("#")) {
                    return@forEachLine // (Continúa a la siguiente línea)
                }

                // Parsea la línea
                try {
                    val parts = cleanLine.split(",")
                    when (parts[0].trim()) {
                        "P_START" -> { // (Relacionado con BTG-006)
                            playerStart = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat())
                        }
                        "PLATFORM" -> { // (Relacionado con BTG-009)
                            platforms.add(
                                Platform(
                                    position = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat()),
                                    size = Vector2D(parts[3].trim().toFloat(), parts[4].trim().toFloat()),
                                    tangibleInDimension = Dimension.valueOf(parts[5].trim()) // Convierte "A" a Dimension.A
                                )
                            )
                        }
                        "ENEMY" -> { // (Relacionado con BTG-011)
                            // (POO: Polimorfismo) Creamos un 'BasicEnemy' pero lo guardamos como 'Enemy'
                            enemies.add(
                                BasicEnemy(
                                    position = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat()),
                                    size = Vector2D(parts[3].trim().toFloat(), parts[4].trim().toFloat()),
                                    dimension = Dimension.valueOf(parts[5].trim())
                                )
                            )
                        }
                        "COLLECTIBLE" -> { // (Relacionado con BTG-013)
                            collectibles.add(
                                Collectible(
                                    position = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat()),
                                    size = Vector2D(parts[3].trim().toFloat(), parts[4].trim().toFloat()),
                                    value = parts[5].trim().toInt(),
                                    
                                    // Inicializa el coleccionable como "no recogido"
                                    isCollected = false
                                )
                            )
                        }
                        
                        /** (NUEVO) Lógica para parsear la puerta de salida. */
                        "EXIT" -> {
                            exitGate = LevelExit(
                                position = Vector2D(parts[1].trim().toFloat(), parts[2].trim().toFloat()),
                                size = Vector2D(parts[3].trim().toFloat(), parts[4].trim().toFloat()),
                                condition = WinCondition.valueOf(parts[5].trim()) // Convierte "ALL_ENEMIES_KILLED"
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
            collectibles = collectibles,
            exitGate = exitGate // (NUEVO) Añade la puerta
        )
    }
}