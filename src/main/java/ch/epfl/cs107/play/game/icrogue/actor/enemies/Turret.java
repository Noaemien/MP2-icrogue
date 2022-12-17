package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;

import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Projectile;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;

public class Turret extends Enemy {

    private Sprite sprite;

    private final float COOLDOWN = 2.f;

    private float timer = 1.2f;

    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    private Orientation[] shootingDirections;


    private class ICRogueTurretInteractionHandler implements ICRogueInteractionHandler { //TODO TURRET N'est PAS UN INTERACTOR, ENLEVER LE HANDLER
        @Override
        public void interactWith(Fire fire, boolean isCellInteraction) {
            if (isCellInteraction){
                dying();
                //fire.consume();
            }
        }

        @Override
        public void interactWith(ICRoguePlayer player, boolean isCellInteraction) {
            if (isCellInteraction){
                dying();
            }
        }
    }

    private ICRogueTurretInteractionHandler handler;


    public Turret(Area area, Orientation orientation, DiscreteCoordinates position, Orientation... shootingDirections) {
        super(area, orientation, position);
        sprite = new Sprite("icrogue/static_npc", 1.5f, 1.5f,
                this, null, new Vector(-0.25f, 0));
        this.shootingDirections = shootingDirections;
        handler = new ICRogueTurretInteractionHandler();
    }

    @Override
    public void draw(Canvas canvas) {
        if(!hasBeenKilled()) sprite.draw(canvas);
    }

    public void update(float deltaTime) {
        if (timer < COOLDOWN) timer += deltaTime;
        else {
            timer = 0.f;
            for (Orientation direction : shootingDirections) {
                throwArrow(direction);
            }
        }
    }

    public void throwArrow(Orientation orientation) {
            Projectile arrow = new Arrow(this.getOwnerArea(), orientation, getCurrentMainCellCoordinates());
            arrow.enterArea(this.getOwnerArea(), getCurrentMainCellCoordinates());
            projectiles.add(arrow);
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(this.handler , isCellInteraction);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
    }
}
