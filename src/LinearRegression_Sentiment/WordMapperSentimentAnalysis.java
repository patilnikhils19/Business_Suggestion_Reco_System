package LinearRegression_Sentiment;
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
 * WordMapperSentimentAnalysis.java
 */

public class WordMapperSentimentAnalysis extends Mapper<LongWritable, Text, Text, Text>{

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{


		String businessId = null,review_id = null,user_id = null,business_id = null,stars = null, 
				date = null,text = null,useful = null,funny = null,cool= null,type = null;
		Set<String> set = new HashSet<String>();
		String CurrentLine = value.toString();
		JSONObject json;
		try {
			json = new JSONObject(CurrentLine);
			businessId = (String) json.get("business_id");
			review_id = (String) json.get("review_id");
			user_id = (String) json.get("user_id");
			stars = json.get("stars").toString(); 
			//date = (String) json.get("date");
			text = (String) json.get("text");
			useful = json.get("useful").toString();
			//funny = (String) json.get("funny");
			//cool = (String) json.get("cool");
			//type = (String) json.get("type");

		} catch (Exception e) {
			e.printStackTrace();
		}

		Text OutKey = new Text (businessId);
		Text OutValue = new Text (text);
		context.write(OutKey, OutValue);

	}
}
