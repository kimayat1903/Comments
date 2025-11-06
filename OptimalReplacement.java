import java.util.Scanner;// for user input
import java.util.Arrays;// for using Arrays utility functions like fill()

public class OptimalReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);// Create Scanner object to take input from user
        System.out.print("Number of Frames: ");
        int frames = sc.nextInt();// Take number of frames (physical memory slots)
        System.out.print("Length of Reference string: ");
        int n = sc.nextInt(); // Take length of reference string

        int[] ref = new int[n];// Create array to store the reference string
        System.out.println("Enter reference string (space/newline separated):");
        for (int i = 0; i < n; i++) ref[i] = sc.nextInt();// read each page number

        int[] buffer = new int[frames];// Create array to represent the page frames
        Arrays.fill(buffer, -1); // -1 means empty
        int[][] memLayout = new int[n][frames];//memLayout[i][f] will store the page in frame f after processing reference i
        int pointer = 0;  // next free frame index (until buffer full)
        boolean full = false;  // flag to indicate if all frames are full
        int hits = 0, faults = 0; // count of page hits & faults

        for (int i = 0; i < n; i++) // Loop through each page reference one by one
        {
            int page = ref[i];// current page request
            boolean found = false;// flag to check if page is already in memory
            // check hit
            for (int f = 0; f < frames; f++) {//STEP 1: Check if the page is already present in any frame (HIT check)
                if (buffer[f] == page) // if page already in buffer
                { 
                    hits++; // increment hit count
                    found = true; // mark as found
                    break; }
            }
            if (!found) {  //STEP 2: If page is not found => PAGE FAULT
                if (!full) {
                    buffer[pointer++] = page;// place page in next empty frame
                    if (pointer == frames) { full = true; pointer = 0; } // mark frames as full...reset pointer (no longer needed)
                } else {
                    // buffer full -> apply optimal: find next use for each page in buffer
                    int[] nextUse = new int[frames];
                    Arrays.fill(nextUse, Integer.MAX_VALUE); // default = never used again
                    for (int f = 0; f < frames; f++) {// For each frame, check when this page will be used again in the future
                        for (int k = i + 1; k < n; k++) {
                            if (ref[k] == buffer[f]) { nextUse[f] = k; break; } // if found again, store next use index
                        }
                    }
                    //STEP 3: Find which page to replace (Victim page)
                    int victim = 0;// index of page to be replaced
                    int farthest = -1;// keeps track of farthest next use position
                    for (int f = 0; f < frames; f++) {
                        int val = (nextUse[f] == Integer.MAX_VALUE) ? Integer.MAX_VALUE : nextUse[f];
                        if (val == Integer.MAX_VALUE) { victim = f; break; } // never used again => choose immediately
                        if (val > farthest) { farthest = val; victim = f; }// otherwise, find the page used farthest in future
                    }
                    buffer[victim] = page;// Replace the chosen victim page with the new page
                }
                faults++;
            }
            //STEP 4: Store current memory state (for layout display)
            for (int f = 0; f < frames; f++) memLayout[i][f] = buffer[f];
        }

        //STEP 5: Print the memory layout table (frames vs time)
        // Each row represents one frame's contents over all time steps
        for (int f = 0; f < frames; f++) {
            for (int t = 0; t < n; t++) System.out.printf("%3d ", memLayout[t][f]);// print page or -1
            System.out.println();// new line after each frame row
        }
        //Print summary of results
        System.out.println("The number of Hits: " + hits);
        System.out.printf("Hit Ratio: %.4f%n", (float) hits / n);
        System.out.println("The number of Faults: " + faults);

        sc.close();
    }
}

/*Concept Overview
> Page Replacement occurs when a new page needs to be loaded into main memory (frames) 
but all frames are already occupied.
> The Optimal Page Replacement Algorithm (Belady’s Algorithm) 
replaces the page that will not be used for the longest time in the future.
> It gives the minimum possible number of page faults but is not practical 
in real systems because it requires future knowledge of page references.


> Reference String: Sequence of pages requested by the CPU (user input as array ref[]).
> Frames (Buffer):	Slots in main memory where pages are loaded (buffer[]).
> Page Hit: When the requested page is already in a frame (no replacement needed).
> Page Fault:	When the requested page is not found in frames (replacement needed).
> Next Use:	For each page currently in memory, find when it will be used next in the future (nextUse[]).
> Victim Page:	The page in memory that will be replaced — chosen as the one with farthest next use or never used again.
> Hit Ratio:	Fraction of total references that are hits → hits / total_references.
*/