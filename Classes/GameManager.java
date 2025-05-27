package Classes;

public class GameManager {
    private static int size = 2;
    private static boolean timerStarted = false;
    private static int time = 60;

    public static void createGame() {
        Board board = new Board(size, size);
        board.instantiateGrid(size);
        board.randomizeGrid();
        board.cleanGrid();

        RenderGrid renderGrid = new RenderGrid(Board.getGrid(), board.getTileSize(Board.getGrid().length));
        Game game = new Game(board, renderGrid);
        renderGrid.setGame(game);

        if(!timerStarted) {
            renderGrid.timer(1000, time);
            timerStarted = true;
        }

        Window.createWindow(1280, 900, renderGrid);
        size++;
    }

    public static void setTimerStarted(boolean timerStarted) {
        GameManager.timerStarted = timerStarted;
    }

    public static void setSize(int size) {
        GameManager.size = size;
    }

    public static void setTime(int time) {
        GameManager.time = time;
    }
}
