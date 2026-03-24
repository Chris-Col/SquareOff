package squareoff.model;

public record Move(int fromRow, int fromCol, int toRow, int toCol, boolean isCapture) {

    public int capturedRow() {
        return (fromRow + toRow) / 2;
    }

    public int capturedCol() {
        return (fromCol + toCol) / 2;
    }

    @Override
    public String toString() {
        String label = isCapture ? " capture" : "";
        return String.format("(%d,%d)->(%d,%d)%s", fromRow, fromCol, toRow, toCol, label);
    }
}
