import java.util.Scanner;// Importing Scanner class for taking input from the user
public class LruShort {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);// Create a Scanner object to read user input
        System.out.print("n: "); // Ask for total number of page references 
        int n = sc.nextInt(); // Read the number of pages in the reference string
        int[] refs = new int[n]; // Create an array to store the reference string 
        for (int i = 0; i < n; i++) refs[i] = sc.nextInt();// Input each page reference one by one
        System.out.print("m: "); int m = sc.nextInt(); // Ask for number of frames 

        int[] frames = new int[m], last = new int[m];// Create two arrays:'frames' stores the actual page numbers currently in memory...'last' stores when each page was last used (timestamp)
        for (int i = 0; i < m; i++) { frames[i] = -1; last[i] = -1; } // Set all frames to -1 (indicating empty) Set all timestamps to -1 (no page used yet)

        int time = 0, faults = 0;// 'time' keeps a logical counter for each page reference....'faults' counts total page faults (misses)
        for (int idx = 0; idx < n; idx++) {// Loop through every page reference one by one
            int page = refs[idx]; time++;// Current page to be referenced & Increase the time counter (used as a timestamp)
            int hit = -1; // Variable to check if current page is already in frames (-1 means miss)
            for (int i = 0; i < m; i++) /*Check if the page is already present in any frame (HIT condition)*/
            if (frames[i] == page) { hit = i; break; }// If page found in memory,Store its frame index

            if (hit != -1) last[hit] = time; //HIT & Update its last used time to current 'time'
            else {
                faults++; int empty = -1; //Increase page fault count // To check if there is any empty frame available
                for (int i = 0; i < m; i++) if (frames[i] == -1) { empty = i; break; }  // Search for an empty frame (value -1 means empty)
                if (empty != -1) { frames[empty] = page; last[empty] = time; }// If an empty frame is found → simply load the new page there
                else { // replace LRU
                    int lru = 0;// Assume first frame is LRU initially
                    for (int i = 1; i < m; i++) if (last[i] < last[lru]) lru = i; // Find frame with smallest 'last' value (least recently used)
                    frames[lru] = page; last[lru] = time;// Put new page into that frame // Update its last used time
                }
            }
            // Print the current reference, whether it's HIT or MISS, and frame contents
            System.out.print("Ref " + page + " -> ");
            System.out.print((hit!=-1) ? "HIT   " : "MISS  ");
            System.out.print("Frames: ");
            for (int f : frames) System.out.print((f==-1 ? "[ ] " : "["+f+"] "));// Display all frames (empty frames shown as [ ])
            System.out.println();
        }
        System.out.println("Total page faults = " + faults);// Finally, print the total number of page faults
        sc.close();
    }
}

/* Concept
> LRU (Least Recently Used) is a page replacement algorithm used in operating systems for memory management.
> When a process needs a page that’s not in memory (page fault), and all memory frames are full, the OS must 
replace one page.
> LRU replaces the page that has not been used for the longest time — i.e., the least recently used page.

Key Concepts
Concept	Meaning
> Page:	                A fixed-size block of a process stored in memory.
> Frame:	            A fixed-size slot in physical memory where a page can be loaded.
> Page Fault:	        Happens when a required page is not found in any frame.
> Hit:	                When the required page is already in memory.
> Timestamp / Counter:	Used to track when a page was last used (helps identify LRU page).
*/
