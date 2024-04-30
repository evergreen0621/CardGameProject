import java.util.Scanner;

public class UpDownCardGame {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        
        int numin;
        int high = 50;
        int low = 0;

        System.out.println("0과 50 사이의 숫자를 랜덤으로 결정하였습니다. 맞춰 보세요!");

        int rannum = (int)(Math.random()*50);



        //ㅇ
        for(int i = 0; i < 5; i++){
            System.out.print("숫자 입력 : ");
            
            numin = sc.nextInt();

            if(numin < rannum){
                System.out.println("UP");
                low = numin;
                System.out.println(low + "~" + high);
            }
            else if(numin > rannum){
                System.out.println("DOWN");
                high = numin;
                System.out.println(low + "~" + high);
            }
            else{
                System.out.println("숫자를 맞췄습니다. 당신의 승리!");
                break;
            }

            if(i == 4){
                System.out.println("맞추지 못했습니다. 당신의 패배!");
            }
        }
        sc.close();
    }
}