package Classes;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Window{
    private static JFrame frame = new JFrame("Game Window");
    private static RenderGrid myPanel;
    private static JPanel containerPanel = new JPanel(new BorderLayout());
    private static JLabel label1;
    private static JLabel label2;
    private static boolean windowCreated = false;

    public static void setLabel(JLabel label1, JLabel label2){
        Window.label1 = label1;
        Window.label2 = label2;
    }

    public static boolean hasWindowCreated() {
        return windowCreated;
    }

    public static JLabel getTimerLabel() {
        return label1;
    }

    public static JLabel getPointsLabel() {
        return label2;
    }

    public static void createWindow(int minWidth, int minHeight, RenderGrid panel) {
        myPanel = panel;
        if(!windowCreated){
            frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            // set panel size to 50% of screen size
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int halfWidth = screenSize.width / 2;
            int fullHeight = screenSize.height;
            panel.setPreferredSize(new Dimension(halfWidth, fullHeight));

            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setBackground(RenderGrid.getBgColor());

            JButton closeButton = new JButton("X");
            closeButton.setFont(new Font("Gotham", Font.PLAIN, 9));
            closeButton.setForeground(Color.WHITE);
            closeButton.setBackground(Color.RED);
            closeButton.setFocusPainted(false);
            closeButton.setBorderPainted(false);
            closeButton.setOpaque(true);
            closeButton.setHorizontalAlignment(SwingConstants.CENTER);
            closeButton.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width - 50, 10, 40, 40);

            closeButton.addActionListener(e -> System.exit(0));

            JLayeredPane layeredPane = frame.getLayeredPane();
            layeredPane.add(closeButton, JLayeredPane.POPUP_LAYER);

            label1.setAlignmentX(Component.CENTER_ALIGNMENT);
            label2.setAlignmentX(Component.CENTER_ALIGNMENT);

            rightPanel.add(Box.createVerticalGlue());
            rightPanel.add(label1);
            rightPanel.add(Box.createVerticalStrut(20));
            rightPanel.add(label2);
            rightPanel.add(Box.createVerticalGlue());

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel, rightPanel);
            splitPane.setDividerLocation(halfWidth);
            splitPane.setEnabled(false);
            splitPane.setDividerSize(0); // Remove visible divider
            splitPane.setBorder(null);

            containerPanel.setBackground(RenderGrid.getBgColor());
            containerPanel.removeAll();
            containerPanel.setLayout(new BorderLayout());
            containerPanel.add(splitPane, BorderLayout.CENTER);

            frame.getContentPane().removeAll();
            frame.getContentPane().add(containerPanel);
            frame.setMinimumSize(new Dimension(minWidth, minHeight));
            frame.setVisible(true);
            windowCreated = true;
        } else {
            // only update grid if window alr exists

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int halfWidth = screenSize.width / 2;
            int fullHeight = screenSize.height;
            panel.setPreferredSize(new Dimension(halfWidth, fullHeight));

            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setBackground(RenderGrid.getBgColor());

            label1.setAlignmentX(Component.CENTER_ALIGNMENT);
            label2.setAlignmentX(Component.CENTER_ALIGNMENT);

            rightPanel.add(Box.createVerticalGlue());
            rightPanel.add(label1);
            rightPanel.add(Box.createVerticalStrut(20));
            rightPanel.add(label2);
            rightPanel.add(Box.createVerticalGlue());

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel, rightPanel);
            splitPane.setDividerLocation(halfWidth);
            splitPane.setEnabled(false);
            splitPane.setDividerSize(0);
            splitPane.setBorder(null);

            containerPanel.removeAll();
            containerPanel.add(splitPane, BorderLayout.CENTER);

            frame.getContentPane().removeAll();
            frame.getContentPane().add(containerPanel);
            frame.revalidate();
            frame.repaint();
        }
    }

    public static void removeAll() {
        frame.remove(containerPanel);
        frame.repaint();
    }

    public static void createEndScreen() {
        frame.getContentPane().removeAll();

        JPanel endScreenPanel = new JPanel();
        endScreenPanel.setLayout(new BoxLayout(endScreenPanel, BoxLayout.Y_AXIS));
        endScreenPanel.setBackground(RenderGrid.getBgColor());

        JLabel title = new JLabel("Game Over!");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Gotham", Font.BOLD, 50));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel();
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setText(
                "<html>" +
                        "<div style='text-align: center;'>" +
                        "Points: " +
                        "<span style='color: rgb(45, 153, 0);'>" +
                        Game.getPoints() +
                        "</span><br>" +
                        "</div>" +
                        "</html>"
        );

        scoreLabel.setFont(new Font("Gotham", Font.PLAIN, 20));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton retryButton = new JButton("Retry");
        retryButton.setFont(new Font("Gotham", Font.PLAIN, 20));
        retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        retryButton.setFocusPainted(false);
        retryButton.setBorderPainted(false);
        retryButton.setContentAreaFilled(true);
        retryButton.setOpaque(true);

        // detect clicks on this button
        retryButton.addActionListener(e -> {
            Game.setPoints(0);
            myPanel.setPoints(0);
            GameManager.setTimerStarted(false);
            GameManager.setSize(2);
            GameManager.createGame();
        });

        JLabel highScoreLabel = new JLabel();
        highScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        highScoreLabel.setText(
                "<html>" +
                        "<div style='text-align: center;'>" +
                        "Highscore: " +
                        "<span style='color: rgb(247, 92, 92);'>" +
                        Game.getHighscore() +
                        "</span><br>" +
                        "</div>" +
                        "</html>"
        );
        highScoreLabel.setFont(new Font("Gotham", Font.BOLD, 24));
        highScoreLabel.setForeground(Color.WHITE);
        highScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        endScreenPanel.add(Box.createVerticalGlue());
        endScreenPanel.add(title, BorderLayout.NORTH);
        endScreenPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        endScreenPanel.add(highScoreLabel);
        endScreenPanel.add(Box.createRigidArea(new Dimension(0, 75)));
        endScreenPanel.add(scoreLabel);
        endScreenPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        endScreenPanel.add(retryButton);
        endScreenPanel.add(Box.createVerticalGlue());

        frame.getContentPane().add(endScreenPanel); // elements are added to jFrame contentframe not frame
        frame.revalidate(); // recount layout
        frame.repaint();
    }

    public static void createStartScreen(int minWidth, int minHeight) {
        if(!windowCreated) {
            frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            JPanel startScreenPanel = new JPanel();
            startScreenPanel.setLayout(new BoxLayout(startScreenPanel, BoxLayout.Y_AXIS));
            startScreenPanel.setBackground(RenderGrid.getBgColor());

            JButton closeButton = new JButton("X");
            closeButton.setFont(new Font("Gotham", Font.PLAIN, 9));
            closeButton.setForeground(Color.WHITE);
            closeButton.setBackground(Color.RED);
            closeButton.setFocusPainted(false);
            closeButton.setBorderPainted(false);
            closeButton.setOpaque(true);
            closeButton.setHorizontalAlignment(SwingConstants.CENTER);
            closeButton.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width - 50, 10, 40, 40);

            closeButton.addActionListener(e -> System.exit(0));

            JLayeredPane layeredPane = frame.getLayeredPane();
            layeredPane.add(closeButton, JLayeredPane.POPUP_LAYER);

            JLabel title = new JLabel("totally epic flow puzzle!!");
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setFont(new Font("Gotham", Font.BOLD, 50));
            title.setForeground(Color.WHITE);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel desc = new JLabel("By: Anush & Abi");
            desc.setHorizontalAlignment(SwingConstants.CENTER);
            desc.setFont(new Font("Gotham", Font.PLAIN, 25));
            desc.setForeground(Color.GRAY);
            desc.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton startButton = new JButton("START");
            startButton.setFont(new Font("Gotham", Font.PLAIN, 20));
            startButton.setForeground(Color.WHITE);
            startButton.setBackground(new Color(99, 201, 58));
            startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            startButton.setPreferredSize(new Dimension(250, 45));

            startButton.setFocusPainted(false);
            startButton.setBorderPainted(false);
            startButton.setContentAreaFilled(true);
            startButton.setOpaque(true);

            JTextField timeAmt = new JTextField("Enter time (sec)...");
            timeAmt.setHorizontalAlignment(SwingConstants.CENTER);
            timeAmt.setFont(new Font("Gotham", Font.PLAIN, 20));
            timeAmt.setForeground(Color.GRAY);
            timeAmt.setBackground(new Color(3, 3,3));
            timeAmt.setBorder(null);
            timeAmt.setCaretColor(Color.WHITE);
            timeAmt.setOpaque(true);
            timeAmt.setAlignmentX(Component.CENTER_ALIGNMENT);
            timeAmt.setPreferredSize(new Dimension(210, 45));
            timeAmt.setMaximumSize(new Dimension(210, 45));

            timeAmt.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (timeAmt.getText().equals("Enter time (sec)...")) {
                        timeAmt.setForeground(Color.WHITE);
                        timeAmt.setText("");
                    }
                }
            });

            timeAmt.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    if (timeAmt.getText().isEmpty()) {
                        timeAmt.setForeground(Color.GRAY);
                        timeAmt.setText("Enter time (sec)...");
                    }
                }
            });

            // detect clicks on this button
            startButton.addActionListener(e -> {
                try {
                    String text = timeAmt.getText().trim();

                    // ignore placeholder
                    if (text.equals("Enter time (sec)...")) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid time in seconds.");
                        return;
                    }

                    int timeInSeconds = Integer.parseInt(text); // string to int
                    GameManager.setTime(timeInSeconds);
                    GameManager.createGame();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
                }
            });

            JLabel version = new JLabel("Build 26");
            version.setHorizontalAlignment(SwingConstants.CENTER);
            version.setFont(new Font("Gotham", Font.PLAIN, 15));
            version.setForeground(Color.GRAY);
            version.setAlignmentX(Component.CENTER_ALIGNMENT);

            startScreenPanel.setBackground(RenderGrid.getBgColor());
            startScreenPanel.removeAll();
            startScreenPanel.setLayout(new BoxLayout(startScreenPanel, BoxLayout.Y_AXIS));

            startScreenPanel.add(Box.createVerticalGlue());
            startScreenPanel.add(title, BorderLayout.NORTH);
            startScreenPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            startScreenPanel.add(desc);
            startScreenPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            startScreenPanel.add(startButton);
            startScreenPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            startScreenPanel.add(timeAmt);
            startScreenPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            startScreenPanel.add(version);
            startScreenPanel.add(Box.createVerticalGlue());

            frame.getContentPane().removeAll();
            frame.getContentPane().add(startScreenPanel);
            frame.setMinimumSize(new Dimension(minWidth, minHeight));
            frame.setVisible(true);
            windowCreated = true;
        } else {
            JPanel startScreenPanel = new JPanel();
            startScreenPanel.setLayout(new BoxLayout(startScreenPanel, BoxLayout.Y_AXIS));
            startScreenPanel.setBackground(RenderGrid.getBgColor());

            JButton closeButton = new JButton("X");
            closeButton.setFont(new Font("Gotham", Font.PLAIN, 9));
            closeButton.setForeground(Color.WHITE);
            closeButton.setBackground(Color.RED);
            closeButton.setFocusPainted(false);
            closeButton.setBorderPainted(false);
            closeButton.setOpaque(true);
            closeButton.setHorizontalAlignment(SwingConstants.CENTER);
            closeButton.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width - 50, 10, 40, 40);

            closeButton.addActionListener(e -> System.exit(0));

            JLayeredPane layeredPane = frame.getLayeredPane();
            layeredPane.add(closeButton, JLayeredPane.POPUP_LAYER);

            JLabel title = new JLabel("Flow Free");
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setFont(new Font("Gotham", Font.BOLD, 50));
            title.setForeground(Color.WHITE);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel desc = new JLabel("By: Anush & Abi");
            desc.setHorizontalAlignment(SwingConstants.CENTER);
            desc.setFont(new Font("Gotham", Font.PLAIN, 25));
            desc.setForeground(Color.GRAY);
            desc.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton startButton = new JButton("Start");
            startButton.setFont(new Font("Gotham", Font.PLAIN, 20));
            startButton.setForeground(Color.WHITE);
            startButton.setBackground(new Color(99, 201, 58));
            startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            startButton.setPreferredSize(new Dimension(250, 45));

            startButton.setFocusPainted(false);
            startButton.setBorderPainted(false);
            startButton.setContentAreaFilled(true);
            startButton.setOpaque(true);

            JTextField timeAmt = new JTextField("Enter time (sec)...");
            timeAmt.setHorizontalAlignment(SwingConstants.CENTER);
            timeAmt.setFont(new Font("Gotham", Font.PLAIN, 20));
            timeAmt.setForeground(Color.GRAY);
            timeAmt.setBackground(new Color(3, 3,3));
            timeAmt.setBorder(null);
            timeAmt.setCaretColor(Color.WHITE);
            timeAmt.setOpaque(true);
            timeAmt.setAlignmentX(Component.CENTER_ALIGNMENT);
            timeAmt.setPreferredSize(new Dimension(210, 45));
            timeAmt.setMaximumSize(new Dimension(210, 45));

            timeAmt.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (timeAmt.getText().equals("Enter time (sec)...")) {
                        timeAmt.setForeground(Color.WHITE);
                        timeAmt.setText("");
                    }
                }
            });

            timeAmt.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    if (timeAmt.getText().isEmpty()) {
                        timeAmt.setForeground(Color.GRAY);
                        timeAmt.setText("Enter time (sec)...");
                    }
                }
            });

            // detect clicks on this button
            startButton.addActionListener(e -> {
                try {
                    String text = timeAmt.getText().trim();

                    // ignore placeholder
                    if (text.equals("Enter time (sec)...")) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid time in seconds.");
                        return;
                    }

                    int timeInSeconds = Integer.parseInt(text); // string to int
                    GameManager.setTime(timeInSeconds);
                    GameManager.createGame();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
                }
            });

            JLabel version = new JLabel("Build 24");
            version.setHorizontalAlignment(SwingConstants.CENTER);
            version.setFont(new Font("Gotham", Font.PLAIN, 15));
            version.setForeground(Color.GRAY);
            version.setAlignmentX(Component.CENTER_ALIGNMENT);

            startScreenPanel.setBackground(RenderGrid.getBgColor());
            startScreenPanel.removeAll();
            startScreenPanel.setLayout(new BoxLayout(startScreenPanel, BoxLayout.Y_AXIS));

            startScreenPanel.add(Box.createVerticalGlue());
            startScreenPanel.add(title, BorderLayout.NORTH);
            startScreenPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            startScreenPanel.add(desc);
            startScreenPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            startScreenPanel.add(startButton);
            startScreenPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            startScreenPanel.add(timeAmt);
            startScreenPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            startScreenPanel.add(version);
            startScreenPanel.add(Box.createVerticalGlue());

            frame.getContentPane().removeAll();
            frame.getContentPane().add(startScreenPanel);
            frame.revalidate();
            frame.repaint();
        }
    }
}
