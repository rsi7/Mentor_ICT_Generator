# ICT Generator

## Description
This is a program that will generate a .NFS file used for importing Managed Block connectivity into an ICT table. The number of Blocks can be specified from command-line arguments, along with the location of the output file.

You will probably need to tweak the source Java code and recompile to fit your needs (i.e. different Block types, adding components & nets, changing properties, etc). This is provided as a sample program to extend & modify.

## Syntax

There is a sample .JAR Java applet provided which generates connectivity for the *SMB_wCRFB* Managed Blocks. This applet can be run from the command line:

`java -jar ICT_Generator.jar <minBlocks> <maxBlocks> <filepath>`

* `minBlocks` specifies the lowest number Block to start with
* `maxBlocks` specifies the hight number Block to end with
* `filepath` is the output file path - must end with a .NFS or .TXT extension

So generating the connectivity for Blocks 64 through 128 to my desktop might look like:

`java -jar ICT_Generator.jar 64 128 "C:\Users\myname\Desktop\file.nfs"`

## Design Process

+ Create a small ICT table modeling the connectivity you want on a small number of Blocks
+ Export to text file through **RMB > Export Connectivity > To File** and examine the contents to get a feel for how to format
+ Tweak the `ICT_Generator.java` code to match your requirements - you'll need to look at:
  * Managed Block symbols & their properties
  * Nets (global & signal)
  * Pin Connections
+ Recompile & run the program to generate the NFS text file
+ Examine the NFS file in a text editor and confirm it matches your requirements - otherwise continuing tweaking code
+ Import into the ICT table through **RMB> Import Connectivity > From File**
+ Package the design from **Tools > Package** and confirm there are no errors
+ Forward-annotate to layout
+ Place the Blocks into the Board manually, or automatically with the keyin `pr -file <placement_file>`

## Sample output

Output will be a text file containing a header with Block properties, followed by a matrix with connectivity:

|                             |                  |                  |                  |                  |
| --------------------------- |:----------------:| :---------------:| :---------------:| :---------------:|
| %Partition%                 | Logical_Physical | Logical_Physical | Logical_Physical | Logical_Physical |
| %RefDes Renumber@SYM@BLOCK%	|       true       |       true       |       true       |        true      |
| %RefDes Renumber@SYM@PATH%  |      false       |      false       |      false       |      false       |
| %Reuse Cell Name@SYM@BLOCK% |     SMB_wCRFP    |     SMB_wCRFP    |     SMB_wCRFP    |     SMB_wCRFP    |
| %Symbol Name%               |    SMB_wCRFP.1   |    SMB_wCRFP.1   |    SMB_wCRFP.1   |    SMB_wCRFP.1   |
| BLK_64_NET_IN_1             |        IN1       |                  |                  |                  |
| BLK_64_NET_OUT_1            |        OUT1      |                  |                  |                  |
| BLK_65_NET_IN_1             |                  |        IN1       |                  |                  |
| BLK_65_NET_OUT_1            |                  |        OUT1      |                  |                  |

## Important Files

Many of the files are programming IDE files which you can ignore - the relevant files are:

* `ICT_Generator.java` - this is the source Java code you'll need to tweak to match your requirements (or rewrite in your preferred language)
* `ICT_Generator.jar` - this is a portable application that can be run on any Windows desktop. It generates connectivity for the *SMB_CRFB.1* Managed Block symbol.
* `ICT_Coordinates.xlsx` - Excel workbook used to generate the Block coordinates. Puts them in a big square (i.e. 16 x 16).
* `ICT_Coordinates.csv` - CSV export from Excel of the coordinates
* `ICT_Coordinates.txt` - this is a placement file suitable for Xpedition. Identical to the CSV except commas were replaced with spaces.
