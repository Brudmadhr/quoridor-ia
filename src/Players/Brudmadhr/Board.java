package Players.Brudmadhr;
import java.util.Arrays;

/**
 * Created by brudmadhr on 20/04/16.
 */
public class Board {
    final int BOARD_SIZE = 9; // plateau quoridor 9x9
    private byte[][] board;
    private boolean[][] intersections;

    // Un tableau 9*9 , un byte pour modéliser les murs entre chaque case (1 bit pour chaque direction)
    public Board(){
        board = new byte[BOARD_SIZE][BOARD_SIZE];
        intersections = new boolean[BOARD_SIZE][BOARD_SIZE];
        Arrays.fill(intersections, Boolean.FALSE);
    }

    // Représentation mur : bit 1 = N bit 2 = E bit 3 = S bit 4 = O
    public boolean deplacementN(int i, int j){
        return (i-1>=0         && ( (board[i][j] & 8) == 0) );
    }
    public boolean deplacementE(int i, int j){
        return (j+1<BOARD_SIZE && ( (board[i][j] & 4) == 0) );
    }
    public boolean deplacementS(int i, int j){
        return (i+1<BOARD_SIZE && ( (board[i][j] & 2) == 0) );
    }
    public boolean deplacementO(int i, int j){
        return (j-1>=0         && ( (board[i][j] & 1) == 0) );
    }

    public byte[][] getBoard(){return board;}
    
    /**
     *  idep, jdep : coords depart ; iarr, jarr : coords arrivee
     *  
     *  retourne true s'il est possible de placer un tel mur; false sinon
     */
    public boolean setWall(int idep, int jdep, int iarr, int jarr){ // ajout 04/05 (Louis) : plusieurs tets a ajouter : collisions murs et bords
	    if(!wallCollisionEdges(idep, jdep, iarr, jarr) && !wallCollisionWall(idep, jdep, iarr, jarr)){ // si pas de collision avec un bord	
    		if(idep == iarr){ // meme ligne
	    		board[idep][jdep]    = (byte) (board[idep][jdep]       | 0b1000);
	    		board[idep][jdep+1]  = (byte) (board[idep][jdep+1]     | 0b1000);
	    		board[idep-1][jdep]  = (byte) (board[idep-1][jdep]     | 0b0010);
	    		board[iarr-1][jarr-1]= (byte) (board[iarr-1][jarr-1]   | 0b0010);
				intersections[idep][jdep-1]=true; // ajout de l'intersection (milieu du mur)
	    	}else if(jdep == jarr){ // meme colonne
	    		board[idep][jdep]    = (byte) (board[idep][jdep]       | 0b0001);
	    		board[idep+1][jdep]  = (byte) (board[idep+1][jdep]     | 0b0001);
	    		board[idep][jdep-1]  = (byte) (board[idep][jdep-1]     | 0b0100);
	    		board[iarr-1][jarr-1]= (byte) (board[iarr-1][jarr-1]   | 0b0100);
				intersections[idep-1][jdep]=true; // ajout de l'intersection (milieu du mur)
	    	}else{
	    		System.err.println("Impossible de placer un mur a cet endroit!");
	    		return false;
	    	}
	    }
		return true;
    }
    
    public boolean wallCollisionEdges(int idep, int jdep, int iarr, int jarr){ // retourne true si collision avec un bord
    	if(idep<0 || iarr>BOARD_SIZE || jdep<0 || jarr>BOARD_SIZE){
    		return true;
    	}else{
    		return true;
    	}
    }
    
    public boolean wallCollisionWall(int idep, int jdep, int iarr, int jarr){ // retourne true si collision avec un autre mur
    	if(idep == iarr){ // meme ligne
    		if(intersections[idep-1][jdep]){
    			return true;
    		}
    	}else if(jdep == jarr){ // meme colonne
    		if(intersections[idep][jdep-1]){
    			return true;
    		}
    	}
    	return false;
    }
}