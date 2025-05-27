package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RenderGrid extends JPanel implements MouseListener, MouseMotionListener{
    private int[][] grid;
    private static final Color background = new Color(10, 10, 10);
    private final Color gridColor = new Color(26, 26, 26);
    private final int tileSize;
    private JLabel timerLabel;
    private JLabel pointsLabel;
    private Color[] colors;

    private ArrayList<int[]> linesToDraw = new ArrayList<int[]>();
    Game game;

    public RenderGrid(int[][] grid, int tileSize) {
        this.grid = grid;
        this.tileSize = tileSize;
        this.setBackground(background);

        addMouseListener(this);
        addMouseMotionListener(this);

        setLayout(new BorderLayout());
        setBackground(background);

        timerLabel = Window.getTimerLabel();
        if (timerLabel == null) {
            timerLabel = new JLabel("<html><div style='text-align: center;'><span style='color: rgb(0, 148, 255);'>Timer:</span><br>60s</div></html>", JLabel.CENTER);
            timerLabel.setFont(new Font("Gotham", Font.BOLD, 25));
            timerLabel.setForeground(Color.WHITE);
        }

        pointsLabel = Window.getPointsLabel();
        if (pointsLabel == null) {
            pointsLabel = new JLabel("<html><div style='text-align: center;'>Points: <span style='color: rgb(45, 153, 0);'>0</span></div></html>", JLabel.CENTER);
            pointsLabel.setFont(new Font("Gotham", Font.BOLD, 25));
            pointsLabel.setForeground(Color.WHITE);
        }

        Window.setLabel(timerLabel, pointsLabel);

        // Colors
        colors = new Color[Board.getNumFlows()];

         // Colors
        colors = new Color[Board.getNumFlows()];

        for (int itr = 0; itr < colors.length; itr++)
        {
            int red = 179;
            int green = 77;
            int blue = 77;

            int amount = (itr + 1) * (612/colors.length);

                green += amount;
                if (green > 179)
                {
                    red -= green - 179;
                    green = 179;
                }
                if (red < 77)
                {
                    blue += 77 - red;
                    red = 77;
                }
                if (blue > 179)
                {
                    green -= blue - 179;
                    blue = 179;
                }
                if (green < 77)
                {
                    red += 77 - green;
                    green = 77;
                }
                if (red > 179)
                {
                    blue -= red - 179;
                    red = 179;
                } 
            
            colors[itr] = new Color(red, green, blue);
        }
    }

    public int getTileSize() {
        return tileSize;
    }

    public void timer(int myDelay, int start) {
        int delay = myDelay;
        int[] count = {start};

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                count[0]--;
                if(count[0] <= 0) {
                    ((Timer) evt.getSource()).stop();
                    System.out.println("Time Over!");
                    timerLabel.setText(
                        "<html>" +
                            "<div style='text-align: center;'>" +
                                "<span style='color: rgb(0, 148, 255);'>Timer:</span><br>" +
                                "Over!" + 
                            "</div>" +
                        "</html>"
                    );
                    // create end screen here
                    Window.createEndScreen();
                } else {
                    System.out.println(count[0]);
                    timerLabel.setText(
                        "<html>" +
                            "<div style='text-align: center;'>" +
                                "<span style='color: rgb(0, 148, 255);'>Timer:</span><br>" +
                                count[0] +
                            "s</div>" +
                        "</html>"
                    );
                }
            }
        };

        Timer timer = new Timer(delay, taskPerformer);
        timer.start();
    }

    public void setPoints(int points) {
        pointsLabel.setText(
            "<html>" +
                "<div style='text-align: center;'>" +
                        "Points: " + 
                        "<span style='color: rgb(45, 153, 0);'>" +
                        Game.getPoints() +
                    "</span><br>" +
                "</div>" +
            "</html>"
        );
    }

    public static Color getBgColor() {
        return background;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setGrid(int[][] newGrid) {
        this.grid = newGrid;
        repaint();
    }

    public void addLine(int currentColor, int startRow, int startCol, int endRow, int endCol) {
        linesToDraw.add(new int[]{currentColor, startRow, startCol, endRow, endCol});
        repaint();
    }

    public void removeLine(int startRow, int startCol, int endRow, int endCol) {
        for(int i = 0; i < linesToDraw.size(); i++) {
            int[] line = linesToDraw.get(i);

            int sr = line[1];
            int sc = line[2];
            int er = line[3];
            int ec = line[4];

            // check all dir
            boolean matches = (sr == startRow && sc == startCol && er == endRow && ec == endCol)
                    || (sr == endRow && sc == endCol && er == startRow && ec == startCol);

            if (matches) {
                linesToDraw.remove(i);
                repaint();
                return;
            }
        }
    }

    public ArrayList<int[]> getLinesToDraw() {
        return new ArrayList<>(linesToDraw);  // Return a copy to avoid external modification
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (grid == null) return;

        int gridWidth = grid[0].length * tileSize;
        int gridHeight = grid.length * tileSize;

        int xOffset = (getWidth() - gridWidth) / 2;
        int yOffset = (getHeight() - gridHeight) / 2;

        // thicker stroke
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // grid lines
        g2d.setStroke(new BasicStroke(2));
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                int x = xOffset + (col * tileSize);
                int y = yOffset + (row * tileSize);
                g2d.setColor(gridColor);
                g2d.drawRect(x, y, tileSize, tileSize);
            }
        }

        // drawing lines
        g2d.setStroke(new BasicStroke(25));
        for (int[] line : linesToDraw) {
            int color = line[0];

            //getting color
            checkColor(g2d, color);

            int sr = line[1], sc = line[2], er = line[3], ec = line[4];
            int startX = xOffset + (sc * tileSize) + tileSize / 2;
            int startY = yOffset + (sr * tileSize) + tileSize / 2;
            int endX = xOffset + (ec * tileSize) + tileSize / 2;
            int endY = yOffset + (er * tileSize) + tileSize / 2;
            g2d.drawLine(startX, startY, endX, endY);
        }

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                int value = grid[row][col];
                int x = xOffset + (col * tileSize);
                int y = yOffset + (row * tileSize);

                g2d.setColor(background);

                if(value < 0) {
                    // drawing circle
                    int padding = 25;
                    int squareSize = tileSize - padding;

                    // getting color
                    checkColor(g2d, value);
                    

                    g2d.fillOval(x + padding / 2, y + padding / 2, squareSize, squareSize);

                    // creating label with number
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Gotham", Font.BOLD, 20));
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = String.valueOf(Math.abs(value));

                    // horiz. centering
                    int textWidth = fm.stringWidth(text);
                    int textX = x + tileSize / 2 - textWidth / 2;

                    // ver. centering
                    int textHeight = fm.getHeight();
                    int textY = y + tileSize / 2 + (fm.getAscent() - textHeight / 2);

                    g2d.drawString(text, textX, textY);
                }
            }
        }
    }

    private void checkColor(Graphics2D g2d, int value)
    {
        for (int i = 0; i < colors.length; i++)
        {
            if (i + 1 == Math.abs(value) )
                g2d.setColor(colors[i]);
        }
    }

    private Point getGridCoordinates(Point point) {
        int x = (int) point.getX();
        int y = (int) point.getY();

        int numRows = grid.length;
        int numCols = grid[0].length;

        int gridWidth = grid[0].length * tileSize;
        int gridHeight = grid.length * tileSize;

        int xOffset = (getWidth() - gridWidth) / 2;
        int yOffset = (getHeight() - gridHeight) / 2;

        // make sure clicking outside of the grid doesnt work
        if(x < xOffset|| x > xOffset + gridWidth || y < yOffset || y > yOffset + gridHeight) {
            return new Point(-1, -1);
        }

        int newX = (x - xOffset) / tileSize;
        int newY = (y - yOffset) / tileSize;

        return new Point(newX, newY);
    }

    public void mousePressed(MouseEvent e) {
        Point newPoint = getGridCoordinates(new Point(e.getX(), e.getY()));
        game.handleMousePress((int) newPoint.getX(), (int) newPoint.getY());
    }

    public void mouseDragged(MouseEvent e) {
        Point newPoint = getGridCoordinates(new Point(e.getX(), e.getY()));
        game.handleMouseDrag((int) newPoint.getX(), (int) newPoint.getY());
    }

    public void mouseReleased(MouseEvent e) {
        Point newPoint = getGridCoordinates(new Point(e.getX(), e.getY()));
        game.handleMouseRelease((int) newPoint.getX(), (int) newPoint.getY());
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}
