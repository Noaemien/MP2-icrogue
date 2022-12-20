package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.HUD;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Heart;
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
import ch.epfl.cs107.play.window.Sound;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class ICRoguePlayer extends ICRogueActor implements Interactor {
    private static int hp;

    private TextGraphics message;

    private Sprite shadow;

    private HUD hud;

    private Sprite mew;

    private Sprite sprite;

    private Sprite[][] spriTab = new Sprite[4][4];

    private Sprite[][] staffTab = new Sprite[4][4];

    private String playerName = "player";

    private final float FIREBALL_COOLDOWN = .65f;

    private float fireBallTimer;



    /// Animation duration in frame number
    private final static int MOVE_DURATION = 4;

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


            if (getFieldOfViewCells().equals(stick.getCurrentCells()) && wIsDown()) {
                stick.collect();
                hasStaff = true;
            }
        }

        @Override
        public void interactWith(Connector connector, boolean isCellInteraction) {
            if (!isCellInteraction) {
                if (collectedKeys.contains(connector.keyId) && wIsDown()) {
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

        @Override
        public void interactWith(Turret turret, boolean isCellInteraction) {
            if (isCellInteraction){
                turret.kill();
            }
        }

        @Override
        public void interactWith(Heart heart, boolean isCellInteraction){
            if (isCellInteraction){
               heart.collect();
               hp += 2;
               if (hp > 6) hp = 6;
            }
        }
    }


    ICRoguePlayerInteractionHandler handler = new ICRoguePlayerInteractionHandler();

    public ICRoguePlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) {
        super(owner, orientation, coordinates);
        iniSpriteTab(spriTab);
        iniStaffTab(staffTab);
        message = new TextGraphics("PLAYER_1", 0.4f, Color.MAGENTA);
        message.setParent(this);
        message.setAnchor(new Vector(-0.1f, 1.2f));
        shadow = new Sprite("shadow", 0.58f, 0.58f, this);
        shadow.setAnchor(new Vector(0.23f, -0.15f));
        mew = new Sprite("mew.fixed", 0.5f, 0.5f, this,
                new RegionOfInterest(0, 0, 16, 32), new Vector(-0.2f, -.15f));

        sprite = new Sprite("zelda/" + playerName, .75f, 1.5f, this,
                new RegionOfInterest(0, 0, 16, 32), new Vector(0.15f, -.15f));
        hud = new HUD(null,640, 640, new RegionOfInterest(0, 0,640, 640),new Vector(0,0));


        hp = 6;
        resetMotion();
    }

    public static int getHp(){
        return hp;
    }


    public void iniSpriteTab(Sprite[][] spriTab){
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

    public void iniStaffTab(Sprite[][] staffTab){
        for(int i = 0; i < 4; ++i){
            staffTab[0][i] = new Sprite("zelda/player.staff_water", 1.5f, 1.5f, this,
                    new RegionOfInterest(32*i,96,32, 32), new Vector(-0.20f, -.15f));
            staffTab[1][i] = new Sprite("zelda/player.staff_water", 1.5f, 1.5f, this,
                    new RegionOfInterest(32*i,64,32, 32), new Vector(-0.20f, -.15f));
            staffTab[2][i] = new Sprite("zelda/player.staff_water", 1.5f, 1.5f, this,
                    new RegionOfInterest(32*i,32,32, 32), new Vector(-0.20f, -.15f));
            staffTab[3][i] = new Sprite("zelda/player.staff_water", 1.5f, 1.5f, this,
                    new RegionOfInterest(32*i,0,32, 32), new Vector(-0.20f, -.15f));
        }
    }

    Animation staffanimationD = new Animation(4,staffTab[3]);
    Animation staffanimationU = new Animation(4,staffTab[2]);
    Animation staffanimationR = new Animation(4,staffTab[1]);
    Animation staffanimationL = new Animation(4,staffTab[0]);
    Animation animationD = new Animation(4, spriTab[3]);
    Animation animationL = new Animation(4, spriTab[0]);
    Animation animationU = new Animation(4, spriTab[1]);
    Animation animationR = new Animation(4, spriTab[2]);

    @Override
    public void update(float deltaTime) {
        if(!hasStaff) {
            switch (getOrientation()) {
                case DOWN -> animationD.update(deltaTime);
                case RIGHT -> animationR.update(deltaTime);
                case UP -> animationU.update(deltaTime);
                case LEFT -> animationL.update(deltaTime);
            }
        } else {
                switch (getOrientation()) {
                    case DOWN -> staffanimationD.update(deltaTime);
                case RIGHT -> staffanimationR.update(deltaTime);
                case UP -> staffanimationU.update(deltaTime);
                case LEFT -> staffanimationL.update(deltaTime);
            }

        }

        if (hp < 0) hp = 0;
        Keyboard keyboard = getOwnerArea().getKeyboard();

        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        if (fireBallTimer < FIREBALL_COOLDOWN) fireBallTimer += deltaTime;
            else if (throwingFireball(keyboard.get(Keyboard.X))) {
            fireBallTimer = 0.f;
            fireBallIfXDown(getOrientation(), keyboard.get(Keyboard.X));

        }
        super.update(deltaTime);

        if (this.projectiles != null) {
            for (int i = 0; i < projectiles.size(); ++i) {
                if (projectiles.get(i).isConsumed()) {
                    projectiles.remove(projectiles.get(i));
                }
            }
        }
        if (hasStaff) switchPlayerName();

        if (isDead()){  //NOT NECESSARY WITH HUD
            leaveArea();
        }

    }

    public void fireBallIfXDown(Orientation orientation, ch.epfl.cs107.play.window.Button b) {
        if (b.isDown() && hasStaff && !isDisplacementOccurs()) {
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
        if (!hasStaff) {
            if (orientation.equals(Orientation.LEFT)) {
                sprite = new Sprite("zelda/player", .75f, 1.5f, this,
                        new RegionOfInterest(0, 96, 16, 32), new Vector(.15f,
                        -.15f));
            } else if (orientation.equals(Orientation.RIGHT)) {
                sprite = new Sprite("zelda/player", .75f, 1.5f, this,
                        new RegionOfInterest(0, 32, 16, 32), new Vector(.15f,
                        -.15f));
            } else if (orientation.equals(Orientation.UP)) {
                sprite = new Sprite("zelda/player", .75f, 1.5f, this,
                        new RegionOfInterest(0, 64, 16, 32), new Vector(.15f,
                        -.15f));
            } else if (orientation.equals(Orientation.DOWN)) {
                sprite = new Sprite("zelda/player", .75f, 1.5f, this,
                        new RegionOfInterest(0, 0, 16, 32), new Vector(.15f, -.15f));
            }
        } else {
            if (orientation.equals(Orientation.DOWN)) {
                sprite = new Sprite("zelda/player.staff_water", 1.5f, 1.5f, this,
                        new RegionOfInterest(64, 0, 32, 32), new Vector(-.20f, -.25f));
            } else if (orientation.equals(Orientation.UP)) {
                sprite = new Sprite("zelda/player.staff_water", 1.5f, 1.5f, this,
                        new RegionOfInterest(64, 32, 32, 32), new Vector(-.20f,
                        -.25f));
            } else if (orientation.equals(Orientation.RIGHT)) {
                sprite = new Sprite("zelda/player.staff_water", 1.5f, 1.5f, this,
                        new RegionOfInterest(64, 64, 32, 32), new Vector(-.20f,
                        -.25f));
            } else if (orientation.equals(Orientation.LEFT)) {
                sprite = new Sprite("zelda/player.staff_water", 1.5f, 1.5f, this,
                        new RegionOfInterest(64, 96, 32, 32), new Vector(-.20f, -.25f));
            }

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

    /** Switchs the name of the player when is collecting the staff
     */
    public void switchPlayerName(){
        playerName = "player.staff_water";
    }

    /*public void switchSprite(){
        sprite = new Sprite("zelda/player.staff_water", 1.5f, 1.5f, this,
                new RegionOfInterest(64, 0, 32, 32), new Vector(-.17f, -.25f));
    }*/

    /** tells if the player is throwing fireball
     @param b Button the button which has to be pressed
     */
    public boolean throwingFireball(   ch.epfl.cs107.play.window.Button b){
        return (b.isDown() && (hasStaff && !isDisplacementOccurs()));
    }

    /** tells if the player is moving
    @param b Button the button which has to be pressed
     */
    public boolean moving(   ch.epfl.cs107.play.window.Button b){
        return (b.isDown());
    }



    @Override
    public void draw(Canvas canvas) {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        if (!hasStaff) {
            if (isDisplacementOccurs()) {
                switch (getOrientation()) {
                    case DOWN -> animationD.draw(canvas);
                    case RIGHT -> animationR.draw(canvas);
                    case UP -> animationU.draw(canvas);
                    case LEFT -> animationL.draw(canvas);
                }
            } else sprite.draw(canvas);
        } else {
            if ( moving(keyboard.get(Keyboard.UP))
                    || moving(keyboard.get(Keyboard.DOWN))
                    || moving(keyboard.get(Keyboard.RIGHT))
                    || moving(keyboard.get(Keyboard.LEFT))
                    || throwingFireball(keyboard.get(Keyboard.X))){
            switch (getOrientation()) {
                case DOWN -> staffanimationD.draw(canvas);
                case RIGHT -> staffanimationR.draw(canvas);
                case UP -> staffanimationU.draw(canvas);
                case LEFT -> staffanimationL.draw(canvas);
            }
        } else sprite.draw(canvas);
        }


        shadow.draw(canvas);
        mew.draw(canvas);
        message.draw(canvas);
        hud.draw(canvas);
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
        return true;
    }

    private boolean wIsDown(){
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