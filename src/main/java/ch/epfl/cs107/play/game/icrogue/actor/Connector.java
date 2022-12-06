package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Positionable;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Connector extends AreaEntity implements Interactable {

    final int NO_KEY_ID = -1;
    final ICRogueRoom ROOM;
    private Orientation orientation;
    
    private DiscreteCoordinates connectorCoords;

    public String destinationRoom;
    
    private Connector.State state;

    private Sprite sprite;


    public enum State{

        OPEN,
        CLOSED,
        LOCKED,
        INVISIBLE;

    }

    public Connector(ICRogueRoom room, DiscreteCoordinates connectorCoords, Orientation orientation){
        super(room, orientation, connectorCoords);
        ROOM = room;
        this.orientation = orientation;
        state = State.INVISIBLE;
        this.connectorCoords = connectorCoords;
        setSprite(state);
    }

    private boolean isOpen(){
        return (this.state == State.OPEN);
    }
    private void setSprite(Connector.State state){
            if (state == State.INVISIBLE){
                // pour invisible:
                sprite = new Sprite("icrogue/invisibleDoor_" + orientation.ordinal(),
                    ((orientation.ordinal() + 1) % 2 + 1), (orientation.ordinal() % 2 + 1), (Positionable) this);
            } else if (state == State.CLOSED) {
                // pour fermé:
                sprite = new Sprite("icrogue/door_" + orientation.ordinal(),
                        (orientation.ordinal() + 1) % 2 + 1, orientation.ordinal() % 2 + 1, (Positionable) this);
            } else if (state == State.LOCKED) {
                // pour verrouillé
                sprite = new Sprite("icrogue/lockedDoor_" + orientation.ordinal(),
                        (orientation.ordinal() + 1) % 2 + 1, orientation.ordinal() % 2 + 1, (Positionable) this);
            }

    }

    private String getDestination(ICRogueRoom room){
        return room.getTitle();
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        DiscreteCoordinates coord = getCurrentMainCellCoordinates();
        return List.of(coord , coord.jump(new
                Vector((getOrientation().ordinal()+1)%2,
                getOrientation().ordinal()%2)));
    }

    public void setState(State state){
        this.state = state;
    }

    public State getState(){
        return this.state;
    }

    public void updateSprite(){
        setSprite(this.state);
    }

    public Orientation getOrientation(){
        return orientation;
    }
    
    public DiscreteCoordinates getCurrentMainCellCoordinates(){
        return connectorCoords;
    }
    
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {

    }

    @Override
    public void onEntering(List<DiscreteCoordinates> coordinates) {

    }

    @Override
    public void draw(Canvas canvas) {
        if (!isOpen()) {
            sprite.draw(canvas);
        }
    }

}
