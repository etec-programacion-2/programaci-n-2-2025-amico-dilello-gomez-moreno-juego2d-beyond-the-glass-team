<p align="center">
  <img src="logo\beyondtheglasslogo.jpeg" width="150"/>
</p>
<div style="background: rgba(135, 206, 250, 0.2); padding: 20px; text-align: center; border-radius: 15px; backdrop-filter: blur(10px); border: 2px solid rgba(135, 206, 250, 0.6);">
  <h1 style="color: white; font-size: 72px; font-family: 'Arial', sans-serif; text-shadow: 0 0 15px rgba(135, 206, 250, 0.8);">
    Beyond the Glass
  </h1>
</div>

Beyond the Glass es un juego de plataformas 2D único, donde el protagonista, Duodan, tiene el poder de cambiar entre dos dimensiones espejo. En una dimensión, algunas plataformas son invisibles e intangibles, mientras que en la otra se vuelven solidarias pero otras desaparecen. El jugador deberá alternar entre estas dimensiones para superar los obstáculos y escapar de una cárcel profunda.

La historia sigue a Duodan, un prisionero atrapado por el oscuro y misterioso Dark Lord. Con su habilidad para manipular las dimensiones, Duodan luchará por escapar y derrotar a su captor. La única forma de hacerlo es aprovechar el entorno cambiante, resolver acertijos complejos y enfrentarse a los desafíos de una prisión que desafía las leyes de la física.
## 📋 Integrantes del Equipo
- **Valentino Dilello**  
- **Nazareno Moreno**
- **Mateo Gómez** 
- **Santino Amico**

# Pasos a seguir para descargar y jugar Beyond the Glass
## Instalación previa:
- Debemos contar con Git instalado en nuestro dispositivo. En caso de no tener Git instalado:
## En Windows:
1. Abrimos la terminal de Windows (Simbolo del sistema) y buscamos Git ejecutanto el comando: `winget search git`. En caso de no tener "**winget**", debe entrar a Microsoft Store e instalar "**winget**".
2. Ahora debemos ejecutar el código: `winget install Git.Git` para instalar Git.

Una vez finalizada la instalación ya podemos pasar a instalar el juego.
## En Linux:
### Para Linux Debian, Ubuntu, Mint (y derivados):
1. Ejecutamos: `sudo apt update`.
2. Luego instalamos Git con el comando: `sudo apt install git`.

Una vez finalizada la instalación ya podemos pasar a instalar el juego.
### Para Linux Fedora, RHEL, CentOS, Rocky (y derivados):
1. Ejecutamos (para Fedora o RHEL/Rocky/Alma 8 y superior): `sudo dnf install git`.
2. Ejecutamos (para CentOS 7 o RHEL 7 más antiguo): `sudo yum install git`.

Una vez finalizada la instalación ya podemos pasar a instalar el juego.
### Para Linux Arch, Manjaro (y derivados):
1. Ejecutamos: `sudo pacman -Syu git`.

Una vez finalizada la instalación ya podemos pasar a instalar el juego.
### Para Linux openSUSE:
1. Ejecutamos: `sudo zypper install git`.

Una vez finalizada la instalación ya podemos pasar a instalar el juego.
# Instalación del juego
Hay dos formas recomendadas para instalar nuestro juego. Es posible elegir la que usted prefiera y no afectará absolutamente en nada.

Para ambas debemos tener abierto nuestro **cmd** en Windows o nuesto **bash** en Linux.
### Con SSH:
1. En la ubicacion donde desees alojar nuestro juego, ejecute: `git clone git@github.com:etec-programacion-2/programaci-n-2-2025-amico-dilello-gomez-moreno-juego2d-beyond-the-glass-team.git`.
> [!IMPORTANT]
> Para este paso deberas contar con certificados de GitHub en tu carpeta `.ssh`.
2. Luego deberá de entrar a la carpeta, para eso ejecute: `cd programaci-n-2-2025-amico-dilello-gomez-moreno-juego2d-beyond-the-glass-team`
3. Para correr nuestro juego, ejecute: `./gradlew desktop:run`.
### Con HTTPS:
1. En la ubicacion donde desees alojar nuestro juego, ejecute: `git clone https://github.com/etec-programacion-2/programaci-n-2-2025-amico-dilello-gomez-moreno-juego2d-beyond-the-glass-team.git`.
2. Luego deberá de entrar a la carpeta, para eso ejecute: `cd programaci-n-2-2025-amico-dilello-gomez-moreno-juego2d-beyond-the-glass-team`.
3. Para correr nuestro juego, ejecute: `./gradlew desktop:run`.

# Controles del juego

- **W**: Saltar
- **D**: Moverse a la derecha
- **A**: Moverse a la izquierda
- **Shift**: Cambiar de dimensión
- **Barra espaciadora**: Atacar
