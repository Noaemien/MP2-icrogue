package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICRogue extends AreaGame{

    private ICRoguePlayer player;
    //private final String[] areas = {"icrogue/Level0Room.png"};

    private Level0 level;

    private static boolean win;

    private static boolean lose;

    private SoundAcoustics winSound;

    private boolean winRepeat;

    private boolean loseRepeat;

    /**
     * Initialises the level
     */
    private void initLevel(){
        DiscreteCoordinates coords = new DiscreteCoordinates(2, 2);
        //Init new level
        level = new Level0();
        level.addArea(this);

        //Set current area
        setCurrentArea(level.getStartTitle(), true);
        ICRogueRoom currentArea = (ICRogueRoom)getCurrentArea();

        //Init player
        player = new ICRoguePlayer(currentArea, Orientation.UP, coords, "player");
        player.enterArea(currentArea, coords);
        currentArea.visiting();

        //Init win sound
        winSound = new SoundAcoustics(ResourcePath.getSound("WinSong")); //TODO PAS FORCEMENT AU BON ENDROIT
    }

    /**
     * Enables the player to transfer from one room to another
     */
    private void switchRoom(){
        //Player leaves area
        player.leaveArea();

        //Set current area to new room
        setCurrentArea(player.connectorDestRoom, true);
        ICRogueRoom currentArea = (ICRogueRoom)getCurrentArea();

        //Player enters new room
        player.enterArea(currentArea, player.connectorDestCoords);
        currentArea.visiting();

    }

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            initLevel();
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        //Reset level if R is pressed
        Keyboard keyboard= getCurrentArea().getKeyboard();
        if (keyboard.get(Keyboard.R).isPressed()){
            initLevel();
            lose = false;
            loseRepeat = false;
            win = false;
            winRepeat = false;
        }

        //Make player switch room when he is in connector
        if(player.isInConnector){
            switchRoom();
            player.isInConnector = false;
        }

        //If level is completed enable win procedure
        if (level.isCompleted() && !winRepeat) {
            win = true;
            winRepeat = true;
            winSound.shouldBeStarted();
            winSound.bip(getWindow());
        }

        //If player is dead enable lose procedure
        if (player.isDead() && !loseRepeat){
            lose = true;
            loseRepeat = true;
        }

        super.update(deltaTime);

    }



    @Override
    public void end() {
        super.end();
    }

    /**
     * returns if the game is won
     * @return (Boolean) win
     */
    public static boolean hasWon(){
        return win;
    }

    /**
     * returns if the game is lost
     * @return (Boolean) lose
     */
    public static boolean hasLost(){
        return lose;
    }


    @Override
    public String getTitle() {
            return "ICRogue";
        }
}
