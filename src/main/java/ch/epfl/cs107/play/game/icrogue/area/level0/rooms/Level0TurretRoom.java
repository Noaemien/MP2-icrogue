package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Enemy;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0TurretRoom extends Level0EnemyRoom{
    Turret turret1;
    Turret turret2;
    public Level0TurretRoom(DiscreteCoordinates tileCoordinates) {
        super(tileCoordinates);
        turret1 = new Turret(this, Orientation.UP, new DiscreteCoordinates(1, 8), Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT);
        turret2 = new Turret(this, Orientation.UP, new DiscreteCoordinates(8, 1), Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT);

        addEnemy(turret1, turret2);

    }
}
