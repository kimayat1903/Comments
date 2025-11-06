import java.util.*;// Importing Scanner class for taking user input

public class SJF {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);// Create Scanner object to take input from user
        System.out.print("Enter number of processes : ");// Ask for number of processes
        int n = sc.nextInt();

        // Declare arrays for process details
        int[] pid = new int[n];   // Process IDs
        int[] at = new int[n];    // Arrival Time
        int[] bt = new int[n];    // Burst Time
        int[] rt = new int[n];    // Remaining Time (for preemption)
        int[] ct = new int[n];    // Completion Time
        int[] wt = new int[n];    // Waiting Time
        int[] tat = new int[n];   // Turnaround Time

        for (int i = 0; i < n; i++) { // Input process details
            System.out.print("Enter process Id : ");
            pid[i] = sc.nextInt();  // process id
            System.out.print("Enter the arrival time : ");
            at[i] = sc.nextInt();   // arrival time
            System.out.print("Enter the burst time : ");
            bt[i] = sc.nextInt();   // burst time
            rt[i] = bt[i];          // initially, remaining time = burst time
        }

        int completed = 0;   // keeps count of completed processes
        int time = 0;        // keeps track of current time

        while (completed < n) {// Run until all processes are completed
            int idx = -1;                // index of the process to execute
            int min = Integer.MAX_VALUE; // smallest remaining time found

            for (int i = 0; i < n; i++) {// Find process with minimum remaining time that has arrived
                if (at[i] <= time && rt[i] > 0 && rt[i] < min) {
                    min = rt[i];  // update smallest remaining time
                    idx = i;      // store index of that process
                }
            }

            if (idx == -1) {// If no process has arrived yet, just increment time
                time++;
                continue;
            }

            rt[idx]--;// Execute the process for 1 unit of time (preemptive)
            time++;

            if (rt[idx] == 0) {// If the process has finished executing
                completed++;                     // increase count of completed processes
                ct[idx] = time;                  // record completion time
                tat[idx] = ct[idx] - at[idx];    // Turnaround time = CT - AT
                wt[idx] = tat[idx] - bt[idx];    // Waiting time = TAT - BT
            }
        }

        double sumW = 0, sumT = 0;// Calculate average waiting and turnaround time

        // Display results in table format
        System.out.println("ProcessId\tArrivalTime\tBurstTime\tCompletionTime\tWaitingTime\tTurnaroundTime");
        for (int i = 0; i < n; i++) {
            System.out.printf("P%d\t\t%d\t\t%d\t\t%d\t\t%d\t\t%d%n",
                    pid[i], at[i], bt[i], ct[i], wt[i], tat[i]);// Print details of each process
            sumW += wt[i]; // Add to totals for average calculation
            sumT += tat[i];
        }
        // Display averages
        System.out.println("Average Waiting Time : " + (sumW / n));
        System.out.println("Average TurnAround Time : " + (sumT / n));

        sc.close();
    }
}

/*
🧠 Concept: SJF CPU Scheduling
Shortest Job First (SJF) is a CPU scheduling algorithm that selects the process with the smallest burst time (the shortest execution time) to run next.
It aims to minimize average waiting time and turnaround time, making it one of the most efficient scheduling algorithms.

⚙️ Types of SJF
Non-Preemptive SJF:
Once a process starts execution, it runs till completion even if a shorter job arrives later.

Preemptive SJF (Shortest Remaining Time First - SRTF):
The CPU can be taken away (preempted) from a running process if a new process arrives with a smaller remaining time.
✅ Your code implements this version.

🧩 Main Concepts Used in the Code
Concept	                Meaning	                                                    How it appears in your code
Arrival Time (AT)	    The time when a process enters the ready queue	            arrivalTime[i] or at[i]
Burst Time (BT)	        Total CPU time required by the process	                    burstTime[i] or bt[i]
Remaining Time (RT)	    Remaining CPU time (used for preemption)	                remainingTime[i] or rt[i]
Completion Time (CT)    The time when a process finishes execution	                completionTime[i] or ct[i]
Turnaround Time (TAT)   Total time taken from arrival to completion → CT - AT	    tat[i] = ct[i] - at[i];
Waiting Time (WT)	    Time process spends waiting → TAT - BT	                    wt[i] = tat[i] - bt[i];

⏱️ How the Preemptive SJF (SRTF) Works
1. Start from time = 0.
2. Check which processes have arrived (arrivalTime <= time).
3. Among them, select the one with the smallest remaining burst time.
4. Execute it for 1 unit of time (this makes it preemptive).
5. Repeat steps 2–4 until all processes finish.
6. Calculate CT, TAT, and WT for all processes.
7. Print results and compute averages.

 */