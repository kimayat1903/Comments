import java.util.*;  // Importing the Java utility package to use Scanner for input
public class FCFS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);   // Scanner object to take user input from keyboard
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();  // 'n' will store the number of processes

        // Arrays to store details of each process
        int[] at = new int[n];   // Arrival Time of each process
        int[] bt = new int[n];   // Burst (CPU) Time of each process
        int[] ft = new int[n];   // Finish Time for each process
        int[] tat = new int[n];  // Turnaround Time (FT - AT)
        int[] wt = new int[n];   // Waiting Time (TAT - BT)
        
        for (int i = 0; i < n; i++) { // Taking input for each process
            System.out.printf("Arrival time of P%d: ", i + 1);
            at[i] = sc.nextInt();   // Input arrival time for process i
            System.out.printf("Burst time of P%d: ", i + 1);
            bt[i] = sc.nextInt();   // Input CPU burst time for process i
        }

        int time = 0;  // 'time' keeps track of current CPU time (when CPU is free)
        
        for (int i = 0; i < n; i++) {// Loop through each process in order of arrival
            // If CPU is idle (i.e., next process hasn’t arrived yet)
            if (time < at[i]) 
                time = at[i];  // Move current time to the process's arrival time
                
            ft[i] = time + bt[i]; // Finish time = current time + burst time

            tat[i] = ft[i] - at[i]; // Turnaround time = finish time - arrival time

            wt[i] = tat[i] - bt[i]; // Waiting time = turnaround time - burst time

            time = ft[i]; // Update 'time' to when this process finishes (for next process)
        }

        double totalTAT = 0, totalWT = 0;// Variables to calculate average TAT and WT
        System.out.println("\nPID\tAT\tBT\tFT\tTAT\tWT");// Display table header

        for (int i = 0; i < n; i++) {// Loop to print details and calculate totals
            totalTAT += tat[i];   // Sum of all turnaround times
            totalWT += wt[i];     // Sum of all waiting times
            System.out.printf("P%d\t%d\t%d\t%d\t%d\t%d\n", // Print details for each process
                              i + 1, at[i], bt[i], ft[i], tat[i], wt[i]);
        }

        System.out.printf("\nAverage Turnaround Time = %.2f", totalTAT / n);  // Print average turnaround time
        System.out.printf("\nAverage Waiting Time = %.2f\n", totalWT / n); // Print average waiting time
        sc.close();  // Close the scanner object to prevent resource leak
    }
}

/*
🧠 What is FCFS CPU Scheduling?
FCFS (First Come, First Served) is the simplest CPU scheduling algorithm.
Processes are executed in the order they arrive.
Once a process gets the CPU, it runs till completion (non-preemptive).
It’s like a queue at a ticket counter — the first person to come is served first.

⚙️ How FCFS Works (Step-by-Step)
Processes arrive with given Arrival Time (AT) and Burst Time (BT).
The CPU picks the process that arrived first and executes it fully.
The next process starts only after the previous one finishes.
For each process, we calculate:
        Finish Time (FT) → When the process completes
        Turnaround Time (TAT) = FT – AT → Total time in system
        Waiting Time (WT) = TAT – BT → Time spent waiting in ready queue

💻 Connection with Code
Concept	                In Code	                          Meaning
Arrival Time (AT)	    at[i]	                          Time when process enters ready queue
Burst Time (BT)	        bt[i]	                          CPU time needed for process
Finish Time (FT)	    ft[i] = time + bt[i];	          When process finishes
Turnaround Time (TAT)	tat[i] = ft[i] - at[i];           Total time spent in system
Waiting Time (WT)	    wt[i] = tat[i] - bt[i];	          Time process waited before execution
Current CPU Time	    time variable	                  Tracks current clock time
Idle CPU handling	    if (time < at[i]) time = at[i];	  Skips idle periods if no process has arrived yet

 */