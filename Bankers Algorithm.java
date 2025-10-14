import java.util.*;

public class Bankers {
    private int[][] need, allocate, max;
    private int[] avail;
    private int np, nr;

    private void input() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of processes and resources:");
        np = sc.nextInt();
        nr = sc.nextInt();

        // Defining tables
        need = new int[np][nr];
        max = new int[np][nr];
        allocate = new int[np][nr];
        avail = new int[nr];

        System.out.println("Enter Allocation matrix:");
        for (int i = 0; i < np; i++) {
            for (int j = 0; j < nr; j++) {
                allocate[i][j] = sc.nextInt();
            }
        }

        System.out.println("Enter Max Need matrix:");
        for (int i = 0; i < np; i++) {
            for (int j = 0; j < nr; j++) {
                max[i][j] = sc.nextInt();
            }
        }

        System.out.println("Enter Available resources:");
        for (int j = 0; j < nr; j++) {
            avail[j] = sc.nextInt();
        }
        sc.close();
    }

    private void calc_need() {
        for (int i = 0; i < np; i++) {
            for (int j = 0; j < nr; j++) {
                need[i][j] = max[i][j] - allocate[i][j];
            }
        }
    }

    private boolean check(int i) {
        for (int j = 0; j < nr; j++) {
            if (avail[j] < need[i][j]) {
                return false;
            }
        }
        return true;
    }

    public void isSafe() {
        input();
        calc_need();

        boolean[] done = new boolean[np];
        int j = 0;

        while (j < np) {
            boolean allocated = false;
            for (int i = 0; i < np; i++) {
                if (!done[i] && check(i)) {
                    // Allocate resources (simulate)
                    for (int k = 0; k < nr; k++) {
                        avail[k] += allocate[i][k]; // release resources
                    }
                    System.out.println("Allocated process: P" + i);
                    done[i] = true;
                    allocated = true;
                    j++;
                }
            }
            if (!allocated) {
                break; // no process could be allocated
            }
        }

        if (j == np) {
            System.out.println("\nSystem is in a SAFE state ✅");
        } else {
            System.out.println("\nSystem is NOT in a safe state ❌");
        }
    }

    public static void main(String[] args) {
        new Bankers().isSafe();
    }
}
