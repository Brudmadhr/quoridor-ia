package Players.BRUDMADHR;
import Interface.Coordinate;
import java.util.List;
/**
 * Created by brudmadhr on 20/04/16.
 */
public class Adjacent {
    final int MAX = 4;
    private int nbElements;
    private Coordinate[] succ;

    public Adjacent(List<Coordinate> s){
        succ = new Coordinate[MAX];
        nbElements = 0;
        for(Coordinate c : s){
            succ[nbElements] = c;
            nbElements++;
        }
    }

    public int getNbElements() {
        return nbElements;

    }

    public void setNbElements(int nbElements) {
        this.nbElements = nbElements;
    }

    public Coordinate[] getSucc() {
        return succ;
    }

    public void setSucc(Coordinate[] succ) {
        this.succ = succ;
    }
}
