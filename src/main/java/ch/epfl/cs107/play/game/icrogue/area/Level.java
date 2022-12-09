package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

abstract public class Level {


    private int height;
    private int width;

    protected ICRogueRoom[][] map; //= new ICRogueRoom[height][width];

    private DiscreteCoordinates spawnCoordinates;

    private DiscreteCoordinates bossRoomCoordinates;

    private String startRoomName;

    public Level (DiscreteCoordinates spawnCoordinates, int height, int width){
        this.spawnCoordinates = spawnCoordinates;
        this.height = height;
        this.width = width;
        this.map = new ICRogueRoom[height][width];
        generateFixedMap();
    }

    public void addArea (AreaGame game){ //ou icrogue
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



}
