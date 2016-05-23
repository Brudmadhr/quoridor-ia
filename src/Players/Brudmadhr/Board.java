package Players.Brudmadhr;
import Interface.Coordinate;

/**
 * Created by brudmadhr on 20/04/16.
 */
public class Board {
    final int BOARD_SIZE = 9; // plateau quoridor 9x9
    private byte[][] board;
    private boolean[][] ListeMurHorizontal;
    private boolean[][] ListeMurVertical;
    private Brudmadhr ia;
    // Un tableau 9*9 , un byte pour modéliser les murs entre chaque case (1 bit pour chaque direction)
    public Board(Brudmadhr b){
        ia = b;
        board = new byte[BOARD_SIZE][BOARD_SIZE];
        ListeMurHorizontal = new boolean[BOARD_SIZE+1][BOARD_SIZE+1];
        ListeMurVertical = new boolean[BOARD_SIZE+1][BOARD_SIZE+1];

        for(int i=0;i<BOARD_SIZE;i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                ListeMurHorizontal[i][j] = false;
                ListeMurVertical[i][j] = false;
            }
        }
    }

    // Représentation mur : bit 1 = N bit 2 = E bit 3 = S bit 4 = O
    public boolean deplacementN(int i, int j){  return (i-1>=0         && ( (board[i][j] & 8) == 0) );}
    public boolean deplacementE(int i, int j){  return (j+1<BOARD_SIZE && ( (board[i][j] & 4) == 0) );}
    public boolean deplacementS(int i, int j){  return (i+1<BOARD_SIZE && ( (board[i][j] & 2) == 0) );}
    public boolean deplacementO(int i, int j){  return (j-1>=0         && ( (board[i][j] & 1) == 0) );}

    public byte[][] getBoard(){return board;}
    
    /**
     *  idep, jdep : coords depart ; iarr, jarr : coords arrivee
     *  
     *  retourne true s'il est possible de placer un tel mur; false sinon
     */
    public boolean setWall(int idep, int jdep, int iarr, int jarr){
        int i = (idep+iarr)/2;
        int j =(jdep+jarr)/2;
        if(!wallCollisionEdges(idep, jdep, iarr, jarr) ){ // si pas de collision avec un bord
    		if(idep == iarr && poseMurHorizontal(i, j) ) { // ajout d'un mur horizontal
	    		board[idep][jdep]    = (byte) (board[idep][jdep]       | 0b1000);
	    		board[idep][jdep+1]  = (byte) (board[idep][jdep+1]     | 0b1000);
	    		board[idep-1][jdep]  = (byte) (board[idep-1][jdep]     | 0b0010);
	    		board[iarr-1][jarr-1]= (byte) (board[iarr-1][jarr-1]   | 0b0010);
				ListeMurHorizontal[i][j]=true;// ajout de l'intersection (milieu du mur) dans liste des murs horizontaux
	    	}else{
                if(jdep == jarr && poseMurVertical(i,j) ){ // meme colonne, ajout d'un mur vertical
                    board[idep][jdep]    = (byte) (board[idep][jdep]       | 0b0001);
                    board[idep+1][jdep]  = (byte) (board[idep+1][jdep]     | 0b0001);
                    board[idep][jdep-1]  = (byte) (board[idep][jdep-1]     | 0b0100);
                    board[iarr-1][jarr-1]= (byte) (board[iarr-1][jarr-1]   | 0b0100);
                    ListeMurVertical[i][j]=true;// ajout de l'intersection (milieu du mur) dans liste des murs verticaux
                }else{
                    System.err.println("Impossible de placer un mur a cet endroit!");
                    return false;
                }
            }
	    }else{ return false; }

        return true;
    }


   /* public void removeWall(int idep, int jdep, int iarr, int jarr){
    	intersections[(idep+iarr)/2][(jdep+jarr)/2] = false;
    }*/


    // Vérifie qu'il n'y a pas d'intersection avec les bords
    public boolean wallCollisionEdges(int idep, int jdep, int iarr, int jarr){ // retourne true si collision avec un bord
    	return (idep<0 || iarr>BOARD_SIZE || jdep<0 || jarr>BOARD_SIZE);
    }


    // Vérifie qu'il n'y a pas d'intersection entre différents murs

    public boolean poseMurHorizontal(int i, int j) {
           if( ListeMurVertical[i][j] || ListeMurHorizontal[i][j] || ListeMurHorizontal[i][j+1] || ListeMurHorizontal[i][j-1] ) {
                  return false;
           }else{
            return true;
        }
    }
    public boolean poseMurVertical(int i ,int j) {
        if (ListeMurHorizontal[i][j] || ListeMurVertical[i][j] || ListeMurVertical[i+1][j] || ListeMurVertical[i-1][j] ){
            return false;
        }else{
            return true;
        }
    }

    public void getListeMurHorizontal(){
        for (int i = 0; i<BOARD_SIZE;i++){
            for (int j=0;j<BOARD_SIZE;j++){
                System.out.println(i +""+ j +"" + ListeMurHorizontal[i][j]);
            }
        }
    }
    public void getListeMurVertical(){
        for (int i = 0; i<BOARD_SIZE;i++){
            for (int j=0;j<BOARD_SIZE;j++){
                System.out.println(i +""+ j +"" + ListeMurVertical[i][j]);
            }
        }
    }
}