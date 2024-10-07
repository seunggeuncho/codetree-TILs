import java.util.*;
import java.io.*;
public class Main {
    static int[][] map;
    static int[] first;
    static int[] second;
    static boolean[] visited;
    static int n,ans;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        map = new int[n][n];
        ans = Integer.MAX_VALUE;
        first = new int[n / 2];
        second = new int[n / 2];
        visited = new boolean[n];
        StringTokenizer st = null;
        for(int i = 0; i < n; i++){
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        permutation(0, 0);
        System.out.println(ans);
    }
    private static void permutation(int depth,int start){
        if(depth == n / 2){
          //  System.out.println(Arrays.toString(visited));
            int cnt1 = 0;
            int cnt2 = 0;
            for(int j = 0; j < n; j++){
                if(visited[j]) first[cnt1++] = j;
                else second[cnt2++] = j;
            }
            int val = Math.abs(check(first) - check(second));
            ans = Math.min(ans, val);
            return;
        }

        for(int i = start ; i < n; i++){
            if(visited[i] == false){
            visited[i] = true;
            permutation(depth + 1 ,i);
            visited[i] = false;
            }
        }
    }

    private static int check(int[] tmp){
        int val = 0;
        for(int i = 0; i < n / 2 - 1; i++){
            for(int j = i + 1; j < n / 2;j++){
                val += map[tmp[i]][tmp[j]];
                val += map[tmp[j]][tmp[i]];
            }
        }
       // System.out.println(Arrays.toString(tmp));
        //System.out.println(val);
        return val;
    }
}