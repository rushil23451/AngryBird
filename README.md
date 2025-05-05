# 🐦 Angry Birds Clone

A Java-based desktop recreation of the classic Angry Birds game, built with libGDX framework and following object-oriented programming principles.

![Angry Birds Clone](path/to/game-screenshot.png)

## 🎮 Game Features

* **Interactive Menu System** with hover effects and sound feedback
* **Multiple Game Levels** with increasing difficulty
* **Save/Load Functionality** to continue your progress
* **Authentic Sound Effects** and background music
* **Responsive UI** that adapts to different screen resolutions

## 🏗️ Project Architecture

The game is built using the **Model-View-Controller** pattern with these key components:

* **First Screen**: Main menu with Play, Load Game, and Exit options
* **Loading Screen**: Transition between menu and gameplay
* **Level Selection**: Choose from available game levels
* **Game Screen**: Main gameplay with physics simulation
* **Bird Types**: Multiple bird types with unique abilities
* **Save System**: Persistent game progress

## 🛠️ Technology Stack

* **Java**: Core programming language
* **libGDX**: Cross-platform game development framework
* **Box2D**: Physics engine for realistic bird and structure interactions
* **Gradle**: Build automation and dependency management

## 📋 Requirements

* Java Development Kit (JDK) 8+
* Gradle 7.0+ (wrapper included)
* OpenGL-compatible graphics card

## 🚀 Getting Started

### Setup and Running

1. Clone this repository:
```bash
git clone https://github.com/yourusername/angrybirds-clone.git
```

2. Navigate to the project directory:
```bash
cd angrybirds-clone
```

3. Run the game:
```bash
./gradlew lwjgl3:run
```
(Use `gradlew.bat` on Windows)

### Building a Distributable

To create a runnable JAR file:
```bash
./gradlew lwjgl3:jar
```

The JAR will be generated in `lwjgl3/build/libs/`.

## 🎯 Game Controls

* **Mouse**: Aim and launch birds
* **Left Click & Drag**: Pull back to set launch power and angle
* **Space**: Activate bird's special ability (when available)
* **R**: Reset current level
* **ESC**: Pause game

## 📁 Project Structure

* `core/`: Main game logic shared across platforms
   * `com.angrybirds/`: Game package
      * Game screens (FirstScreen, LoadingScreen, etc.)
      * Game entities (birds, obstacles, etc.)
      * Physics handlers
      * Asset managers
* `lwjgl3/`: Desktop platform implementation
* `assets/`: Game resources (textures, sounds, fonts)

## 🔧 Development Tools

### Gradle Tasks
* `build` - Builds sources and archives
* `clean` - Removes build folders
* `test` - Runs unit tests

### IDE Integration
* For Eclipse: `./gradlew eclipse`
* For IntelliJ IDEA: `./gradlew idea`

To clean IDE project files:
* Eclipse: `./gradlew cleanEclipse`
* IntelliJ: `./gradlew cleanIdea`
