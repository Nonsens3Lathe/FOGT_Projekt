
import java.util.HashSet;
import java.util.Set;

public class Cell {
    public final int x;
    public final int y;

    public int state;


    public Cell(int x, int y, int s) {
        this.x = x;
        this.y = y;
        this.state = s;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getState() {
        return state;
    }
}