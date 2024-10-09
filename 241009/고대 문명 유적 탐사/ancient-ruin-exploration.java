import java.util.*;
import java.io.*;
public class Main {

    static int map[][] = new int[5][5];
    static int pieces[];
    static int count,idx;
    static int[][] dir = {{1,0},{0,1},{-1,0},{0,-1}};
    static boolean[][] visited;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int k = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        pieces = new int[M];
        for(int i = 0; i < 5; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < 5; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < M; i++) {
            pieces[i] = Integer.parseInt(st.nextToken());
        }

        int cnt = 0;
        while(cnt++ < k) {
            idx = 0;
            int max_row = 0;
            int max_col = 0;
            int max_count = 0;
            int max_rotat = 0;
            for(int col = 0; col < 3; col++) {    //3x3 2차원 배열 추출
                for(int row = 0; row < 3; row++) {
                    for(int i = 0; i < 3; i++) {    //90,180,270,360도 회전
                        rotation(row,col);
                        int num = check();
                        if(num >= max_count){
                            if(num > max_count) {
                                max_row = row;
                                max_col = col;
                                max_count = num;
                                max_rotat = i;
                            } else if (num == max_count){
                                if(max_rotat > i){
                                    max_row = row;
                                    max_col = col;
                                    max_count = num;
                                    max_rotat = i;
                                }
                            }
                        }
                    }
                    rotation(row,col);
                }
            }
            if(max_count == 0) break;

//            System.out.println("max_row : " + max_row + " max_col : " + max_col + " max_rotat : " + max_rotat);
            for(int i = 0 ; i < max_rotat + 1; i++){
                rotation(max_row,max_col);
            }
            int val = total_num();
            int ans = val;
            while(val > 0){
//                System.out.println("val : " + val);
                val = total_num();
                ans += val;
            }
            System.out.print(ans + " ");
        }
    }

    private static int check() {
        visited = new boolean[5][5];
        int total = 0;
        for(int r = 0; r < 5; r++) {
            for(int c = 0; c < 5; c++) {
                if(visited[r][c]) continue;
//                System.out.println("check>>>> row : " + r + " col : " + c);
                int tmp = check_num(r,c,map[r][c]);
                if(tmp >= 3) total += tmp;
            }
        }

        return total;
    }


    private static int check_num(int r, int c, int i) {
        Queue<int[]> que = new ArrayDeque<>();
        que.add(new int[] {r,c});
        int cnt = 0;
        while(!que.isEmpty()) {
            int[] cur = que.poll();
//            System.out.println("check_num>>>>>>>> cur[0] : " + cur[0] + " cur[1] : " + cur[1] + " val : " + i + " cnt : " + cnt);
            visited[cur[0]][cur[1]] = true;
            cnt++;
            for(int j = 0; j < 4; j++) {
                int t_r = dir[j][0] + cur[0];
                int t_c = dir[j][1] + cur[1];
                if(0 > t_r || t_r >= 5 || 0 > t_c || t_c >=5 || map[t_r][t_c] != i || visited[t_r][t_c])continue;
                que.add(new int[]{t_r,t_c});
            }
        }
        return cnt;
    }


    static void rotation(int s_row, int s_col) {
        int[][] tmp = new int[3][3];
//        System.out.println(s_row + " " + s_col + "////");
        for(int r = s_row ; r < s_row + 3; r++) {
            for(int c = s_col; c < s_col + 3; c++) {
//                System.out.println(" tmpr : " + (c-s_col) + " tmpc : " + (2-r - s_row) + " mapr : " + r + " mapc : " + c);
                tmp[c -s_col][2-(r - s_row)] = map[r][c];
            }
        }

        for(int r = 0 ; r < 3; r++) {
            for(int c = 0; c < 3; c++) {
                map[s_row + r][s_col + c] = tmp[r][c];
            }
        }
//
//        for(int i = 0 ; i < 5; i++){
//            System.out.println(Arrays.toString(map[i]));
//        }
//        System.out.println("/////////////////////////");

    }

    static int total_num() {
        visited = new boolean[5][5];
        int total = 0;
        PriorityQueue<int[]> que = new PriorityQueue<>((o1, o2) -> {
            if (o1[1] == o2[1]) {
                return o2[0] - o1[0];
            } else {
                return o1[1] - o2[1];
            }
        });
//        System.out.println("total change before");
//        for(int i= 0 ;i < 5;i++){
//            System.out.println(Arrays.toString(map[i]));
//        }

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                if (visited[r][c]) continue;
                List<int[]> tmp = check_lst(r, c, map[r][c]);
                if (tmp.size() >= 3) {
                    total += tmp.size();
                    for (int[] cur : tmp) {
                        que.add(cur);
                    }
                }
            }
        }
        while (!que.isEmpty()) {
            int[] cur = que.poll();
//            System.out.println("change row : " + cur[0] + "change col : " + cur[1] + "pieces val : " + pieces[idx]);
            map[cur[0]][cur[1]] = pieces[idx++];
        }
//        System.out.println("final change");
//        for(int i= 0 ;i < 5;i++){
//            System.out.println(Arrays.toString(map[i]));
//        }
        return total;

    }

    static List<int[]> check_lst(int r, int c, int i) {
        Queue<int[]> que = new ArrayDeque<>();
        que.add(new int[] {r,c});
        List<int[]> lst = new ArrayList<>();
        lst.add(new int[]{r,c});
        while(!que.isEmpty()) {
            int[] cur = que.poll();
            visited[cur[0]][cur[1]] = true;

            for(int j = 0; j < 4; j++) {
                int t_r = dir[j][0] + cur[0];
                int t_c = dir[j][1] + cur[1];
                if(0 > t_r || t_r >= 5 || 0 > t_c || t_c >=5 || map[t_r][t_c] != i || visited[t_r][t_c])continue;
                que.add(new int[]{t_r,t_c});
                lst.add(new int[]{t_r,t_c});
            }
        }

        return lst;
    }
}