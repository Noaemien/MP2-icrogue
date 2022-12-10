package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.tutosSolution.area.Tuto2Area;
import ch.epfl.cs107.play.io.DefaultFileSystem;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICRogue extends AreaGame{

    public final static float CAMERA_SCALE_FACTOR = 13.f;

    private ICRoguePlayer player;
    private final String[] areas = {"icrogue/Level0Room.png"};

    private Level0 niveau;

    private int areaIndex;
    /**
     * Add all the areas
     */
    private void initLevel(){
        DiscreteCoordinates coords = new DiscreteCoordinates(0, 0);
        niveau = new Level0();
        niveau.addArea(this);
        setCurrentArea(niveau.getStartTitle(), true);

        ICRogueRoom salleCourante = (ICRogueRoom)getCurrentArea();
        player = new ICRoguePlayer(salleCourante, Orientation.UP, new DiscreteCoordinates(2, 2), "max");
        player.rotateSpriteToOrientation(player.getOrientation());
        player.enterArea(salleCourante, new DiscreteCoordinates(2, 2));
        salleCourante.visiting();
    }

    private void switchRoom(){
        player.leaveArea();
        setCurrentArea(player.connectorDestRoom, true);
        ICRogueRoom salleCourante = (ICRogueRoom)getCurrentArea();
        player.enterArea(salleCourante, player.connectorDestCoords);
        salleCourante.visiting();

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
        Keyboard keyboard= getCurrentArea().getKeyboard();
        if (keyboard.get(Keyboard.R).isPressed()){
            initLevel();
        }

        if(player.isInConnector){
            switchRoom();
            player.isInConnector = false;
        }

        if (niveau.isCompleted()){
            System.out.println("Win");
        } else if (player.isDead()) {
            System.out.println("GameOver");
        }

        super.update(deltaTime);

    }

    @Override
    public void end() {
    }

    @Override
    public String getTitle() {
            return "ICRogue";
        }


}
