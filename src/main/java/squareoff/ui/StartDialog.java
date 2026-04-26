package squareoff.ui;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import squareoff.model.PieceColor;
import squareoff.strategy.Difficulty;
import squareoff.strategy.MiniMaxStrategy;
import squareoff.strategy.MoveStrategy;
import squareoff.strategy.RandomStrategy;

import java.util.Optional;

public class StartDialog {

    private static final String DIALOG_TITLE = "SquareOff";
    private static final String DIALOG_HEADER = "Choose your color and AI difficulty";
    private static final String AI_RANDOM = "Random";
    private static final String AI_EASY = "Easy (depth 2)";
    private static final String AI_HARD = "Hard (depth 5)";
    private static final int GRID_GAP = 10;
    private static final int GRID_PADDING = 20;

    public Optional<GameSetup> show() {
        Dialog<GameSetup> dialog = new Dialog<>();
        dialog.setTitle(DIALOG_TITLE);
        dialog.setHeaderText(DIALOG_HEADER);

        ChoiceBox<PieceColor> colorBox = new ChoiceBox<>();
        colorBox.getItems().addAll(PieceColor.RED, PieceColor.BLACK);
        colorBox.setValue(PieceColor.BLACK);

        ChoiceBox<String> aiBox = new ChoiceBox<>();
        aiBox.getItems().addAll(AI_RANDOM, AI_EASY, AI_HARD);
        aiBox.setValue(AI_HARD);

        GridPane grid = new GridPane();
        grid.setHgap(GRID_GAP);
        grid.setVgap(GRID_GAP);
        grid.setPadding(new Insets(GRID_PADDING));
        grid.add(new Label("Your color:"), 0, 0);
        grid.add(colorBox, 1, 0);
        grid.add(new Label("AI opponent:"), 0, 1);
        grid.add(aiBox, 1, 1);
        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(button -> {
            if (button != ButtonType.OK) {
                return null;
            }
            return new GameSetup(colorBox.getValue(), buildStrategy(aiBox.getValue()), aiBox.getValue());
        });

        return dialog.showAndWait();
    }

    private MoveStrategy buildStrategy(String label) {
        return switch (label) {
            case AI_RANDOM -> new RandomStrategy();
            case AI_EASY -> new MiniMaxStrategy(Difficulty.EASY);
            case AI_HARD -> new MiniMaxStrategy(Difficulty.HARD);
            default -> new MiniMaxStrategy(Difficulty.HARD);
        };
    }
}
