package ckeckinDataCount;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
		
		/**
		 * @author Nikhil Patil <patilnikhils19@gmail.com>
		 * Apr 6, 2017
		 * WordReducerCheckinCount.java
		 */

		public class WordReducerCheckinCount extends Reducer<Text,Text,Text,Text>{
				public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
						int count = 0;
						for(Text val: values) {
							count++;
						}
						context.write(key, new Text(""));
				}
}