# Installation

Git clone this project

Then go into the folder and type mvn install

# Run

The following are the different executions of the project

## Crawler

To crawl and gather every links from a website, type:

java -jar target/JohnnyDoop.jar -crawl 'http://mywebsite.com' 'myResults.txt' 'depth'

(The optimal depth is 2)

## Page Rank (Hadoop)

To rank the links with the page rank algorithm using Hadoop, type:

*mvn clean install package
*java -jar target/JohnnyDoop.jar -rank myUrls.txt

## Page Rank (Pig)
To rank the links using Pig, type:

pig -x local -p FILE=myResults.txt pig/SpiderPig.pl
