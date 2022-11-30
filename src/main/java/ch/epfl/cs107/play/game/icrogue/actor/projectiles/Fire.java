package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Fire extends Projectile{

    private int damage = 1;
    private int framesForMove = 5;
    Sprite sprite = new Sprite("zelda/fire", 1f, 1f, this ,
            new RegionOfInterest(0, 0, 16, 16), new
            Vector(0, 0));

    public Fire(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 1, 5);
        setSprite(sprite);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        //TODO CONSUME FIREBALL
        //if (isTargetReached()){
        //    consume();
        //}
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }
}
