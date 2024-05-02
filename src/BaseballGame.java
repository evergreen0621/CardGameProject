import java.util.*;

public class BaseballGame {
    private static final int NUM_DIGITS = 3; // 야구 숫자는 3자리
    private static final int MAX_ATTEMPTS = 7; // 최대 시도 횟수

    public static void main(String[] args) {
        System.out.println("Welcome to Baseball Card Game!\n");

        // 컴퓨터가 생각하는 랜덤한 숫자 생성
        int[] answer = generateRandomNumber();

        Scanner scanner = new Scanner(System.in);

        // 사용자가 정답을 맞출 때까지 또는 최대 시도 횟수를 초과할 때까지 게임 진행
        int attempts = 0;
        boolean gameWon = false;
        while (!gameWon && attempts < MAX_ATTEMPTS) {
            attempts++;
            System.out.print(NUM_DIGITS + "자리의 숫자 카드를 뽑아 입력해주세요 : ");
            String guessStr = scanner.nextLine();

            // 입력받은 숫자를 정수 배열로 변환
            int[] guess = parseGuess(guessStr);

            // 스트라이크와 볼 판정
            int strikes = countStrikes(answer, guess);
            int balls = countBalls(answer, guess);

            // 결과 출력
            if (strikes == NUM_DIGITS) {
                System.out.println("\n축하합니다!! 당신이 이겼습니다 정답 : " + Arrays.toString(answer));
                gameWon = true;
            } else {
                System.out.println("스트~라이크 : " + strikes + ", 볼.. : " + balls);
                System.out.println("다시 맞춰보세요!! (남은 시도 횟수: " + (MAX_ATTEMPTS - attempts) + ")\n");
            }
        }

        // 최대 시도 횟수를 초과하여 게임 종료
        if (!gameWon) {
            System.out.println("아쉽지만 시도 횟수를 초과하여 당신이 패배했습니다.");
            System.out.println("정답은 : " + Arrays.toString(answer) + " 입니다.");
        }

        scanner.close();
    }

    // 랜덤한 3자리 숫자 생성
    private static int[] generateRandomNumber() {
        Random random = new Random();
        int[] number = new int[NUM_DIGITS];
        boolean[] used = new boolean[10]; // 중복 숫자 방지

        for (int i = 0; i < NUM_DIGITS; i++) {
            int digit;
            do {
                digit = random.nextInt(10); // 0부터 9까지의 랜덤 숫자 생성
            } while (digit == 0 || used[digit]); // 숫자가 0이거나 이미 사용된 숫자인 경우 다시 생성

            number[i] = digit;
            used[digit] = true;
        }

        return number;
    }

    // 문자열로 입력받은 숫자를 정수 배열로 변환
    private static int[] parseGuess(String guessStr) {
        int[] guess = new int[NUM_DIGITS];
        for (int i = 0; i < NUM_DIGITS; i++) {
            guess[i] = Character.getNumericValue(guessStr.charAt(i));
        }
        return guess;
    }

    // 스트라이크 개수 계산
    private static int countStrikes(int[] answer, int[] guess) {
        int strikes = 0;
        for (int i = 0; i < NUM_DIGITS; i++) {
            if (answer[i] == guess[i]) {
                strikes++;
            }
        }
        return strikes;
    }

    // 볼 개수 계산
    private static int countBalls(int[] answer, int[] guess) {
        int balls = 0;
        boolean[] matched = new boolean[NUM_DIGITS];

        for (int i = 0; i < NUM_DIGITS; i++) {
            for (int j = 0; j < NUM_DIGITS; j++) {
                if (i != j && answer[i] == guess[j] && !matched[j]) {
                    balls++;
                    matched[j] = true;
                    break;
                }
            }
        }
        return balls;
    }
}