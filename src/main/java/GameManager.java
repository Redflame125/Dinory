public class GameManager {
    private final GameUI ui;
    public GameManager(GameUI ui) {
        this.ui = ui;
        if (ui.endCounter == ui.getX() * ui.getY() / 2) {
            ui.dispose();
            new EndUI(ui.playerScores);
        }
    }

    // ToDo Mach Game Logic und Ansagen!!!
}
