//FIFO Page Replacement algorithm in java
import java.io.*; //Imports the input-output package to read user input using BufferedReader
public class FIFO //Defines a public class named FIFO
{
    public static void main(String[] args) throws IOException //throws IOException allows handling input errors from the user.
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//BufferedReader br: used to take input from the user
        int frames, pointer = 0, hit = 0, fault = 0,ref_len;
        //frames: number of available memory frames.
        //pointer: points to the next frame to be replaced (acts as queue front).
        //hit: counts number of page hits.
        //fault: counts number of page faults.
        //ref_len: length of the reference string.
        int buffer[]; //array representing frames in memory.
        int reference[]; //array holding the page reference string.
        int mem_layout[][]; //2D array to store frame contents
        
        System.out.println("Please enter the number of Frames: ");
        frames = Integer.parseInt(br.readLine());//Reads number of frames from the user.
        
        System.out.println("Please enter the length of the Reference string: ");
        ref_len = Integer.parseInt(br.readLine());//Reads total number of references in the sequence.
        
        reference = new int[ref_len];
        mem_layout = new int[ref_len][frames];
        buffer = new int[frames]; //Allocates memory for the arrays based on user inputs.
        for(int j = 0; j < frames; j++)
                buffer[j] = -1; //all frames are empty, -1 indicates that the frame has no page loaded yet.
        
        System.out.println("Please enter the reference string: ");
        for(int i = 0; i < ref_len; i++)
        {
            reference[i] = Integer.parseInt(br.readLine());// user enters the reference string, Each number represents a page requested by the CPU
        }
        System.out.println();
        for(int i = 0; i < ref_len; i++)
        {
         int search = -1;//search checks if the current page is already in memory.
         for(int j = 0; j < frames; j++)
         {
          if(buffer[j] == reference[i])//Checks each frame to see if the current page (reference[i]) is already present.
          {
           search = j;
           hit++;//If found → it's a hit;
           break;
          } 
         }
         if(search == -1)
         {
          buffer[pointer] = reference[i];//If not found: Replace the oldest page at position pointer.
          fault++;//Increment fault counter.
          pointer++;//Move the pointer to the next frame in a circular way.
          if(pointer == frames)//When pointer == frames, reset to 0 → (FIFO circular queue behavior).
           pointer = 0;
         }
            for(int j = 0; j < frames; j++)
                mem_layout[i][j] = buffer[j];// Stores the content of all frames after each reference, so that it can be printed later.
        }
        
        for(int i = 0; i < frames; i++)
        {
            for(int j = 0; j < ref_len; j++)//Displays the page arrangement in each frame at every step.
            // Outer loop → rows (frames)
            // Inner loop → columns (each reference)
                System.out.printf("%3d ",mem_layout[j][i]);//%3d ensures proper spacing and alignment.
            System.out.println();
        }
        
        System.out.println("The number of Hits: " + hit);
        System.out.println("Hit Ratio: " + (float)((float)hit/ref_len));
        System.out.println("The number of Faults: " + fault);
        //Displays the total hits, faults, and hit ratio (hits ÷ total references).
    }
    
}

/*FIFO Page Replacement 
> Goal: Manage limited memory frames efficiently when processes request pages.
> FIFO (First In First Out) → Replace the oldest page in memory first 
  (the one that came earliest).
> Works like a queue — pages enter at the rear and leave from the front.

Part	Explanation
> frames:	Total memory slots available.
> reference[]:	The reference string (sequence of page requests).
> buffer[]:	Represents current pages in memory.
> pointer:	Points to the frame to be replaced next (acts like queue front).
> hit, fault:	Count of page hits and page faults.
> mem_layout[][]:	Stores frame content after each request for display.
*/