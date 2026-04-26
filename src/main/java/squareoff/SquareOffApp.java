package squareoff;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squareoff.engine.GameEngine;
import squareoff.model.PieceColor;
import squareoff.observer.GameLogger;
import squareoff.player.AIPlayer;
import squareoff.player.HumanPlayer;
import squareoff.player.Player;
import squareoff.strategy.MoveStrategy;
import squareoff.ui.GameController;
import squareoff.ui.GameSetup;
import squareoff.ui.StartDialog;

import java.util.Optional;

public class SquareOffApp extends Application {

    static Logger logger = LoggerFactory.getLogger(SquareOffApp.class);

    private static final String WINDOW_TITLE = "SquareOff - Checkers";

    @Override
    public void start(Stage stage) {
        Optional<GameSetup> setup = new StartDialog().show();
        if (setup.isEmpty()) {
            logger.info("Start dialog cancelled - exiting");
            stage.close();
            return;
        }
        launchGame(stage, setup.get());
    }

    private void launchGame(Stage stage, GameSetup setup) {
        PieceColor humanColor = setup.humanColor();
        PieceColor aiColor = humanColor.opposite();
        MoveStrategy aiStrategy = setup.aiStrategy();

        HumanPlayer human = new HumanPlayer(humanColor);
        Player ai = new AIPlayer(aiColor, aiStrategy);
        GameEngine engine = buildEngine(humanColor, human, ai);

        GameController controller = new GameController(engine, human, humanColor);
        stage.setTitle(WINDOW_TITLE + "  (You: " + humanColor + ", AI: " + setup.aiLabel() + ")");
        stage.setScene(new Scene(controller.getRoot()));
        stage.setResizable(false);
        stage.show();
        controller.start();
    }

    private GameEngine buildEngine(PieceColor humanColor, HumanPlayer human, Player ai) {
        Player redPlayer = (humanColor == PieceColor.RED) ? human : ai;
        Player blackPlayer = (humanColor == PieceColor.BLACK) ? human : ai;
        GameEngine engine = new GameEngine(redPlayer, blackPlayer);
        engine.addObserver(new GameLogger());
        return engine;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
