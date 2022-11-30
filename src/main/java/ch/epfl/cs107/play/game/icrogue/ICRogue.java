package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.io.DefaultFileSystem;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ICRogue extends AreaGame{

    public final static float CAMERA_SCALE_FACTOR = 13.f;

    private ICRoguePlayer player;
    private final String[] areas = {"icrogue/Level0Room.png"};

    private Level0Room salleCourante;

    private int areaIndex;
    /**
     * Add all the areas
     */
    private void initLevel(){
        DiscreteCoordinates coords = new DiscreteCoordinates(0, 0);
        salleCourante = new Level0Room(coords);
        addArea(salleCourante);
        setCurrentArea(salleCourante.getTitle(), true);
        //player = new ICRoguePlayer();
        //player.enterArea(area, coords);

    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {


        if (super.begin(window, fileSystem)) {
            initLevel();
            areaIndex = 0;
            initArea(areas[areaIndex]);
            return true;
        }
        return false;
    }

    private void initArea(String areaKey) {

        ICRogueRoom area = (ICRogueRoom)setCurrentArea(areaKey, true);
        DiscreteCoordinates coords = area.getPlayerSpawnPosition();
        //player = new ICRoguePlayer();
        //player.enterArea(area, coords);
        //player.centerCamera();

    }

    @Override
    public void update(float deltaTime) {
        //if(player.isWeak()){
        //    switchArea();
        //}
        super.update(deltaTime);

    }

    @Override
    public void end() {
    }

    @Override
    public String getTitle() {
            return "ICRogue";
        }
        protected void switchArea() {

        //player.leaveArea();

        areaIndex = (areaIndex==0) ? 1 : 0;

        ICRogueRoom currentArea = (ICRogueRoom)setCurrentArea(areas[areaIndex], false);
        //player.enterArea(currentArea, currentArea.getPlayerSpawnPosition());

        //player.strengthen();
    }


}
