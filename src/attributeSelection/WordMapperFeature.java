package attributeSelection;
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
 * WordMapperFeature.java
 */

public class WordMapperFeature extends Mapper<LongWritable, Text, Text, Text>{

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{


		String businessId = null,feature = null,stars = null;

		String CurrentLine = value.toString();
		String[] data = CurrentLine.split("\t");
		businessId = data[0];
		stars = data[1];
		feature = data[2];

		Text OutKey = new Text (feature);
		Text OutValue = new Text (businessId+","+stars);
		context.write(OutKey, OutValue);

	}
}
