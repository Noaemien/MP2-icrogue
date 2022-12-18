package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Stick;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Projectile;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.game.areagame.actor.Animation.createAnimations; //TODO pas sur de ca a verif

public class ICRoguePlayer extends ICRogueActor implements Interactor {
    private float hp;

    private Sprite hpView;

    private TextGraphics message;

    private Sprite shadow;

    private Sprite mew;

    private Sprite sprite;

    private Sprite[][] spriTab = new Sprite[4][4];



    /// Animation duration in frame number
    private final static int MOVE_DURATION = 5;

    private boolean hasStaff = false;

    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    private ArrayList<Integer> collectedKeys = new ArrayList<Integer>();

    public boolean isInConnector = false;
    public String connectorDestRoom;
    public DiscreteCoordinates connectorDestCoords;

    private class ICRoguePlayerInteractionHandler implements ICRogueInteractionHandler {
        @Override
        public void interactWith(Cherry cherry, boolean isCellInteraction) {
            if (isCellInteraction) {
                cherry.collect();
                hp += 1;
            }
        }

        @Override
        public void interactWith(Stick stick, boolean isCellInteraction) {
            if (wantsViewInteraction() && getFieldOfViewCells().equals(stick.getCurrentCells())) {
                stick.collect();
                hasStaff = true;
            }
        }

        @Override
        public void interactWith(Connector connector, boolean isCellInteraction) {
            if (!isCellInteraction) {
                if (collectedKeys.contains(connector.keyId)) {
                    connector.setState(Connector.State.OPEN);
                }
            } else if (!isDisplacementOccurs()) {
                isInConnector = true;
                connectorDestRoom = connector.getDestinationRoom();
                connectorDestCoords = connector.getConnectorDestRoom();
            }
        }

        @Override
        public void interactWith(Key key, boolean isCellInteraction) {
            if (isCellInteraction) {
                key.collect();
                collectedKeys.add(key.ID);
            }
        }


    }


    ICRoguePlayerInteractionHandler handler = new ICRoguePlayerInteractionHandler();

    Foreground hpOneDisplay = new Foreground("zelda/heartDisplay", 1, 1, new RegionOfInterest(32,0,16,16));
    Foreground hpHalfDisplay = new Foreground("zelda/heartDisplay", 1, 1, new RegionOfInterest(16,0,16,16));
    Foreground hpNullDisplay = new Foreground("zelda/heartDisplay", 1, 1, new RegionOfInterest(0,0,16,16));


