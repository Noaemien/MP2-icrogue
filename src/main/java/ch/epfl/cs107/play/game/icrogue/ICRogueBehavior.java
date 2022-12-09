package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Projectile;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.window.Window;

public class ICRogueBehavior extends AreaBehavior {


    public enum CellType{
        //https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        NONE(0,false), // Should never have been used except in the toType method
        GROUND(-16777216, true), // traversable
        WALL(-14112955, false), // non traversable
        HOLE(-65536, true);

        final int type;
        final boolean isWalkable;

        CellType(int type, boolean isWalkable){
            this.type = type;
            this.isWalkable = isWalkable;
        }

        public static ICRogueBehavior.CellType toType(int type){
            for(ICRogueBehavior.CellType ict : ICRogueBehavior.CellType.values()){
                if(ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            System.out.println(type);
            return null;
        }
    }

    /**
     * Default Tuto2Behavior Constructor
     * @param window (Window), not null
     * @param name (String): Name of the Behavior, not null
     */
    public ICRogueBehavior(Window window, String name){
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                ICRogueBehavior.CellType color = ICRogueBehavior.CellType.toType(getRGB(height-1-y, x));
                setCell(x,y, new ICRogueBehavior.ICRogueCell(x,y,color));
            }
        }
    }

    /**
     * Cell adapted to the Tuto2 game
     */
    public class ICRogueCell extends ICRogueBehavior.Cell {
        /// Type of the cell following the enum
        private final ICRogueBehavior.CellType type;

        /**
         * Default Tuto2Cell Constructor
         * @param x (int): x coordinate of the cell
         * @param y (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */
        public  ICRogueCell(int x, int y, ICRogueBehavior.CellType type){
            super(x, y);
            this.type = type;
        }

        public CellType getType(){
            return type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        protected boolean isAllEntitiesTraversable(){
            for (Interactable entity: this.entities){
                if (entity.takeCellSpace()) return false;
            }
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            if (entity.takeCellSpace() && !isAllEntitiesTraversable()) { return false; }
            else return type.isWalkable;
        }

        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
        }


    }
}
