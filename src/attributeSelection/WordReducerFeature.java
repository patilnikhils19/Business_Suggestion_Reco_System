package attributeSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

/**
 * @author Nikhil Patil <patilnikhils19@gmail.com>
 * Apr 26, 2017
 * WordReducerFeature.java
 */
public class WordReducerFeature extends Reducer<Text,Text,Text,Text>{
	TreeMap<Integer, String> featureCount = new TreeMap<Integer,String>(); 

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		int count = 0;
		for(Text val:values){
			count++;
		}

		context.write(new Text (String.valueOf(count)), new Text(key.toString()));
	}




}