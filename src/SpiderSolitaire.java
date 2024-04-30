import java.util.*;

public class SpiderSolitaire {
    private static final int NUM_BUILDS = 8;
    private static final int NUM_SUITS = 4;
    private static final int NUM_RANKS = 13;

    private List<Stack<Card>> builds; // 8개의 빌드
    private List<Card> deck; // 카드 덱

    // 카드 클래스
    private static class Card {
        private final int rank; // 카드 랭크
        private final int suit; // 카드 슈트

        public Card(int rank, int suit) {
            this.rank = rank;
            this.suit = suit;
        }

        // 카드를 문자열로 반환 (예: "6♠")
        public String toString() {
            String[] suits = {"♠", "♥", "♦", "♣"};
            return rank + suits[suit];
        }
    }

    // 생성자
    public SpiderSolitaire() {
        initializeDeck();
        shuffleDeck();
        initializeBuilds();
    }

    // 카드 덱 초기화
    private void initializeDeck() {
        deck = new ArrayList<>();
        for (int suit = 0; suit < NUM_SUITS; suit++) {
            for (int rank = 1; rank <= NUM_RANKS; rank++) {
                deck.add(new Card(rank, suit));
            }
        }
    }

    // 카드 덱 섞기
    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    // 8개의 빌드 초기화
    private void initializeBuilds() {
        builds = new ArrayList<>();
        for (int i = 0; i < NUM_BUILDS; i++) {
            builds.add(new Stack<>());
        }
        for (int i = 0; i < NUM_BUILDS; i++) {
            for (int j = 0; j < 6; j++) { // 각 빌드에 6장의 카드 배치
                builds.get(i).push(deck.remove(deck.size() - 1));
            }
        }
    }

    // 게임 시작
    public void play() {
        Scanner scanner = new Scanner(System.in);
        while (!isGameOver()) {
            displayBuilds();
            System.out.print("이동할 카드 선택 (예: 2 5): ");
            int fromBuild = scanner.nextInt() - 1;
            int toBuild = scanner.nextInt() - 1;
            moveCard(fromBuild, toBuild);
        }
        System.out.println("게임 승리!");
    }

    // 빌드 출력
    private void displayBuilds() {
        for (int i = 0; i < NUM_BUILDS; i++) {
            System.out.print((i + 1) + ": ");
            Stack<Card> build = builds.get(i);
            if (!build.isEmpty()) {
                for (Card card : build) {
                    System.out.print(card + " ");
                }
            }
            System.out.println();
        }
    }

    // 카드 이동
    private void moveCard(int fromBuild, int toBuild) {
        Stack<Card> from = builds.get(fromBuild);
        Stack<Card> to = builds.get(toBuild);
        if (!from.isEmpty() && !to.isEmpty()) {
            Card fromCard = from.peek();
            Card toCard = to.peek();
            if (fromCard.rank == toCard.rank - 1 && fromCard.suit == toCard.suit) {
                to.push(from.pop());
            } else {
                System.out.println("잘못된 이동입니다!");
            }
        } else {
            System.out.println("잘못된 이동입니다!");
        }
    }

    // 게임 종료 여부 확인
    private boolean isGameOver() {
        for (Stack<Card> build : builds) {
            if (build.size() != NUM_RANKS) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SpiderSolitaire game = new SpiderSolitaire();
        game.play();
    }
}