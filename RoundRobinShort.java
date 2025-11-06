import java.util.*;// Importing all classes from java.util package (for Scanner, Queue, LinkedList, etc.)
public class RoundRobinShort {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);// Creating a Scanner object for user input
        System.out.print("Enter number of processes : ");// Asking for total number of processes
        int n = sc.nextInt();  // store number of processes

        // Declaring arrays to store process details
        int[] pid = new int[n];  // Process IDs
        int[] at = new int[n];   // Arrival Times
        int[] bt = new int[n];   // Burst Times
        int[] rt = new int[n];   // Remaining Times (for preemption)

        for (int i = 0; i < n; i++) { // Taking input for each process
            System.out.print("Enter process Id : ");
            pid[i] = sc.nextInt(); // process ID

            System.out.print("Enter arrival time : ");
            at[i] = sc.nextInt(); // arrival time

            System.out.print("Enter burst time : ");
            bt[i] = sc.nextInt(); // burst time

            rt[i] = bt[i]; // initially, remaining time = burst time
        }

        System.out.print("Enter time quantum : "); // Taking input for time quantum
        int tq = sc.nextInt(); // time slice each process gets

        // Initializing data structures for Round Robin scheduling
        Queue<Integer> q = new LinkedList<>(); // ready queue
        boolean[] inQ = new boolean[n];        // track if process is already in queue
        int time = 0;                          // current time
        int done = 0;                          // number of completed processes

        // Arrays for result calculations
        int[] ct = new int[n];   // Completion Time
        int[] wt = new int[n];   // Waiting Time
        int[] tat = new int[n];  // Turnaround Time

        System.out.print("\nGantt Chart"); // Printing the start of Gantt Chart

        while (done < n) { // Loop runs until all processes are completed

            // Step 1: Add all processes that have arrived till current time into the ready queue
            for (int i = 0; i < n; i++)
                if (!inQ[i] && rt[i] > 0 && at[i] <= time) {
                    q.add(i);
                    inQ[i] = true;
                }

            // Step 2: If no process is ready, jump time to the next process’s arrival
            if (q.isEmpty()) {
                int next = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++)
                    if (rt[i] > 0 && at[i] < next)
                        next = at[i];
                time = Math.max(time, next);
                continue;
            }

            // Step 3: Take first process from the ready queue (Round Robin order)
            int i = q.poll();
            inQ[i] = false; // mark as not in queue now

            // Step 4: Decide how much time to execute (min of time quantum or remaining time)
            int exec = Math.min(tq, rt[i]);

            // Step 5: If process arrived later than current time, adjust clock
            if (time < at[i]) time = at[i];

            // Step 6: Print process in Gantt Chart
            System.out.print("| P" + pid[i] + " ");

            // Step 7: Reduce remaining time and increase current time
            rt[i] -= exec;
            time += exec;

            // Step 8: Add newly arrived processes during this execution time
            for (int j = 0; j < n; j++)
                if (!inQ[j] && rt[j] > 0 && at[j] <= time) {
                    q.add(j);
                    inQ[j] = true;
                }

            // Step 9: If process completed execution
            if (rt[i] == 0) {
                done++; // increment completed count
                ct[i] = time; // record completion time
                tat[i] = ct[i] - at[i]; // Turnaround = Completion - Arrival
                wt[i] = tat[i] - bt[i]; // Waiting = Turnaround - Burst
            }
            // Step 10: If process still has remaining time, put it back into queue
            else {
                q.add(i);
                inQ[i] = true;
            }
        }

        // End of Gantt Chart
        System.out.print("|");

        // Step 11: Display process details and calculate averages
        System.out.println();
        System.out.println("ProcessId\tArrivalTime\tBurstTime\tWaitingTime\tTurnAroundTime\tCompletionTime");

        float sumW = 0, sumT = 0; // totals for averages

        // Step 12: Print details of each process
        for (int i = 0; i < n; i++) {
            System.out.printf("P%d\t\t%d\t\t%d\t\t%d\t\t%d\t\t%d\n",
                    pid[i], at[i], bt[i], wt[i], tat[i], ct[i]);
            sumW += wt[i];
            sumT += tat[i];
        }

        // Step 13: Print average waiting and turnaround times
        System.out.println("Average Waiting Time : " + (sumW / n));
        System.out.println("Average TurnAround Time : " + (sumT / n));

        sc.close();
    }
}

/* Round Robin CPU Scheduling – Concept
Type: Preemptive CPU Scheduling Algorithm
Idea: Each process gets an equal time quantum (fixed time slice) to execute in a circular order.
Goal: Ensure fairness — no process waits too long and all get equal CPU time turns.

⚙️ How It Works (Step-by-Step)
> Ready Queue:
All processes are kept in a queue (FIFO order).
When one process’s time quantum expires, it’s sent back to the queue’s end if not completed.

> Time Quantum:
A fixed time duration that each process can use the CPU for (e.g., 4 ms).

> Preemption:
If a process’s burst time is greater than the time quantum, it’s interrupted after the time quantum and re-added to the queue.

> Scheduling Order:
CPU cycles through all processes repeatedly until all finish execution.

> Fairness:
Every process gets CPU access regularly — good for time-sharing systems (like OS multitasking).

🧠 Key Terms Used in the Code
Term	                Meaning	                                    Role in Code
Arrival Time (at)	    When process enters the system	            Used to decide when a process is ready to enter the queue
Burst Time (bt)	        Total CPU time needed by a process	        Determines total execution requirement
Remaining Time (rt) 	Remaining CPU time left	                    Decreases after every execution
Time Quantum (tq)	    CPU time per process turn	                Controls preemption; main Round Robin parameter
Ready Queue	            Queue that holds ready processes	        Implemented using LinkedList
Waiting Time (wt)	    Time process spends waiting in queue	    Calculated after process completes
Turnaround Time (tat)	Total time from arrival to completion	    = Completion Time – Arrival Time
Completion Time (ct)	When process finishes execution	            Recorded when remaining time becomes 0

*/
