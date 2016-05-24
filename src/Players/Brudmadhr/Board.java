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
    public Board(){

    }

    public Board(Brudmadhr b){
        ia = b;
        board = new byte[BOARD_SIZE][BOARD_SIZE];
        ListeMurHorizontal = new boolean[BOARD_SIZE+1][BOARD_SIZE+1];
        ListeMurVertical = new boolean[BOARD_SIZE+1][BOARD_SIZE+1];

        for(int i=0;i<BOARD_SIZE;i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = 0b00000000;
                ListeMurHorizontal[i][j] = false;
                ListeMurVertical[i][j] = false;
            }
        }
    }

    // Représentation mur : bit 1 = N bit 2 = E bit 3 = S bit 4 = O
    public boolean deplacementN(int i, int j){  return (i-1>=0         && !ListeMurHorizontal[i][j] && !ListeMurHorizontal[i][j+1]);}
    public boolean deplacementE(int i, int j){  return (j+1<BOARD_SIZE && !ListeMurVertical[i][j+1] && !ListeMurVertical[i+1][j+1]);}
    public boolean deplacementS(int i, int j){  return (i+1<BOARD_SIZE && !ListeMurHorizontal[i+1][j] && !ListeMurHorizontal[i+1][j+1]);}
    public boolean deplacementO(int i, int j){  return (j-1>=0         && !ListeMurVertical[i][j] && ! ListeMurVertical [i+1][j]);}


    
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
	    		board[idep][jdep]    = (byte) (board[idep][jdep]       | 0b00001000);
	    		board[idep][jdep+1]  = (byte) (board[idep][jdep+1]     | 0b00001000);
	    		board[idep-1][jdep]  = (byte) (board[idep-1][jdep]     | 0b00000010);
	    		board[iarr-1][jarr-1]= (byte) (board[iarr-1][jarr-1]   | 0b00000010);
				ListeMurHorizontal[i][j]=true;// ajout de l'intersection (milieu du mur) dans liste des murs horizontaux
	    	}else{
                if(jdep == jarr && poseMurVertical(i,j) ){ // meme colonne, ajout d'un mur vertical
                    board[idep][jdep]    = (byte) (board[idep][jdep]       | 0b00000001);
                    board[idep+1][jdep]  = (byte) (board[idep+1][jdep]     | 0b00000001);
                    board[idep][jdep-1]  = (byte) (board[idep][jdep-1]     | 0b00000100);
                    board[iarr-1][jarr-1]= (byte) (board[iarr-1][jarr-1]   | 0b00000100);
                    ListeMurVertical[i][j]=true;// ajout de l'intersection (milieu du mur) dans liste des murs verticaux
                }else{
                   // System.err.println("Impossible de placer un mur a cet endroit! lors du set" + "\n");
                    return false;
                }
            }
	    }else{ return false; }

        return true;
    }
    public boolean removeWall(int idep, int jdep, int iarr, int jarr){
        int i = (idep+iarr)/2;
        int j =(jdep+jarr)/2;
        if(!wallCollisionEdges(idep, jdep, iarr, jarr) ){ // si pas de collision avec un bord
            if(idep == iarr && !poseMurHorizontal(i, j) ) { // ajout d'un mur horizontal
                board[idep][jdep]    = 0b00000000;
                board[idep][jdep+1]  = 0b00000000;
                board[idep-1][jdep]  = 0b00000000;
                board[iarr-1][jarr-1]= 0b00000000;
                ListeMurHorizontal[i][j]=false ;// ajout de l'intersection (milieu du mur) dans liste des murs horizontaux
            }else{
                if(jdep == jarr && !poseMurVertical(i,j) ){ // meme colonne, ajout d'un mur vertical
                    board[idep][jdep]    = 0b00000000;
                    board[idep+1][jdep]  = 0b00000000;
                    board[idep][jdep-1]  = 0b00000000;
                    board[iarr-1][jarr-1]= 0b00000000;
                    ListeMurVertical[i][j]=false;// ajout de l'intersection (milieu du mur) dans liste des murs verticaux
                }else{
                   // System.out.println("Impossible d'enlever un mur à cet endroit" + "\n");
                }
            }
        }else{ return false; }

        return true;
    }

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

    public Brudmadhr getIa(){
        return this.ia;
    }

    public void setIa(Brudmadhr bonjour){
        this.ia=bonjour;
    }

    public byte[][] getBoard(){return board;}

    public void setBoard(byte[][] toto,int i,int j) {
        board[i][j]= (byte) (board[i][j] | toto[i][j]);

    }

    public boolean[][] getListeMurHorizontal(){
        return this.ListeMurHorizontal;
    }

    public void setListeMurHorizontal(boolean[][] toto,int i, int j) {
        this.ListeMurHorizontal[i][j]=toto[i][j];
    }

    public boolean[][] getListeMurVertical()
    {
        return this.ListeMurVertical;
    }

    public void setListeMurVertical(boolean[][] toto, int i , int j)
    {
        this.ListeMurVertical[i][j]=toto[i][j];
    }
}













