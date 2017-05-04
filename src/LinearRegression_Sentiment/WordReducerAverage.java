//OutPut Business_Id \t Stars \t Categories \t FeatureCount \t ReviewCount

package LinearRegression_Sentiment;
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


public class WordReducerAverage extends Reducer<Text,Text,Text,Text>{

	public static Map<String, String> star = new HashMap<String, String>();

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		
		//HashMap<String, String> star = new HashMap<String, String>();
		String line = null;
		if (star.isEmpty()){
			Path path = new Path(context.getConfiguration().get("starfile"));
			FileSystem f = FileSystem.get(new Configuration());
			BufferedReader reader = new BufferedReader(new InputStreamReader(f.open(path)));

			while ((line = reader.readLine()) != null){
				String Data[] =  line.toString().split("\t");
				star.put(Data[0],Data[1]);
			}
			reader.close();
		} 
		
		float sentiscore= 0, count = 0; 
		for(Text val:values){
			String a = val.toString();
			sentiscore = sentiscore + Float.valueOf(a);
			count++;
			
		}
		
		float average = (sentiscore/count);
		if (count>15){
		if(star.containsKey(key.toString())){
			context.write(new Text(key.toString()), new Text (star.get(key.toString())+"\t"+average));
		}
		}
	}
}
