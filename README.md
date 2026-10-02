# 2042: Interceptor (Java)

A modern remake of the classic arcade shooter *1942* with an alien-invasion theme. You pilot a star-fighter, shoot down eight alien ships, dodge their bullets and collect extra lives. Built in Java with the [StdDraw](https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html) library and written entirely in a procedural style (no classes).

🎥 [Watch the gameplay video](https://youtu.be/VA7wIlqdBnE?si=unqAETEy7FAhl_Kt)

## Features

- **Calibration menu:** adjust the FPS and aircraft speed before the game starts, and test them right in the menu by moving and shooting.
- **Randomized enemy fire:** each alien ship has its own firing cooldown, so attacks are never predictable.
- **Frame-rate independent difficulty:** cooldowns are scaled with the chosen FPS, so the game feels the same on different computers.
- **Extra life drops:** destroyed ships have a 40% chance to drop a heart. You can hold up to five lives.
- **Explosion animation:** a small explosion followed by a large one when a ship is destroyed.
- **End screens:** Victory and Game Over screens with the final score and a keyboard-driven Restart / End Game menu.

## Controls

| Key | Action |
|---|---|
| Arrow keys | Move the ship |
| `SPACE` | Shoot |
| `ENTER` | Start the game / confirm a menu choice |
| `Q` / `E` | Increase / decrease aircraft speed (menu) |
| `A` / `D` | Adjust FPS calibration (menu) |
| `↑` / `↓` | Navigate the end-game menu |

## Gameplay

| | |
|---|---|
| Enemies | 8 alien ships moving horizontally across the screen |
| Score | 30 points per destroyed ship |
| Lives | Start with 3, maximum 5 |
| Heart drop chance | 40% per destroyed ship |
| Aircraft speed | 10 – 40 (adjustable in the menu) |
| Win | Destroy all alien ships |
| Lose | Lose all lives |

## Game Stages

1. **Main Menu:** shows the title and current settings (FPS and speed). Each key press changes a setting only once, even if the key is held down.
2. **Gameplay:** fight the aliens, avoid their blue bullets and collect hearts.
3. **End Game:** Victory or Game Over screen with the final score. Choose *Restart* or *End Game*.

## How It Works

### Randomized firing
Every enemy has its own cooldown stored in an `ArrayList`. The first cooldown is random, and after each shot it is reset to a new random value. The cooldown decreases by `1 / fps` every frame, so firing speed does not depend on the FPS setting.

### Collision detection
All entities (ship, aliens, bullets, hearts) are treated as axis-aligned rectangles. A collision is detected when the distance between two centers is below a threshold on both the x-axis and the y-axis. Bullet-alien, bullet-ship and ship-heart collisions all use this check.

### Entity management
Positions of enemies, bullets and hearts are stored in parallel `ArrayList`s and updated together in the main game loop. Loops run backwards when removing items so indexes stay valid.

## Project Structure

```
├── src/       // Java source code
├── assets/    // sprites, backgrounds, explosion images
└── README.md
```

## Getting Started

### Requirements
- Java JDK 17 or newer
- `StdDraw.java` (included in the repo / [download here](https://introcs.cs.princeton.edu/java/stdlib/))

<

### Run

```bash
# compile
javac *.java

# run
java Main
```