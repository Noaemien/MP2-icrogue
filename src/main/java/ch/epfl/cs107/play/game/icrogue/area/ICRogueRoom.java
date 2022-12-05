package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.tutosSolution.Tuto2;
import ch.epfl.cs107.play.game.tutosSolution.Tuto2Behavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICRogueRoom extends Area {

    private ICRogueBehavior behavior;
    final protected DiscreteCoordinates roomCoordinates;
    final protected String behaviorName;

    private List<Connector> connectors = new ArrayList<Connector>();

    protected ICRogueRoom(List<DiscreteCoordinates > connectorsCoordinates ,
                          List<Orientation> orientations ,
                          String behaviorName , DiscreteCoordinates roomCoordinates){
        this.roomCoordinates = roomCoordinates;
        this.behaviorName = behaviorName;
        initConnectors(connectorsCoordinates, orientations);
    }

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

    protected void registerConnectors(){
        for (Connector connector: connectors){
            registerActor(connector);
        }
    }
    /// EnigmeArea extends Area

    @Override
    public final float getCameraScaleFactor() {
        return 11.f;//ICRogue.CAMERA_SCALE_FACTOR;
    }

    public abstract DiscreteCoordinates getPlayerSpawnPosition();



    /// Demo2Area implements Playable

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

    private void setAllConnectorStates(Connector.State state){
        for(Connector connector : connectors){
            if (connector.getState() != Connector.State.LOCKED) {
                connector.setState(state);
                connector.updateSprite();
            }
        }
    }
    
    private void switchUnlockedConnectorStates(){
        Connector.State state;
        for(Connector connector : connectors){
            if (connector.getState() != Connector.State.LOCKED) {
                if (connector.getState() == Connector.State.CLOSED){
                    state = Connector.State.OPEN;
                } else {
                    state = Connector.State.CLOSED;
                }

                connector.setState(state);
                connector.updateSprite();
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard= this.getKeyboard();

        if (keyboard.get(Keyboard.O).isPressed()){
            setAllConnectorStates(Connector.State.OPEN);
        } else if (keyboard.get(Keyboard.T).isPressed()) {
            switchUnlockedConnectorStates();
        } else if (keyboard.get(Keyboard.L).isPressed()){
            connectors.get(0).setState(Connector.State.LOCKED);
            connectors.get(0).updateSprite();
        }

    }
}
