package production;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
public class GUI extends JFrame {
    public static final int CELL_SIZE = 100;
    public static final int GRID_WIDTH = 2;
    public static final int GRID_WIDHT_HALF = GRID_WIDTH / 2;
    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int SYMBOL_STROKE_WIDTH = 8;
    private JLabel gameStatusBar;
    private BoardCanvas BoardCanvas;
    private boolean flag;
    JFrame frame;
    JRadioButton bluePlayerButton1;
    JRadioButton bluePlayerButton2;
    JRadioButton redPlayerButton1;
    JRadioButton redPlayerButton2;
    private SOSGame game;
    public GUI(){
        this(new SOSGame(3));
    }
    public GUI(SOSGame game) {
        this.game = game;
        setContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("SOS game");
        setVisible(true);
        frame = this;
        flag = false;
    }
    private void setContentPane() {

        BoardCanvas = new BoardCanvas();
        BoardCanvas.setPreferredSize(new Dimension(CELL_SIZE * game.getTotalRows(), CELL_SIZE * game.getTotalColumns()));

        gameStatusBar = new JLabel("  ");
        gameStatusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        gameStatusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(BoardCanvas,BorderLayout.NORTH);
        panel.add(gameStatusBar,BorderLayout.SOUTH);
        contentPane.add(panel,BorderLayout.CENTER);

        JPanel panel1 = new JPanel();
        JLabel sosLabel = new JLabel("SOS");
        JRadioButton SimpleBtn = new JRadioButton("Simple game",true);
        JRadioButton GeneralBtn = new JRadioButton("General Game", true);
        JLabel boardLabel = new JLabel("Board Size");
        JButton confirm = new JButton("Ok");

        JTextArea text = new JTextArea();
        text.setPreferredSize(new Dimension(20,20));
        confirm.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {

                        GUI gui = new GUI(new SOSGame (Integer.parseInt(text.getText())));
                        frameInit();
                        new TestThread(gui).start();
                    }
                });
            }
        });

        ButtonGroup sosGroup = new ButtonGroup();
        sosGroup.add(SimpleBtn);
        sosGroup.add(GeneralBtn);
        SimpleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.setCurrentGameType(SOSGame.GameModeType.Simple);
            }
        });
        GeneralBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.setCurrentGameType(SOSGame.GameModeType.General);
            }
        });
        panel1.add(sosLabel);
        panel1.add(SimpleBtn);
        panel1.add(GeneralBtn);
        panel1.add(boardLabel);
        panel1.add(text);
        panel1.add(confirm);
        contentPane.add(panel1, BorderLayout.NORTH);////top panel

        JPanel panel2 = new JPanel();
        JLabel bluePlayer = new JLabel("Blue Player");

        bluePlayerButton1 = new JRadioButton("S",true);
        bluePlayerButton2 = new JRadioButton("O");
        ButtonGroup bluePlayerGroup = new ButtonGroup();
        bluePlayerGroup.add(bluePlayerButton1);
        bluePlayerGroup.add(bluePlayerButton2);
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

        panel2.add(bluePlayer);
        panel2.add(bluePlayerButton1);
        panel2.add(bluePlayerButton2);
        contentPane.add(panel2,BorderLayout.WEST); /// left panel
        panel2.setPreferredSize(new Dimension(100,400));

        JPanel panel3 = new JPanel();
        JLabel redPlayer = new JLabel("Red Player");

        redPlayerButton1 = new JRadioButton("S",true);
        redPlayerButton2 = new JRadioButton("O");
        ButtonGroup redPlayerGroup = new ButtonGroup();
        redPlayerGroup.add(redPlayerButton1);
        redPlayerGroup.add(redPlayerButton2);

        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        panel3.add(redPlayer);
        panel3.add(redPlayerButton1);
        panel3.add(redPlayerButton2);

        contentPane.add(panel3,BorderLayout.EAST); /// right panel
        panel3.setPreferredSize(new Dimension(100,400));
    }
    class BoardCanvas extends JPanel {
        BoardCanvas() {
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (game.getGameState() == SOSGame.GameState.PLAYING) {
                        int rowSelected = e.getY() / CELL_SIZE;
                        int colSelected = e.getX() / CELL_SIZE;
                        char turn = game.getTurn();
                        int type;
                        if (turn == 'B')
                            type = bluePlayerButton1.isSelected() ?  0 : 1;
                        else
                            type = redPlayerButton1.isSelected() ? 0 : 1;
                        game.makeMove(rowSelected, colSelected, type);
                    }
                    else {
                        game.resetGame();
                    }
                    repaint();
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            drawGridLines(g);
            drawBoard(g);
            drawLines(g);
            printStatusBar();
        }
        private void drawGridLines(Graphics g) {

            g.setColor(Color.LIGHT_GRAY);
            for (int row = 1; row < game.getTotalRows(); ++row) {
                g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDHT_HALF, CELL_SIZE * game.getTotalRows() - 1, GRID_WIDTH,
                        GRID_WIDTH, GRID_WIDTH);
            }
            for (int col = 1; col < game.getTotalColumns(); ++col) {
                g.fillRoundRect(CELL_SIZE * col - GRID_WIDHT_HALF, 0, GRID_WIDTH,
                        CELL_SIZE * game.getTotalColumns() - 1, GRID_WIDTH, GRID_WIDTH);
            }
        }
        private void drawBoard(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int row = 0; row < game.getTotalRows(); ++row) {
                for (int col = 0; col < game.getTotalColumns(); ++col) {
                    int x1 = col * CELL_SIZE + CELL_PADDING;
                    int y1 = row * CELL_SIZE + CELL_PADDING;
                    if (game.getCell(row, col) == SOSGame.Cell.S) {

                        g2d.drawArc(x1 + CELL_SIZE / 5, y1, CELL_SIZE / 2 - CELL_PADDING, CELL_SIZE / 2 - CELL_PADDING,
                                60, 210);
                        g2d.drawArc(x1 + CELL_SIZE / 5, y1 + CELL_SIZE / 2 - CELL_PADDING, CELL_SIZE / 2 - CELL_PADDING,
                                CELL_SIZE / 2 - CELL_PADDING, 240, 210);

                    } else if (game.getCell(row, col) == SOSGame.Cell.O) {
                        g2d.drawOval(x1 + CELL_SIZE / 10, y1, (int) (SYMBOL_SIZE * 0.8), SYMBOL_SIZE);
                    }
                }
            }
        }
        private void drawLines(Graphics g) {
            ArrayList<ArrayList<Integer>> info = game.getSosInfo();
            Graphics2D g2d = (Graphics2D) g;
            if (info == null)
                return;
            for (ArrayList<Integer> it : info) {
                if (it.size() > 1) {
                    if (it.get(0) == 0)
                        g2d.setColor(Color.RED);
                    else
                        g2d.setColor(Color.BLUE);
                    for (int i = 1; i < it.size(); i += 4) {
                        int x1 = it.get(i + 1) * CELL_SIZE + CELL_SIZE / 2;
                        int y1 = it.get(i) * CELL_SIZE + CELL_SIZE / 2;
                        int x2 = it.get(i + 3) * CELL_SIZE + CELL_SIZE / 2;
                        int y2 = it.get(i + 2) * CELL_SIZE + CELL_SIZE / 2;
                        g2d.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        }
        private void printStatusBar() {
            if (game.getGameState() == SOSGame.GameState.PLAYING) {
                gameStatusBar.setForeground(Color.BLACK);
                if (game.getTurn() == 'S') {
                    gameStatusBar.setText("Blue Player's Turn");
                } else {
                    gameStatusBar.setText("Red Player's Turn");
                }
            } else if (game.getGameState() == SOSGame.GameState.DRAW) {
                gameStatusBar.setForeground(Color.BLUE);
                gameStatusBar.setText("It's a Draw! Click to play again.");
                if(!flag){
                    flag = true;
                }
            } else if (game.getGameState() == SOSGame.GameState.BLUE_WON) {
                gameStatusBar.setForeground(Color.RED);
                gameStatusBar.setText("'O' Won! Click to play again.");
                if(!flag){
                    flag=true;
                }
            } else if (game.getGameState() == SOSGame.GameState.RED_WON) {
                gameStatusBar.setForeground(Color.RED);
                gameStatusBar.setText("'S' Won! Click to play again.");
                if(!flag){
                    flag = true;
                }
            }
        }
    }
    class TestThread extends Thread{
        private GUI gui;
        public TestThread(GUI gui){
            this.gui = gui;
        }
    }
    public static void main(String[] args) {
        new GUI(new SOSGame(3));
    }
}
