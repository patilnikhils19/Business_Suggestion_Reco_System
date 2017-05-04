package featureCountBasedPredictionData;
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
 * Apr 26, 2017
 * WordMapperCollectiveFeatures.java
 */

public class WordMapperCollectiveFeatures extends Mapper<LongWritable, Text, Text, Text>{

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{

		String CurrentLine, state = null, city = null, attributes = "\t";
		String pinCode = null, businessId = null, businessName = null, stars = null, categories = null;
		//Set<String> set = new HashSet<String>();
		//CurrentLine = "{\"business_id\":\"0DI8Dt2PJp07XkVvIElIcQ\",\"name\":\"Innovative Vapors\"}";

		//String CurrentLine, businessId = null;
		CurrentLine = value.toString();
		JSONObject json;
		try {
			json = new JSONObject(CurrentLine);
			businessId = (String) json.get("business_id");
			businessName = (String) json.get("name");
			stars = json.get("stars").toString();
			JSONArray cat = json.getJSONArray("categories");
			JSONArray attr = json.getJSONArray("attributes");
			for (int i = 0; i < attr.length(); i++) {
				String features = attr.getString(i).toString();
				String[] split = features.split(":");																								
				if (split[1].toString().contains("True")){
					Text OutKey = new Text (businessId+"\t"+stars+"\t"+cat.length());
					Text OutValue = new Text (split[0]);
					context.write(OutKey, OutValue);
				}
			}
			//city = (String) json.get("city");
			//pinCode = (String) json.get("postal_code");


		} catch (Exception e) {
			e.printStackTrace();
		}


		/*	Text OutKey = new Text (attributes);
			Text OutValue = new Text ("one");
			context.write(OutKey, OutValue);*/

	}
}
