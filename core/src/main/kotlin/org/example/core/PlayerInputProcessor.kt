
package org.example.core

/**
 * SRP: Responsable único de traducir la entrada del usuario (InputService) 
 * en una intención de movimiento (velocidad) sobre el Player.
 */
class PlayerInputProcessor {

    /**
     * Criterios: El jugador se mueve, el jugador puede saltar.
     */
    fun processInput(player: Player, inputService: InputService) {
        
        // 1. Movimiento Horizontal
        var xDirection = 0f
        if (inputService.isKeyPressed(InputKeys.LEFT)) {
            xDirection = -1f
        } else if (inputService.isKeyPressed(InputKeys.RIGHT)) {
            xDirection = 1f
        }
        player.velocity.x = xDirection * Physics.MOVEMENT_SPEED
        
        // 2. Salto (Solo si está en el suelo)
        if (inputService.isKeyPressed(InputKeys.SPACE)) {
            if (player.isOnGround) {
                player.velocity.y = Physics.JUMP_VELOCITY
                player.isOnGround = false
            }
        }
    }
}
