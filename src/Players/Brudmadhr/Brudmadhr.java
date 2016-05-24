package Players.Brudmadhr;

import Engine.Logger;
import Interface.Coordinate;
import Interface.PlayerModule;
import Interface.PlayerMove;

import java.util.*;

// TODO : verifier règle saut fonctionnelle (diagonale)
// TODO : problème pose mur
// TODO : gestion quand on est joueur 2 ?

/**
 * Created by brudmadhr on 05/04/16.
 */
public class Brudmadhr implements PlayerModule {

    private Logger logger;
    private int myId;
    private int opponentId;
    private int myNbWalls;
    private Map<Integer, Coordinate> playersCoord;
    private Map<Integer, Integer> playersNbWalls;
    private Board quoridorBoard;

    public void Brudmadhr(){

    }
    @Override
    public void init(Logger logger, int i, int i1, Map<Integer, Coordinate> map) {
        this.logger = logger;
        this.myId = i;
        this.opponentId = (myId == 1) ? 2 : 1;
        this.myNbWalls = i1;

        playersCoord = new HashMap<>();
        playersCoord.putAll(map);

        playersNbWalls = new HashMap<>();
        for (Integer in : playersCoord.keySet()) {
            if (playersCoord.keySet().size() == 2)
                playersNbWalls.put(in, 10); // 10 murs par défaut au début du jeu si 2 joueurs
            else playersNbWalls.put(in, 5); // 5 sinon

        }

        quoridorBoard = new Board(this);
    }


    @Override
    public void lastMove(PlayerMove playerMove) {
        System.out.println("in lastMove ..." + playerMove + "\n");
        int playerId = playerMove.getPlayerId(); // récupère l'id du joueur qui vient de jouer
        if (playerMove.isMove()) // renvoi true si le joueur a bougé
            playersCoord.put(playerId, playerMove.getEnd()); // maj des coord
        else {
            // diminution du nombre de murs du joueur
            playersNbWalls.put(playerId, getWallsRemaining(playerId) - 1);
            // ajout du mur dans le board
            quoridorBoard.setWall(playerMove.getStart().getRow(), playerMove.getStart().getCol(),
                    playerMove.getEnd().getRow(), playerMove.getEnd().getCol());
        }
    }


    @Override
    public Set<Coordinate> getNeighbors(Coordinate coordinate) {
        Set<Coordinate> setCoordRet = new HashSet<>();
        int row = coordinate.getRow();
        int col = coordinate.getCol();
        if (quoridorBoard.deplacementN(row, col)) { // N
            setCoordRet.add(new Coordinate(row - 1, col));
        }
        if (quoridorBoard.deplacementS(row, col)) { // S
            setCoordRet.add(new Coordinate(row + 1, col));
        }
        if (quoridorBoard.deplacementO(row, col)) { // O
            setCoordRet.add(new Coordinate(row, col - 1));
        }
        if (quoridorBoard.deplacementE(row, col)) { // E
            setCoordRet.add(new Coordinate(row, col + 1));
        }
        return setCoordRet;
    }

