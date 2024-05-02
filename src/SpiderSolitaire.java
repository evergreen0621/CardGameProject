import java.util.*;

public class SpiderSolitaire {
    private static final int NUM_BUILDS = 5; // 총 5줄
    private static final int[] CARDS_PER_LINE = {6, 6, 5, 5, 5}; // 각 줄별 카드 수

    private static final int NUM_SUITS = 2; // 두 개의 슈트만 사용 (하트와 클로버)
    private static final int NUM_RANKS = 13;

    private List<Stack<Card>> builds; // 10줄의 빌드
    private List<Card> deck; // 카드 덱

    // 카드 클래스
    private static class Card {
        private final int rank; // 카드 랭크
        private final int suit; // 카드 슈트

        public Card(int rank, int suit) {
            this.rank = rank;
            this.suit = suit;
        }

        // 카드를 문자열로 반환 (예: "6♣")
        public String toString() {
            String[] suits = {"♥ |", "♣ |"}; // 하트와 클로버만 사용
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
            for (int i = 0; i < NUM_BUILDS; i++) {
                for (int j = 0; j < CARDS_PER_LINE[i]; j++) {
                    deck.add(new Card(j + 1, suit));
                }
            }
        }
    }

    // 카드 덱 섞기
    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    // 10줄의 빌드 초기화
    private void initializeBuilds() {
        builds = new ArrayList<>();
        for (int i = 0; i < NUM_BUILDS; i++) {
            builds.add(new Stack<>());
        }
        int totalCards = 0;
        for (int i = 0; i < NUM_BUILDS; i++) {
            for (int j = 0; j < CARDS_PER_LINE[i]; j++) {
                if (totalCards >= deck.size()) {
                    break;
                }
                builds.get(i).push(deck.remove(deck.size() - 1));
                totalCards++;
            }
        }
    }

    // 게임 시작
    public void play() {
        Scanner scanner = new Scanner(System.in);
        while (!isGameOver()) {
            displayBuilds();
            System.out.println("\n카드 추가: 'a', 종료: 'q'");
            System.out.println("ex. 2번째 줄에서 5번째 줄로 2장 이동 : 2 5 3");
            System.out.print("이동할 카드를 입력하세요. : ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) {
                break;
            } else if (input.equalsIgnoreCase("a")) {
                addRandomCard();
            } else {
                String[] tokens = input.split(" ");
                if (tokens.length == 3) {
                    try {
                        int fromBuild = Integer.parseInt(tokens[0]) - 1;
                        int toBuild = Integer.parseInt(tokens[1]) - 1;
                        int numCards = Integer.parseInt(tokens[2]);
                        moveCard(fromBuild, toBuild, numCards);
                    } catch (NumberFormatException e) {
                        System.out.println("잘못된 입력입니다!");
                    }
                } else {
                    System.out.println("잘못된 입력입니다!");
                }
            }
        }
        System.out.println("게임 종료!");
    }

    // 빌드 출력
    private void displayBuilds() {
        for (int i = 0; i < NUM_BUILDS; i++) {
            System.out.print((i + 1) + ": ");
            Stack<Card> build = builds.get(i);
            for (Card card : build) {
                System.out.print(card + " ");
            }
            System.out.println();
        }
    }

    private void moveCard(int fromBuild, int toBuild, int numCards) {
        if (fromBuild < 0 || fromBuild >= NUM_BUILDS || toBuild < 0 || toBuild >= NUM_BUILDS) {
            System.out.println("잘못된 빌드 번호입니다!");
            return;
        }
        Stack<Card> from = builds.get(fromBuild);
        Stack<Card> to = builds.get(toBuild);
        if (from.isEmpty()) {
            System.out.println("이동할 카드가 없습니다!");
            return;
        }
        if (numCards <= 0 || numCards > from.size()) {
            System.out.println("잘못된 카드 개수입니다!");
            return;
        }
        Card[] cardsToMove = new Card[numCards]; // 옮길 카드들을 저장할 배열 생성
        // 옮길 카드들을 역순으로 배열에 저장
        for (int i = numCards - 1; i >= 0; i--) {
            cardsToMove[numCards - 1 - i] = from.get(from.size() - 1 - i);
        }
        Card toCard = to.isEmpty() ? null : to.peek();
        if (toCard == null && cardsToMove[0].rank == NUM_RANKS) {
            System.out.println("이동할 수 없는 위치입니다!");
            return;
        }
        if (toCard != null && (cardsToMove[0].rank != toCard.rank - 1 || cardsToMove[0].suit != toCard.suit)) {
            System.out.println("잘못된 이동입니다!");
            return;
        }
        for (int i = 0; i < numCards; i++) {
            to.push(cardsToMove[i]); // 역순으로 카드를 옮김
            from.pop(); // 옮겨진 카드는 원래 위치에서 제거
        }
    }
    
    
    
    // 랜덤 카드 추가
    private void addRandomCard() {
        if (deck.isEmpty()) {
            System.out.println("더 이상 추가할 카드가 없습니다!");
            return;
        }
        for (Stack<Card> build : builds) {
            if (!deck.isEmpty()) {
                Card randomCard = deck.remove(deck.size() - 1);
                build.push(randomCard);
            } else {
                System.out.println("더 이상 추가할 카드가 없습니다!");
                return;
            }
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
