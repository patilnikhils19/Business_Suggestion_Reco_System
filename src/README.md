# Project3
Staging and Analytics of Wikipedia Edit History Using Hadoop,
HBase, and the Hive Query Language

The goal of project is to stage data in HBase, and perform interactive analytics
using Hive Query Language(HQL).

The dataset working on is a subset of the Wikipedia edit dump history from the first day of
that service up until 2008-01-03. The dataset contains a collection of records where each record looks
like below:

REVISION article_id rev_id article_title timestamp [ip:]username user_id
CATEGORY list of categories

IMAGE list of images (each listed as many times as it occurs)

MAIN through OTHER cross-references to pages in other namespaces

EXTERNAL hyperlinks to pages outside Wikipedia

TEMPLATE list of all templates (each listed as many times as it occurs)

COMMENT contains the comments as entered by the author

MINOR whether the edit was marked as minor by the author

TEXTDATA word count of revision's plain text

Data Parsing

Once you have loaded the dataset into your HDFS, you should run a Map-Reduce job to convert the
format of this dataset into the format that is suitable for HBase bulk loading. HBase supports the CSV
file format for bulk loading. As a part of this process, you should write your WikiHBaseInputFormat
that provides suitable InputSplit and RecordReader for this dataset.

Each record contains 13 lines of text data separated by newline
characters. Therefore, the object created by Hadoop’s RecordReader does not encompass complete
contents of a record. You are required to write your own WikiHBaseInputFormat to generate correct
inputs for your mapper. Your mapper should parse the record and generate a string that is compatible
with the CSV format.

Bulk Import
load your data (formatted as CSV) into your HBase table. HBase provides a bulk
loading feature that allows you to import in your data directly from HDFS into an HBase table. You
may use the importtsv utility in HBase to do.

Before you import data into an HBase table, you are required to create a table (structure of the table is
up to you) by using an HBase Shell or through client-side APIs.
You will then import this dataset into a table in HBase.

To perform interactive analytics over the data stored in your HBase, use the Hibernate Query
Language that Hive provides, whose syntax is very similar to SQL.
Your command line queries (using HQL) should provide accurate results. You may combine outputs
from multiple queries to get your results
