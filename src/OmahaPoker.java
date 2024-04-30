import java.util.*;

public class OmahaPoker {
    private Deck deck;
    private Player1 humanPlayer;
    private Player1 computerPlayer;
    private List<Card> communityCards;

    public OmahaPoker() {
        deck = new Deck();
        humanPlayer = new Player1("플레이어");
        computerPlayer = new Player1("컴퓨터");
        communityCards = new ArrayList<>();
    }

    public void playGame() {
        deck.shuffle();
        dealCards();
        showHands();
        // 버릴 카드 선택 및 다시 뽑기
        discardAndRedraw(humanPlayer);
        // 컴퓨터가 자동으로 카드를 교환합니다.
        computerAutomaticDraw();
        determineWinner();
    }

    private void dealCards() {
        humanPlayer.addCard(deck.drawCard());
        humanPlayer.addCard(deck.drawCard());
        computerPlayer.addCard(deck.drawCard());
        computerPlayer.addCard(deck.drawCard());
        for (int i = 0; i < 5; i++) {
            communityCards.add(deck.drawCard());
        }
    }

    private void showHands() {
        System.out.println("플레이어의 패: " + humanPlayer.getHand());
        System.out.println("공개 카드: " + communityCards);
    }

    private void discardAndRedraw(Player1 player) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("버릴 카드를 골라주세요. (e.g., 1 2):");
        String input = scanner.nextLine();
        String[] indices = input.split(" ");
        List<Integer> discardIndices = new ArrayList<>();
        for (String index : indices) {
            discardIndices.add(Integer.parseInt(index) - 1);
        }

        // Remove discarded cards from hand
        Collections.sort(discardIndices, Collections.reverseOrder());
        for (int index : discardIndices) {
            player.discardCard(index);
        }

