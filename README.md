# SquareOff

A fully playable checkers game where a human player competes against an AI opponent, built with JavaFX.

## Overview

SquareOff features a graphical checkers board with click-based piece selection and move highlighting. The AI opponent uses the **minimax algorithm with alpha-beta pruning** to evaluate moves and play competitively at configurable difficulty levels.

## Features

- **Graphical Board** — JavaFX-rendered board with click-based piece selection and move highlighting
- **AI Opponent** — Minimax algorithm with alpha-beta pruning for competitive play
- **Difficulty Levels** — Easy, Medium, and Hard, controlling how many moves ahead the AI looks
- **Standard American Checkers Rules** — Forced captures, multi-jump chains, and king promotion
- **Save & Resume** — Persist game state so a match can be saved and continued later

## Architecture

The project emphasizes clean object-oriented design principles:

- **Polymorphic Players** — A `Player` abstraction allows `HumanPlayer` and `AIPlayer` to be treated interchangeably, with AI strategies injected via dependency injection
- **Inheritance over Conditionals** — Regular pieces and kings handle movement differently through inheritance rather than if-else checks
- **Coding to Abstractions** — Game entities (pieces, board tiles, move logic) are coded to abstractions rather than concrete implementations

## Tech Stack

- **Language:** Java 25
- **UI Framework:** JavaFX
- **Build Tool:** Gradle
- **Algorithm:** Minimax with Alpha-Beta Pruning

## Getting Started

### Prerequisites

- Java 25+
- Gradle 8.x+

### Build & Run

```bash
# Clone the repository
git clone <repository-url>
cd SquareOff

# Build and run
./gradlew run
```

## How to Play

1. Launch the application and select a difficulty level (Easy, Medium, Hard)
2. Click a piece to select it — valid moves will be highlighted
3. Click a highlighted tile to move
4. Captures are forced when available; multi-jump chains must be completed
5. Reach the opposite end of the board to promote a piece to a king
6. Save your game at any time and resume later
