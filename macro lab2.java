import java.util.*;
import java.io.*;

public class Macro {
    public static void main(String args[]) {
        BufferedReader br;
        String input = null;
        String tt = null;
        String arg = null;
        String macroTokens = null;

        String mnt[] = new String[10];   // Macro Name Table
        String mdt[] = new String[20];   // Macro Definition Table (not fully used here)
        String AR[]  = new String[20];   // Argument List Array (ALA)

        int macroindex[] = new int[10];
        int mcount = 0;       // Number of macros
        int arg_count = 0;    // Number of arguments
        int index = 1;        // MDT index
        int macro_enc = 0;    // Macro definition encountered flag

        try {
            br = new BufferedReader(new FileReader("Input.txt"));
            File f3 = new File("mnt.txt");
            File f4 = new File("mdt.txt");
            File f5 = new File("adt.txt");

            PrintWriter p3 = new PrintWriter(f3); // MNT
            PrintWriter p4 = new PrintWriter(f4); // MDT
            PrintWriter p5 = new PrintWriter(f5); // ALA / ADT

            while ((input = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(input, " ");
                tt = st.nextToken();

                // When a MACRO definition starts
                if (tt.equals("MACRO")) {
                    macro_enc = 1;

                    // Read Macro Name
                    tt = st.nextToken();
                    mnt[mcount] = tt;
                    macroindex[mcount] = index;

                    // Write to MNT
                    p3.println(mnt[mcount] + "\t" + macroindex[mcount]);

                    // Start writing MDT & ADT entries
                    p4.println(mnt[mcount]);
                    p5.println(mnt[mcount]);

                    mcount++;

                    // Read arguments
                    tt = st.nextToken();
                    StringTokenizer t = new StringTokenizer(tt, ",");

                    while (t.hasMoreTokens()) {
                        arg = t.nextToken();
                        if (arg.charAt(0) == '&') {
                            AR[arg_count] = arg;
                            p5.println(AR[arg_count]);
                            arg_count++;
                        }
                    }
                }
                else {
                    // Inside macro body
                    if (macro_enc == 1) {
                        if (input.equals("MEND")) {
                            macro_enc = 0;
                            p4.println("MEND");
                        } else {
                            StringTokenizer t = new StringTokenizer(input, " ");

                            while (t.hasMoreTokens()) {
                                macroTokens = t.nextToken();

                                // Check if token is an argument
                                boolean isArg = false;
                                for (int i = 0; i < arg_count; i++) {
                                    if (macroTokens.charAt(0) == '&' && macroTokens.equals(AR[i])) {
                                        p4.print("AR" + i);
                                        isArg = true;
                                    }
                                }

                                // Normal tokens
                                if (!isArg && macroTokens.charAt(0) != '&') {
                                    p4.print(macroTokens + " ");
                                }

                                if (!t.hasMoreTokens()) {
                                    p4.println();
                                }
                            }
                        }
                    }
                }
                index++;
            }

            p3.close();
            p4.close();
            p5.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*MACRO INCR1 &FIRST,&SECOND
ADD AREG &FIRST
LDA BREG &SECOND
MEND
MACRO INCR2 &ARG1,&ARG2
MOV CREG &ARG1
SUB DREG &ARG2
MEND
START 100
MOV AREG A
MOV BREG B
INCR1
MOV CREG =2
MOV DREG =3
ADD AREG BREG
A DC 05
B DS 03
END */
