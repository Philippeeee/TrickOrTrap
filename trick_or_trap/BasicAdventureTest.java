
import java.io.IOException;

import AdventureModel.AdventureGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class BasicAdventureTest
 */
public class BasicAdventureTest {

    /**
     * Constructor
     */
    public BasicAdventureTest(){
    }
    @Test
    /**
     * test commands
     * @throws IOException
     */
    void getCommandsTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String commands = game.player.getCurrentRoom().getCommands();
        assertEquals("WEST,UP,NORTH,IN,SOUTH,DOWN", commands);
    }

    @Test
    /**
     * test object string
     * @throws  IOException
     */
    void getObjectString() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a water bird", objects);
    }

    @Test
    /**
     * test object string again
     * @throws IOException
     */
    void getObjectString2() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a water bird", objects);
    }


}
