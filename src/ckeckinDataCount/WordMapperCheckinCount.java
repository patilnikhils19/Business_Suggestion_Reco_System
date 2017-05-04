package ckeckinDataCount;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.util.*;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.json.JSONArray;
import org.json.JSONObject;


	/**
	 * @author Nikhil Patil <patilnikhils19@gmail.com>
	 * Apr 6, 2017
	 * WordMapperCheckinCount.java
	 */

public class WordMapperCheckinCount extends Mapper<LongWritable, Text, Text, Text>{

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{

		String CurrentLine, attributes = "\t";
		String type = null, businessId = null;
		Set<String> set = new HashSet<String>();
		CurrentLine = value.toString();
		JSONObject json;

		
		int friCount=0, satCount=0, sunCount=0, monCount=0, tueCount=0, wedCount=0, thuCount=0; 
		int totalCount=0;

		try {
			json = new JSONObject(CurrentLine);
			businessId = (String) json.get("business_id");
			type = (String) json.get("type");
			
			JSONArray businessTimeArray = json.getJSONArray("time");

			for (int i=0; i<businessTimeArray.length(); i++){
				String item = businessTimeArray.getString(i).toString();
				//		    		String[] dayFreq = item.split(":");
				if (item.contains("Fri")){
					//			    		if (Integer.parseInt(item.split("-")[1]){}
					friCount = friCount + Integer.parseInt(item.split(":")[1]);	
				} // fri
				if (item.contains("Sat")){
					satCount = satCount + Integer.parseInt(item.split(":")[1]);	
				} // sat
				if (item.contains("Sun")){
					sunCount = sunCount + Integer.parseInt(item.split(":")[1]);	
				} // sun
				if (item.contains("Mon")){
					monCount = monCount + Integer.parseInt(item.split(":")[1]);	
				} // mon
				if (item.contains("Tue")){
					tueCount = tueCount + Integer.parseInt(item.split(":")[1]);	
				} // tue
				if (item.contains("Wed")){
					wedCount = wedCount + Integer.parseInt(item.split(":")[1]);	
				} // wed
				if (item.contains("Thu")){
					thuCount = thuCount + Integer.parseInt(item.split(":")[1]);	
				} // thu

			} 
			
			totalCount = satCount + sunCount + monCount + tueCount + wedCount + thuCount + friCount;


		} catch (Exception e) {
			e.printStackTrace();
		}

		Text OutKey = new Text (businessId + "==" + totalCount);

		Text OutValue = new Text ("one");
		context.write(OutKey, OutValue);

	} 
}
	