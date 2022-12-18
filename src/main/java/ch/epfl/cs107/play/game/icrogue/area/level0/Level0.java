package ch.epfl.cs107.play.game.icrogue.area.level0;

import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0KeyRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0StaffRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0TurretRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Level0 extends Level {

    final int PART_1_KEY_ID = 1;
    final int BOSS_KEY_ID = 996;

    public enum RoomType {
        TURRET_ROOM(3), // type and number of roon
        STAFF_ROOM(1),
        BOSS_KEY(1),
        SPAWN(1),
        NORMAL(1);

        int quantity;

        RoomType(int quantity){
            this.quantity = quantity;
        }

        public int[] getValues(){
            int [] out = new int[5];
            for (int i = 0; i < values().length; ++i)
                out[i] = values()[i].quantity;

            return out;
        }

    }

    private DiscreteCoordinates startRoom;
    private DiscreteCoordinates spawnCoordinates;

    public Level0(boolean randomMap){
        super(randomMap, new DiscreteCoordinates(2, 0), RoomType.BOSS_KEY.getValues(), 4, 2);

        this.spawnCoordinates = new DiscreteCoordinates(2, 0);

        if (!randomMap) startRoom = new DiscreteCoordinates(1, 0);
    }

    public Level0() {
        this(true);
    }

    public String getStartTitle(){
        return "icrogue/level0" + startRoom.x + "" + startRoom.y;
    }

    public String getRoomTitle(DiscreteCoordinates coordinates){
        return "icrogue/level0" + coordinates.x + "" + coordinates.y;
    }

    public ICRogueRoom getCurrentArea(){
        return map[1][0];
    }

    protected void generateSingleRoom(int roomType, DiscreteCoordinates roomCoords){
        switch (roomType) {
            case 0 -> setRoom(roomCoords, new Level0TurretRoom(roomCoords));
            case 1 -> setRoom(roomCoords, new Level0StaffRoom(roomCoords));
            case 2 -> setRoom(roomCoords, new Level0KeyRoom(roomCoords, BOSS_KEY_ID));
            case 3 -> {
                setRoom(roomCoords, new Level0Room(roomCoords));
                startRoom = roomCoords;
            }
            case 4 -> setRoom(roomCoords, new Level0Room(roomCoords));
        }
    }



    @Override
    protected void generateRoomConnectors(DiscreteCoordinates coords, DiscreteCoordinates[] destCoords, boolean isBossRoom) {
        Level0Room.Level0Connectors[] orientation = new Level0Room.Level0Connectors[] {Level0Room.Level0Connectors.W,
                Level0Room.Level0Connectors.N, Level0Room.Level0Connectors.E, Level0Room.Level0Connectors.S};
        DiscreteCoordinates[] neighbors = coords.getNeighbours().toArray(new DiscreteCoordinates[0]);
        ArrayList<DiscreteCoordinates> destCoords_a = new ArrayList<>(Arrays.stream(destCoords).toList());

        for (int i = 0; i < neighbors.length; ++i){
            if (!destCoords_a.contains(neighbors[i]))
                neighbors[i] = null;
        }

        if (!isBossRoom){
            for (int i = 0; i < neighbors.length; ++i) { //ORDER LEFT UP RIGHT DOWN but check for missing
                if (neighbors[i] != null) {
                    setRoomConnector(coords, "icrogue/level0" + neighbors[i].x + "" + neighbors[i].y, orientation[i]);
                    if (neighbors[i].equals(getBossRoomCoordinates())) {
                        System.out.println("CREATING BOSS ROOM CONNECTOR");
                        lockRoomConnector(coords, orientation[i], BOSS_KEY_ID);
                    }
                }
            }
        } else {
            for (int i = 0; i < neighbors.length; ++i) //ORDER LEFT UP RIGHT DOWN but check for missing
                if (neighbors[i] != null ) {
                    setRoomConnector(coords, "icrogue/level0" + neighbors[i].x + "" + neighbors[i].y, orientation[i]);
                    lockRoomConnector(coords, orientation[i],  BOSS_KEY_ID);
                }
        }
    }

    @Override
    protected void generateRoomConnectors(DiscreteCoordinates coords, DiscreteCoordinates[] destCoords) {
        generateRoomConnectors(coords, destCoords, false);
    }

    protected void generateFixedMap(){
        //generateMap1();
        //generateMap2();
        generateFinalMap();
    }

    private void generateMap1(){
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0KeyRoom(room00, PART_1_KEY_ID));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);
        lockRoomConnector(room00, Level0Room.Level0Connectors.E,  PART_1_KEY_ID);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1, 0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level000", Level0Room.Level0Connectors.W);
    }

    private void generateMap2(){
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0Room(room00));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1,0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level011", Level0Room.Level0Connectors.S);
        setRoomConnector(room10, "icrogue/level020", Level0Room.Level0Connectors.E);

        lockRoomConnector(room10, Level0Room.Level0Connectors.W,  BOSS_KEY_ID);
        setRoomConnectorDestination(room10, "icrogue/level000", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room20 = new DiscreteCoordinates(2,0);
        setRoom(room20,  new Level0StaffRoom(room20));
        setRoomConnector(room20, "icrogue/level010", Level0Room.Level0Connectors.W);
        setRoomConnector(room20, "icrogue/level030", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3,0);
        setRoom(room30, new Level0KeyRoom(room30, BOSS_KEY_ID));
        setRoomConnector(room30, "icrogue/level020", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        setRoom (room11, new Level0Room(room11));
        setRoomConnector(room11, "icrogue/level010", Level0Room.Level0Connectors.N);
    }

    private void generateFinalMap(){
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0TurretRoom(room00));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1,0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level011", Level0Room.Level0Connectors.S);
        setRoomConnector(room10, "icrogue/level020", Level0Room.Level0Connectors.E);

        lockRoomConnector(room10, Level0Room.Level0Connectors.W,  BOSS_KEY_ID);
        setRoomConnectorDestination(room10, "icrogue/level000", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room20 = new DiscreteCoordinates(2,0);
        setRoom(room20,  new Level0StaffRoom(room20));
        setRoomConnector(room20, "icrogue/level010", Level0Room.Level0Connectors.W);
        setRoomConnector(room20, "icrogue/level030", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3,0);
        setRoom(room30, new Level0KeyRoom(room30, BOSS_KEY_ID));
        setRoomConnector(room30, "icrogue/level020", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        setRoom (room11, new Level0Room(room11));
        setRoomConnector(room11, "icrogue/level010", Level0Room.Level0Connectors.N);
    }
}
