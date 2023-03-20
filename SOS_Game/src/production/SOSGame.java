package production;

import java.util.ArrayList;
public class SOSGame {
    public  int TOTAL_ROWS;
    public  int TOTAL_COLUMNS;
    public enum Cell {EMPTY, S, O}
    public Cell[][] grid;
    private char turn;
    protected int x, y;
    protected int blueScore, redScore;
    protected ArrayList<ArrayList<Integer>> sosInfo;
    public enum GameModeType {Simple, General}
    protected GameModeType currentGameModeType;
    public enum GameState {PLAYING, DRAW, BLUE_WON, RED_WON}
    private GameState currentGameState;
    public void setCurrentGameType(GameModeType currentGameModeType) {
        this.currentGameModeType = currentGameModeType;
    }
    public SOSGame(int n){
        currentGameModeType = GameModeType.Simple;
        grid = new Cell[n][n];
        TOTAL_ROWS = TOTAL_COLUMNS = n;
        initGame();
    }
    private void initGame() {
        for (int row = 0; row < TOTAL_ROWS; ++row) {
            for (int col = 0; col < TOTAL_COLUMNS; ++col) {
                grid[row][col] = Cell.EMPTY;
            }
        }
        currentGameState = GameState.PLAYING;
        turn = 'S';
        blueScore = redScore = 0;
        sosInfo = new ArrayList<>();
    }
    public void resetGame() {
        initGame();
    }
    public int getTotalRows() {
        return TOTAL_ROWS;
    }
    public int getTotalColumns() {
        return TOTAL_COLUMNS;
    }
    public GameModeType getCurrentGameType() {
        return currentGameModeType;
    }
    public ArrayList<ArrayList<Integer>> getSosInfo() {
        return sosInfo;

    }
    public Cell getCell(int row, int column) {
        if (row >= 0 && row < TOTAL_ROWS && column >= 0 && column < TOTAL_COLUMNS) {
            return grid[row][column];
        } else {
            return null;
        }
    }
    public char getTurn() {
        return turn;
    }

    public void makeMove(int row, int column, int type) {
        if (row >= 0 && row < TOTAL_ROWS && column >= 0 && column < TOTAL_COLUMNS && grid[row][column] == Cell.EMPTY) {
            x = row;
            y = column;
            grid[row][column] = (type == 1) ? Cell.S : Cell.O;
            sosInfo.add(checkSOS());
            updateGameState();
            String input = (type == 0) ? "S" : "O";

            String whoseTurn = (turn == 'B') ? "Red" : "Blue";
            turn = (turn == 'B') ? 'R' : 'B';
        }
    }
    public ArrayList<Integer> checkSOS() {
        ArrayList<Integer> scoring = new ArrayList<Integer>();
        if (turn == 'B')
            scoring.add(0);
        else
            scoring.add(1);
        boolean isChanged = false;
        if (grid[x][y] == Cell.O) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (x - i < 0 || x - i >= TOTAL_ROWS || x + i < 0 || x + i >= TOTAL_ROWS || y - j < 0
                            || y - j >= TOTAL_COLUMNS || y + j < 0 || y + j >= TOTAL_COLUMNS)
                        continue;
                    if (grid[x - i][y - j] == Cell.S && grid[x + i][y + j] == Cell.S) {
                        scoring.add(x - i);
                        scoring.add(y - j);
                        scoring.add(x + i);
                        scoring.add(y + j);
                        if (turn == 'B')
                            blueScore++;
                        else
                            redScore++;
                        isChanged = true;
                    }
                }
            }
        } else {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (x + 2 * i < 0 || x + 2 * i >= TOTAL_ROWS || y + 2 * j < 0 || y + 2 * j >= TOTAL_COLUMNS)
                        continue;
                    if (grid[x + 2 * i][y + 2 * j] == Cell.S && grid[x + i][y + j] == Cell.O) {
                        scoring.add(x);
                        scoring.add(y);
                        scoring.add(x + 2 * i);
                        scoring.add(y + 2 * j);
                        if (turn == 'B')
                            blueScore++;
                        else
                            redScore++;
                        isChanged = true;
                    }
                }
            }
        }
        if (isChanged)
            turn = (turn == 'B') ? 'R' : 'B';
        return scoring;
    }
    private void updateGameState() {
        int x = hasWon();
        if (x > 0) {
            if (x == 1)
                currentGameState = GameState.BLUE_WON;
            else if (x == 2)
                currentGameState = GameState.RED_WON;
            else if (x == 3)
                currentGameState = GameState.DRAW;
        }
    }
    private boolean isFull() {
        for (int row = 0; row < TOTAL_ROWS; ++row) {
            for (int col = 0; col < TOTAL_COLUMNS; ++col) {
                if (grid[row][col] == Cell.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    private int hasWon() {
        if (currentGameModeType == GameModeType.Simple) {
            if (blueScore > 0)
                return 1;
            if (redScore > 0)
                return 2;
            return 0;
        } else {
            if (!isFull())
                return 0;
            if (blueScore > redScore)
                return 1;
            else if (blueScore < redScore)
                return 2;
            else
                return 3;
        }
    }
    public GameState getGameState() {
        return currentGameState;
    }
}
