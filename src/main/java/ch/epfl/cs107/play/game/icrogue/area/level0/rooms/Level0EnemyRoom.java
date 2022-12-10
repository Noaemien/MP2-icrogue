package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.enemies.Enemy;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.Arrays;

public class Level0EnemyRoom extends Level0Room {

    private ArrayList<Enemy> activeEnemies = new ArrayList<Enemy>();

    public Level0EnemyRoom(DiscreteCoordinates tileCoordinates) {
        super(tileCoordinates);

    }

    protected void addEnemy(Enemy... newEnemies){
        activeEnemies.addAll(Arrays.asList(newEnemies));
    }

    protected void removeDeadEnemies(){
        activeEnemies.removeIf(Enemy::hasBeenKilled);
    }

    @Override
    protected void createArea() {
        super.createArea();
        for (Enemy enemy: activeEnemies){
            registerActor(enemy);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        removeDeadEnemies();
    }

    @Override
    protected boolean isCompleted() {
        return activeEnemies.isEmpty();
    }
}
