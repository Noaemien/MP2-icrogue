package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Stick;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0StaffRoom extends Level0ItemRoom{

    Stick stick;
    public Level0StaffRoom(DiscreteCoordinates tileCoordinates) {
        super(tileCoordinates);
        stick = new Stick(this, Orientation.UP, new DiscreteCoordinates(5, 5));
        super.addItem(stick);
    }

    @Override
    public void addItem(Item item) {
    }

    @Override
    public void createArea(){
        super.createArea();
        registerActor(stick);
    }
}
