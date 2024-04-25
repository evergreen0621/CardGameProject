import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OmahaPoker {
    private static final int NUM_CARDS_PER_PLAYER = 4; // 각 플레이어에게 나눠지는 카드 수

    public static void main(String[] args) {
        // 카드 덱 생성
        List<String> deck = createDeck();

        // 카드 섞기
        Collections.shuffle(deck);

        // 플레이어와 컴퓨터에게 카드 나눠주기
        List<String> playerCards = dealCards(deck, NUM_CARDS_PER_PLAYER);
        List<String> computerCards = dealCards(deck, NUM_CARDS_PER_PLAYER);

        // 카드 출력
        System.out.println("플레이어의 카드: " + playerCards);
        System.out.println("컴퓨터의 카드: " + computerCards);

        // 플레이어의 최종 패 선택
        selectPlayerHand(playerCards, deck);

        // 컴퓨터의 최종 패 선택
        selectComputerHand(computerCards, deck);

        // 각 플레이어의 최종 패 분석
        Map<String, String> playerHand = analyzeHand(playerCards);
        Map<String, String> computerHand = analyzeHand(computerCards);

        // 플레이어의 최종 패 출력
        System.out.println("플레이어의 최종 패: " + playerHand.get("type") + " " + playerHand.get("rank"));
        // 컴퓨터의 최종 패 출력
        System.out.println("컴퓨터의 최종 패: " + computerHand.get("type") + " " + computerHand.get("rank"));

        // 승부 결정
        determineWinner(playerHand, computerHand);
    }

    // 카드 덱 생성
    private static List<String> createDeck() {
        List<String> deck = new ArrayList<>();
        String[] suits = {"스페이드", "다이아몬드", "하트", "클로버"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(rank + " " + suit);
            }
        }
        return deck;
    }

    // 카드 나눠주기
    private static List<String> dealCards(List<String> deck, int numCards) {
        List<String> hand = new ArrayList<>();
        for (int i = 0; i < numCards; i++) {
            hand.add(deck.remove(0));
        }
        return hand;
    }

    // 플레이어의 최종 패 선택
    private static void selectPlayerHand(List<String> playerCards, List<String> deck) {
        Scanner sc = new Scanner(System.in);
        System.out.println("플레이어의 카드 중 버릴 카드를 선택하세요 (예: 1 3): ");
        String[] discardIndices = sc.nextLine().split(" ");
        for (String indexStr : discardIndices) {
            int index = Integer.parseInt(indexStr) - 1;
            playerCards.set(index, deck.remove(0));
        }
        System.out.println("플레이어의 최종 패: " + playerCards);
        sc.close();
    }

    // 컴퓨터의 최종 패 선택 (무작위로 선택)
    private static void selectComputerHand(List<String> computerCards, List<String> deck) {
        // 여기서는 간단히 무작위로 2장을 선택하도록 하겠습니다.
        Collections.shuffle(deck);
        computerCards.set(2, deck.remove(0));
        computerCards.set(3, deck.remove(0));
        System.out.println("컴퓨터의 최종 패: " + computerCards);
    }

    // 패 분석
    private static Map<String, String> analyzeHand(List<String> cards) {
        // 패를 분석하여 패의 종류와 강도를 반환합니다.
        // 여기서는 간단히 원페어부터 포카드까지의 패만 고려하도록 하겠습니다.
        // 패의 종류와 강도를 문자열로 반환합니다.
        // 예: "type": "원페어", "rank": "10"

        // 카드 숫자별 개수를 저장하는 Map 생성
        Map<String, Integer> rankCount = new HashMap<>();
        for (String card : cards) {
            String rank = card.split(" ")[0];
            rankCount.put(rank, rankCount.getOrDefault(rank, 0) + 1);
        }

        // 패 분석
        if (rankCount.containsValue(4)) {
            return Map.of("type", "포카드", "rank", Collections.max(rankCount.keySet()));
        } else if (rankCount.containsValue(3) && rankCount.containsValue(2)) {
            return Map.of("type", "풀하우스", "rank", Collections.max(rankCount.keySet()));
        } else if (isFlush(cards)) {
            return Map.of("type", "플러시", "rank", Collections.max(rankCount.keySet()));
        } else if (isStraight(cards)) {
            return Map.of("type", "스트레이트", "rank", Collections.max(rankCount.keySet()));
        } else if (rankCount.containsValue(3)) {
            return Map.of("type", "트리플", "rank", Collections.max(rankCount.keySet()));
        } else if (Collections.frequency(rankCount.values(), 2) == 2) {
            return Map.of("type", "투페어", "rank", getTwoPairRank(rankCount));
        } else if (Collections.frequency(rankCount.values(), 2) == 1) {
            return Map.of("type", "원페어", "rank", getOnePairRank(rankCount));
        } else {
            return Map.of("type", "하이카드", "rank", getHighCardRank(cards));
        }
    }

    // 투페어의 랭크 반환
    private static String getTwoPairRank(Map<String, Integer> rankCount) {
        List<String> pairs = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 2) {
                pairs.add(entry.getKey());
            }
        }
        Collections.sort(pairs);
        return pairs.get(1); // 더 높은 페어의 랭크를 반환
    }

    // 원페어의 랭크 반환
    private static String getOnePairRank(Map<String, Integer> rankCount) {
        for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 2) {
                return entry.getKey(); // 페어의 랭크를 반환
            }
        }
        return ""; // 예외 처리
    }


    // 하이카드의 랭크 반환
    private static String getHighCardRank(List<String> cards) {
        List<Integer> ranks = new ArrayList<>();
        for (String card : cards) {
            String rank = card.split(" ")[0];
            ranks.add(getRankValue(rank));
        }
        Collections.sort(ranks);
        return Integer.toString(ranks.get(ranks.size() - 1)); // 가장 높은 카드의 랭크를 반환
    }

    // 플러시 확인
    private static boolean isFlush(List<String> cards) {
        String suit = cards.get(0).split(" ")[1];
        return cards.stream().allMatch(card -> card.split(" ")[1].equals(suit));
    }

    // 스트레이트 확인
    private static boolean isStraight(List<String> cards) {
        List<Integer> ranks = new ArrayList<>();
        for (String card : cards) {
            String rank = card.split(" ")[0];
            ranks.add(getRankValue(rank));
        }
        Collections.sort(ranks);
        return ranks.get(0) + 1 == ranks.get(1) &&
                ranks.get(1) + 1 == ranks.get(2) &&
                ranks.get(2) + 1 == ranks.get(3);
    }

    // 랭크 값 반환
    private static int getRankValue(String rank) {
        switch (rank) {
            case "A": return 14;
            case "K": return 13;
            case "Q": return 12;
            case "J": return 11;
            default: return Integer.parseInt(rank);
        }
    }

    // 승부 결정
    private static void determineWinner(Map<String, String> playerHand, Map<String, String> computerHand) {
        // 패의 종류와 강도를 비교하여 승자를 결정합니다.
        // 각 패의 종류가 같은 경우에는 가장 높은 카드 숫자를 기준으로 승부를 결정합니다.
        // 예: "원페어 10" vs "원페어 9" 일 때 "원페어 10"이 승리합니다.
        // "하이카드"의 경우 가장 높은 카드의 숫자를 비교합니다.

        String[] playerHandType = playerHand.keySet().toArray(new String[0]);
        String[] computerHandType = computerHand.keySet().toArray(new String[0]);

        // 패의 종류 비교
        int compare = compareHandTypes(playerHandType[0], computerHandType[0]);
        if (compare > 0) {
            System.out.println("플레이어가 승리했습니다!");
        } else if (compare < 0) {
            System.out.println("컴퓨터가 승리했습니다!");
        } else { // 패의 종류가 같을 경우 강도 비교
            int playerRank = getRankValue(playerHand.get(playerHandType[0]).split(" ")[0]);
            int computerRank = getRankValue(computerHand.get(computerHandType[0]).split(" ")[0]);
            if (playerRank > computerRank) {
                System.out.println("플레이어가 승리했습니다!");
            } else if (playerRank < computerRank) {
                System.out.println("컴퓨터가 승리했습니다!");
            } else {
                System.out.println("무승부입니다!");
            }
        }
    }

    // 패의 종류 비교
    private static int compareHandTypes(String handType1, String handType2) {
        List<String> handTypes = List.of("포카드", "풀하우스", "플러시", "스트레이트", "트리플", "투페어", "원페어", "하이카드");
        return handTypes.indexOf(handType1) - handTypes.indexOf(handType2);
    }
}
