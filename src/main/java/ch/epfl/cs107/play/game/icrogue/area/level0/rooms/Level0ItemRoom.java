package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

abstract public class Level0ItemRoom extends Level0Room{
    private ArrayList<Item> items = new ArrayList<Item>();

    public Level0ItemRoom(DiscreteCoordinates tileCoordinates) {
        super(tileCoordinates);
    }

    public void addItem(Item item){
        items.add(item);
    }

    @Override
    protected boolean isCompleted() {
        for (Item item: items) {
            if (!item.isCollected()) return false;
        }
        return true;
    }

}
