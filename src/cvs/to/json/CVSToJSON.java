
package cvs.to.json;


import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @scott purcell
 */


public class CVSToJSON extends JFrame{// simple gui window for file choice
    private static final long serialVersionUID = 1L;
    
    private static BufferedReader readFile;
    private static BufferedWriter writeFile;
    private static File CSVFileName;
    int rowCount = 0;
    
   

    public static void main(String args[]){
        CVSToJSON parse = new CVSToJSON();
        parse.convert();

        System.exit(0);
    }
     public CVSToJSON(){
        FileNameExtensionFilter filter = new FileNameExtensionFilter("comma separated values", "csv");
        JFileChooser choice = new JFileChooser();
        choice.setFileFilter(filter); //limit the files displayed

        int option = choice.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            CSVFileName = choice.getSelectedFile();
        }
        else{// if file not selected
            JOptionPane.showMessageDialog(this, "File not selected. Program will  now exit.", "System Dialog", JOptionPane.PLAIN_MESSAGE);         
            System.exit(1);
        }
    }

    private void convert(){
        /*Converts a .csv file to .json. Assumes first line is header with columns*/
        try {
            readFile = new BufferedReader(new FileReader(CSVFileName));

            String outputName = CSVFileName.toString().substring(0, 
                    CSVFileName.toString().lastIndexOf(".")) + ".json"; 
            writeFile = new BufferedWriter(new FileWriter(new File(outputName)));
              //System.out.println("trying to convert");
            String csvLine;
            String columns[]; //contains column names
            int num_cols;
            String[] entries;

            int fileReaderProgress = 0; //check fileReaderProgress percent

            
        
            //initialize all columns*******************************************************
            csvLine = readFile.readLine(); 
            columns = csvLine.split(",");
            //System.out.println("columns = " + columns);
            num_cols = columns.length;
            //System.out.println("initialized columns");

            writeFile.write("["); //begin file as array
            csvLine = readFile.readLine();

            //System.out.println("reading lines");
            while(true) {
                entries = csvLine.split(",");// split the entries

                if (entries.length == num_cols){ //if number columns equal to number entries
                    writeFile.write("{");

                    for (int k = 0; k < num_cols; ++k){ //for each column 
                        if (entries[k].matches("^-?[0-9]*\\.?[0-9]*$")){ //check if a number
                            writeFile.write("\"" + columns[k] + "\": " + entries[k]);
                            if (k < num_cols - 1) writeFile.write(", ");                                                }
                        else { //if a string
                            writeFile.write("\"" + columns[k] + "\": \"" + entries[k] + "\"");
                            if (k < num_cols - 1) writeFile.write(", ");
                        }
                    }

                    ++fileReaderProgress; //progress update
                    if (fileReaderProgress % 10000 == 0) 
                        System.out.println(fileReaderProgress); //print the progress           


                    if((csvLine = readFile.readLine()) != null){//write if not last line
                        writeFile.write("},");
                        writeFile.newLine();
                    }
                    else{
                        writeFile.write("}]");//write if last line
                        writeFile.newLine();
                        break;
                    }
                }
                else{// this fires when the number of entries exceeds the number of columns
                   
                    JOptionPane.showMessageDialog(this, "ERROR: CSV is incorrectly formatted, please fix. " + (fileReaderProgress + 2), 
                            "System Dialog", JOptionPane.PLAIN_MESSAGE);                    
                    System.exit(-1); //print error message
                }
            }

            JOptionPane.showMessageDialog(this, "Your CSV file has been succesfully converted to: "     + outputName, 
                    "System Dialog", JOptionPane.PLAIN_MESSAGE);

            writeFile.close();
            readFile.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }       
    }
    }