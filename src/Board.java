import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class Board {
    public static int width;
    public static int height;
    static Cell[][] array2D;
    public Board(String BMPFileName) throws IOException {
        BufferedImage image = ImageIO.read(new File(BMPFileName));
        width = image.getWidth();
        height = image.getHeight();
        int widthBorder = image.getWidth() + 2;
        int heightBorder = image.getHeight() + 2;

        array2D = new Cell[widthBorder][heightBorder];

        for (int xPixel = 0; xPixel < widthBorder; xPixel++)  {
            for (int yPixel = 0; yPixel < heightBorder; yPixel++) {
                if(xPixel == 0 || yPixel == 0 || xPixel == (widthBorder - 1) || yPixel == (heightBorder - 1)) {
                    array2D[xPixel][yPixel] = new Cell(xPixel, yPixel,0);
                } else {
                    int color = image.getRGB(xPixel - 1, yPixel - 1);
                    if (color == Color.BLACK.getRGB()) {
                        array2D[xPixel][yPixel] = new Cell(xPixel, yPixel,0);
                    } else {
                        array2D[xPixel][yPixel] = new Cell(xPixel, yPixel,1);
                    }
                }
            }
        }
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static Boolean isInside(Point point){
        int x = point.x;
        int y = point.y;
        try {
            if (array2D[x][y].state == 0) {
                return false;
            } else {
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException e){
            return false;
        }
    }

    public static Point collDetect(Point position, double moveX, double moveY){
        double mainSlope =  moveY/moveX;
        if(array2D[position.x][position.y].state == 0){
            System.out.println("PANIC");
            return position;
        } else while(moveX > 0 && moveY > 0){
            double slope = moveY/moveX;
            if (slope <= mainSlope){
             position.x++;
             moveX--;
                if(array2D[position.x][position.y].state == 0){
                    return position;
                }
            } else {
                position.y++;
                moveY--;
                if(array2D[position.x][position.y].state == 0){
                    return position;
                }
            }

        }
        return position;
    }
}
