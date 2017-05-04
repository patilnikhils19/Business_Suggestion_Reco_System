package reviewCommentSentimentAnalysis;
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

public class WordMapperValuableUser extends Mapper<LongWritable, Text, Text, Text>{

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{

		String CurrentLine, user_id = null, 
				review_id = null, useful = null, 
				sentiScore = null, poscount = null, 
				negcount = null, businessId = null,  
				stars = null;

		CurrentLine = value.toString();
		String data[] =CurrentLine.split(",");
		businessId = data[0];
		user_id = data[1];
		review_id = data[2];
		stars = data[3];
		useful = data[4].trim();
		sentiScore = data[5];
		poscount = data[6];
		negcount = data[7];

		Text OutKey = new Text (businessId);
		Text OutValue = new Text (user_id+","+sentiScore+","+stars+","+useful);
		context.write(OutKey, OutValue);

	}
}
