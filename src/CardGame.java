import java.util.Scanner;

public class CardGame {

    public static void main(String[] args) throws Exception {
        int score1 = 0;
        int score2 = 0;
        int score3 = 0;
        int score4 = 0;
        int score5 = 0;
        int score6 = 0;
        int totalScore = 0;
        Scanner sc = new Scanner(System.in);

        System.out.println("카드 게임 랜드에 오신 것을 환영합니다!");
        System.out.println("|\\_/|");
        System.out.println("|q p|   /");
        System.out.println("( 0 )\"\"\"\\");
        System.out.println("|\"^\"`    |");
        System.out.println("||_/=\\\\__|");

        while(true){
            System.out.println("다음 선택지 중 하나를 골라주세요.\n1. 게임하기\n2. 점수 확인\n3. 종료하기");

            int choice = sc.nextInt();

            if(choice == 1){
                // 게임하기 선택 시
                while(true){
                    System.out.println("지금 플레이하실 수 있는 게임들은 다음과 같습니다.\n어떤 게임을 하시겠습니까?\n1. 카지노 워 (Casino War)\n2. 업 다운 (Up & Down) \n3. 세븐 (Sevens7) \n4. 오마하 포커 (Omaha Poker) \n5. 숫자야구 (Baseball Game)\n6. 스파이더 솔리테어 (Spider Solitaire)");
                
                    int gamechoice = sc.nextInt();

                    if(gamechoice == 1){
                        System.out.println("카지노 워 카드게임이 시작됩니다 !!");
                        CasinoWar.main(args);
                        // 1번 게임 선택 시
                        if(WinLose.winlose == 1){
                            score1 = 10;
                        }
                        else if(WinLose.winlose == 0){
                            score1 = 0;
                        }
                        else{
                            score1 = 5;
                        }
                        break;
                    }
                    else if(gamechoice == 2){
                        System.out.println("업다운 카드게임이 시작됩니다 !!");
                        UpDownCardGame.main(args);
                        // 2번 게임 선택 시
                        if(WinLose.winlose == 1){
                            score1 = 20;
                        }
                        else{
                            score1 = 0;
                        }
                        break;
                    }
                    else if(gamechoice == 3){
                        System.out.println("Sevens 카드게임이 시작됩니다 !!");
                        SevensGame.main(args);
                        // 3번 게임 선택 시
                        if(WinLose.winlose == 1){
                            score1 = 30;
                        }
                        else if(WinLose.winlose == 0){
                            score1 = 0;
                        }
                        else{
                            score1 = 15;
                        }
                        break;
                    }
                    else if(gamechoice == 4){
                        System.out.println("오마하 포커 카드게임이 시작됩니다 !!");
                        OmahaPoker.main(args);
                        // 4번 게임 선택 시
                        if(WinLose.winlose == 1){
                            score1 = 40;
                        }
                        else if(WinLose.winlose == 0){
                            score1 = 0;
                        }
                        else{
                            score1 = 20;
                        }
                        break;
                    }
                    else if(gamechoice == 5){
                        System.out.println("야구 카드게임이 시작됩니다 !!");
                        BaseballGame.main(args);
                        // 5번 게임 선택 시
                        if(WinLose.winlose == 1){
                            score1 = 50;
                        }
                        else{
                            score1 = 0;
                        }
                        break;
                    }
                    else if(gamechoice == 6){
                        System.out.println("스파이더 솔리테어 게임이 시작됩니다 !!");
                        SpiderSolitaire.main(args);
                        // 6번 게임 선택 시
                        if(WinLose.winlose == 1){
                            score1 = 60;
                        }
                        else if(WinLose.winlose == 0){
                            score1 = 0;
                        }
                        else{
                            score1 = 30;
                        }
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

                totalScore = score1 + score2 + score3 + score4 + score5 + score6;

                System.out.println("점수는 다음과 같습니다.");
                System.out.println("1번 게임 결과: " + score1);
                System.out.println("2번 게임 결과: " + score2);
                System.out.println("3번 게임 결과: " + score3);
                System.out.println("4번 게임 결과: " + score4);
                System.out.println("5번 게임 결과: " + score5);
                System.out.println("6번 게임 결과: " + score6);
                System.out.println("");
                System.out.println("총 합산 점수: " + totalScore);
                System.out.println("");
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