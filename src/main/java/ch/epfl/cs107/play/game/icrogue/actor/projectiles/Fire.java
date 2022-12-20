package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Heart;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Fire extends Projectile {
    private final double DROP_HEART_PROPABILITY = .3f;

    private SoundAcoustics fireBoom;

    Sprite[] spriTabo = {new Sprite("zelda/fire", 1f, 1f, this,
            new RegionOfInterest(0, 0, 16, 16), new
            Vector(0, 0)), new Sprite("zelda/fire", 1f, 1f, this,
            new RegionOfInterest(16, 0, 16, 16), new
            Vector(0, 0)), new Sprite("zelda/fire", 1f, 1f, this,
            new RegionOfInterest(32, 0, 16, 16), new
            Vector(0, 0)), new Sprite("zelda/fire", 1f, 1f, this,
            new RegionOfInterest(48, 0, 16, 16), new
            Vector(0, 0)), new Sprite("zelda/fire", 1f, 1f, this,
            new RegionOfInterest(64, 0, 16, 16), new
            Vector(0, 0)), new Sprite("zelda/fire", 1f, 1f, this,
            new RegionOfInterest(80, 0, 16, 16), new
            Vector(0, 0)), new Sprite("zelda/fire", 1f, 1f, this,
            new RegionOfInterest(96, 0, 16, 16), new Vector(0, 0))};


    Sprite[] boomSprite = {new Sprite("zelda/explosion", 1f, 1f, this,
            new RegionOfInterest(0, 0, 32, 32), new
            Vector(0, 0)), new Sprite("zelda/explosion", 1f, 1f, this,
            new RegionOfInterest(32, 0, 32, 32), new
            Vector(0, 0)), new Sprite("zelda/explosion", 1f, 1f, this,
            new RegionOfInterest(64, 0, 32, 32), new
            Vector(0, 0)), new Sprite("zelda/explosion", 1f, 1f, this,
            new RegionOfInterest(96, 0, 32, 32), new
            Vector(0, 0)), new Sprite("zelda/explosion", 1f, 1f, this,
            new RegionOfInterest(128, 0, 32, 32), new
            Vector(0, 0)), new Sprite("zelda/explosion", 1f, 1f, this,
            new RegionOfInterest(160, 0, 32, 32), new
            Vector(0, 0)), new Sprite("zelda/explosion", 1f, 1f, this,
            new RegionOfInterest(196, 0, 32, 32), new Vector(0, 0))};

    /*private void initTab(Sprite[][] spriTab){
        for(int i = 0; i < 7; ++i){
                spriTab[0][i] = new Sprite("zelda/fire", 1f, 1f, this,
                        new RegionOfInterest(16 * i, 0, 16, 16), new
                        Vector(0, 0));
                spriTab[0][i] = new Sprite("zelda/fire", 1f, 1f, this,
                        new RegionOfInterest(16 * i, 16, 16, -16), new
                        Vector(0, 0),);)

        }
    }*/
    Animation animation = new Animation(2, spriTabo);
    Animation animboom = new Animation(1, boomSprite, false);

    private class ICRogueFireInteractionHandler implements ICRogueInteractionHandler {
        @Override
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if (!isCellInteraction) {
                if (cell.getType().equals(ICRogueBehavior.CellType.WALL) || cell.getType() == ICRogueBehavior.CellType.HOLE) {
                    consume();
                    fireBoom.shouldBeStarted();
                    fireBoom.bip(AreaGame.getWindow());
                }
            }
        }

        @Override
        public void interactWith(Turret turret, boolean isCellInteraction) {
            if (isCellInteraction) {
                turret.kill();
                if (Math.random() < DROP_HEART_PROPABILITY) {
                    getOwnerArea().registerActor(new Heart(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates()));
                }
                consume();
                fireBoom.shouldBeStarted();
                fireBoom.bip(AreaGame.getWindow());
            }
        }
    }


    ICRogueFireInteractionHandler handler;


    public Fire(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 1, 4);
        handler = new ICRogueFireInteractionHandler();
        fireBoom = new SoundAcoustics(ResourcePath.getSound("Feu-mur"));


    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(!isConsumed()) animation.update(deltaTime);
        else animboom.update(deltaTime);

    }


    @Override
    public void draw(Canvas canvas) {
        if(!isConsumed()) animation.draw(canvas);
        else animboom.draw(canvas);

    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (!isConsumed())
            ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(this.handler , isCellInteraction);
    }
}
