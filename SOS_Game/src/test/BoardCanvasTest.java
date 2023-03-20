package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import production.SOSGame;
import static org.junit.Assert.assertEquals;

public class BoardCanvasTest {
    public Object[][] grid = new Object[3][3];
    private char turn;
    private SOSGame game;

    @Before
    public void setUp() throws Exception {
        game = new SOSGame(3);
    }
    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void testEmptyBoard() {
        new SOSGame(3);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void MoveTest() {
        game.makeMove(0, 0,1);
        game.makeMove(1, 1,1);
        new SOSGame(3);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(SOSGame.Cell.S, game.grid[0][0]); // Check if the grid is still unchanged
        assertEquals('R', game.getTurn()); // Check if it's still player 1's turn
    }
}