///////////////////////////////////////////////////////////////////////////////
// ICT_Generator.java
//
// Author: Rehan Iqbal
// Email: Rehan_Iqbal@mentor.com
// Date: 09/28/2017
// Company: Mentor Graphics Corp.
//
// This program writes out an NFS file suitable for importing into an ICT
// table within DX Designer. The user must specify two integer arguments:
//
// Syntax: ICT_Generator <numBlocsMin> <numbBlocksMax>
//
// User must also edit the string arrays below to add the properties, values,
// nets, and global nets desired. This is hardcoded to work with the SMB_wCRFP.1
// Block but can be tweaked to work with other types of Blocks.
//
///////////////////////////////////////////////////////////////////////////////

import java.io.FileWriter;
import java.io.IOException;

public class ICT_Generator {

    public static void main(String args[]) {

        FileWriter file;
        String filepath;

        int numBlocksMin, numBlocksMax;

        // This array specifies which properties to add in the NSF file's header
        String[] headerProps = {"%Partition%",
                                "%RefDes Renumber@SYM@BLOCK%",
                                "%RefDes Renumber@SYM@PATH%",
                                "%Reuse Cell Name@SYM@BLOCK%",
                                "%Symbol Name%"};

        // This array specifies the values for the properties listed above
//        String[] headerValues = {"Logical_Physical",
//                                "True",
//                                "False",
//                                "SMB_wCRFP",
//                                "SMB_wCRFP.1"};

        String[] headerValues = {"Logical_Physical",
                "True",
                "False",
                "LP_BLOCK_A",
                "LP_BLOCK_A.1"};

        // This array specifies all pins within a Block
//        String[] netlistPins = {"ALT", "RFI1", "RFI2", "RFO1",
//                                "RFO2", "RFO3", "RFO4"};
        String[] netlistPins = {"BLOCK_A_IN","BLOCK_A_OUT"};

        // This array specifies the net names which should connect the pins
        // These net names will be prefixed with "BLK_##_"

//        String[] netlistNets = {"ALT", "RFIN_1", "RFIN_2", "RFOUT_1",
//                                "RFOUT_2", "RFOUT_3", "RFOUT_4"};
        String[] netlistNets = {"IN","OUT"};

        // This array species global nets - they will automatically connect
        // to pins with the same name
//        String[] globalNets = {"VCC", "GND"};

        ///////////////
        // Main Loop //
        ///////////////

        try {

            if (args.length != 3) {
                System.out.println("You need to provide three arguments!");
                System.out.println("Command syntax - ICT_Generator <numBlocksMin> <numBlocksMax> <filepath>");
                throw new Exception();
            }

            // parse the two input arguments for an integer....
            // if none is found, throws NumberFormatException
            numBlocksMin = Integer.parseInt(args[0]);
            numBlocksMax = Integer.parseInt(args[1]);

            filepath = args[2];

            // do some error checking on the arguments to make
            // sure they're in reasonable range
            if (numBlocksMin > numBlocksMax) {
                System.out.println("Minimum number needs to be smaller than the maximum!");
                System.out.println("Command syntax - ICT_Generator <numBlocksMin> <numBlocksMax> <filepath>");
                throw new Exception();
            }

            else if ((numBlocksMin < 0) || (numBlocksMax < 0)) {
                System.out.println("Arguments need to be greater than 0!");
                System.out.println("Command syntax - ICT_Generator <numBlocksMin> <numBlocksMax> <filepath>");
                throw new Exception();
            }

            else if ((numBlocksMin > 4096) || (numBlocksMax > 4096)) {
                System.out.println("Too many blocks - reduce to less than 4096!");
                System.out.println("Command syntax - ICT_Generator <numBlocksMin> <numBlocksMax> <filepath>");
                throw new Exception();
            }

            // make sure filepath is not null
            if (filepath == null || filepath.trim().isEmpty()) {
                System.out.println("You need to specify a path!");
                System.out.println("Command syntax - ICT_Generator <numBlocksMin> <numBlocksMax> <filepath>");
                throw new IOException();
            }

            // make sure the filepath ends in a .txt or .nfs extension
            // this will encourage user to check filepath e.g. not plug in an integer mistakenly
            else if ( !filepath.substring(filepath.length()-4).matches("\\.(txt|nfs|TXT|NFS)") ) {
                System.out.println("You need to specify a filepath ending in .txt or .nfs!");
                System.out.println("Current filepath ending: " + filepath.substring(filepath.length()-4));
                System.out.println("Command syntax - ICT_Generator <numBlocksMin> <numBlocksMax> <filepath>");
                throw new IOException();
            }

            // check file access to Desktop location
            // if denied, throws IOException
            file = new FileWriter(filepath);

            ////////////////////////
            // Writing the Header //
            ////////////////////////

            // Write the header using the arrays at the top
            // Outer loop on the property names...
            for (int i = 0; i < headerProps.length; i ++) {
                file.write(headerProps[i]);

                // Inner loop on the property values...
                for (int j = numBlocksMin; j <= numBlocksMax; j++) {
                    file.write("\t" + headerValues[i]);
                }

                file.write("\n");
            }

            /////////////////////////
            // Writing the Netlist //
            /////////////////////////

            // Loop 1: covers the range of Blocks to iterate over
            for (int i = numBlocksMin; i <= numBlocksMax; i++) {

                // Loop 2: covers the range nets/pins to iterate over
                for (int j = 0; j < netlistNets.length; j++) {
                    file.write("BLK_" + i + "_" + netlistNets[j]);

                    // Loop 3: indents the pin name over correctly
                    for (int k = 0; k <= (i - numBlocksMin); k++) {
                        file.write("\t");
                    }
                    file.write(netlistPins[j] + "\n");
                }
            }

            /////////////////////////////
            // Writing the Global Nets //
            /////////////////////////////

            // Loop 1: covers the range of global nets to iterate over
//            for (int i = 0; i < globalNets.length; i++) {
//                file.write(globalNets[i]);
//
//                // Loop 2: populates the global net in each column
//                for (int j = numBlocksMin; j <= numBlocksMax; j++) {
//                    file.write("\t" + globalNets[i]);
//                }
//                file.write("\n");
//            }

            // Finished writing to file...
            // Go ahead and exit program cleanly
            file.close();
            System.out.println("NFS file successfully created! Review contents before importing into ICT.");
            System.exit(1);
        }

        ////////////////
        // Exceptions //
        ////////////////

        catch (IOException ioe) {
            System.out.println("There was an I/O exception! Check file path specified for the output.");
            ioe.printStackTrace();
            System.exit(0);
        }

        catch (NumberFormatException nfe) {
            // The arguments aren't a valid integer.  Print
            // an error message, then exit with an error code.
            System.out.println("The arguments must be an integer.");
             nfe.printStackTrace();
            System.exit(0);
        }

        catch (ArrayIndexOutOfBoundsException ae) {
            System.out.println("There was an array out-of-bounds exception - check the property array sizes.");
            ae.printStackTrace();
            System.exit(0);
        }

        catch (Exception e) {
            System.out.println("There was an exception - review stack trace.");
            e.printStackTrace();
            System.exit(0);
        }

    } // main

} // ICT_Generator