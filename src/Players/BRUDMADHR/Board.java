package Players.BRUDMADHR;

import Interface.Coordinate;

import java.util.ArrayList;

/**
 * Created by brudmadhr on 20/04/16.
 */
public class Board {
    final int BOARD_SIZE = 9;
    private byte[][] board;

    public Board(){
        board = new byte[BOARD_SIZE][BOARD_SIZE];
    }

    public boolean deplacementN(int i, int j){
        return (i-1>=0 && ( (board[i][j] & 8) == 0) );
    }
    public boolean deplacementE(int i, int j){
        return (j+1<BOARD_SIZE && ( (board[i][j] & 4) == 0) );
    }
    public boolean deplacementS(int i, int j){
        return (i+1<BOARD_SIZE && ( (board[i][j] & 2) == 0) );
    }
    public boolean deplacementO(int i, int j){
        return (j-1>=0 && ( (board[i][j] & 1) == 0) );
    }

    public byte[][] getBoard(){return board;}

    public void setBoard(){

    }
}