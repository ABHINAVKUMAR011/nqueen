import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class nqueen {
    final static int M = 8;
    static JLabel[][] jLabel = new JLabel[M][M];
    static int board[][] = new int[M][M];   
    static Image queenImage;
    static volatile boolean isPaused = false; // to control the pause state
    static JButton controlButton;
    static int currentStep = -1; // Track the current step

    static {
        try {
            Image originalImage = ImageIO.read(new File("C:\\Users\\HP\\OneDrive\\Desktop\\nqueen\\Queen"));
            int squareSize = 500 / M; // Assuming the JFrame size is 500x500
            queenImage = getScaledImage(originalImage, squareSize, squareSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void printSolution() {
        for (int i = 0; i < M; ++i) {
            for (int j = 0; j < M; ++j) {
                System.out.printf("%d ", board[i][j]);
            }
            System.out.printf("\n");
        }
    }

    static boolean isSafe(int row, int col) {
        try {
            Thread.sleep(100); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < col; ++i)
            if (board[row][i] == 1)
                return false;

        for (int i = row, j = col; i >= 0 && j >= 0; --i, --j) {
            if (board[i][j] == 1)
                return false;
        }

        for (int i = row, j = col; i < M && j >= 0; ++i, --j) {
            if (board[i][j] == 1)
                return false;
        }

        return true;
    }

    static boolean findSolution(int col) {
        if (col >= M)   //returns true if any queen is present
            return true;

        for (int i = 0; i < M; ++i) {   //it will attempt to put queen in every row
            try {
                Thread.sleep(100); // Increased delay to 100 ms for visibility
                pauseIfNeeded(); // Check for pause state
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isSafe(i, col)) {
                board[i][col] = 1;
                setQueenIcon(i, col, true);

                if (findSolution(col + 1))
                    return true;

                // Backtracking
                board[i][col] = 0;
                setQueenIcon(i, col, false);

                try {
                    Thread.sleep(100); // Delay when backtracking
                    pauseIfNeeded(); // Check for pause state
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    static void solveNQueen() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < M; ++i) {
            for (int j = 0; j < M; ++j) {
                try {
                    Thread.sleep(10);
                    pauseIfNeeded(); // Check for pause state
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                board[i][j] = 0;
                jLabel[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
                jLabel[i][j].setIcon(null);
            }
        }

        if (!findSolution(0))
            System.out.println("No Solution.\n");
        else
            printSolution();
    }

    static void setQueenIcon(int row, int col, boolean set) {
        if (set) {
            jLabel[row][col].setIcon(new ImageIcon(queenImage));
        } else {
            jLabel[row][col].setIcon(null);
        }
    }

    static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    nqueen() {
        JFrame jFrame = new JFrame("NQueen Visualizer.");
        jFrame.setLayout(new BorderLayout());
        jFrame.setSize(500, 500);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel boardPanel = new JPanel(new GridLayout(M, M));
        for (int i = 0; i < M; ++i) {
            for (int j = 0; j < M; ++j) {
                jLabel[i][j] = new JLabel();
                jLabel[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                jLabel[i][j].setOpaque(true);
                jLabel[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
                boardPanel.add(jLabel[i][j]);
            }
        }

        controlButton = new JButton("Pause");
        controlButton.addActionListener(e -> togglePause());

        JPanel controlPanel = new JPanel();
        controlPanel.add(controlButton);

        jFrame.add(boardPanel, BorderLayout.CENTER);
        jFrame.add(controlPanel, BorderLayout.SOUTH);
        jFrame.setVisible(true);
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(nqueen::new);
        solveNQueen();
    }

    static void togglePause() {
        isPaused = !isPaused;
        controlButton.setText(isPaused ? "Play" : "Pause");
    }

    static void pauseIfNeeded() {
        while (isPaused) {
            try {
                Thread.sleep(0); // Sleep while paused
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}