package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class Stick extends Item{

    
    Sprite[] spriTabo = {new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(0,0 , 32, 32), new
            Vector(0, 0)), new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(32, 0, 32,32 ), new
            Vector(0, 0)), new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(64, 0,32, 32), new
            Vector(0, 0)), new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(96, 0, 32, 32), new
            Vector(0, 0)), new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(128, 0, 32,32), new
            Vector(0, 0)), new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(160, 0, 32, 32), new
            Vector(0, 0)),new Sprite("zelda/staff", 1f, 1f, this ,
            new RegionOfInterest(192, 0, 32, 32), new Vector(0, 0))};

    Animation animation = new Animation(2, spriTabo);

    public boolean showDialogue;
    Sprite dialogueBubble;
    TextGraphics message;
    TextGraphics message2;
    

    public Stick(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        //Initialises dialogue bubble
        message = new TextGraphics("This magic stick will shoot fireballs and might", 0.4f, Color.black);
        message.setParent(this);
        message.setAnchor(new Vector(-2.9f, -3.5f));
        message2 = new TextGraphics("drop a heart when killing an enemy", 0.4f, Color.black);
        message2.setParent(this);
        message2.setAnchor(new Vector(-2.1f, -3.9f));
        dialogueBubble = new Sprite("zelda/dialog", 8f, 0.9f, this);
        dialogueBubble.setAnchor(new Vector(-4f, -4.1f));
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        showDialogue = false;
        animation.update(deltaTime);
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void draw(Canvas canvas){

        if(!isCollected()) {
            animation.draw(canvas);
            if (showDialogue) {
                dialogueBubble.draw(canvas);
                message.draw(canvas);
                message2.draw(canvas);
            }
        }

    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    @Override
    public boolean takeCellSpace() {
        return (!isCollected());
    }
}



