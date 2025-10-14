//FCFS
import java.util.*;

public class FCFS_Swapping {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int pid[] = new int[n];
        int at[] = new int[n];
        int bt[] = new int[n];
        int ct[] = new int[n];
        int tat[] = new int[n];
        int wt[] = new int[n];

        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("Enter arrival time for P" + pid[i] + ": ");
            at[i] = sc.nextInt();
            System.out.print("Enter burst time for P" + pid[i] + ": ");
            bt[i] = sc.nextInt();
        }

        // Sort by arrival time
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (at[i] > at[j]) {
                    int temp = at[i]; at[i] = at[j]; at[j] = temp;
                    temp = bt[i]; bt[i] = bt[j]; bt[j] = temp;
                    temp = pid[i]; pid[i] = pid[j]; pid[j] = temp;
                }
            }
        }

        // Completion times
        ct[0] = at[0] + bt[0];
        for (int i = 1; i < n; i++) {
            if (at[i] > ct[i - 1]) {
                ct[i] = at[i] + bt[i];
            } else {
                ct[i] = ct[i - 1] + bt[i];
            }
        }

        // TAT & WT
        for (int i = 0; i < n; i++) {
            tat[i] = ct[i] - at[i];
            wt[i] = tat[i] - bt[i];
        }

        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + pid[i] + "\t" + at[i] + "\t" + bt[i] + "\t" + ct[i] + "\t" + tat[i] + "\t" + wt[i]);
        }
    }
}

//SJF Preepmtive
import java.util.*;

class SJFProcess {
    int id, at, bt, ct, tat, wt, rt;
    int remainingBt;
    boolean isCompleted = false;
}

public class SJF_Preemptive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        SJFProcess[] p = new SJFProcess[n];
        for (int i = 0; i < n; i++) {
            p[i] = new SJFProcess();
            p[i].id = i + 1;
            System.out.print("Enter Arrival time of P" + (i + 1) + ": ");
            p[i].at = sc.nextInt();
            System.out.print("Enter Burst time of P" + (i + 1) + ": ");
            p[i].bt = sc.nextInt();
            p[i].remainingBt = p[i].bt;
        }

        int time = 0, completed = 0;
        int sumTAT = 0, sumWT = 0, sumRT = 0;

        while (completed != n) {
            int idx = -1;
            int minBT = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (p[i].at <= time && !p[i].isCompleted && p[i].remainingBt < minBT) {
                    minBT = p[i].remainingBt;
                    idx = i;
                }
            }

            if (idx != -1) {
                if (p[idx].remainingBt == p[idx].bt) {
                    p[idx].rt = time - p[idx].at;
                }

                p[idx].remainingBt--;
                time++;

                if (p[idx].remainingBt == 0) {
                    p[idx].ct = time;
                    p[idx].tat = p[idx].ct - p[idx].at;
                    p[idx].wt = p[idx].tat - p[idx].bt;

                    sumTAT += p[idx].tat;
                    sumWT += p[idx].wt;
                    sumRT += p[idx].rt;

                    p[idx].isCompleted = true;
                    completed++;
                }
            } else {
                time++;
            }
        }

        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT\tRT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + p[i].id + "\t" + p[i].at + "\t" + p[i].bt + "\t" +
                    p[i].ct + "\t" + p[i].tat + "\t" + p[i].wt + "\t" + p[i].rt);
        }

        System.out.println("\nAverage TAT: " + (sumTAT / (float) n));
        System.out.println("Average WT: " + (sumWT / (float) n));
        System.out.println("Average RT: " + (sumRT / (float) n));
    }
}

//Round Robbin Preemptive
import java.util.*;

class RRProcess {
    int id, arrivalTime, burstTime, remainingTime, completionTime, waitingTime, turnaroundTime;

    RRProcess(int id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

public class RoundRobin {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        System.out.print("Enter time quantum: ");
        int tq = sc.nextInt();

        RRProcess[] processes = new RRProcess[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time of P" + (i + 1) + ": ");
            int at = sc.nextInt();
            System.out.print("Enter burst time of P" + (i + 1) + ": ");
            int bt = sc.nextInt();
            processes[i] = new RRProcess(i + 1, at, bt);
        }

        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));
        Queue<RRProcess> queue = new LinkedList<>();
        boolean[] visited = new boolean[n];

        queue.add(processes[0]);
        visited[0] = true;
        int time = processes[0].arrivalTime;

        while (!queue.isEmpty()) {
            RRProcess current = queue.poll();

            if (current.remainingTime > tq) {
                time += tq;
                current.remainingTime -= tq;
            } else {
                time += current.remainingTime;
                current.remainingTime = 0;
                current.completionTime = time;
                current.turnaroundTime = current.completionTime - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
            }

            for (int i = 0; i < n; i++) {
                if (!visited[i] && processes[i].arrivalTime <= time) {
                    queue.add(processes[i]);
                    visited[i] = true;
                }
            }

            if (current.remainingTime > 0) {
                queue.add(current);
            }
        }

        double avgTAT = 0, avgWT = 0;
        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (RRProcess p : processes) {
            System.out.println("P" + p.id + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" +
                    p.completionTime + "\t" + p.turnaroundTime + "\t" + p.waitingTime);
            avgTAT += p.turnaroundTime;
            avgWT += p.waitingTime;
        }

        System.out.println("\nAverage Turnaround Time: " + (avgTAT / n));
        System.out.println("Average Waiting Time: " + (avgWT / n));
    }
}

//Priority Non-Preemptive
import java.util.Scanner;

public class PriorityNonPreemptiveEasy {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] bt = new int[n];
        int[] pr = new int[n];
        int[] wt = new int[n];
        int[] tat = new int[n];
        int[] pid = new int[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter burst time for Process " + (i + 1) + ": ");
            bt[i] = sc.nextInt();
            System.out.print("Enter priority for Process " + (i + 1) + ": ");
            pr[i] = sc.nextInt();
            pid[i] = i + 1;
        }

        // Sort by priority
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (pr[i] > pr[j]) {
                    int temp = pr[i]; pr[i] = pr[j]; pr[j] = temp;
                    temp = bt[i]; bt[i] = bt[j]; bt[j] = temp;
                    temp = pid[i]; pid[i] = pid[j]; pid[j] = temp;
                }
            }
        }

        wt[0] = 0;
        tat[0] = bt[0];
        for (int i = 1; i < n; i++) {
            wt[i] = wt[i - 1] + bt[i - 1];
            tat[i] = wt[i] + bt[i];
        }

        int totalWT = 0, totalTAT = 0;
        System.out.println("\nPID\tBT\tPR\tWT\tTAT");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + pid[i] + "\t" + bt[i] + "\t" + pr[i] + "\t" + wt[i] + "\t" + tat[i]);
            totalWT += wt[i];
            totalTAT += tat[i];
        }

        System.out.println("\nAverage Waiting Time = " + (float) totalWT / n);
        System.out.println("Average Turnaround Time = " + (float) totalTAT / n);
    }
}
