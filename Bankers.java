import java.util.Scanner;// Import Scanner class for taking user input
public class Bankers{
    private int need[][],allocate[][],max[][],avail[][],np,nr;// Declare matrices and variables
    // need[][]     -> resources still needed by each process
    // allocate[][] -> resources currently allocated to each process
    // max[][]      -> maximum resources required by each process
    // avail[][]    -> currently available resources in the system
    // np           -> number of processes
    // nr           -> number of resource types
    
    private void input(){ // Function to take all input from the user
     Scanner sc=new Scanner(System.in); // Create a Scanner object to read input
     System.out.print("Enter no. of processes and resources : ");// Ask user for number of processes and resources
     np=sc.nextInt();  //no. of process
     nr=sc.nextInt();  //no. of resources
     need=new int[np][nr];  //initializing arrays
     max=new int[np][nr];
     allocate=new int[np][nr];
     avail=new int[1][nr];// 1 row and nr columns for available resources
     
     System.out.println("Enter allocation matrix -->");// --- Read Allocation Matrix ---
     for(int i=0;i<np;i++)// loop through each process
          for(int j=0;j<nr;j++)// loop through each resource type
         allocate[i][j]=sc.nextInt();  //allocation matrix
      
     System.out.println("Enter max matrix -->"); // --- Read Maximum Matrix ---
     for(int i=0;i<np;i++)// for each process
          for(int j=0;j<nr;j++)// for each resource
         max[i][j]=sc.nextInt();  //max matrix
      
        System.out.println("Enter available matrix -->");// --- Read Available Resources ---
        for(int j=0;j<nr;j++)// for each resource type
         avail[0][j]=sc.nextInt();  //available matrix
        
        sc.close(); // Close the scanner (no more input will be taken)
    }
    
    private int[][] calc_need(){ // Function to calculate the NEED matrix using formula: Need = Max - Allocation
       for(int i=0;i<np;i++) // for each process
         for(int j=0;j<nr;j++)  // for each resource type
          need[i][j]=max[i][j]-allocate[i][j];//calculating need matrix
       
       return need;
    }
 
    private boolean check(int i){// Function to check if a process can be allocated with the currently available resources
       for(int j=0;j<nr;j++) 
       if(avail[0][j]<need[i][j]) // if any needed resource is more than available
          return false; // can't allocate this process right now
   
    return true;// all needed resources are available → can alloca
    }

    public void isSafe(){ // Function to check whether the system is in a safe state
       input(); // Step 1: Take input and calculate need matrix
       calc_need();
       boolean done[]=new boolean[np]; // done[i] keeps track of whether process i has finished execution
       int j=0;// j counts number of processes that have been safely allocated

       while(j<np){  // Step 2: Try allocating processes one by one until all are done or deadlock
       boolean allocated=false;// flag to check if any process got allocated in this round
       for(int i=0;i<np;i++) // Check each process
        if(!done[i] && check(i)){  // If process i is not yet allocated and its needs can be satisfied
            for(int k=0;k<nr;k++)// Simulate resource allocation and release
            avail[0][k]=avail[0][k]-need[i][k]+max[i][k];// After process finishes, it releases its allocated resources
                        // avail = avail - need + max is same as avail = avail + allocate
         System.out.println("Allocated process : "+i);// Print which process got allocated safely
         allocated=done[i]=true;// Mark this process as done and increment counter
               j++;// increase number of allocated processes
             }
          if(!allocated) break;// If in this entire loop, no process could be allocated → system is unsafe
       }
       if(j==np) // Step 3: Check if all processes could be safely allocated
        System.out.println("\nSafely allocated"); // All processes finished → safe state
       else
        System.out.println("All proceess cant be allocated safely"); // unsafe state
    }
    
    public static void main(String[] args) {// Main function - execution starts here
       new Bankers().isSafe();// Create an object and call the isSafe() method to execute the algorithm
    }
}

/*Concept
Purpose:
To avoid deadlock in a system by checking whether resource allocation will leave the system in a safe state.
Idea:
Every process tells the system in advance the maximum number of resources it might need.
The system only grants a resource request if it keeps the system safe, i.e., if all processes can still finish eventually.
Safe state:
A state where there exists some safe sequence of processes (like P1 → P3 → P4...) such that each process can get the resources it needs and complete, one by one.
 

Key Terms 
Term	            Meaning	                                                Represented by
Allocation:      	Resources currently held by each process	               allocate[i][j]
Max:	            Maximum resources each process may need	               max[i][j]
Available:        Currently available (free) resources in the system	      avail[0][j]
Need:	            Remaining resources required by each process	            need[i][j] = max[i][j] - allocate[i][j]
Safe Sequence:    Order in which all processes can finish safely	         printed by the program

Steps of Banker’s Algorithm
1. Input Matrices
Take Allocation, Max, and Available from user.
2. Compute Need Matrix
Need = Max - Allocation.
3. Check for Safe Sequence
Find a process whose Need ≤ Available.
Pretend to allocate resources to it and then release its Allocation (as if it finished).
Update Available accordingly.
Repeat for all processes.
4. Decide Safety
If all processes can be allocated in some order → Safe state.
If some processes remain unallocatable → Unsafe (possible deadlock).
*/