import java.util.*;  // Importing utility package for Scanner and List classes

public class PrioritySchedulingShort {
    static class P {
        int id, at, bt, pr, wt, tat; // process id, arrival time, burst time, priority, waiting time, turnaround time
        boolean done; // flag to check if the process is completed

        P(int id, int at, int bt, int pr) { // Constructor to initialize process attributes
            this.id = id;
            this.at = at;
            this.bt = bt;
            this.pr = pr;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // Scanner for user input

        System.out.print("Enter number of processes : ");
        int n = sc.nextInt(); // Number of processes

        List<P> ps = new ArrayList<>(n);// List to store all process objects

        for (int i = 0; i < n; i++) {// Taking input for all processes
            System.out.print("Enter processId : ");
            int id = sc.nextInt();

            System.out.print("Enter arrival time : ");
            int at = sc.nextInt();

            System.out.print("Enter burst time : ");
            int bt = sc.nextInt();

            System.out.print("Enter priority (lower number has higher priority) : ");
            int pr = sc.nextInt();

            ps.add(new P(id, at, bt, pr));// Creating a new process and adding it to the list
        }

        int time = 0;       // Keeps track of current CPU time
        int completed = 0;  // Number of completed processes

        // Run until all processes are completed
        while (completed < n) {
            P best = null;  // Variable to store the process with highest priority (lowest pr value)

            // Find the process which has arrived and has the highest priority
            for (P p : ps) {
                if (!p.done && p.at <= time) { // process not completed and has arrived
                    if (best == null || p.pr < best.pr || (p.pr == best.pr && p.at < best.at))
                        best = p; // choose the process with smaller priority or earlier arrival
                }
            }
            // If no process is ready, jump time to next arrival
            if (best == null) {
                int nextAt = Integer.MAX_VALUE;
                for (P p : ps)
                    if (!p.done && p.at < nextAt)
                        nextAt = p.at;
                time = nextAt; // Move time to the next arriving process
                continue; // Repeat loop
            }
            // Calculate waiting time = current time - arrival time
            best.wt = time - best.at;
            // Update current time (CPU executes this process now)
            time += best.bt;
            // Calculate turnaround time = waiting time + burst time
            best.tat = best.wt + best.bt;
            // Mark process as completed
            best.done = true;
            completed++; // Increment completed process count
        }
        // Display header
        System.out.println("\nProcessId\tArrivalTime\tBurstTime\tWaitingTime\tTurnAroundTime");

        int totalW = 0, totalT = 0; // total waiting time and turnaround time
        // Display each process details
        for (P p : ps) {
            System.out.printf("P%d\t\t%d\t\t%d\t\t%d\t\t%d%n",
                    p.id, p.at, p.bt, p.wt, p.tat);
            totalW += p.wt;  // add waiting time
            totalT += p.tat;  // add turnaround time
        }
        // Print averages
        System.out.printf("\nAverage Waiting Time : %.2f\n", totalW / (double) n);
        System.out.printf("Average Turnaround Time : %.2f\n", totalT / (double) n);

        sc.close(); // close Scanner
    }
}
/*
Concept: Non-Preemptive Priority Scheduling

Each process has a priority number.
Lower number = higher priority.
At any time, the CPU selects the ready process with the highest priority (lowest priority number).
Once a process starts executing, it runs till completion — it cannot be stopped midway (that’s why it’s non-preemptive).
If multiple processes have the same priority, the one that arrived earlier gets the CPU first.

⚙️ Key Terms
Term	                    Meaning
Arrival Time (AT)	        When the process arrives in the ready queue
Burst Time (BT)	            Total CPU time the process needs
Priority (PR)	            Determines importance (smaller = higher priority)
Waiting Time (WT)	        Time process waits before getting CPU → StartTime - ArrivalTime
Turnaround Time (TAT)	    Total time from arrival to completion → WT + BT

🧩 How the Code Works
> Input Section
You enter process ID, arrival time, burst time, and priority.
These are stored in a list of P objects.

> Selection of Process
At each moment, the program looks at all arrived but not completed processes.
It chooses the one with highest priority (lowest priority number).

> Execution
The selected process runs fully (non-preemptive).
Its waiting time = current time − arrival time.
Then the CPU time increases by its burst time (process completes).
Turnaround time = waiting time + burst time.

> Completion
When all processes finish, program prints:
Process ID
Arrival Time
Burst Time
Waiting Time
Turnaround Time
It also calculates average waiting time and average turnaround time.

*/