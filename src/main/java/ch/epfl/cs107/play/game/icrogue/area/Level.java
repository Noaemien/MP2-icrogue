package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

abstract public class Level {

    protected ICRogueRoom[][] map;
    private int nbRooms;
    private int[] roomsDistribution;

    private DiscreteCoordinates spawnCoordinates;

    private DiscreteCoordinates bossRoomCoordinates;


    public Level(boolean randomMap , DiscreteCoordinates startPosition ,
                 int[] roomsDistribution , int width , int height){


        if(!randomMap){ //Init fixed map
            this.map = new ICRogueRoom[width][height];
            this.spawnCoordinates = startPosition;
            bossRoomCoordinates = new DiscreteCoordinates(0, 0);
            generateFixedMap();
        } else { //Init procedurally generated map
            nbRooms = sum(roomsDistribution);
            this.map = new ICRogueRoom[nbRooms][nbRooms];
            this.roomsDistribution = roomsDistribution;
            generateRandomMap();
        }

    }

    /**
     *
     * @param listToSum (int[]): list of integers to be summed
     * @return (int) sum of elements in listToSum
     */
    private int sum(int[] listToSum){
        int out = 0;
        for (int val : listToSum){
            out += val;
        }
        return out;
    }

    /**
     * Generates a map in a procedural manner
     */
    public void generateRandomMap(){
        //Room Placements
        MapState[][] roomPlacements = generateRandomRoomPlacement();
        //printMap(roomPlacements);

        //Room & Connector Generation
        generateRooms(roomPlacements, roomsDistribution);
        generateConnectors(roomPlacements);

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


    /**
     * Finds rooms around current room depending on State criteria
     * @param position (DiscreteCoordinates): Room position
     * @param map (MapState[][]): current map state
     * @param states (MapState...): states to find, filter
     * @return (List(DiscreteCoordinates)): Array of available rooms responding to MapState criteria
     */
    private List<DiscreteCoordinates> findNeighborSlotsOfType(DiscreteCoordinates position, MapState[][] map, MapState... states){
        //Methode only used to filter up to two states
        assert states.length < 3;

        List<DiscreteCoordinates> freeSlots = position.getNeighbours();

        //remove if out of bounds
        freeSlots.removeIf(n -> n.x < 0);
        freeSlots.removeIf(n -> n.x >= nbRooms);
        freeSlots.removeIf(n -> n.y < 0);
        freeSlots.removeIf(n -> n.y >= nbRooms);

        //remove if slot state is not chosen one
        if (states.length == 2) {
            freeSlots.removeIf(n -> map[n.x][n.y] != states[0] && map[n.x][n.y] != states[1]);
        } else if (states.length == 1){
            freeSlots.removeIf(n -> map[n.x][n.y] != states[0]);
        }
        return freeSlots;
    }

    /**
     * Procedurally generates a random map
     * @return (MapState[][]): room locations in map
     */
    protected MapState[][] generateRandomRoomPlacement(){
        MapState[][] mapPlacements = new MapState[nbRooms][nbRooms];
        List<DiscreteCoordinates> placedRooms = new ArrayList<>();

        //Initialises all slots to NULL
        for (int i = 0; i < nbRooms; ++i) {
            for (int j = 0; j < nbRooms; ++j){
                mapPlacements[i][j] = MapState.NULL;
            }
        }

        //Place center room
        int centerCoords = nbRooms / 2;
        mapPlacements[centerCoords][centerCoords] = MapState.PLACED;
        placedRooms.add(new DiscreteCoordinates(centerCoords, centerCoords));
        int roomsToPlace = nbRooms - 1;

        //Place all other rooms
        while (roomsToPlace > 0){

            roomsToPlace -= placeRoom(roomsToPlace, placedRooms, mapPlacements, MapState.PLACED);

        }

        //Place bossRoom
        int bossRoomPlaced = 0;
        while (bossRoomPlaced == 0)
            bossRoomPlaced = placeRoom(1, placedRooms, mapPlacements, MapState.BOSS_ROOM);
        return mapPlacements;
    }

    /** placeRoom
     * Places rooms of defined type in map procedurally
     * @param roomsToPlace (int): Number of rooms to place,
     * @param placedRooms (DiscreteCoordinates): list of already placed room coordinates,
     * @param mapPlacements (MapState[][]): list of already placed rooms,
     * @param roomType (MapState): of room to place,
     * @return (int): number of placed rooms
     */
    private int placeRoom(int roomsToPlace, List<DiscreteCoordinates> placedRooms, MapState[][] mapPlacements, MapState roomType ){
        int outputPlacedRooms = 0;
        /*
        Select Random placed room:
            Replace line 174 with:
                int rndPlacedRoomIdx = RandomHelper.roomGenerator.nextInt(0, placedRooms.size());
            to have easy control on seed.

            else use:
                int rndPlacedRoomIdx = (int) (Math.random() * placedRooms.size());
        */
        int rndPlacedRoomIdx = (int) (Math.random() * placedRooms.size());  //RandomHelper.roomGenerator.nextInt(0, placedRooms.size())
        DiscreteCoordinates placedRoomCoords = placedRooms.get(rndPlacedRoomIdx);

        //Get all free slots around placed room
        List<DiscreteCoordinates> freeSlots = findNeighborSlotsOfType(placedRoomCoords, mapPlacements, MapState.NULL);

        //If no free slots skip adding rooms
        if (!freeSlots.isEmpty()) {

            //Choses a random amount of rooms in available free slots
            if (freeSlots.size() > 1 && roomsToPlace > 1) {
                roomsToPlace = RandomHelper.roomGenerator.nextInt(1, Math.min(freeSlots.size(), roomsToPlace));
            } else
                roomsToPlace = 1;

            while (roomsToPlace > 0) {
                //Chooses random room in surounding free rooms
                int roomIndex = RandomHelper.roomGenerator.nextInt(0, freeSlots.size());
                DiscreteCoordinates roomCoords = freeSlots.get(roomIndex);

                //Sets room as Placed and adds it to placed rooms
                mapPlacements[roomCoords.x][roomCoords.y] = roomType;
                placedRooms.add(roomCoords);

                freeSlots.remove(roomIndex);
                roomsToPlace--;
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

    /**
     * Generates rooms according to map schematic
     * Places room types randomly
     * @param map (MapState[][]): current map state
     * @param roomsDistribution (int[]): distribution of room types
     */
    private void generateRooms(MapState[][] map, int[] roomsDistribution){

        //Get valid room locations
        List<DiscreteCoordinates> rooms = new ArrayList<>();
        for (int i = 0; i < nbRooms; ++i) {
            for (int j = 0; j < nbRooms; ++j){
                if (map[i][j] == MapState.EXPLORED || map[i][j] == MapState.PLACED)
                    rooms.add(new DiscreteCoordinates(i, j));
            }
        }

        //Places each room type one after the other
        for (int i = 0; i < roomsDistribution.length; ++i){
            int roomsToPlace = roomsDistribution[i];
            while (roomsToPlace > 0){
                //Pick random room
                int rndRoomIdx = RandomHelper.roomGenerator.nextInt(0, rooms.size());
                DiscreteCoordinates roomCoords = rooms.get(rndRoomIdx);

                //generate the room
                generateSingleRoom(i, roomCoords);

                //set as created
                map[roomCoords.x][roomCoords.y] = MapState.CREATED;
                rooms.remove(roomCoords);
                --roomsToPlace;
            }
        }

        //Generates the boss room
        for (int i = 0; i < nbRooms; ++i) {
            for (int j = 0; j < nbRooms; ++j){
                if (map[i][j] == MapState.BOSS_ROOM ) {
                    generateSingleRoom(0, new DiscreteCoordinates(i, j));
                    bossRoomCoordinates = new DiscreteCoordinates(i, j);
                }
            }
        }

    }

    /**
     * Generates connectors between rooms
     * @param map (MapState[][]): current map state
     */
    private void generateConnectors(MapState[][] map){

        //Get valid room locations
        List<DiscreteCoordinates> rooms = new ArrayList<>();
        for (int i = 0; i < nbRooms; ++i) {
            for (int j = 0; j < nbRooms; ++j){
                if (map[i][j] == MapState.CREATED)
                    rooms.add(new DiscreteCoordinates(i, j));
            }
        }


        for (DiscreteCoordinates coords: rooms){

            List<DiscreteCoordinates> destCoords = findNeighborSlotsOfType(coords, map, MapState.CREATED, MapState.BOSS_ROOM);
            //Generate the connectors
            generateRoomConnectors(coords, destCoords.toArray(new DiscreteCoordinates[0]));
        }

        //Generate boss room connectors
        generateBossRoomConnectors(map);
    }

    /**
     * Generates the boss room connectors
     * @param map (MapState[][]): current map state
     */
    private void generateBossRoomConnectors(MapState[][] map){
        List<DiscreteCoordinates> destCoords = findNeighborSlotsOfType(bossRoomCoordinates, map, MapState.CREATED);
        generateRoomConnectors(bossRoomCoordinates, destCoords.toArray(new DiscreteCoordinates[0]), true);
    }

    /**
     * Enables adding the areas to the game without using an intrusive getter
     * @param game (AreaGame): game instance
     */
    public void addArea (AreaGame game){
        for (ICRogueRoom[] height : map){
            for (ICRogueRoom area : height) {
                if (area != null)
                    game.addArea(area);
            }
        }
    }

    //----------------
    //ABSTRACT METHODS
    //----------------

    abstract protected void generateSingleRoom(int roomType, DiscreteCoordinates roomCoords);
    abstract protected void generateRoomConnectors(DiscreteCoordinates coords, DiscreteCoordinates[] destCoords, boolean isBossRoom);
    abstract protected void generateRoomConnectors(DiscreteCoordinates coords, DiscreteCoordinates[] destCoords);
    protected abstract void generateFixedMap();



    //-------
    //SETTERS
    //-------

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
        room.behaviorName = "icrogue/level0" + roomCoords.x + "" + roomCoords.y;
    }

    // -------
    // GETTERS
    // -------

    protected DiscreteCoordinates getBossRoomCoordinates() {
        return bossRoomCoordinates;
    }

    public boolean isCompleted(){
        return map[bossRoomCoordinates.x][bossRoomCoordinates.y].isCompleted();
    }



}