        // Draw new cards
        for (int i = 0; i < discardIndices.size(); i++) {
            player.addCard(deck.drawCard());
        }
    }

    private void computerAutomaticDraw() {
        List<Card> currentHand = computerPlayer.getHand();
        List<Card> newHand = new ArrayList<>(currentHand);

        // 컴퓨터의 현재 패를 평가합니다.
        HandRank currentRank = evaluateHand(currentHand, communityCards);

        // 각 카드를 버려보고 새 카드를 뽑아서 평가합니다.
        for (int i = 0; i < currentHand.size(); i++) {
            List<Card> testHand = new ArrayList<>(currentHand);
            testHand.remove(i); // 현재 카드를 제거합니다.
            testHand.add(deck.drawCard()); // 새 카드를 뽑습니다.

            HandRank testRank = evaluateHand(testHand, communityCards);

            // 새 패가 더 좋은 경우에만 교환합니다.
            if (testRank.compareTo(currentRank) > 0) {
                newHand = testHand;
                currentRank = testRank;
            }
        }

        // 새로운 패로 업데이트합니다.
        computerPlayer.setHand(newHand);
    }

    private void determineWinner() {
        HandRank humanRank = evaluateHand(humanPlayer.getHand(), communityCards);
        HandRank computerRank = evaluateHand(computerPlayer.getHand(), communityCards);

        System.out.println("플레이어의 패: " + humanRank + " - " + humanPlayer.getHand() + " + " + communityCards);
        System.out.println("컴퓨터의 패: " + computerRank + " - " + computerPlayer.getHand() + " + " + communityCards);

        if (humanRank.compareTo(computerRank) > 0) {
            System.out.println("당신의 승리입니다!");
        } else if (humanRank.compareTo(computerRank) < 0) {
            System.out.println("컴퓨터의 승리입니다!");
        } else {
            int result = compareHands(humanPlayer.getHand(), computerPlayer.getHand());
            if (result > 0) {
                System.out.println("당신의 승리입니다!");
            } else if (result < 0) {
                System.out.println("컴퓨터의 승리입니다!");
            } else {
                System.out.println("무승부입니다!");
            }
        }
    }

    private int compareHands(List<Card> hand1, List<Card> hand2) {
        Collections.sort(hand1, Comparator.comparing(Card::getRank).reversed());
        Collections.sort(hand2, Comparator.comparing(Card::getRank).reversed());
        
        for (int i = 0; i < hand1.size(); i++) {
            Rank rank1 = hand1.get(i).getRank();
            Rank rank2 = hand2.get(i).getRank();
            if (rank1.compareTo(rank2) != 0) {
                return rank1.compareTo(rank2);
            }
        }
        return 0;
    }

    private HandRank evaluateHand(List<Card> hand, List<Card> communityCards) {
        List<Card> allCards = new ArrayList<>(hand);
        allCards.addAll(communityCards);
        Collections.sort(allCards, Comparator.comparing(Card::getRank));

        if (hasRoyalFlush(allCards)) return HandRank.ROYAL_FLUSH;
        if (hasStraightFlush(allCards)) return HandRank.STRAIGHT_FLUSH;
        if (hasFourOfAKind(allCards)) return HandRank.FOUR_OF_A_KIND;
        if (hasFullHouse(allCards)) return HandRank.FULL_HOUSE;
        if (hasFlush(allCards)) return HandRank.FLUSH;
        if (hasStraight(allCards)) return HandRank.STRAIGHT;
        if (hasThreeOfAKind(allCards)) return HandRank.THREE_OF_A_KIND;
        if (hasTwoPair(allCards)) return HandRank.TWO_PAIR;
        if (hasOnePair(allCards)) return HandRank.ONE_PAIR;
        return HandRank.HIGH_CARD;
    }

    private boolean hasRoyalFlush(List<Card> allCards) {
        return hasStraightFlush(allCards) && hasAce(allCards);
    }

    private boolean hasStraightFlush(List<Card> allCards) {
        for (int i = 0; i <= allCards.size() - 5; i++) {
            if (isFlush(allCards.subList(i, i + 5)) && isStraight(allCards.subList(i, i + 5))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasFourOfAKind(List<Card> allCards) {
        Map<Rank, Integer> rankCount = getRankCount(allCards);
        return rankCount.values().stream().anyMatch(count -> count == 4);
    }

    private boolean hasFullHouse(List<Card> allCards) {
        Map<Rank, Integer> rankCount = getRankCount(allCards);
        boolean hasThree = false;
        boolean hasPair = false;
        for (int count : rankCount.values()) {
            if (count == 3) hasThree = true;
            if (count == 2) hasPair = true;
        }
        return hasThree && hasPair;
    }

    private boolean hasFlush(List<Card> allCards) {
        Map<Suit, Integer> suitCount = getSuitCount(allCards);
        return suitCount.values().stream().anyMatch(count -> count >= 5);
    }

    private boolean hasStraight(List<Card> allCards) {
        for (int i = 0; i <= allCards.size() - 5; i++) {
            if (isStraight(allCards.subList(i, i + 5))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasThreeOfAKind(List<Card> allCards) {
        Map<Rank, Integer> rankCount = getRankCount(allCards);
        return rankCount.values().stream().anyMatch(count -> count == 3);
    }

    private boolean hasTwoPair(List<Card> allCards) {
        Map<Rank, Integer> rankCount = getRankCount(allCards);
        long pairCount = rankCount.values().stream().filter(count -> count == 2).count();
        return pairCount >= 2;
    }

    private boolean hasOnePair(List<Card> allCards) {
        Map<Rank, Integer> rankCount = getRankCount(allCards);
        return rankCount.values().stream().anyMatch(count -> count == 2);
    }

    private Map<Rank, Integer> getRankCount(List<Card> cards) {
        Map<Rank, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            Rank rank = card.getRank();
            rankCount.put(rank, rankCount.getOrDefault(rank, 0) + 1);
        }
        return rankCount;
    }

    private Map<Suit, Integer> getSuitCount(List<Card> cards) {
        Map<Suit, Integer> suitCount = new HashMap<>();
        for (Card card : cards) {
            Suit suit = card.getSuit();
            suitCount.put(suit, suitCount.getOrDefault(suit, 0) + 1);
        }
        return suitCount;
    }

    private boolean isFlush(List<Card> cards) {
        Suit suit = cards.get(0).getSuit();
        return cards.stream().allMatch(card -> card.getSuit() == suit);
    }

    private boolean isStraight(List<Card> cards) {
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).getRank().ordinal() + 1 != cards.get(i + 1).getRank().ordinal()) {
                return false;
            }
        }
        return true;
    }

    private boolean hasAce(List<Card> cards) {
        return cards.stream().anyMatch(card -> card.getRank() == Rank.ACE);
    }

    public static void main(String[] args) throws Exception{
        OmahaPoker game = new OmahaPoker();
        game.playGame();
    }
}

class Player1 {
    private String name;
    private List<Card> hand;

    public Player1(String name) {
        this.name = name;
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void discardCard(int index) {
        hand.remove(index);
    }

    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public void setHand(List<Card> newHand) {
        hand = newHand;
    }
}

class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.remove(0);
    }
}

class Card {
    private Rank rank;
    private Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

enum Rank {
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
}

enum Suit {
    HEARTS, DIAMONDS, CLUBS, SPADES
}

enum HandRank {
    HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH
}