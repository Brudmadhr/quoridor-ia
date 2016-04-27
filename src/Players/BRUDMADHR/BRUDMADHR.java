package Players.BRUDMADHR;
import Engine.Logger;
import Interface.Coordinate;
import Interface.PlayerModule;
import Interface.PlayerMove;

import java.util.*;

/**
 * Created by brudmadhr on 05/04/16.
 */
public class BRUDMADHR implements PlayerModule {

    private Logger                  logger;
    private int                     myId;
    private int                     myNbWalls;
    private Map<Integer,Coordinate> playersCoord;
    private Map<Integer,Integer>    playersNbWalls;
    private Board                   quoridorBoard;

    @Override
    public void init(Logger logger, int i, int i1, Map<Integer, Coordinate> map) {
        this.logger     = logger;
        this.myId       = i;
        this.myNbWalls  = i1;

        playersCoord = new HashMap<>();
        playersCoord.putAll(map);

        playersNbWalls = new HashMap<>();
        for(Integer in : playersCoord.keySet()) {
            playersNbWalls.put(in, 10); // 10 murs par défaut au début du jeu
        }

        quoridorBoard = new Board();
    }


    @Override
    public void lastMove(PlayerMove playerMove) {
        System.out.println("in lastMove ..." + playerMove);
        int playerId = playerMove.getPlayerId(); // récupère l'id du joueur qui vient de jouer
        if(playerMove.isMove()) // renvoi true si le joueur a bougé
            playersCoord.put(playerId,playerMove.getEnd()); // maj des coord
        else {
            // diminution du nombre de murs du joueur
            playersNbWalls.put(playerId, getWallsRemaining(playerId) - 1);
            // ajout du mur dans le board
            quoridorBoard.setBoard(playerMove.getStart().getRow(),playerMove.getStart().getCol(),
                                   playerMove.getEnd().getRow(),playerMove.getEnd().getCol());
        }
        System.out.print(playerMove);
    }

    @Override
    public int getID() {
        return myId;
    }

    @Override
    public Set<Coordinate> getNeighbors(Coordinate coordinate) {
        Set<Coordinate> setCoordRet = new HashSet<>();
        int row = coordinate.getRow();
        int col = coordinate.getCol();
        // TODO : test murs
        if(quoridorBoard.deplacementN(row,col)){ // N
            setCoordRet.add(new Coordinate(row-1,col));
        }
        if(quoridorBoard.deplacementS(row,col)){ // S
            setCoordRet.add(new Coordinate(row+1,col));
        }
        if(quoridorBoard.deplacementO(row,col)){ // O
            setCoordRet.add(new Coordinate(row,col-1));
        }
        if(quoridorBoard.deplacementE(row,col)) { // E
            setCoordRet.add(new Coordinate(row, col + 1));
        }
        return setCoordRet;
    }

    @Override
    public List<Coordinate> getShortestPath(Coordinate coordinate, Coordinate coordinate1) {
        return null;
    }

    @Override
    public int getWallsRemaining(int i) {
        return playersNbWalls.get(i);
    }

    @Override
    public Coordinate getPlayerLocation(int i) {
        return playersCoord.get(i);
    }

    @Override
    public Map<Integer, Coordinate> getPlayerLocations() {
        return playersCoord;
    }

    @Override
    public Set<PlayerMove> allPossibleMoves() {
        return null;
    }

    @Override
    public void playerInvalidated(int i) {
        //playersCoord.remove(i);
    }

    @Override
    public PlayerMove move() {
        return null;
    }
}
