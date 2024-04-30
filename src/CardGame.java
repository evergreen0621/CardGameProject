import java.util.Scanner;

public class CardGame {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        // 응애

        System.out.println("카드 게임 랜드에 오신 것을 환영합니다!");

        while(true){
            System.out.println("다음 선택지 중 하나를 골라주세요.\n1. 게임하기\n2. 랭킹 확인\n3. 종료하기");

            int choice = sc.nextInt();

            if(choice == 1){
                // 게임하기 선택 시
                while(true){
                    System.out.println("지금 플레이하실 수 있는 게임들은 다음과 같습니다. 어떤 게임을 하시겠습니까?\n1. 1번 게임(카지노워)\n2. 2번 게임(오마하포커)\n3. 3번 게임(업다운 카드게임)\n4번 게임(Seven7)");
                
                    int gamechoice = sc.nextInt();

                    if(gamechoice == 1){
                        System.out.println("카드게임이 시작됩니다 !!");
                        CasinoWar.main(args);
                        // 1번 게임 선택 시
                        break;
                    }
                    else if(gamechoice == 2){
                        System.out.println("카드게임이 시작됩니다 !!");
                        OmahaPoker.main(args);
                        // 2번 게임 선택 시
                        break;
                    }
                    else if(gamechoice == 3){
                        System.out.println(" 업 다운 카드게임이 시작됩니다 !!");
                        UpDownCardGame.main(args);
                        // 3번 게임 선택 시
                        break;
                    }
                    else if(gamechoice == 4){
                        System.out.println("Sevens 카드게임이 시작됩니다 !!");
                        SevensGame.main(args);
                        // 3번 게임 선택 시
                        break;
                    }
                    else{
                        // 선택지에 없는 번호 선택 시
                        System.out.println("없는 번호입니다. 다시 입력해 주세요.");
                    }
                }
            }
            else if(choice == 2){
                // 랭킹 확인 선택 시
                System.out.println("랭킹은 다음과 같습니다.");
            }
            else if(choice == 3){
                // 종료하기 선택 시
                System.out.println("카드 게임 랜드를 이용해 주셔서 감사합니다.");
                break;
            }
            else{
                // 선택지에 없는 번호 선택 시
                System.out.println("없는 번호입니다. 다시 입력해 주세요.");
            }
        }
        sc.close();
    }
}