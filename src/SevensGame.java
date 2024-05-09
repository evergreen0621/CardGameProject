import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

// 게임에 사용되는 플레이어 클래스
abstract class Player {
    protected String name;
    protected ArrayList<Integer> hand; // 손에 있는 카드
    protected static final int MAX_DRAW_COUNT = 7; // 최대 카드 뽑기 횟수

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

    // 플레이어의 턴 수행 메소드(추상 메소드)
    public abstract void playTurn(SevensGame game);
}

// 플레이어 클래스 구현
class HumanPlayer1 extends Player {
    public HumanPlayer1(String name) {
        super(name);
    }

    // 플레이어의 턴 수행
    @Override
    public void playTurn(SevensGame game) {
        System.out.println("\n" + name + "의 턴 ");
        displayHand();

        Scanner sc = new Scanner(System.in);
        System.out.print("낼 카드 선택(또는 1장 뽑기 위해 0 입력) : ");
        int cardToPlay = sc.nextInt();

        if (cardToPlay == 0) {
            game.drawCard(this);
            System.out.println(name + "가 카드를 뽑습니다.");
        } else if (cardToPlay % 7 == 0) {
            if (!hand.contains(cardToPlay)) {
                System.out.println("오류입니다! 손에 없는 카드를 선택하셨습니다.");
            } else {
                removeCard(cardToPlay);
                System.out.println(name + "가 " + cardToPlay + "를 선택하여 제거하였습니다.");
            }
        } else {
            System.out.println("오류입니다! 7의 배수인 카드를 내야 합니다.");
        }

        // 플레이어가 카드를 7번 뽑으면 게임 종료
        if (game.getPlayerDrawCount() == MAX_DRAW_COUNT) {
            game.findWinner();
        }
    }
}

// 컴퓨터 클래스 구현
class ComputerPlayer1 extends Player {
    public ComputerPlayer1(String name) {
        super(name);
    }

    // 컴퓨터의 턴 수행
    @Override
    public void playTurn(SevensGame game) {
        System.out.println("\n" + name + "의 턴");
        displayHand();

        boolean sevenMultipleCardExists = false;
        int cardToPlay = 0;
        for (int card : hand) {
            if (card % 7 == 0) {
                sevenMultipleCardExists = true;
                cardToPlay = card;
                break;
            }
        }

        if (sevenMultipleCardExists) {
            removeCard(cardToPlay);
            System.out.println(name + "가 " + cardToPlay + "를 선택하여 제거하였습니다.");
        } else {
            if (game.getComputerDrawCount() < MAX_DRAW_COUNT) {
                game.drawCard(this);
                System.out.println(name + "가 카드를 뽑습니다.");
                game.incrementComputerDrawCount();
            } else {
                System.out.println(name + "가 최대 드로우 한계에 도달했습니다.");
            }
        }

        // 컴퓨터가 카드를 7번 뽑으면 게임 종료
        if (game.getComputerDrawCount() == MAX_DRAW_COUNT) {
            game.findWinner();
        }
    }
}

// 게임 클래스
public class SevensGame extends GameIntroduction{
    @Override
    public void introGame() {
        System.out.println(" 이 게임은 3레벨의 게임입니다\r\n" + //
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
    protected int playerDrawCount; // 플레이어가 카드를 뽑은 횟수
    protected int computerDrawCount; // 컴퓨터가 카드를 뽑은 횟수
    protected static final int MAX_DRAW_COUNT = 7; // 최대 카드 뽑기 횟수

    // 생성자
    public SevensGame() {
        player = new HumanPlayer1("플레이어");
        computer = new ComputerPlayer1("컴퓨터");
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
            player.playTurn(this);
            if (!isGameOver()) {
                computer.playTurn(this);
            }
        }
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

    // 카드를 뽑음
    public void drawCard(Player player) {
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

        // 플레이어가 카드를 뽑은 횟수 증가
        if (player instanceof HumanPlayer1) {
            playerDrawCount++;
        } else if (player instanceof ComputerPlayer1) {
            computerDrawCount++;
        }
    }

    // 게임 종료 여부 확인
    private boolean isGameOver() {
        return player.getHandSize() == 0 || computer.getHandSize() == 0 || (playerDrawCount >= MAX_DRAW_COUNT && computerDrawCount >= MAX_DRAW_COUNT);
    }

    // 승자 찾기
    public void findWinner() {
        if (player.getHandSize() == 0) {
            System.out.println("최종 승자는 " + player.getName() + "입니다!");
        } else if (computer.getHandSize() == 0) {
            System.out.println("최종 승자는 " + computer.getName() + "입니다!");
        } else if (playerDrawCount >= MAX_DRAW_COUNT && computerDrawCount >= MAX_DRAW_COUNT) {
            // 최대 뽑기 횟수에 도달한 경우, 카드 수가 더 적은 쪽이 승리
            if (player.getHandSize() < computer.getHandSize()) {
                System.out.println("최종 승자는 " + player.getName() + "입니다!");
            } else if (player.getHandSize() > computer.getHandSize()) {
                System.out.println("최종 승자는 " + computer.getName() + "입니다!");
            } else {
                System.out.println("승자는 없습니다!");
            }
        }
    }

    // 플레이어가 카드를 뽑은 횟수 반환
    public int getPlayerDrawCount() {
        return playerDrawCount;
    }

    // 컴퓨터가 카드를 뽑은 횟수 반환+*
    public int getComputerDrawCount() {
        return computerDrawCount;
    }

    // 컴퓨터가 카드를 뽑은 횟수 증가
    public void incrementComputerDrawCount() {
        computerDrawCount++;
    }

    // 메인 메소드
    public static void main(String[] args) {
        SevensGame sevensGame = new SevensGame();
        sevensGame.introGame();
        sevensGame.startGame();
    }
}
