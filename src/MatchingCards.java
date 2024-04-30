/*
 * 
위 코드는 주어진 4x4 크기의 카드 게임 보드에서 특정 위치에서 시작하여 
모든 카드를 짝지어서 없애기 위해 필요한 최소 이동 횟수를 계산하는 프로그램입니다. 이 게임의 규칙은 다음과 같습니다:

게임 보드는 4x4 크기로 이루어져 있습니다.
각 카드는 1부터 시작하여 연속된 정수로 표현됩니다. 0은 카드가 없음을 의미합니다.
게임의 시작 위치인 (r, c) 좌표에서부터 시작합니다.
짝이 맞는 두 카드는 서로 같은 숫자를 가지며, 이러한 카드는 짝지어서 제거됩니다.
이동할 수 있는 방향은 상하좌우로 이웃한 칸으로 이동할 수 있습니다.
이동할 때는 카드가 있는 칸으로만 이동할 수 있으며, 빈 칸으로는 이동할 수 없습니다.
Ctrl + 방향키를 눌러 이동할 경우, 해당 방향으로 카드가 나타날 때까지 계속 이동합니다.
짝이 맞는 카드를 찾을 때까지 이동하는 동안의 이동 횟수를 최소화하여 짝을 찾아야 합니다.
모든 짝이 맞추어지면 게임이 종료됩니다.
이 프로그램은 시작 위치 (r, c)에서부터 모든 카드를 짝지어서 없애기 위한 최소 이동 횟수를 계산하고 출력합니다.
 */


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Pair {
    int x;
    int y;
    int move;

    public Pair(int x, int y, int move) {
        this.x = x;
        this.y = y;
        this.move = move;
    }
}

public class MatchingCards {
    static List<String> orders;
    static int[] dx = {1, -1, 0, 0};
    static int[] dy = {0, 0, 1, -1};

    public int solution(int[][] board, int r, int c) {
        int cardNum = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != 0) {
                    cardNum++;
                }
            }
        }
        cardNum /= 2;

        int[] card = new int[cardNum];
        for (int i = 0; i < cardNum; i++) {
            card[i] = i + 1;
        }

        orders = new ArrayList<>();
        permutation("", 0, card);

        int min = Integer.MAX_VALUE;
        for (String comb : orders) {
            String[] order = comb.split("");

            int totalMove = 0;
            int[] pos = new int[2];
            pos[0] = r;
            pos[1] = c;

            int[][] copyBoard = new int[4][4];
            for (int i = 0; i < 4; i++) {
                System.arraycopy(board[i], 0, copyBoard[i], 0, 4);
            }

            for (String targetCard : order) {
                int cardNumInt = Integer.parseInt(targetCard);

                totalMove += cardSearch(pos, cardNumInt, copyBoard);
                copyBoard[pos[0]][pos[1]] = 0;

                totalMove++;
            }

            min = Math.min(min, totalMove);
        }

        return min;
    }

    static void permutation(String comb, int depth, int[] card) {
        if (card.length == depth) {
            orders.add(comb);
            return;
        }

        for (int i = 0; i < card.length; i++) {
            int num = card[i];
            if (!comb.contains("" + num)) {
                permutation(comb + num, depth + 1, card);
            }
        }
    }

    static int cardSearch(int[] pos, int targetCard, int[][] copyBoard) {
        Queue<Pair> queue = new LinkedList<>();
        boolean[][] check = new boolean[4][4];
        int x = pos[0];
        int y = pos[1];

        check[x][y] = true;
        queue.add(new Pair(x, y, 0));
        while (!queue.isEmpty()) {
            Pair next = queue.poll();
            int px = next.x;
            int py = next.y;
            int move = next.move;

            if (copyBoard[next.x][next.y] == targetCard) {
                System.out.println("[" + targetCard + "] find! " + next.x + "," + next.y + ":" + move);
                pos[0] = next.x;
                pos[1] = next.y;
                return move;
            }

            for (int i = 0; i < 4; i++) {
                int nx = px + dx[i];
                int ny = py + dy[i];

                if (nx < 0 || ny < 0 || nx > 3 || ny > 3) continue;
                if (check[nx][ny]) continue;

                check[nx][ny] = true;
                queue.add(new Pair(nx, ny, move + 1));
            }

            for (int i = 0; i < 4; i++) {
                Pair res = checkRoute(px, py, i, copyBoard);
                int nx = res.x, ny = res.y;

                if (nx == x && ny == y) continue;
                if (check[nx][ny]) continue;

                check[nx][ny] = true;
                queue.add(new Pair(nx, ny, move + 1));
            }
        }
        return 0;
    }

    static Pair checkRoute(int x, int y, int direction, int[][] copyBoard) {
        x += dx[direction];
        y += dy[direction];

        while (x >= 0 && x < 4 && y >= 0 && y < 4) {
            if (copyBoard[x][y] != 0) return new Pair(x, y, 0);

            x += dx[direction];
            y += dy[direction];
        }

        return new Pair(x - dx[direction], y - dy[direction], 0);
    }
    
    public static void main(String[] args) {
        int[][] board = {
            {1, 0, 1, 0},
            {2, 2, 2, 0},
            {1, 0, 1, 0},
            {0, 0, 0, 0}
        };
        int r = 1;
        int c = 0;

        MatchingCards matchingCards = new MatchingCards();
        int minMoves = matchingCards.solution(board, r, c);
        System.out.println("Minimum moves required: " + minMoves);
    }
}
