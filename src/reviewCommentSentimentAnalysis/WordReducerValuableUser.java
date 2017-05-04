//OutPut Business_Id \t Stars \t Categories \t FeatureCount \t ReviewCount

package reviewCommentSentimentAnalysis;
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
 * Apr 6, 2017
 * WordReducerValuableUser.java
 */


public class WordReducerValuableUser extends Reducer<Text,Text,Text,Text>{

	//public static TreeMap<String, String> sentiScore = new TreeMap<String, String>();
	//public static TreeMap<String, String> useful = new TreeMap<String, String>();

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		TreeMap<String, String> sentiScore = new TreeMap<String, String>();
		for(Text val:values){
			String a = val.toString();
			String valueData[] = a.split(",");
			String userId = valueData[0];
			String usefulvalue = valueData[3];
			String star = valueData[2];
			String sentiScorevalue = valueData[1];
			sentiScore.put(star+"\t"+sentiScorevalue+"\t"+usefulvalue, userId);

		}
		int topNusers = 0;
		for(String D : sentiScore.descendingKeySet()){
			topNusers++;
			context.write(new Text(key.toString()), new Text (sentiScore.get(D)+"\t"+D));
			if (topNusers == 10){
				break;
			}
		}
		/*for(Text val:values){
			context.write(new Text(key.toString()), new Text (val.toString()));
			}*/
	}
}
