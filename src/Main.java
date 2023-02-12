
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
public class Main {

    public static void main(String[] args) throws IOException {
        new Main();
    }
    Board board;
    BufferedImage plate;
    String pathname = System.getProperty("user.dir");

    public Main() throws IOException{
        JFrame frame = new JFrame("Rozkład ładunku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLayout(null);

        final JFileChooser fc = new JFileChooser(System.getProperty("user.dir") + "/resources");

        String[] advancement = new String[]{"Kwadrat", "Trójkąt", "Koło", "Własne"};
        int n = JOptionPane.showOptionDialog(null, "Wybierz kształt:", "Wybór kształtu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, advancement, advancement[1]);

        if(n == 0){
            pathname += "/resources/square.bmp";
        }
        else if (n == 1) {
            pathname += "/resources/triangle.bmp";
        }
        else if (n == 2) {
            pathname += "/resources/circle.bmp";
        }
        else {
            fc.setDialogTitle("Wczytaj kształt");
            int returnVal = fc.showOpenDialog(frame);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                File selected = fc.getSelectedFile();
                if(selected.getName().endsWith(".bmp")) {
                    pathname = fc.getSelectedFile().getAbsolutePath();
                }
            }
        }

        this.board = new Board(pathname);

        //int width = Board.getWidth();
        //int height = Board.getHeight();

        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }

            try {
                frame.add(new MainPane());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public class MainPane extends JPanel {
        public BufferedImage blueCircle;
        public Point[] electronPositions = new Point[64];
        Point center = new Point(300,300);

        Electron[] electrons = new Electron[64];
        int eleCounter = 2;

        public MainPane() throws IOException {
            setLayout(null);


            BufferedImage bc = null;
            BufferedImage p = null;
            try {
                bc = ImageIO.read(new File(System.getProperty("user.dir") + "/resources/electron.png"));
                p = ImageIO.read(new File(pathname));
            } catch (IOException e){
                e.printStackTrace();
            }
            blueCircle = bc;
            plate = p;

            electrons[0] = new Electron(200,200);
            electronPositions[0] = new Point(200, 200);
            electrons[1] = new Electron(400,400);
            electronPositions[1] = new Point(400, 400);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(eleCounter < 64) {
                        electronPositions[eleCounter] = e.getPoint();
                        if (board.isInside(electronPositions[eleCounter])) {
                            double dx = e.getX();
                            double dy = e.getY();
                            electrons[eleCounter] = new Electron((int)dx, (int)dy);
                            electronPositions[eleCounter] = new Point((int) dx, (int) dy);
                            repaint();
                            eleCounter++;
                        }
                    }
                }
            });


            Timer timer = new Timer(50, e -> {
                double centerX = 0;
                double centerY = 0;
                for (int i = 0; i < eleCounter; i++) {
                    for (int j = 0; j<eleCounter; j++) {
                        if(i != j) {
                            electrons[i].sum(electronPositions[j]);
                        }
                    }
                }
                for (int i = 0; i < eleCounter; i++) {
                    /*Point starting = new Point((int) electrons[i].X, (int) electrons[i].Y);
                    Point stopping = Board.collDetect(starting, electrons[i].nextX, electrons[i].nextY);
                    electrons[i].X = stopping.x;
                    electrons[i].Y = stopping.y;
                    electronPositions[i].translate(stopping.x - starting.x,stopping.y - starting.y);
                    */
                    System.out.println(electronPositions[i].x-electrons[i].X);
                    if(Board.isInside(electronPositions[i])) {
                        electrons[i].X += (int) electrons[i].nextX;
                        electrons[i].Y += (int) electrons[i].nextY;
                        electronPositions[i].translate((int) electrons[i].nextX, (int) electrons[i].nextY);

                    }
                    electrons[i].nextX = 0;
                    electrons[i].nextY = 0;
                }
                repaint();
            });
            timer.start();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 600);
        }
        @Override
        protected void paintComponent(Graphics gr){
            super.paintComponent(gr);
            Graphics2D g = (Graphics2D) gr.create();
            g.drawImage(plate, 0, 0, this);
            AffineTransform oldAT = g.getTransform();
            for(int i = 0; i < eleCounter; i++) {
                g.drawImage(blueCircle, electronPositions[i].x -12, electronPositions[i].y -12, this);
            }
            //g.drawImage(blueCircle, center.x, center.y,this);
            g.setTransform(oldAT);

            g.dispose();
        }
    }
}