import java.util.*;

/*게임 규칙   (아직 미완성)  - 천성윤 작업 중
 플레이어와 컴퓨터는 각각 7장의 카드를 받고, 
각 턴마다 플레이어는 자신의 카드를 선택하거나 새로운 카드를 뽑아야 하며, 
 컴퓨터는 자동으로 카드를 선택하거나 새로운 카드를 뽑습니다. 
 게임은 두 플레이어 중 하나가 모든 카드를 소진하거나 최대 뽑기 횟수에 도달할 때 종료됩니다.
 */
// 플레이어 클래스
class Player {
    private String name;
    private ArrayList<Integer> hand; // 손에 있는 카드

    // 생성자
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    // 이름 반환
    public String getName() {
        return name;
    }

    // 손에 카드 추가
    public void addCard(int card) {
        hand.add(card);
    }

    // 손에 있는 카드 출력
    public void displayHand() {
        System.out.print(name + "'s hand: ");
        for (int card : hand) {
            System.out.print(card + " ");
        }
        System.out.println();
    }

    // 손에 있는 특정 카드 제거
    public void removeCard(int card) {
        hand.remove(Integer.valueOf(card));
    }

    // 손에 있는 카드 수 반환
    public int getHandSize() {
        return hand.size();
    }

    // 손에 있는 모든 카드 반환
    public ArrayList<Integer> getHand() {
        return hand;
    }
}

// 게임 클래스
public class SevensGame {
    private Player player; // 플레이어
    private Player computer; // 컴퓨터
    private ArrayList<Integer> center;  // 중앙에 놓인 카드
    private int playerDrawCount; // 플레이어가 카드를 뽑은 횟수
    private int computerDrawCount; // 컴퓨터가 카드를 뽑은 횟수
    private final int MAX_DRAW_COUNT = 10; // 최대 카드 뽑기 횟수

    // 생성자
    public SevensGame() {
        player = new Player("Player");
        computer = new Player("Computer");
        center = new ArrayList<>();
        playerDrawCount = 0;
        computerDrawCount = 0;

        // 카드를 중앙에 놓음 (시작 카드)   
        center.add(7);
    }

    // 게임 시작
    public void startGame() {
        // 카드를 나눠줌
        dealCards();

        // 게임이 끝날 때까지 반복
        while (!isGameOver()) {
            // 각 플레이어의 턴 수행
            playerTurn();
            if (!isGameOver()) {
                computerTurn();
            }
        }

        // 게임 종료 후 승자를 출력
        Player winner = findWinner();
        System.out.println("Game over! " + winner.getName() + " wins!");
    }

    // 카드를 나눠줌
    private void dealCards() {
        ArrayList<Integer> deck = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            deck.add(i);
        }
        // 7의 배수인 카드를 더 추가합니다.
        for (int i = 0; i < 7; i++) {
            deck.add(7); // 7의 배수 카드 추가
        }
        Collections.shuffle(deck);

        for (int i = 0; i < 7; i++) {
            player.addCard(deck.remove(0));
            computer.addCard(deck.remove(0));
        }
    }


    // 플레이어의 턴
private void playerTurn() {
    System.out.println("\nPlayer's turn");
    player.displayHand();
    computer.displayHand(); // 컴퓨터가 가지고 있는 카드 출력

    Scanner scanner = new Scanner(System.in);
    System.out.print("Choose a card to play (or 0 to draw): ");
    int cardToPlay = scanner.nextInt();

    if (cardToPlay == 0) {
        if (playerDrawCount < MAX_DRAW_COUNT) {
            drawCard(player);
            System.out.println("Player draws a card");
            playerDrawCount++;
        } else {
            System.out.println("You have reached the maximum draw limit.");
        }
    } else if (cardToPlay % 7 == 0) {
        if (center.isEmpty() || cardToPlay > center.get(center.size() - 1)) {
            player.removeCard(cardToPlay);
            center.add(cardToPlay);
        } else {
            System.out.println("Invalid move! You can't play this card.");
            playerTurn();
        }
    } else {
        System.out.println("Invalid move! You must play a card that is a multiple of 7.");
        playerTurn();
    }
}

// 컴퓨터의 턴
private void computerTurn() {
    System.out.println("\nComputer's turn");
    displayComputerHand(); // 컴퓨터가 가지고 있는 카드 출력
    Random rand = new Random();
    int cardToPlay = computer.getHand().get(rand.nextInt(computer.getHandSize()));

    if (cardToPlay % 7 == 0 && (center.isEmpty() || cardToPlay > center.get(center.size() - 1))) {
        computer.removeCard(cardToPlay);
        center.add(cardToPlay);
        System.out.println("Computer plays a " + cardToPlay);
    } else {
        if (computerDrawCount < MAX_DRAW_COUNT) {
            drawCard(computer);
            System.out.println("Computer draws a card.");
            computerDrawCount++;
        } else {
            System.out.println("Computer has reached the maximum draw limit.");
        }
    }
}

// 컴퓨터가 가지고 있는 카드 출력
private void displayComputerHand() {
    System.out.print("Computer's hand: ");
    for (int card : computer.getHand()) {
        System.out.print(card + " ");
    }
    System.out.println();
}


    // 카드를 뽑음
    private void drawCard(Player player) {
        ArrayList<Integer> deck = new ArrayList<>();
        for (int i = 1; i <= 52; i++) {
            if (!center.contains(i) && !player.getHand().contains(i)) {
                deck.add(i);
            }
        }

        // 덱이 비어있는지 확인
        if (deck.isEmpty()) {
            System.out.println("Deck is empty! No more cards to draw.");
            return;
        }

        // 랜덤하게 카드를 뽑음
        Random rand = new Random();
        int drawnCardIndex = rand.nextInt(deck.size());
        int drawnCard = deck.get(drawnCardIndex);

        // 뽑은 카드를 플레이어의 손에 추가
        player.addCard(drawnCard);

        // 중앙에 있는 카드와 중복되는 경우 제거
        if (center.contains(drawnCard)) {
            center.remove(Integer.valueOf(drawnCard));
        }
    }

    // 게임 종료 여부 확인
private boolean isGameOver() {
    return player.getHandSize() == 0 || computer.getHandSize() == 0 || (playerDrawCount >= MAX_DRAW_COUNT && computerDrawCount >= MAX_DRAW_COUNT);
}

// 승자 찾기
private Player findWinner() {
    if (player.getHandSize() == 0) {
        return player;
    } else if (computer.getHandSize() == 0) {
        return computer;
    } else if (playerDrawCount >= MAX_DRAW_COUNT && computerDrawCount >= MAX_DRAW_COUNT) {
        // 최대 뽑기 횟수에 도달한 경우, 카드 수가 더 적은 쪽이 승리
        if (player.getHandSize() < computer.getHandSize()) {
            return player;
        } else if (player.getHandSize() > computer.getHandSize()) {
            return computer;
        } else {
            return null; // 카드 수가 같을 경우 무승부
        }
    } else {
        return null; // 게임이 아직 종료되지 않았을 경우
    }
}


    public static void main(String[] args) {
        SevensGame game = new SevensGame();
        game.startGame();
    }
}