package squareoff.model;

public class GameState {

    private final Board board;
    private PieceColor currentTurn;
    private boolean gameOver;
    private PieceColor winner;

    public GameState(Board board) {
        this.board = board;
        this.currentTurn = PieceColor.RED;
    }

    public Board getBoard() {
        return board;
    }

    public PieceColor getCurrentTurn() {
        return currentTurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public PieceColor getWinner() {
        return winner;
    }

    public void switchTurn() {
        currentTurn = currentTurn.opposite();
    }

    public void setGameOver(PieceColor winner) {
        this.gameOver = true;
        this.winner = winner;
    }

    @Override
    public String toString() {
        return String.format("Turn: %s, GameOver: %s, Winner: %s", currentTurn, gameOver, winner);
    }
}
