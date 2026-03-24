# SquareOff

A checkers game built in Java with an AI opponent powered by minimax with alpha-beta pruning.

## Overview

SquareOff implements standard American checkers rules including forced captures and king promotion. The AI opponent evaluates moves using the minimax algorithm to play competitively. The project emphasizes clean object-oriented design with multiple design patterns.

## Design Patterns

### 1. Strategy Pattern

**Where:** `squareoff.strategy` and `squareoff.player.AIPlayer`

**How:** The `MoveStrategy` interface declares a single method `selectMove(Board, PieceColor, List<Move>)`. Concrete implementations like `RandomStrategy` provide different algorithms for choosing a move. `AIPlayer` receives a `MoveStrategy` through its constructor (dependency injection) and delegates move selection to it in `chooseMove()`.

**Why:** The Strategy pattern decouples the AI player from any specific move-selection algorithm. New strategies (e.g., defensive, aggressive, or MiniMax-backed) can be added by implementing `MoveStrategy` without modifying `AIPlayer`. This makes AI behavior pluggable and testable in isolation.

### 2. Template Method Pattern

**Where:** `squareoff.model.Piece`, `RegularPiece`, and `KingPiece`

**How:** The abstract `Piece` class defines `getValidMoves(Board, int, int)` as a `final` method that contains the shared algorithm for generating moves and captures. It calls the abstract hook method `getMoveDirections()`, which subclasses override. `RegularPiece` returns only forward directions (based on color), while `KingPiece` returns all four diagonal directions.

**Why:** The move-generation logic (checking bounds, detecting empty squares, detecting opponent pieces for captures) is identical for both piece types. The Template Method pattern avoids duplicating this algorithm in each subclass. Only the part that varies (which directions to check) is deferred to subclasses.

### 3. Observer Pattern

**Where:** `squareoff.observer` and `squareoff.engine.GameEngine`

**How:** The `GameObserver` interface declares three event methods: `onMoveMade`, `onKingPromotion`, and `onGameOver`. `GameEngine` maintains a list of observers and notifies all of them when these events occur during `playTurn()`. `GameLogger` is a concrete observer that logs game events via SLF4J.

**Why:** The Observer pattern decouples the game engine from any specific form of output or reaction to game events. The engine does not need to know whether its events are being logged to the console, displayed in a GUI, sent over a network, or recorded for replay. New observers can be added without changing the engine.

## Tech Stack

- **Language:** Java 25
- **Build Tool:** Gradle 8.12
- **Testing:** JUnit 5
- **Logging:** SLF4J / Logback
- **Coverage:** JaCoCo

## Getting Started

### Prerequisites

- Java 25+
- Gradle 8.x+

### Build & Test

```bash
./gradlew build
./gradlew test
./gradlew jacocoTestReport
```

## Tests

8 tests covering:

- Board initial setup and king promotion
- Regular piece forward movement
- King piece 4-directional movement
- Forced capture rule enforcement
- Capture removes opponent piece
- MiniMax selects capture moves
- AI player strategy injection