    /**
     * coordinate : position actuelle
     * coordinate1 : position a atteindre
     */
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
        while (!endOfPath) { // on s'arrete quand on est arrivé
            Coordinate c = queue.poll();
            if (c == null) {
                return new ArrayList<>();
            } // chemin impossible (la case destination est bloquée par les murs) !
            for (Coordinate neighbor : getNeighbors(c)) { // calcul des distances pour les voisins de la case
                if (neighbor.getCol() == coordinate1.getCol() && neighbor.getRow() == coordinate1.getRow()) {
                    endOfPath = true;
                }
                if (distance[neighbor.getRow()][neighbor.getCol()] != -1) {
                    continue;
                } // on ne revient pas sur nos pas
                distance[neighbor.getRow()][neighbor.getCol()] = distance[c.getRow()][c.getCol()] + 1; // on a visité la case : incrémentation distance
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
        while (!endOfPath) {
            Coordinate c = queue.poll();
            Coordinate cMin = null;
            int dMin = 100000;
            for (Coordinate neighbor : getNeighbors(c)) {
                if ((neighbor.getCol() == coordinate.getCol()) && (neighbor.getRow() == coordinate.getRow()))
                    endOfPath = true;
                if (distance[neighbor.getRow()][neighbor.getCol()] != -1 && distance[neighbor.getRow()][neighbor.getCol()] < dMin) {
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
    public void playerInvalidated(int i) {
        playersCoord.remove(i);
    }

    @Override
    public int getID() {
        return myId;
    }


    @Override
    public Set<PlayerMove> allPossibleMoves() {
        Set<PlayerMove> sRet = new HashSet<>();
        sRet.addAll(getAllPieceMoves());
       // sRet.addAll(getAllWallsMoves());
        return sRet;
    }

    private Set<PlayerMove> getAllPieceMoves() {
        Set<PlayerMove> sRet = new HashSet<>();
        int myLig = getPlayerLocation(myId).getRow();
        int myCol = getPlayerLocation(myId).getCol();
        for (Coordinate neighbor : getNeighbors(getPlayerLocation(myId))) { //cases voisines du joueur
            Coordinate finalPosition = neighbor; // destination possible
            // test si adversaire sur case voisine
            Coordinate opponentPosition = getPlayerLocation(2);
            int i = opponentPosition.getRow();
            int j = opponentPosition.getCol();
            if ((i == neighbor.getRow() && j == neighbor.getCol())) {
                /* on a un adversaire à côté de nous on peut éventuellement le sauter :
                 *  - s'il n'est pas sur un bord
                 *  - s'il n'y a pas de mur derrière lui
                 *  les méthodes de la classes board vérifient cela !
                 */
                if (myLig == i) {
                    //EST
                    if (quoridorBoard.deplacementE(i, j)) {
                        finalPosition = new Coordinate(i, j + 1);
                        sRet.add(new PlayerMove(myId, true, getPlayerLocation(myId), finalPosition));
                    }
                        //DIAGONALE E
                    else if (quoridorBoard.deplacementE(myLig - 1, myCol)) {
                        finalPosition = new Coordinate(i - 1, j + 1);
                        sRet.add(new PlayerMove(myId, true, getPlayerLocation(myId), finalPosition));
                    } else if (quoridorBoard.deplacementE(myLig, myCol + 1)) {
                        finalPosition = new Coordinate(i + 1, j + 1);
                        sRet.add(new PlayerMove(myId, true, getPlayerLocation(myId), finalPosition));
                    }
                    //OUEST
                    if (quoridorBoard.deplacementO(i, j)) {
                        finalPosition = new Coordinate(i, j - 1);
                        sRet.add(new PlayerMove(myId, true, getPlayerLocation(myId), finalPosition));
                    }
                        //DIAGONALE O
                    else if (quoridorBoard.deplacementO(myLig - 1, myCol)) {
                        finalPosition = new Coordinate(i - 1, j - 1);
                        sRet.add(new PlayerMove(myId, true, getPlayerLocation(myId), finalPosition));
                    } else if (quoridorBoard.deplacementO(myLig + 1, myCol)) {
                        finalPosition = new Coordinate(i + 1, j - 1);
                        sRet.add(new PlayerMove(myId, true, getPlayerLocation(myId), finalPosition));
                    }

                } else if (myCol == j) {
                    //NORD
                    if (quoridorBoard.deplacementN(i, j)) {
                        finalPosition = new Coordinate(i - 1, j);
                        sRet.add(new PlayerMove(myId, true, getPlayerLocation(myId), finalPosition));
                    }
                    //DIAGONALE N
                    else if (quoridorBoard.deplacementN(myLig, myCol - 1)) {
                        finalPosition = new Coordinate(i, j - 1);
                        sRet.add(new PlayerMove(myId, true, getPlayerLocation(myId), finalPosition));
                    } else if (quoridorBoard.deplacementN(myLig, myCol + 1)) {
                        finalPosition = new Coordinate(i, j + 1);
                        sRet.add(new PlayerMove(myId, true, getPlayerLocation(myId), finalPosition));
                    }
                    //SUD
                    if (quoridorBoard.deplacementS(i, j)) {
                        finalPosition = new Coordinate(i + 1, j);
                        sRet.add(new PlayerMove(myId, true, getPlayerLocation(myId), finalPosition));
                    }
                        //DIAGONALE S
                    else if (quoridorBoard.deplacementS(myLig, myCol - 1)) {
                        finalPosition = new Coordinate(i + 1, j - 1);
                        sRet.add(new PlayerMove(myId, true, getPlayerLocation(myId), finalPosition));
                    } else if (quoridorBoard.deplacementS(myLig, myCol + 1)) {
                        finalPosition = new Coordinate(i + 1, j + 1);
                        sRet.add(new PlayerMove(myId, true, getPlayerLocation(myId), finalPosition));
                    }
                }
            }
            else sRet.add(new PlayerMove(myId, true, getPlayerLocation(myId), finalPosition));
        }
        return sRet;
    }

    //Obtention de tout les murs possibles sans s'occuper s'il bloque le passage ou non
    public Set<PlayerMove> getAllWallsMovesInit(){
        Set<PlayerMove> sRet = new HashSet<>();
        for (int i = 1; i < quoridorBoard.BOARD_SIZE; i++) {
            for (int j = 1; j < quoridorBoard.BOARD_SIZE; j++) {
                // verification mur valide horizontal
                if (quoridorBoard.poseMurHorizontal(i, j)) {
                    sRet.add(new PlayerMove(myId, false, new Coordinate(i, j - 1), new Coordinate(i, j + 1)));
                }
                //vertical
                if (quoridorBoard.poseMurVertical(i, j)) {
                    sRet.add(new PlayerMove(myId, false, new Coordinate(i - 1, j), new Coordinate(i + 1, j)));
                }

            }
        }
        return sRet;

    }

    public Set<PlayerMove> getAllWallsMoves() {
        /* une pose de mur est valide ssi :
         *  1) le joueur a encore au moins un mur
         *  2) si les murs ne se croisent pas
         *  3) la pose de ce mur n'empeche pas tous les joueurs à avoir au moins un chemin possible pour gagner
         */

        Set<PlayerMove> sInterdit = new HashSet<>();
        Set<PlayerMove> sRet = new HashSet<PlayerMove>();

        /* Condition 1 : il reste un mur au moins au joueur à placer */

        if (getWallsRemaining(myId) == 0) {
            return sRet;
        }

        /* Condition 2 : on construit la liste des murs possibles  */
        sRet = getAllWallsMovesInit();

        /* Condition 3 : pour chaque joueur on vérifie que la méthode getShortestPath retourne quelque chose (chemin possible) */
         for (PlayerMove playermove : sRet) {
           // boolean wallIsOk = false;
            quoridorBoard.setWall(playermove.getStart().getRow(), playermove.getStart().getCol(), playermove.getEnd().getRow(), playermove.getEnd().getCol());

            //for (int i =0; i<quoridorBoard.BOARD_SIZE; i++) {

              /* if (getShortestPath(getPlayerLocation(myId),new Coordinate(0,i)).size()  !=0  ){
                    wallIsOk = true;
                }*/
               /* if (getShortestPath((getPlayerLocation(2)), new Coordinate(8, i)).size() != 0) {
                    wallIsOk = true;
                }*/
            //}
            //if(!wallIsOk){
              //  sInterdit.add(playermove);
            //}

            quoridorBoard.removeWall(playermove.getStart().getRow(), playermove.getStart().getCol(), playermove.getEnd().getRow(), playermove.getEnd().getCol());

        }

        for(PlayerMove forbidden : sInterdit){
            sRet.remove(forbidden);
        }

        return sRet;
    }



    
    /**
     * Fonction de calcul de l'heuristique dans un cadre 1v1
     * 
     * On suppose que le joueur 1 (id=1) doit aller en haut du plateau (row==0)
     * On suppose que le joueur 2 (id=2) doit aller en haut du plateau (row==8)
     */
    public int evaluate(){
    	int score_you = 1000; // pour minimiser et trouver le plus court chemin gagnant
    	int score_adv = 1000; // idem
    	
    	int path_you;
    	int path_adv;
    	
    	Coordinate goal_you;
    	Coordinate goal_adv;
    	
    	
    	// astuce a eventuellement implementer : 
    	// ajouter une case de row -1 (resp 9) dont toutes les cases de row 0 (resp 8) sont voisines
    	// cela permettrait de ne pas faire la boucle sur les 9 cases
    	if(getID()==1){
    		for(int i =0; i<9; i++){ 
    			goal_you = new Coordinate(0,i);
    			path_you = getShortestPath(getPlayerLocation(1),goal_you).size();
    			score_you = ( path_you < score_you) ? path_you : score_you;
    			
    			goal_adv = new Coordinate(8,i);
    			path_adv = getShortestPath(getPlayerLocation(2),goal_adv).size();
    			score_adv = ( path_adv < score_adv) ? path_adv : score_adv;
    		}
    	}else{ // player est le joueur 2
    		for(int i =0; i<9; i++){
    			goal_you = new Coordinate(8,i);
    			path_you = getShortestPath(getPlayerLocation(2),goal_you).size();
    			score_you = ( path_you < score_you) ? path_you : score_you;
    			
    			goal_adv = new Coordinate(0,i);
    			path_adv = getShortestPath(getPlayerLocation(1),goal_adv).size();
    			score_adv = ( path_adv < score_adv) ? path_adv : score_adv;
    		}
    	}
    	return score_adv-score_you;
    }
    
    public void make(PlayerMove move){
    	if(move.isMove()){
    		playersCoord.put(move.getPlayerId(),new Coordinate(move.getEndRow(), move.getEndCol()));
    	}else{
    		quoridorBoard.setWall(move.getStartRow(), move.getStartCol(), move.getEndRow(), move.getEndCol());
    	}
    }
    
    public void unmake(PlayerMove move){
    	if(move.isMove()){
    		playersCoord.put(move.getPlayerId(),new Coordinate(move.getStartRow(), move.getStartCol()));
    	}else{
    		quoridorBoard.removeWall(move.getStartRow(), move.getStartCol(), move.getEndRow(), move.getEndCol());
    	}
    }

    PlayerMove move_minimax() {
        List<Object> result = minimax(10, myId, Integer.MIN_VALUE, Integer.MAX_VALUE);
           // depth, max-turn, alpha, beta
        return (PlayerMove)result.get(1);  // returns best move
     }
   
     /** Minimax (recursive) at level of depth for maximizing or minimizing player
         with alpha-beta cut-off. Return int[3] of {score, row, col}  */



    private List<Object> minimax(int depth, int playerId, int alpha, int beta) {
        // Generate possible next moves in a list of int[2] of {row, col}.
        Set<PlayerMove> nextMoves = allPossibleMoves();
   
        // myId is maximizing; while opponentId is minimizing

        int score = -1;
        PlayerMove move_player = null;
  	    List<Object> answer = new ArrayList<Object>();


        if (nextMoves.isEmpty() || depth == 0) {
           // Gameover or depth reached, evaluate score
           score = evaluate();
  		   answer.add(1, new PlayerMove(playerId, false, new Coordinate(-1,-1), new Coordinate(-1,-1))); // fin du jeu
  		   answer.add(0, score);
           return answer;
        } else {
           for (PlayerMove move : nextMoves) {
              // try this move for the current "player"
        	   
              make(move); // fonction a implementer
              
              if (playerId == myId) {
                 score =(int) minimax(depth - 1, opponentId, alpha, beta).get(0);
                 if (score > alpha) {
                    alpha = score;
                    move_player = move;
                 }
              } else { 
                 score =(int) minimax(depth - 1, myId, alpha, beta).get(0);
                 if (score < beta) {
                    beta = score;
                    move_player = move;
                 }
              }
              
              // undo move
              unmake(move); // fonction a implementer
              
              // cut-off
              if (alpha >= beta) break;
           }
           answer.add(1, move_player);
  		 answer.add(0, score);
  		 return answer;
        }
     }



    @Override
    public PlayerMove move() {
        // implémentation random pour l'instant
        List<PlayerMove> moves = new LinkedList<>(allPossibleMoves());
        Collections.shuffle(moves);
        return moves.get(0);
        //return move_minimax();
        //return new PlayerMove(myId,true,getPlayerLocation(myId),new Coordinate(getPlayerLocation(myId).getRow()-1,getPlayerLocation(myId).getCol()));
    }
}