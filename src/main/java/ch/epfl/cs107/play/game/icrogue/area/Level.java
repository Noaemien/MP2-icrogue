package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

import java.util.ArrayList;
import java.util.List;

abstract public class Level {


    private int height;
    private int width;

    protected ICRogueRoom[][] map; //= new ICRogueRoom[height][width];
    private int nbRooms;
    private int[] roomsDistribution;

    private DiscreteCoordinates spawnCoordinates;

    private DiscreteCoordinates bossRoomCoordinates;

    private String startRoomName;

/*    public Level (DiscreteCoordinates spawnCoordinates, int height, int width){
        this.spawnCoordinates = spawnCoordinates;
        this.height = height;
        this.width = width;
        this.map = new ICRogueRoom[height][width];
        bossRoomCoordinates = new DiscreteCoordinates(0, 0);
        generateFixedMap();
    }
*/

    private int sum(int[] listToSum){
        int out = 0;
        for (int val : listToSum){
            out += val;
        }
        return out;
    }

    public Level(boolean randomMap , DiscreteCoordinates startPosition ,
          int[] roomsDistribution , int width , int height){

        this.height = height;
        this.width = width;


        if(!randomMap){
            this.map = new ICRogueRoom[height][width];
            generateFixedMap();
        } else {
            nbRooms = sum(roomsDistribution);
            this.map = new ICRogueRoom[nbRooms][nbRooms];
            this.roomsDistribution = roomsDistribution;
            generateRandomMap();
        }

    }

    public void generateRandomMap(){
        //PLACEMENT DES SALLES
        System.out.println("I AM HERE");
            MapState[][] roomPlacements = generateRandomRoomPlacement();
            printMap(roomPlacements);
        //GENERATION DES SALLES
    }

    protected enum MapState {
        NULL , // Empty space
        PLACED , // The room has been placed but not yet explored by the room placement algorithm
        EXPLORED , // The room has been placed and explored by the algorithm
        BOSS_ROOM , // The room is a boss room
        CREATED; // The room has been instantiated in the room map
        @Override
        public String toString() {
            return Integer.toString(ordinal());
        }
    }

    private List<DiscreteCoordinates> freeSlots(int posX, int  posY, MapState[][] map){
        List<DiscreteCoordinates> freeSlots = new DiscreteCoordinates(posX, posY).getNeighbours();
        for (int i = -1; i < 2; ++i){
            for (int j = -1; j < 2; ++j) {
                if (i + j != 0){
                    DiscreteCoordinates toRemove = new DiscreteCoordinates(posX + i, posY + j);
                    if (posX + i == 7 || posY + j == 7){
                        System.out.println("7");
                    }
                    if (posX + i < 0 || (posX + i >= nbRooms) || posY + j < 0 || (posY + j >= nbRooms)){
                        freeSlots.remove(toRemove);
                    } else if (map[posX + i][posY + j] != MapState.NULL){
                        freeSlots.remove(toRemove);
                    }
                }
            }
        }
        System.out.println(freeSlots.size()); //TODO REMOVE
        return freeSlots;
    }

    protected MapState[][] generateRandomRoomPlacement(){
        MapState[][] mapPlacements = new MapState[nbRooms][nbRooms];
        List<DiscreteCoordinates> placedRooms = new ArrayList<DiscreteCoordinates>();

        //Initialise toutes les cases a NULL
        for (int i = 0; i < nbRooms; ++i) {
            for (int j = 0; j < nbRooms; ++j){
                mapPlacements[i][j] = MapState.NULL;
            }
        }

        int centerCoords = nbRooms / 2;
        mapPlacements[centerCoords][centerCoords] = MapState.PLACED;

        placedRooms.add(new DiscreteCoordinates(centerCoords, centerCoords));
        int roomsToPlace = nbRooms - 1;

        while (roomsToPlace > 0){
            printMap(mapPlacements);

            roomsToPlace -= placeRoom(roomsToPlace, placedRooms, mapPlacements, MapState.PLACED);

        }

        placeRoom(1, placedRooms, mapPlacements, MapState.BOSS_ROOM); //TODO NOT PLACED
        System.out.println("FINAL");
        printMap(mapPlacements);
        return mapPlacements;
    }

