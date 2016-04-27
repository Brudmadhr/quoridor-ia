package Players.BRUDMADHR;

import Interface.Coordinate;

/**
 * Created by brudmadhr on 06/04/16.
 */
public class Wall {
    //final int WIDTH = 2;
    private Coordinate deb;
    private Coordinate fin;

    public Wall(Coordinate d,Coordinate f){
        deb = d;
        fin = f;
    }

    public Coordinate getDeb() {
        return deb;
    }

    public Coordinate getFin() {
        return fin;
    }
}
