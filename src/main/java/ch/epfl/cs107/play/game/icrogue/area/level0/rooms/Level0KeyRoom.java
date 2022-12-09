package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0KeyRoom extends Level0ItemRoom{

    Key key;

    public Level0KeyRoom(DiscreteCoordinates tileCoordinates, int keyId) {
        super(tileCoordinates);
        key = new Key(this, Orientation.UP, new DiscreteCoordinates(5, 5), keyId);
        super.addItem(key);
    }

    @Override
    protected void createArea() {
        super.createArea();
        registerActor(key);
    }

    @Override
    public void addItem(Item item) {
    }
}