    private int placeRoom(int roomsToPlace, List<DiscreteCoordinates> placedRooms, MapState[][] mapPlacements, MapState roomType ){
        int outputPlacedRooms = 0;
        //Select Random placed room
        int rndPlacedRoomIdx = RandomHelper.roomGenerator.nextInt(0, placedRooms.size());
        DiscreteCoordinates placedRoomCoords = placedRooms.get(rndPlacedRoomIdx);

        //Get all free slots around placed room
        List<DiscreteCoordinates> freeSlots = freeSlots(placedRoomCoords.x, placedRoomCoords.y, mapPlacements);

        int neighboringRoomNumber = 0;

        if (!freeSlots.isEmpty()) { //If no free slots skip adding rooms

            if (freeSlots.size() > 1 && roomsToPlace > 1) {
                neighboringRoomNumber = RandomHelper.roomGenerator.nextInt(1, Math.min(freeSlots.size(), roomsToPlace));
            } else
                neighboringRoomNumber = 1;

            while (neighboringRoomNumber > 0 && roomsToPlace > 0) {
                //Chooses random room in surounding free rooms
                int roomIndex = RandomHelper.roomGenerator.nextInt(0, freeSlots.size());
                DiscreteCoordinates roomCoords = freeSlots.get(roomIndex);

                //Sets room as Placed and adds it to placed rooms
                mapPlacements[roomCoords.x][roomCoords.y] = MapState.PLACED;
                placedRooms.add(roomCoords);

                freeSlots.remove(roomIndex);
                roomsToPlace--;
                neighboringRoomNumber--;
                outputPlacedRooms++;
            }

        }

        //Sets room as explored and removes it from placedRooms
        mapPlacements[placedRoomCoords.x][placedRoomCoords.y] = MapState.EXPLORED;
        placedRooms.remove(placedRoomCoords);

        return outputPlacedRooms;
    }

    private void printMap(MapState [][] map) {
        System.out.println("Generated map:");
        System.out.print(" | ");
        for (int j = 0; j < map[0].length; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
        System.out.print("--|-");
        for (int j = 0; j < map[0].length; j++) {
            System.out.print("--");
        }
        System.out.println();
        for (int i = 0; i < map.length; i++) {
            System.out.print(i + " | ");
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[j][i] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void addArea (AreaGame game){
        for (ICRogueRoom[] height : map){
            for (ICRogueRoom area : height) {
                if (area != null)
                    game.addArea(area);
            }
        }
    }

    protected abstract void generateFixedMap();

    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room) {
        map[coords.x][coords.y] = room;
    }

    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination,
                                               ConnectorInRoom connector) {
        map[coords.x][coords.y].connectors.get(connector.getIndex()).setDestinationRoom(destination);
        map[coords.x][coords.y].connectors.get(connector.getIndex()).setDestinationCoords(connector.getDestination());
    }

    protected void setRoomConnector(DiscreteCoordinates coords, String destination,
                                    ConnectorInRoom connector) {
        setRoomConnectorDestination(coords, destination, connector);
        map[coords.x][coords.y].connectors.get(connector.getIndex()).setState(Connector.State.CLOSED);
    }
    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector,
                                     int keyId){
        map[coords.x][coords.y].connectors.get(connector.getIndex()).setState(Connector.State.LOCKED);
        map[coords.x][coords.y].connectors.get(connector.getIndex()).setKeyId(keyId);
    }

    protected void setTitle(ICRogueRoom room, DiscreteCoordinates roomCoords){
        room.behaviorName = "icrogue/level0" + roomCoords.x + "" + roomCoords.y; //TODO PAS SUR
    }

    public boolean isCompleted(){
        return map[bossRoomCoordinates.x][bossRoomCoordinates.y].isCompleted();
    }

}
