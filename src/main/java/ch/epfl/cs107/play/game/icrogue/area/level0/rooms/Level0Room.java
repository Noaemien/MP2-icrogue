package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.game.icrogue.actor.items.Stick;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0Room  extends ICRogueRoom {

    public Level0Room(DiscreteCoordinates tileCoordinates) {
        super("icrogue/Level0Room", tileCoordinates);
    }

    @Override
    public String getTitle() {
        return "icrogue/level0" + roomCoordinates.x + "" + roomCoordinates.y;
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(2,10);
    } //TODO a reverifier

    protected void createArea() {
        // Base
        registerActor(new Background(this, behaviorName));
        registerActor(new Stick(this, Orientation.DOWN, new DiscreteCoordinates(4, 3)));

        registerActor(new Cherry(this, Orientation.DOWN, new DiscreteCoordinates(6, 3), 1));
        //registerActor(new Foreground(this));
    }
}
