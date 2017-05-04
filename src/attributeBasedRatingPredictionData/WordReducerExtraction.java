//OutPut Business_Id \t Stars \t Categories \t FeatureCount \t ReviewCount

package attributeBasedRatingPredictionData;
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

/**
 * @author Nikhil Patil <patilnikhils19@gmail.com>
 * Apr 26, 2017
 * WordReducer.java
 */
public class WordReducerExtraction extends Reducer<Text,Text,Text,Text>{


	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		String line = null;
		for(Text val:values){
			line = val.toString();
		}
		context.write(new Text(key.toString()), new Text (line));
	}
}