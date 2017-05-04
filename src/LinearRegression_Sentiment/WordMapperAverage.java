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
 * WordMapper.java
 */

public class WordMapperAverage extends Mapper<LongWritable, Text, Text, Text>{

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{

		String CurrentLine,
				sentiScore = null,businessId = null;

		CurrentLine = value.toString();
		String data[] =CurrentLine.split("\t");
		businessId = data[0];
		sentiScore = data[1];

		Text OutKey = new Text (businessId);
		Text OutValue = new Text (sentiScore);
		context.write(OutKey, OutValue);

	}
}
