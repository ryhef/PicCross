package piccross;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.Test;


class GameTest {
    
    @Test
    void checkTable(){
        GameLogic testGame = new GameLogic();
        testGame.newGame();
        assertFalse(testGame.checkWin());
    }

    @Test
    void checkSquare(){
        GameLogic testGame = new GameLogic();
        testGame.newGame();
        testGame.debug2();
        testGame.resetGame();
        assertTrue(testGame.checkGrid(1));
    }
        
}
