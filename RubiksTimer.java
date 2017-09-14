import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.text.DecimalFormat;


public class RubiksTimer{
	NumberFormat formatter;
	NumberFormat intFormat;

	public RubiksTimer(){
		formatter = new DecimalFormat("#00.000");
		intFormat = new DecimalFormat("#00");
		//System.out.print(intFormat.format(1));
		//readTimes();
		//System.out.println(convertMinute(68.4));
	}



	public long startWatch(){
		long startTime = System.currentTimeMillis();
		return startTime;
	}

	public long endWatch(){
		long endTime = System.currentTimeMillis();
		return endTime;
	}

	public double getTime(long e, long s){
		double seconds = (e - s) / 1000.0;
		return seconds;
	}

	public String convertMinute(double time){
		if (time < 60)
			return time + "";

		int min = 0;
		int sec = 0;
		//System.out.println(formatter.format(time));
		String timeString = formatter.format(time);
		timeString = timeString.substring(timeString.length() - 3, timeString.length());
		int timeInt = (int) time;

		min = timeInt / 60;
		sec =  timeInt % 60;

		return  min + ":" + intFormat.format(sec) + "." + timeString;

	}

	public double getAverage(List<Double> times){
		double total = 0.0;
		double average = 0.0;
		for (int i = 0; i < times.size(); i++){
			total += times.get(i);
		}
		average = total / (times.size() + 0.0);
		formatter.format(average);

		return average;
	}

	public double getBest(List<Double> times){
		double min = times.get(0);
		for (int i = 0; i < times.size(); i++){
			if (times.get(i) < min)
				min = times.get(i);
		}
		return min;
	}

	public void writeTimes(List<Double> times, String type){
		String fileName = type + ".txt";
		FileWriter fw = null;
		BufferedWriter bw = null;

		try{
			fw = new FileWriter(fileName, true);
			bw = new BufferedWriter(fw);

			for (int i = 0; i < times.size(); i++){
				String timeStamp = Double.toString(times.get(i));
				bw.write(timeStamp);
				bw.newLine();
			}
		}
		catch (IOException e){
			System.out.print("caught");
		}
		finally{
			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public ArrayList<Double> readTimes(String type){
  	String fileName = type + ".txt";

  	String line = null;
		double time = 0.0;
		ArrayList<Double> finalTimes = new ArrayList<Double>();

    try {
      // FileReader reads text files in the default encoding.
      FileReader fileReader = new FileReader(fileName);

        // Always wrap FileReader in BufferedReader.
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      while((line = bufferedReader.readLine()) != null) {
				time = Double.parseDouble(line);
				finalTimes.add(time);
      }

      // Always close files.
      bufferedReader.close();
    }
    catch(FileNotFoundException ex) {
      System.out.println("Unable to open file '" + fileName + "'");
    }
    catch(IOException ex) {
      System.out.println("Error reading file '"  + fileName + "'");
    }
		return finalTimes;
	}

  public static void main(String[] args){
    RubiksTimer test = new RubiksTimer();
  }

}
