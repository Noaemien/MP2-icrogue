package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.tutosSolution.Tuto2;
import ch.epfl.cs107.play.game.tutosSolution.Tuto2Behavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;
public abstract class ICRogueRoom extends Area implements Logic {

    private ICRogueBehavior behavior;

    protected boolean hasBeenVisited;
    final protected DiscreteCoordinates roomCoordinates;
    protected String behaviorName;

    private SoundAcoustics door;

    private boolean reapeted;

    protected List<Connector> connectors = new ArrayList<Connector>();

    protected ICRogueRoom(List<DiscreteCoordinates > connectorsCoordinates ,
                          List<Orientation> orientations ,
                          String behaviorName , DiscreteCoordinates roomCoordinates){
        this.roomCoordinates = roomCoordinates;
        this.behaviorName = behaviorName;
        initConnectors(connectorsCoordinates, orientations);
        door = new SoundAcoustics(ResourcePath.getSound("porte"));

    }

    /**
     * Initialises the connectors of the room
     * @param connectorsCoordinates (List(DiscreteCoordinates)) of all connector coordinates
     * @param orientations (List(Orientation)) of all connector orientations
     */
    private void initConnectors(List<DiscreteCoordinates > connectorsCoordinates , List<Orientation> orientations){
        int n = connectorsCoordinates.size();
        for (int i = 0; i < n; ++i){
            connectors.add(new Connector(this, connectorsCoordinates.get(i), orientations.get(i).opposite() ));
        }
    }

    /**
     * Create the area by adding it all actors
     * called by begin method
     * Note it set the Behavior as needed !
     */
    protected abstract void createArea();

    /**
     * Registers connectors as actors
     */
    protected void registerConnectors(){
        for (Connector connector: connectors){
            registerActor(connector);
        }
    }

    @Override
    public final float getCameraScaleFactor() {
        return 11.f;//ICRogue.CAMERA_SCALE_FACTOR;
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new ICRogueBehavior(window, behaviorName);
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }

    /**
     * Sets all connectors that aren't LOCKED or INVISIBLE to a defined state
     * @param state (Connector.State): state to set connectors to
     */
    private void setAllConnectorStates(Connector.State state){
        for(Connector connector : connectors){
            if(connector.getState() !=Connector.State.LOCKED && connector.getState() != Connector.State.INVISIBLE) {
                connector.setState(state);
            }
        }
    }


    /**
     * For all connectors that are not LOCKED or INVISIBLE
     *  Sets to open if is closed or sets to closed if is open
     */
    private void switchUnlockedConnectorStates(){
        Connector.State state;
        for(Connector connector : connectors){
            if (connector.getState() != Connector.State.LOCKED && connector.getState() != Connector.State.INVISIBLE) {
                if (connector.getState() == Connector.State.CLOSED){
                    state = Connector.State.OPEN;
                } else {
                    state = Connector.State.CLOSED;
                }
                connector.setState(state);
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        //Un-comment section below for debugging purposes.
        //Enables opening doors when pressing o
        //Closing unlocked doors with t
        //Locking west door with l

        Keyboard keyboard= this.getKeyboard();

        if (keyboard.get(Keyboard.O).isPressed()){
            setAllConnectorStates(Connector.State.OPEN);
        } else if (keyboard.get(Keyboard.T).isPressed()) {
            switchUnlockedConnectorStates();
        } else if (keyboard.get(Keyboard.L).isPressed()){
            connectors.get(0).setState(Connector.State.LOCKED);
        }


        //if level is completed, do once
        if (isCompleted() && !reapeted){ //TODO: SI ON VEUT FAIRE QUE LES PORTENT SPAWN MEME PAS QUAND TU ENTRE DANS LA SALLE SI C'EST PAS UNE ItEM ROOM
            setAllConnectorStates(Connector.State.OPEN);
            door.shouldBeStarted();
            door.bip(AreaGame.getWindow());
            reapeted = true;
        }
    }


    //-------
    //GETTERS
    //-------
    public void visiting(){
        hasBeenVisited = true;
    }

    protected boolean isCompleted(){
        return hasBeenVisited;
    }
}
