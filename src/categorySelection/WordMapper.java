package categorySelection;
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
 * WordMapper.java
 */

public class WordMapper extends Mapper<LongWritable, Text, Text, Text>{

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{

		String CurrentLine;
		String businessId = null, stars = null;

		CurrentLine = value.toString();
		JSONObject json;
		try {
			json = new JSONObject(CurrentLine);
			businessId = (String) json.get("business_id");
			stars = json.get("stars").toString();
			JSONArray cat = json.getJSONArray("categories");
			JSONArray attr = json.getJSONArray("attributes");
			for (int i = 0; i < cat.length(); i++) {
				String category = cat.getString(i).toString();
				Text OutKey = new Text (businessId+"\t"+stars);
				Text OutValue = new Text (category);
				context.write(OutKey, OutValue);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
