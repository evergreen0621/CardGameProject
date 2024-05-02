import java.util.Random;
import java.util.Scanner;

public class CasinoWar {
    public static void main(String[] args) throws Exception{
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int playercount = 0;
        int computercount = 0;

        for(int i = 0; i < 5; i++) {
            System.out.println("카드를 뽑기 위해 엔터키를 눌러주세요.");
            sc.nextLine();

            // 플레이어와 컴퓨터의 카드 뽑기(2부터 14(에이스)까지의 랜덤 숫자)
            int playercard = random.nextInt(13) + 2;
            int computercard = random.nextInt(13) + 2;

            System.out.println("당신의 카드 : " + playercard);
            System.out.println("컴퓨터의 카드 : " + computercard + "\n");

            if(playercard > computercard){
                // 사용자의 카드가 더 클 경우
                System.out.println("당신의 승리입니다!\n");
                playercount++;
            }
            else if(playercard < computercard){
                // 컴퓨터의 카드가 더 클 경우
                System.out.println("컴퓨터의 승리입니다!\n");
                computercount++;
            }
            else{
                // 무승부인 경우
                System.out.println("무승부입니다!\n");
            }

        }

        if(playercount > computercount){
            System.out.println("최종 승자는 당신입니다!");
            WinLose.winlose = 1;
        }
        else if(playercount < computercount){
            System.out.println("최종 승자는 컴퓨터입니다!");
            WinLose.winlose = 0;
        }
        else{
            System.out.println("최종 승자는 없습니다!");
            WinLose.winlose = 2;
        }

        sc.close();
    }
}