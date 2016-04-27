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
            quoridorBoard.setWall(playerMove.getStart().getRow(),playerMove.getStart().getCol(),
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
        Queue<Coordinate> queue = new LinkedList<>(); // file des voisins
        int[][] distance = new int[quoridorBoard.BOARD_SIZE][quoridorBoard.BOARD_SIZE]; // matrice des distances

        // initialisation de la matrice distance à -1 (case non parcourue)
        for (int i = 0; i < quoridorBoard.BOARD_SIZE; i++) {
            for (int j = 0; j < quoridorBoard.BOARD_SIZE; j++) {
                distance[i][j] = -1;
            }
        }

        distance[coordinate.getRow()][coordinate.getCol()] = 0; // point de départ
        queue.add(coordinate); // ajout du point de départ dans la file

        /*
         * ETAPE 1 : recherche du chemin
         */
        boolean endOfPath = false; // vaut vrai quand une coord de la file vaut coordinate1
        while(!endOfPath){ // on s'arrete quand on est arrivé
            Coordinate c = queue.poll();
            if(c == null){ return new ArrayList<>();} // chemin impossible (la case destination est bloquée par les murs) !
            for(Coordinate neighbor : getNeighbors(c)){ // calcul des distances pour les voisins de la case
                if(neighbor.getCol() == coordinate1.getCol() && neighbor.getRow() == coordinate1.getRow()){ endOfPath = true;}
                if(distance[neighbor.getRow()][neighbor.getCol()] != -1) { continue; } // on ne revient pas sur nos pas
                distance[neighbor.getRow()][neighbor.getCol()]= distance[c.getRow()][c.getCol()]+1; // on a visité la case : incrémentation distance
                queue.add(neighbor); // ajout à la file pour le traiter
            }
        }
        /*
         * ETAPE 2 : retourner la liste du chemin le plus court
         */
        List<Coordinate> lCoordRet = new ArrayList<>();
        lCoordRet.add(coordinate1);
        queue.clear();
        queue.add(coordinate1);
        endOfPath = false;
        while(!endOfPath){
            Coordinate c    = queue.poll();
            Coordinate cMin = null;
            int dMin        = 1000;
            for(Coordinate neighbor : getNeighbors(c)){
                if((neighbor.getCol() == coordinate.getCol()) && (neighbor.getRow() == coordinate.getRow()))
                    endOfPath = true;
                if(distance[neighbor.getRow()][neighbor.getCol()] != -1 && distance[neighbor.getRow()][neighbor.getCol()] < dMin){
                    dMin = distance[neighbor.getRow()][neighbor.getCol()];
                    cMin = neighbor;
                }
            }
            queue.add(cMin);
            lCoordRet.add(cMin);
        }
        Collections.reverse(lCoordRet); // pour retourner le chemin dans le bon sens
        return lCoordRet;
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
