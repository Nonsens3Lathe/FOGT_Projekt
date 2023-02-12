import java.awt.*;
import java.util.Arrays;

import java.lang.Math.*;

public class Electron {
    public double X;
    public double Y;
    public double nextX=0;
    public double nextY=0;

    final double K = 254; // m^3 * s ^-2
    final double ppm = 400;
    final double tps = 400;
    // final double constans = (K * Math.pow(ppm, 3)) / (2 * Math.pow(tps, 2)); //px^3 * tc^-2
    final double constans = 300000;

    public Electron(double xcoord, double ycoord){
        this.X = xcoord;
        this.Y = ycoord;
        this.nextX = 0;
        this.nextY = 0;
    }
    public void sum(Point center) {
        double dx = X - center.x;
        double dy = Y - center.y;
        double angleRad = Math.atan2(dy, dx);
        double dr = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));

        double totMove2 = constans * Math.pow(dr, -2);

        nextX += totMove2 * Math.cos(angleRad);
        nextY += totMove2 * Math.sin(angleRad);
    }
}
