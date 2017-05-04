package reviewCommentSentimentAnalysis;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.*;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
//import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
//import org.json.JSONObject;
//import org.json.JSONException;

/**
 * @author Nikhil Patil <patilnikhils19@gmail.com>
 * Apr 6, 2017
 * MainClass.java
 */
public class MainClassSentimentAnalysis {

	private static final String OUT_PATH1="OUTPUTJOB1-WordCount";

	public static void main(String[] args) throws IOException, ClassNotFoundException,InterruptedException {
		if (args.length != 2) {
			System.out.printf("Usage: <jar file> <input dir> <output dir>\n");
			System.exit(-1);
		}

		Configuration conf =new Configuration();
		DistributedCache.addFileToClassPath(new Path("/projectData/json-20160212.jar"), conf); 
		conf.set("negWords", "hdfs://nashville:30261" + "/projectData/neg-words.txt");
		conf.set("posWords", "hdfs://nashville:30261" + "/projectData/pos-words.txt");
		Job job=Job.getInstance(conf);
		job.addFileToClassPath(new Path("/projectData/json-20160212.jar"));
		job.setJarByClass(MainClassSentimentAnalysis.class);
		job.setMapperClass(WordMapperSentimentAnalysis.class);
		job.setReducerClass(WordReducerSentimentAnalysis.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(OUT_PATH1));
		if (job.waitForCompletion(true)) System.out.println("Job One Completed");



		Job job1=Job.getInstance(conf);
		job1.setJarByClass(MainClassSentimentAnalysis.class);
		job1.setMapperClass(WordMapperValuableUser.class);
		job1.setReducerClass(WordReducerValuableUser.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(Text.class);
		job1.setInputFormatClass(TextInputFormat.class);
		job1.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.setInputPaths(job1, new Path(OUT_PATH1));
		FileOutputFormat.setOutputPath(job1, new Path(args[1]));
		if (job1.waitForCompletion(true)) System.out.println("Job Two Completed ");

		System.exit(job1.waitForCompletion(true) ? 0 : 1);
	}
}