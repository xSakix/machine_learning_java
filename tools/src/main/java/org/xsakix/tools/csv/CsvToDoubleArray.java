package org.xsakix.tools.csv;

import cern.colt.list.DoubleArrayList;

import java.io.*;
import java.util.Arrays;

public class CsvToDoubleArray {

    public static double[] read(String fileName) throws IOException {
        DoubleArrayList result = new DoubleArrayList();
        BufferedReader bufferedReader = null;
        try
        {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));

            bufferedReader.lines().forEach(line -> {

                String[] parts = line.split(",");
                if (parts[1] != null && !"null".equals(parts[1]))
                {
                    Double val = Double.valueOf(parts[1]);
                    if(((int)Math.round(val)) > 0) {
                        int len = result.size();
                        if (len == 0) {
                            result.add(val);
                        } else if (Math.floor(Math.abs(val - result.elements()[len - 1])) > 0) {
                            result.add(val);
                        }
                    }
                }
            });
            if(bufferedReader != null){
                bufferedReader.close();
            }

        } finally
        {
            if (bufferedReader != null)
            {
                bufferedReader.close();
            }
        }
        int i = 0;
        for(; i < result.size();i++){
            if(((int)Math.round(result.get(i))) == 0)
                break;
        }

        return Arrays.copyOfRange( result.elements(),0,i);
    }

    public static void main(String[] argv) throws IOException {

        double spy[] = CsvToDoubleArray.read("c:\\downloaded_data\\USD\\SPY.csv ");
        System.out.println(Arrays.toString(spy));
    }

    public static double[] loadSpyTestDataset(){
        try {
            return CsvToDoubleArray.read("c:\\downloaded_data\\USD\\SPY.csv ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