    public ICRoguePlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) {
        super(owner, orientation, coordinates);
        iniTab(spriTab);
        message = new TextGraphics("PLAYER_1", 0.4f, Color.MAGENTA);
        message.setParent(this);
        message.setAnchor(new Vector(-0.1f, 1.2f));
        shadow = new Sprite("shadow", 0.58f, 0.58f, this);
        shadow.setAnchor(new Vector(0.23f, -0.15f));
        mew = new Sprite("mew.fixed", 0.5f, 0.5f, this,
                new RegionOfInterest(0, 0, 16, 32), new Vector(-0.2f, -.15f));

        sprite = new Sprite("zelda/player", .75f, 1.5f, this,
                new RegionOfInterest(0, 0, 16, 32), new Vector(0.15f, -.15f));

        hp = 3;
        resetMotion();
    }

    /**
     * Center the camera on the player
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    public void iniTab(Sprite[][] spriTab){
        for(int i = 0; i < 4; ++i){
            spriTab[0][i] = new Sprite("zelda/player", .75f, 1.5f, this,
                    new RegionOfInterest(16*i,96,16, 32), new Vector(0.15f, -.15f));
            spriTab[1][i] = new Sprite("zelda/player", .75f, 1.5f, this,
                    new RegionOfInterest(16*i,64,16, 32), new Vector(0.15f, -.15f));
            spriTab[2][i] = new Sprite("zelda/player", .75f, 1.5f, this,
                    new RegionOfInterest(16*i,32,16, 32), new Vector(0.15f, -.15f));
            spriTab[3][i] = new Sprite("zelda/player", .75f, 1.5f, this,
                    new RegionOfInterest(16*i,0,16, 32), new Vector(0.15f, -.15f));
        }
    }


    Animation animationD = new Animation(4, spriTab[3]);

    Animation animationL = new Animation(4, spriTab[0]);
    Animation animationU = new Animation(4, spriTab[1]);
    Animation animationR = new Animation(4, spriTab[2]);

    @Override
    public void update(float deltaTime) {
        switch (getOrientation()) {
            case DOWN -> animationD.update(deltaTime);
            case RIGHT -> animationR.update(deltaTime);
            case UP -> animationU.update(deltaTime);
            case LEFT -> animationL.update(deltaTime);

        }

        if (hp < 0) hp = 0;
        Keyboard keyboard = getOwnerArea().getKeyboard();

        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        fireBallIfXDown(getOrientation(), keyboard.get(Keyboard.X));

        super.update(deltaTime);

        if (this.projectiles != null) {
            for (int i = 0; i < projectiles.size(); ++i) {
                if (projectiles.get(i).isConsumed()) {
                    //projectiles.get(i).leaveArea();
                    projectiles.remove(projectiles.get(i));
                    //TODO SI JE COMMENTE CA FIXE LA ACTOR VEUT PAS SE LEAVE MAIS CHELOU
                }
            }
        }

        if (isDead()){
            leaveArea();
        }

    }

    public void fireBallIfXDown(Orientation orientation, ch.epfl.cs107.play.window.Button b) {
        if (b.isPressed() && hasStaff && !isDisplacementOccurs()) {
            Projectile newFireBall = new Fire(this.getOwnerArea(), orientation, getCurrentMainCellCoordinates());
            newFireBall.enterArea(this.getOwnerArea(), getCurrentMainCellCoordinates());
            projectiles.add(newFireBall);

        }
    }

    /**
     * Replace Sprite with Sprite facing orientation
     *
     * @param orientation (Orientation): given orientation, not null
     */
    public void rotateSpriteToOrientation(Orientation orientation) {
        if (orientation.equals(Orientation.LEFT)) {
            sprite = new Sprite("zelda/player" , .75f, 1.5f, this,
                    new RegionOfInterest(0, 96, 16, 32), new Vector(.15f,
                    -.15f));
        } else if (orientation.equals(Orientation.RIGHT)) {
            sprite = new Sprite("zelda/player" , .75f, 1.5f, this,
                    new RegionOfInterest(0, 32, 16, 32), new Vector(.15f,
                    -.15f));
        } else if (orientation.equals(Orientation.UP)) {
            sprite = new Sprite("zelda/player" , .75f, 1.5f, this,
                    new RegionOfInterest(0, 64, 16, 32), new Vector(.15f,
                    -.15f));
        } else if (orientation.equals(Orientation.DOWN)) {
            sprite = new Sprite("zelda/player" , .75f, 1.5f, this,
                    new RegionOfInterest(0, 0, 16, 32), new Vector(.15f, -.15f));
        }

    }



    /**
     * Orientate and Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param b (Button): button corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, ch.epfl.cs107.play.window.Button b){
        if(b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                rotateSpriteToOrientation(orientation);
                move(MOVE_DURATION);
            }
        }
    }



    @Override
    public void draw(Canvas canvas) {
        if (isDisplacementOccurs()) {
            switch (getOrientation()) {
                case DOWN -> animationD.draw(canvas);
                case RIGHT -> animationR.draw(canvas);
                case UP -> animationU.draw(canvas);
                case LEFT -> animationL.draw(canvas);
            }
        } else   sprite.draw(canvas);
        switch ((int) hp) {
            case 3 -> hpOneDisplay.draw(canvas);
            case 2 -> hpHalfDisplay.draw(canvas);
            case 1 -> hpNullDisplay.draw(canvas);
        }
        shadow.draw(canvas);
        mew.draw(canvas);
        message.draw(canvas);

    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())); //La cellule a laquelle le joueur fait face
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        Keyboard keyboard= getOwnerArea().getKeyboard();
        return keyboard.get(Keyboard.W).isPressed(); //retourn true si est W est appuiyé sinon retourne faulse
    }



    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(this.handler , isCellInteraction);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    public void inflictDamage(int damage){
        hp -= damage;
    }

    public boolean isDead(){
        return hp <=0;
    }
}