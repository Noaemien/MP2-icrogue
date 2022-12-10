package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Stick;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.*;

public class Level0Room  extends ICRogueRoom {

    private boolean HasBeenVisited;

    public Level0Room(DiscreteCoordinates tileCoordinates) {
        super(Level0Connectors.E.getAllConnectorsPosition(), Level0Connectors.E.getAllConnectorsOrientation(), "icrogue/Level0Room", tileCoordinates);
    }


    public void Visiting(){
        //if()
        HasBeenVisited = true;
    }
    @Override
    public boolean isOn() {
        return HasBeenVisited;
    }

    @Override
    public boolean isOff() {return !HasBeenVisited;}

    @Override
    public float getIntensity() {
        return 0;
    }

    public enum Level0Connectors implements ConnectorInRoom {
        // ordre des attributs: position , destination , orientation
        W(new DiscreteCoordinates(0, 4),
                new DiscreteCoordinates(8, 5), Orientation.LEFT),
        S(new DiscreteCoordinates(4, 0),
                new DiscreteCoordinates(5, 8), Orientation.DOWN),
        E(new DiscreteCoordinates(9, 4),
                new DiscreteCoordinates(1, 5), Orientation.RIGHT),
        N(new DiscreteCoordinates(4, 9),
                new DiscreteCoordinates(5, 1), Orientation.UP);

        final DiscreteCoordinates position;
        final DiscreteCoordinates destination;
        final Orientation orientation;

        Level0Connectors(DiscreteCoordinates position, DiscreteCoordinates destination, Orientation orientation) {
            this.position = position;
            this.destination = destination;
            this.orientation = orientation;
        }

        @Override
        public int getIndex() {
            return this.ordinal();
        }

        @Override
        public DiscreteCoordinates getDestination() {
            return this.destination;
        }

        public List<Orientation > getAllConnectorsOrientation(){
            ArrayList<Orientation> orientations = new ArrayList<Orientation>();

            orientations.add(Level0Connectors.W.orientation);
            orientations.add(Level0Connectors.S.orientation);
            orientations.add(Level0Connectors.E.orientation);
            orientations.add(Level0Connectors.N.orientation);

            return orientations;
        }
        public List <DiscreteCoordinates > getAllConnectorsPosition(){
            ArrayList<DiscreteCoordinates> positions = new ArrayList<DiscreteCoordinates>();

            positions.add(Level0Connectors.W.position);
            positions.add(Level0Connectors.S.position);
            positions.add(Level0Connectors.E.position);
            positions.add(Level0Connectors.N.position);
            return positions;
        }
    }
    
    @Override
    public String getTitle() {
        return "icrogue/level0" + roomCoordinates.x + "" + roomCoordinates.y;
    }


    protected void createArea() {
        // Base
        registerActor(new Background(this, behaviorName));
        registerConnectors();
    }


}
