package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public class ICRoguePlayer extends ICRogueActor{
    private float hp;
    private TextGraphics message;
    private Sprite sprite;
    /// Animation duration in frame number
    private final static int MOVE_DURATION = 8;




    public ICRoguePlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) {
        super(owner, orientation, coordinates);
        this.hp = 10;
        message = new TextGraphics(Integer.toString((int)hp), 0.4f, Color.BLUE);
        message.setParent(this);
        message.setAnchor(new Vector(-0.3f, 0.1f));
        sprite = new Sprite(spriteName, 1.f, 1.f,this);

        resetMotion();
    }

    /**
     * Center the camera on the player
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    @Override
    public void update(float deltaTime) {
        if (hp > 0) {
            hp -=deltaTime;
            message.setText(Integer.toString((int)hp));
        }
        if (hp < 0) hp = 0.f;
        Keyboard keyboard= getOwnerArea().getKeyboard();

        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        fireBallIfXDown(getOrientation(), keyboard.get(Keyboard.X));

        super.update(deltaTime);

    }

    public void fireBallIfXDown(Orientation orientation, ch.epfl.cs107.play.window.Button b){
        if(b.isDown()) {

        }
    }

    /**
     * Replace Sprite with Sprite facing orientation
     * @param orientation (Orientation): given orientation, not null
     */
    public void rotateSpriteToOrientation(Orientation orientation){
            if (orientation.equals(Orientation.LEFT)){
                sprite = new Sprite("zelda/player", .75f, 1.5f, this ,
                        new RegionOfInterest(0, 96, 16, 32), new Vector(.15f,
                        -.15f));
            } else if (orientation.equals(Orientation.RIGHT)){
                sprite = new Sprite("zelda/player", .75f, 1.5f, this ,
                        new RegionOfInterest(0, 32, 16, 32), new Vector(.15f,
                        -.15f));
            } else if (orientation.equals(Orientation.UP)){
                sprite = new Sprite("zelda/player", .75f, 1.5f, this ,
                        new RegionOfInterest(0, 64, 16, 32), new Vector(.15f,
                        -.15f));
            } else if (orientation.equals(Orientation.DOWN)) {
                sprite = new Sprite("zelda/player", .75f, 1.5f, this ,
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
        sprite.draw(canvas);
        message.draw(canvas);
    }

    public boolean isWeak() {
        return (hp <= 0.f);
    }

    public void strengthen() {
        hp = 10;
    }

    ///Ghost implements Interactable

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
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
    }
}
