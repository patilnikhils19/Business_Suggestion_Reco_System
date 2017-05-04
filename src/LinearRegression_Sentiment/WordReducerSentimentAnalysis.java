package LinearRegression_Sentiment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
 * WordReducerSentimentAnalysis.java
 */
public class WordReducerSentimentAnalysis extends Reducer<Text,Text,Text,Text>{


	public static Map<String, String> posWord = new HashMap<String, String>();
	public static Map<String, String> negWord = new HashMap<String, String>();

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		String line = null;
		if (posWord.isEmpty()){
			Path path = new Path(context.getConfiguration().get("posWords"));
			FileSystem f = FileSystem.get(new Configuration());
			BufferedReader reader = new BufferedReader(new InputStreamReader(f.open(path)));

			while ((line = reader.readLine()) != null){
				posWord.put(line.toString(),"1");
			}
			reader.close();
		} 

		String line1 = null;
		if (negWord.isEmpty()){
			Path path = new Path(context.getConfiguration().get("negWords"));
			FileSystem f = FileSystem.get(new Configuration());
			BufferedReader reader = new BufferedReader(new InputStreamReader(f.open(path)));

			while ((line1 = reader.readLine()) != null){
				negWord.put(line1.toString(),"1");
			}
			reader.close();
		} 

		Map<String, String> SaveData = new HashMap<String, String>();

		for(Text val: values) {
			float poscount = 0, negcount = 0;
			float sentiScore = 0;
			String Sentance = val.toString().toLowerCase();
			String commentData = Sentance.replaceAll("[^a-zA-Z0-9 ]", "").trim();
			String [] comment = commentData.split(" ");
			for(int i=0; i< comment.length; i++){
				if(posWord.containsKey(comment[i])){
					poscount++;
				}
				if(negWord.containsKey(comment[i])){
					negcount++;
				}
			}
			if(poscount+negcount != 0){
				sentiScore =  (float) ((poscount-negcount)/(poscount+negcount));
			}
			SaveData.put(String.valueOf(poscount+","+negcount),String.valueOf(sentiScore));
		}
		for(String A: SaveData.keySet()) {

			context.write(new Text(key.toString()), new Text (SaveData.get(A)));
		}
	} 



}