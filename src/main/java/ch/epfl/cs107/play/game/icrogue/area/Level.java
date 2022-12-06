package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level {


    private int height;
    private int width;

    private ICRogueRoom[][] map; //= new ICRogueRoom[height][width];

    private DiscreteCoordinates spawnCoordinates;

    private DiscreteCoordinates bossRoomCoordinates;

    private String startRoomName;

    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room) {
        map[coords.x][coords.y] = room;
    }

    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination,
                                               ConnectorInRoom connector) {

        map[coords.x][coords.y].setConnectors(connector.getIndex(), (Connector) connector);
    }

    protected void setRoomConnector(DiscreteCoordinates coords, String destination,
                                    ConnectorInRoom connector) {
        ((Connector) connector).setState(Connector.State.CLOSED); 
        map[coords.x][coords.y].setConnectors(connector.getIndex(), (Connector) connector);

    }
    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector,
                                     int keyId){

    }
}
