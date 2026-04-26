# SquareOff

**Author:** Chris Coleman
**Java version:** 25

A JavaFX checkers game with an AI opponent using minimax with alpha-beta pruning. 

## Overview

SquareOff implements standard American checkers rules: 8×8 board, 12 pieces per side on dark squares, regular pieces move forward only, kings move in all four diagonal directions, captures are forced, and reaching the opposite back rank promotes a piece to king.

The opponent uses a minimax search with alpha-beta pruning and a piece-count + king-bonus evaluation. The human chooses color and difficulty (Random / Easy / Hard) at startup.

## How to run

./gradlew run               
./gradlew test                 


## Design Patterns

### 1. Strategy Pattern

**Where:** squareoff.strategy and squareoff.player.AIPlayer

The MoveStrategy interface declares a single method selectMove(Board, PieceColor, List<Move>). Three concrete implementations provide different algorithms for choosing a move:

- RandomStrategy - picks a random legal move (beginner difficulty).
- MiniMaxStrategy - wraps the MiniMax engine, parameterized by a Difficulty enum (EASY = 2 plies, HARD = 5 plies).

AIPlayer receives a MoveStrategy through its constructor (dependency injection) and delegates move selection to it. Adding a new AI behavior means adding a new MoveStrategy implementation no changes to AIPlayer or GameEngine.

### 2. Template Method Pattern

**Where:** squareoff.model.Piece, RegularPiece, and KingPiece

The abstract Piece class defines getValidMoves(Board, int, int) as a final method that contains the shared algorithm for generating moves and captures. It calls the abstract hook getMoveDirections(), which subclasses override:

- RegularPiece returns only forward directions (depending on color).
- KingPiece returns all four diagonal directions.

The move-generation algorithm (bounds checks, empty-square detection, capture detection) lives in one place. Only the part that varies which directions to check is deferred to subclasses.

### 3. Observer Pattern

**Where:** squareoff.observer and squareoff.engine.GameEngine

GameObserver declares three event methods: onMoveMade, onKingPromotion, and onGameOver. GameEngine keeps a list of observers and notifies all of them during playTurn(). Two concrete observers exist:

- GameLogger - logs game events via SLF4J.
- GameController (the JavaFX UI) - repaints the board after every move.

The engine is decoupled from any specific output mechanism: console logging, GUI updates, network broadcasting, or replay recording can all coexist without engine changes.

### 4. Factory Method Pattern

**Where:** squareoff.model.PieceFactory

PieceFactory exposes two static factory methods createRegular(PieceColor) and createKing(PieceColor)  that hide concrete RegularPiece and KingPiece constructors from the rest of the codebase. Board.executeMove calls PieceFactory.createKing when promoting a piece, and Board.copy calls the factory when cloning the board for minimax search. If a new piece variant were ever introduced, only the factory would need to change and call sites remain unaware of concrete classes.



### Coverage screenshot

![coverage report](Screenshot%202026-04-25%20203602.png)

## Assumptions

- American checkers rules. 8×8 board, 12 pieces per side, dark-square play, regular pieces move forward only, captures are mandatory.
- No multi-jump chaining. A capture is a single jump; chained jumps in one turn are not implemented.
- Red moves first.
- Game ends when the player to move has no legal moves; the opponent is declared winner.



