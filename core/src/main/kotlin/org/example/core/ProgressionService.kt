package org.example.core

/**
 * (NUEVO) Servicio de Lógica de Progresión (SOLID: S).
 *
 * Su ÚNICA responsabilidad es gestionar la secuencia de niveles
 * y comprobar si se cumplen las condiciones de victoria.
 *
 * MiJuego (el "Director") le delega toda esta lógica.
 */
class ProgressionService {

    /**
     * (POO: Encapsulamiento) La lista de niveles está oculta aquí.
     * MiJuego no sabe cuántos niveles hay ni cuáles son.
     */
    private val levelList = listOf(
        "level1.txt",
        "level2.txt",
        "level3.txt",
        "level4.txt",
        "level5.txt"
    )
    
    private var currentLevelIndex = 0

    /**
     * Comprueba si la condición de victoria para el nivel actual se ha cumplido.
     *
     * @param levelData El estado actual del nivel (para comprobar enemigos, etc.).
     * @return 'true' si la puerta puede desbloquearse, 'false' en caso contrario.
     */
    fun isExitConditionMet(levelData: LevelData?): Boolean {
        val data = levelData ?: return false
        val exitCondition = data.exitGate?.condition ?: WinCondition.REACH_EXIT

        // (SOLID: O) Si añadimos nuevas WinCondition, solo modificamos este 'when'.
        return when (exitCondition) {
            WinCondition.ALL_ENEMIES_KILLED -> {
                // Comprueba si todos los enemigos están muertos
                levelData.enemies.all { !it.isAlive }
            }
            WinCondition.REACH_EXIT -> {
                // No hay requisitos, la puerta siempre está "abierta".
                true
            }
        }
    }

    /**

     * Avanza al siguiente nivel en la lista.
     */
    fun advanceToNextLevel() {
        if (hasNextLevel()) {
            currentLevelIndex++
        }
    }

    /**
     * Resetea el progreso al primer nivel.
     * @param startLevel El nivel por el cual empezar (ej. "level1.txt").
     */
    fun resetProgress(startLevel: String) {
        currentLevelIndex = levelList.indexOf(startLevel).coerceAtLeast(0)
    }

    /** Devuelve el nombre del archivo del nivel actual. */
    fun getCurrentLevelName(): String = levelList[currentLevelIndex]

    /** Comprueba si hay un nivel después del actual. */
    fun hasNextLevel(): Boolean = currentLevelIndex < levelList.size - 1
}