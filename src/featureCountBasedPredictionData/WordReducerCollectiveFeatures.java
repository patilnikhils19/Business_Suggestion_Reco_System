//OutPut Business_Id \t Stars \t Categories \t FeatureCount \t ReviewCount

package featureCountBasedPredictionData;
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
 * WordReducerCollectiveFeatures.java
 */
public class WordReducerCollectiveFeatures extends Reducer<Text,Text,Text,Text>{

	public static Map<String, String> reviewCount = new HashMap<String, String>();
	public static Map<String, String> checkinCount = new HashMap<String, String>();

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		String line = null, rCount = null;
		if (reviewCount.isEmpty()){
			Path path = new Path(context.getConfiguration().get("userReviewCount"));
			FileSystem f = FileSystem.get(new Configuration());
			BufferedReader reader = new BufferedReader(new InputStreamReader(f.open(path)));

			while ((line = reader.readLine()) != null){
				String Data[] =  line.toString().split("\t");
				reviewCount.put(Data[0],Data[1]);
			}
			reader.close();
		} 

		String line1 = null, cCount = null;
		if (checkinCount.isEmpty()){
			Path path = new Path(context.getConfiguration().get("userCheckinCount"));
			FileSystem f = FileSystem.get(new Configuration());
			BufferedReader reader = new BufferedReader(new InputStreamReader(f.open(path)));
			while ((line1 = reader.readLine()) != null){
				String Data[] =  line1.toString().split("==");
				checkinCount.put(Data[0],Data[1]);
			}
			reader.close();
		} 


		int count = 0;
		for(Text val: values) {
			count++;
		}
		String keys = key.toString();
		String[] keySplit = keys.split("\t");
		String findValue = (String) keySplit[0];
		//if(reviewCount.containsKey(key.toString().split("\t")[0].toString())){

		if(reviewCount.containsKey(findValue)){
			//rCount = reviewCount.get(key.toString().split("\t")[0].toString());		
			rCount = reviewCount.get(findValue);	
		}
		else{rCount= "0";}

		if(checkinCount.containsKey(findValue)){
			//rCount = reviewCount.get(key.toString().split("\t")[0].toString());		
			cCount = checkinCount.get(findValue);	
		}
		else{cCount = "0";}

		//context.write(key, new IntWritable(count+"\t"+ rCount));
		context.write(new Text(key.toString()), new Text (count+"\t"+ rCount +"\t"+cCount));
	}
}