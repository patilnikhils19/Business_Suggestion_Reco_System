package attributeBasedRatingPredictionData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.util.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * @author Nikhil Patil <patilnikhils19@gmail.com>
 * Apr 26, 2017
 * WordMapperExtraction.java
 */

public class WordMapperExtraction extends Mapper<LongWritable, Text, Text, Text>{
	
	public static Map<String, String> features = new HashMap<String, String>();
	

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		
		String line = null;
		if (features.isEmpty()){
			Path path = new Path(context.getConfiguration().get("allFeatures"));
			FileSystem f = FileSystem.get(new Configuration());
			BufferedReader reader = new BufferedReader(new InputStreamReader(f.open(path)));

			while ((line = reader.readLine()) != null){
				String Data[] =  line.toString().split("\t");
				features.put(Data[1],Data[0]);
			}
			reader.close();
		} 

		String CurrentLine;
		String businessId = null, stars = null;
		Map<String, String> featuresPresent = new HashMap<String, String>();
		CurrentLine = value.toString();
		JSONObject json;
		try {
			json = new JSONObject(CurrentLine);
			businessId = (String) json.get("business_id");
			stars = json.get("stars").toString();
			JSONArray cat = json.getJSONArray("categories");
			JSONArray attr = json.getJSONArray("attributes");
			for (int i = 0; i < attr.length(); i++) {
				String features = attr.getString(i).toString();
				String[] split = features.split(":");	
				for(int j =0; j<split.length;j++){
					if (split[j].replaceAll("[^a-zA-Z0-9 ]", "").contains("True")){
						featuresPresent.put(split[0], "1");
						/*Text OutKey = new Text (businessId+"\t"+stars);
						Text OutValue = new Text (split[0]);
						context.write(OutKey, OutValue);*/
						break;
					}
				}
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String featureValues = ",";
		for (String keys: features.keySet()){
			if (featuresPresent.containsKey(keys)){
				
				featureValues = featureValues+"1,";

			}
			else{
				featureValues = featureValues+"0,";
				
			}
		}
		
		Text OutKey = new Text (businessId+","+stars);
		Text OutValue = new Text (featureValues);
		context.write(OutKey, OutValue);

		
	}
}