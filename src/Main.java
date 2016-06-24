import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        double[] V = new double[101]; //状態価値関数
        int[] pi = new int[101]; //方策
        final double p = 0.4; //表が出る確率

        //状態価値関数の初期化
        for(int s = 0; s < 100; s++){
            V[s] = 0.0;
        }
        V[100] = 1.0;

        final double theta = 1e-5; //ループ終了の閾値
        double delta = 1.0; //最大変更料

        while(delta >= theta){
            delta = 0.0;
            for(int s = 0; s < 100; s++){
                double V_old = V[s];
                double cand = 0.0;

                //可能な掛け金ごとに勝率を調べる
                for(int bet = 1; bet <= Math.min(s, 100 - s); bet++){
                    double tmp  = p * V[s+bet] + (1.0 - p) * V[s-bet];
                    cand = Math.max(tmp, cand);
                }
                V[s] = cand;
                delta = Math.max(delta, Math.abs(V_old - V[s])); //変更量のチェック
            }

            //状態価値関数の表示
            for(int i = 0; i < 101; i++){
                System.out.println("V[ " + i + " ] = " + V[i]);
            }
        }

        //最適方策の更新
        double threshold = 1e-5;
        for (int s = 1; s < 100; s++){
            double cand = 0.0;
            double tmp;
            for (int bet = 1; bet <= Math.min(s, 100 - s); bet++){
                tmp = p * V[s + bet] + (1 - p) * V[s - bet];
                if(tmp > cand + threshold){
                    cand = tmp;
                    pi[s] = bet;
                }
            }
        }

        //最適方策の表示
        for(int i = 1; i < 100; i++){
            System.out.println("pi[ " + i + " ] = " + pi[i]);
        }

        try {
            File file = new File("src/pi.csv");
            FileWriter filewriter = new FileWriter(file);
            PrintWriter pw = new PrintWriter(filewriter);
            pw.println("no" + "," + "pi");
            for (int i = 0; i < pi.length; i++) {
                pw.print(i);
                pw.println("," + pi[i]);
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("ファイル書き込みエラー");
        }
    }
}

