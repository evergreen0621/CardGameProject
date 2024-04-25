import java.util.Scanner;

public class OmokGame {
    public static final int BOARD_SIZE = 19;
    public static final char EMPTY = '.';
    public static final char PLAYER_STONE = 'O';
    public static final char COMPUTER_STONE = 'X';

    public static char[][] board = new char[BOARD_SIZE][BOARD_SIZE];

    public static void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    public static void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println();
        }
    }

    public static boolean isValidMove(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE && board[row][col] == EMPTY;
    }

    public static boolean checkWin(int row, int col, char stone) {
        // 가로 방향으로 승리 조건 확인
        int count = 0;
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (board[row][j] == stone) {
                count++;
                if (count == 5) return true;
            } else {
                count = 0;
            }
        }
    
        // 세로 방향으로 승리 조건 확인
        count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][col] == stone) {
                count++;
                if (count == 5) return true;
            } else {
                count = 0;
            }
        }
    
        // 대각선 방향으로 승리 조건 확인 (우하향)
        count = 0;
        int i = row; int j = col;
        while (i < BOARD_SIZE && j < BOARD_SIZE) {
            if (board[i][j] == stone) {
                count++;
                if (count == 5) return true;
            } else {
                count = 0;
            }
            i++; // 행 증가
            j++; // 열 증가
        }

        // 대각선 방향으로 승리 조건 확인 (우상향)
        count = 0;
        i = row; j = col;
        while (i >= 0 && j < BOARD_SIZE) {
            if (board[i][j] == stone) {
                count++;
                if (count == 5) return true;
            } else {
                count = 0;
            }
            i--; // 행 감소
            j++; // 열 증가
        }

        // 대각선 방향으로 승리 조건 확인 (좌상향)
        count = 0;
        i = row; j = col;
        while (i >= 0 && j >= 0) {
            if (board[i][j] == stone) {
                count++;
                if (count == 5) return true;
            } else {
                count = 0;
            }
            i--; // 행 감소
            j--; // 열 감소
        }

        // 대각선 방향으로 승리 조건 확인 (좌하향)
        count = 0;
        i = row; j = col;
        while (i < BOARD_SIZE && j >= 0) {
            if (board[i][j] == stone) {
                count++;
                if (count == 5) return true;
            } else {
                count = 0;
            }
            i++; // 행 증가
            j--; // 열 감소
        }
    
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initializeBoard();

        boolean playerTurn = true;
        boolean gameEnd = false;

        while (!gameEnd) {
            if (playerTurn) {
                System.out.println("Player's turn:");
            } else {
                System.out.println("Computer's turn:");
            }

            printBoard();

            int row, col;
            if (playerTurn) {
                System.out.print("Enter row (0-18): ");
                row = scanner.nextInt();
                System.out.print("Enter col (0-18): ");
                col = scanner.nextInt();
            } else {
                // 컴퓨터가 랜덤하게 돌 놓음
                row = (int) (Math.random() * BOARD_SIZE);
                col = (int) (Math.random() * BOARD_SIZE);
            }

            if (isValidMove(row, col)) {
                char stone = playerTurn ? PLAYER_STONE : COMPUTER_STONE;
                board[row][col] = stone;

                if (checkWin(row, col, stone)) {
                    printBoard();
                    System.out.println((playerTurn ? "Player" : "Computer") + " wins!");
                    gameEnd = true;
                } else {
                    playerTurn = !playerTurn; // 턴 바꾸기
                }
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }

        scanner.close();
    }
}
