import java.util.*;
// 현재 컴퓨터는 자동으로 7의 배수 카드를 버리지 않음
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
        System.out.print(name + "의 덱 : ");
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
public class SevensGame extends GameIntroduction{
    @Override
    public void introGame() {
        System.out.println("이 게임은 3레벨의 게임입니다.\r\n" + //
                        "\r\n" + //
                        "- 'Sevensgame'은 '7의 배수'를 중심으로 플레이어와 컴퓨터가 카드를 선택하고 매 턴마다 카드를 뽑는 게임입니다.\r\n" + //
                        "\r\n" + //
                        "1. 게임이 시작되면 7장의 카드가 주어집니다.\r\n" + //
                        "2. 플레이어는 턴마다 7의 배수인 카드를 선택하여 제거를 할 수 있습니다.\r\n" + //
                        "3. 7의 배수가 없는 경우 0번 눌러 카드를 뽑을 수 있습니다.\r\n" + //
                        "4. 게임이 종료되었을 때에 카드의 수로 승패를 가릅니다\r\n" + //
                        "\r\n" + //
                        "게임을 시작하겠습니다. ");
    }
    private Player player; // 플레이어
    private Player computer; // 컴퓨터
    private int playerDrawCount; // 플레이어가 카드를 뽑은 횟수
    private int computerDrawCount; // 컴퓨터가 카드를 뽑은 횟수
    private final int MAX_DRAW_COUNT = 10; // 최대 카드 뽑기 횟수

    // 생성자
    public SevensGame() {
        player = new Player("플레이어");
        computer = new Player("컴퓨터");
        playerDrawCount = 0;
        computerDrawCount = 0;
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
        System.out.println("게임 종료!");
        Player winner = findWinner();
    }

    // 카드를 나눠줌
    private void dealCards() {
        ArrayList<Integer> deck = new ArrayList<>();
        for (int i = 6; i <= 50; i++) {
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
        System.out.println("\n플레이어의 턴 ");
        player.displayHand();

        Scanner scanner = new Scanner(System.in);
        System.out.print("낼 카드 선택(또는 1장 뽑기 위해 0 입력) : ");
        int cardToPlay = scanner.nextInt();

        if (cardToPlay == 0) {
            if (playerDrawCount < MAX_DRAW_COUNT) {
                drawCard(player);
                System.out.println("플레이어가 카드를 뽑습니다.");
                playerDrawCount++;
            } else {
                System.out.println("최대 드로우 한계에 도달했습니다.");
            }
        } else if (cardToPlay % 7 == 0) {
            if (!player.getHand().contains(cardToPlay)) {
                System.out.println("오류입니다! 손에 없는 카드를 선택하셨습니다.");
            } else{
                player.removeCard(cardToPlay);
                System.out.println("플레이어가 " + cardToPlay + "를 선택하여 제거하였습니다.");
            }
        } else {
            System.out.println("오류입니다! 7의 배수인 카드를 내야 합니다.");
        }
    }

    // 컴퓨터의 턴
    private void computerTurn() {
        System.out.println("\n컴퓨터의 턴");
        displayComputerHand(); // 컴퓨터가 가지고 있는 카드 출력
        Random rand = new Random();

        // 컴퓨터가 가지고 있는 카드 중 7의 배수인 카드를 찾음
        boolean sevenMultipleCardExists = false;
        int cardToPlay = 0;
        for (int card : computer.getHand()) {
            if (card % 7 == 0) {
                sevenMultipleCardExists = true;
                cardToPlay = card;
                break;
            }
        }

        if (sevenMultipleCardExists) { // 7의 배수인 카드가 있을 경우
            computer.removeCard(cardToPlay);
            System.out.println("컴퓨터가 " + cardToPlay + "를 선택하여 제거하였습니다.");
        } else { // 7의 배수인 카드가 없을 경우
            // 카드를 뽑음
            if (computerDrawCount < MAX_DRAW_COUNT) {
                drawCard(computer);
                System.out.println("컴퓨터가 카드를 뽑습니다.");
                computerDrawCount++;
            } else {
                System.out.println("컴퓨터가 최대 드로우 한계에 도달했습니다.");
            }
        }
    }


    // 컴퓨터가 가지고 있는 카드 출력
    private void displayComputerHand() {
        System.out.print("컴퓨터의 덱 : ");
        for (int card : computer.getHand()) {
            System.out.print(card + " ");
        }
        System.out.println();
    }

    // 카드를 뽑음
    private void drawCard(Player player) {
        ArrayList<Integer> deck = new ArrayList<>();
        for (int i = 1; i <= 52; i++) {
            if (!player.getHand().contains(i)) {
                deck.add(i);
            }
        }

        // 덱이 비어있는지 확인
        if (deck.isEmpty()) {
            System.out.println("덱이 비었습니다! 드로우할 카드가 없습니다.");
            return;
        }

        // 랜덤하게 카드를 뽑음
        Random rand = new Random();
        int drawnCardIndex = rand.nextInt(deck.size());
        int drawnCard = deck.get(drawnCardIndex);

        // 뽑은 카드를 플레이어의 손에 추가
        player.addCard(drawnCard);
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
                System.out.println("최종 승자는 당신입니다!");
                WinLose.winlose = 1;
                return player;
            } else if (player.getHandSize() > computer.getHandSize()) {
                System.out.println("승자는 컴퓨터입니다!");
                WinLose.winlose = 0;
                return computer;
            } else {
                System.out.println("승자는 없습니다!");
                WinLose.winlose = 2;
                return player; // 카드 수가 같을 경우 무승부
            }
        } else {
            return null; // 게임이 아직 종료되지 않았을 경우
        }
    }

    public static void main(String[] args) {
        SevensGame sevensGame = new SevensGame();
        sevensGame.introGame();
        SevensGame game = new SevensGame();
        game.startGame();
    }
}
