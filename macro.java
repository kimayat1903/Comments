// Importing required Java utility and IO classes
import java.util.*;
import java.io.*;

// Main class for Macro Processor
public class macro
{
    public static void main(String args[])
    {
        // Declare input/output objects and variables
        BufferedReader br;          // To read input file line by line
        OutputStream oo;             // (Not used here, can be removed)
        String input = null;         // Stores each line from the input file
        String tt = null;            // Temporary string token
        String arg = null;           // To store macro argument
        String macroTokens = null;   // To store each token inside macro definition

        // Arrays for different macro processor tables
        String mnt[] = new String[10];    // Macro Name Table (stores macro names)
        String mdt[] = new String[20];    // Macro Definition Table (stores macro body)
        String AR[]  = new String[20];    // Argument list for macros (e.g. &ARG1)

        // Supporting arrays and counters
        int macroindex[] = new int[10];   // Stores index (or line number) of each macro in MDT
        int mcount = 0;                   // Counts number of macros
        int arg_count = 0;                // Counts arguments in a macro
        int middlecount = 0;              // (Unused variable)
        int index = 1;                    // Keeps track of line number in input
        int macro_enc = 0;                // Flag to indicate we are inside a macro definition

        try
        {
            // Open the input assembly file
            br = new BufferedReader(new FileReader("Input.txt"));

            // Create output files for MNT, MDT, and ADT
            File f3 = new File("mnt.txt");   // Macro Name Table file
            File f4 = new File("mdt.txt");   // Macro Definition Table file
            File f5 = new File("adt.txt");   // Argument Definition Table file

            // Create PrintWriter objects to write to these files
            PrintWriter p3 = new PrintWriter(f3);
            PrintWriter p4 = new PrintWriter(f4);
            PrintWriter p5 = new PrintWriter(f5);

            // Read the input file line by line
            while ((input = br.readLine()) != null)
            {
                // Tokenize the line by space
                StringTokenizer st = new StringTokenizer(input, " ");
                tt = st.nextToken();   // Get the first token (could be MACRO, macro name, or label)

                // Check if this line starts a macro definition
                if (tt.equals("MACRO"))
                {
                    macro_enc = 1;   // Set flag - now inside macro definition

                    // Next token is macro name
                    tt = st.nextToken();
                    mnt[mcount] = tt;              // Store macro name in MNT
                    macroindex[mcount] = index;    // Record the MDT index for that macro

                    // Write to MNT file (macro name + MDT index)
                    p3.println(mnt[mcount] + "\t" + macroindex[mcount]);

                    // Write macro name to MDT and ADT files
                    p4.println(mnt[mcount]);
                    p5.println(mnt[mcount]);

                    mcount++;   // Increment macro count

                    // Get the next token (argument list)
                    tt = st.nextToken();

                    // Split arguments by commas (e.g. &ARG1,&ARG2)
                    StringTokenizer t = new StringTokenizer(tt, ",");

                    // Process each argument
                    while (t.hasMoreTokens())
                    {
                        arg = t.nextToken();
                        // If argument starts with '&', it’s a macro parameter
                        if (arg.charAt(0) == '&')
                        {
                            AR[arg_count] = arg;   // Store argument name
                            p5.println(AR[arg_count]);  // Write to ADT file
                            arg_count++;                // Increment argument counter
                        }
                    }
                }
                else
                {
                    // If we are inside a macro definition (after MACRO line)
                    if (macro_enc == 1)
                    {
                        // Check if macro ends
                        if (input.equals("MEND"))
                        {
                            macro_enc = 0;           // End of macro definition
                            p4.println("MEND");      // Write MEND to MDT
                        }
                        else
                        {
                            // Process the body of the macro
                            StringTokenizer t = new StringTokenizer(input, " ");

                            // Process each token in the macro body line
                            while (t.hasMoreTokens())
                            {
                                macroTokens = t.nextToken();
                                // Replace macro arguments (&ARG) with argument reference (AR index)
                                for (int i = 0; i < arg_count; i++)
                                {
                                    if (macroTokens.charAt(0) == '&' && macroTokens.equals(AR[i]))
                                    {
                                        p4.print("AR" + i);   // Write argument reference to MDT
                                    }
                                }
                                // If token is not an argument (not starting with '&'), write as is
                                if (macroTokens.charAt(0) == '&') {}
                                else
                                {
                                    p4.print(macroTokens + " ");
                                }

                                // Print a newline at the end of the line
                                if (!t.hasMoreTokens())
                                {
                                    p4.println();
                                }
                            }
                        }
                    }
                }
                // Increment line counter for each new line
                index++;
            }
            // Close all output files
            p3.close();
            p4.close();
            p5.close();
        }
        // Catch and display any file or IO errors
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
