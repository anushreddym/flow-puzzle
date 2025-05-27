package Classes;
import java.awt.*;
import java.util.*;

public class Game {
    private Board board;
    private RenderGrid renderGrid;

    private int currentColor = 0;
    private boolean dragging = false;
    private ArrayList<Point> currentPath = new ArrayList<Point>();
    private HashMap<Integer, ArrayList<Point>> allPaths = new HashMap<>();
    private int pathsComplete = 0;
    private static int points = 0;
    private static int highscore = 0;

    public Game(Board board, RenderGrid renderGrid) {
        this.board = board;
        this.renderGrid = renderGrid;
    }

    public void handleMousePress(int x, int y) {
        handlePressOnPath(x, y);
        int value = Board.getGrid()[y][x];
        if(value != 0) {
            if(value > 0) {
                currentColor = -value;
            } else {
                currentColor = value;
            }

            dragging = true;
            currentPath.clear();
            currentPath.add(new Point(x, y));
        }
    }

    public static int getPoints() {
        System.out.println("Points: " + points);
        return points;
    }

    public static int getHighscore() {
        return highscore;
    }
    public static void setPoints(int points) {
        Game.points = points;

    }

    public void increaseScore() {
        points++;
        if(points > highscore) {
            highscore = points;
        }
    }

    // need to make it so if person drags out of bounds it resets the grid
    public void handleMouseDrag(int x, int y) {
        if (!dragging) return;
        if(x == -1 || y == -1) {
            System.out.println("Out of bounds");
        }

        Point next = new Point(x, y);
        Point last = currentPath.get(currentPath.size() - 1);

        if (!next.equals(last) && isAdjacent(last, next) && x < Board.getGrid()[0].length && y < Board.getGrid().length) {
            if (Board.getGrid()[y][x] == 0 || Board.getGrid()[y][x] == -currentColor || Board.getGrid()[y][x] == currentColor) {
                if(next.equals(currentPath.get(0))) return; // dont recheck first node

                currentPath.add(next);

                // mark path lines
                if(Board.getGrid()[next.y][next.x] == 0 ||  Board.getGrid()[next.y][next.x] == -Math.abs(currentColor)) {
                    renderGrid.addLine(currentColor, last.y, last.x, next.y, next.x);
                    renderGrid.repaint();
                }

                // actually set color
                if (Board.getGrid()[y][x] == 0 || Board.getGrid()[y][x] == -currentColor) {
                    board.setGrid(y, x, -currentColor);
                }

                if (isPathComplete(currentPath)) {

                    // dont double count path
                    if (!allPaths.containsKey(currentColor)) {
                        increaseScore();
                        renderGrid.setPoints(points);
                        pathsComplete++;
                        System.out.println("Path completed. Paths complete: " + pathsComplete);
                    }

                    dragging = false;
                    allPaths.put(currentColor, new ArrayList<>(currentPath));
                }

            }
        }
    }

    public boolean isPathComplete(ArrayList<Point> path) {
        Point start = path.get(0);
        Point end = path.get(path.size() - 1);

        int startVal = Board.getGrid()[start.y][start.x];
        int endVal = Board.getGrid()[end.y][end.x];

        System.out.println(startVal);
        System.out.println(endVal);

        return startVal < 0 && endVal < 0 && startVal == endVal;
    }

    public void handleMouseRelease(int x, int y) {
        dragging = false;
        ArrayList<Point> path = new ArrayList<>();

        if(!allPaths.containsKey(currentColor)) {


            for(int row = 0; row < board.getGrid().length; row++) {
                for(int col = 0; col < board.getGrid()[row].length; col++) {
                    if(board.getGrid()[row][col] == Math.abs(currentColor) ||  board.getGrid()[row][col] == -Math.abs(currentColor)) {
                        path.add(new Point(col, row));
                    }
                }
            }
        }

        System.out.println(path);

        // hashset is faster and prevents duplicates automatically
        HashSet<Point> pathSet = new HashSet<>(path);

        for (Point p : path) {
            int x1 = p.x;
            int y1 = p.y;

            if(Board.getGrid()[y1][x1] != -Math.abs(currentColor)) {
                board.setGrid(y1, x1, 0);
            }

            int[][] dirs = {{0,1}, {1,0}, {0,-1}, {-1,0}};
            for (int[] d : dirs) {
                int x2 = x1 + d[0];
                int y2 = y1 + d[1];

                Point neighbor = new Point(x2, y2);
                if (pathSet.contains(neighbor)) {
                    renderGrid.removeLine(y1, x1, y2, x2);
                }
            }
        }

        renderGrid.repaint();

        if(hasGameFinished()) {
            System.out.println("Game has been finished!");
        }
    }

    public boolean isAdjacent(Point a, Point b) {
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);
        return (dx + dy == 1);
    }

    public boolean hasGameFinished() {
        if(pathsComplete == Board.getNumFlows()) {
            GameManager.createGame();
            return true;
        }

        return false;
    }

    public void handlePressOnPath(int x, int y) {
        Point clicked = new Point(x, y);

        for (Map.Entry<Integer, ArrayList<Point>> entry : allPaths.entrySet()) {
            ArrayList<Point> path = entry.getValue();
            if (path.contains(clicked)) {
                int color = entry.getKey();
                HashSet<Point> pathSet = new HashSet<>(path);

                for (Point p : path) {
                    int x1 = p.x;
                    int y1 = p.y;

                    // only tiles, not start or end nodes
                    if (Board.getGrid()[y1][x1] == Math.abs(color)) {
                        board.setGrid(y1, x1, 0);
                    }

                    int[][] dirs = {{0,1}, {1,0}, {0,-1}, {-1,0}};
                    for (int[] d : dirs) {
                        int x2 = x1 + d[0];
                        int y2 = y1 + d[1];
                        Point neighbor = new Point(x2, y2);
                        if (pathSet.contains(neighbor)) {
                            renderGrid.removeLine(y1, x1, y2, x2);
                        }
                    }
                }

                renderGrid.repaint();

                // reduce path amount, reset tracked paths
                allPaths.remove(color);
                pathsComplete--;
                points--;
                renderGrid.setPoints(points);
                System.out.println("Path removed. Paths complete: " + pathsComplete);
                break; // make sure only one path is removed
            }
        }
    }


}
