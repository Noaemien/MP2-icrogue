package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;

import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Projectile;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Turret extends Enemy {

    private Sprite sprite;

    private SoundAcoustics mobDeath;

    private SoundAcoustics throwArrow;

    private final float ARROWCOOLDOWN = .6f;

    private float arrowTimer = 0f;

    private float moveTimer = 0.f;

    private final float MOVECOOLDOWN = 1.f;


    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    private Orientation[] shootingDirections;

    public Turret(Area area, Orientation orientation, DiscreteCoordinates position, Orientation... shootingDirections) {
        super(area, orientation, position);
        sprite = new Sprite("icrogue/static_npc", 1.5f, 1.5f,
                this, null, new Vector(-0.25f, 0));
        this.shootingDirections = shootingDirections;
        throwArrow = new SoundAcoustics((ResourcePath.getSound("Flèche-lance")));
        mobDeath = new SoundAcoustics((ResourcePath.getSound("mobDeath")));
    }

    @Override
    public void draw(Canvas canvas) {
        if(!hasBeenKilled()) sprite.draw(canvas);
    }

    @Override
    public void kill() {
        super.kill();
        mobDeath.shouldBeStarted();
        mobDeath.bip(AreaGame.getWindow());

    }

    public void update(float deltaTime) {
        super.update(deltaTime);
        if (!isDisplacementOccurs()) {
            if (arrowTimer < ARROWCOOLDOWN) arrowTimer += deltaTime;
            else {
                arrowTimer = 0.f;
                for (Orientation direction : shootingDirections) {
                    throwArrow(direction);
                }
                throwArrow.shouldBeStarted();
                throwArrow.bip(AreaGame.getWindow());
            }
            if (moveTimer < MOVECOOLDOWN) moveTimer += deltaTime;
            else {
                moveTimer = 0.f;
                arrowTimer = 0.f;
                moveRandomly();
            }
        }

    }

    private void moveRandomly(){
        Orientation[] orientations = new Orientation[] {Orientation.LEFT, Orientation.UP, Orientation.DOWN, Orientation.RIGHT};
        List<DiscreteCoordinates> border = new ArrayList<>();
        for (int i = 4; i < 6; ++i ){
            border.add(new DiscreteCoordinates(0, i));
            border.add(new DiscreteCoordinates(9, i));
            border.add(new DiscreteCoordinates(i, 0));
            border.add(new DiscreteCoordinates(i, 9));
        }
        if (!isDisplacementOccurs()) {
            orientate(orientations[(int)(Math.random() * 4)]);
            if(!border.contains(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))
                move(2);
        }

    }

    public void throwArrow(Orientation orientation) {
            Projectile arrow = new Arrow(this.getOwnerArea(), orientation, getCurrentMainCellCoordinates());
            arrow.enterArea(this.getOwnerArea(), getCurrentMainCellCoordinates());
            projectiles.add(arrow);
    }


    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
    }
}
