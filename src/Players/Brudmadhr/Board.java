package Players.Brudmadhr;

/**
 * Created by brudmadhr on 20/04/16.
 */
public class Board {
    final int BOARD_SIZE = 9;
    private byte[][] board;

    public Board(){
        board = new byte[BOARD_SIZE][BOARD_SIZE];
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
     */
    public void setWall(int idep, int jdep, int iarr, int jarr){
    	if(idep == iarr){ // meme ligne
    		board[idep][jdep]    = (byte) (board[idep][jdep]       | 0b1000);
    		board[idep][jdep+1]  = (byte) (board[idep][jdep+1]     | 0b1000);
    		board[idep-1][jdep]  = (byte) (board[idep-1][jdep]     | 0b0010);
    		board[iarr-1][jarr-1]= (byte) (board[iarr-1][jarr-1]   | 0b0010);
    	}else{ // meme colonne
    		board[idep][jdep]    = (byte) (board[idep][jdep]       | 0b0001);
    		board[idep+1][jdep]  = (byte) (board[idep+1][jdep]     | 0b0001);
    		board[idep][jdep-1]  = (byte) (board[idep][jdep-1]     | 0b0100);
    		board[iarr-1][jarr-1]= (byte) (board[iarr-1][jarr-1]   | 0b0100);
    	}
    }
}