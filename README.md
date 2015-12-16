# Installation

Git clone this project

Then go into the folder and type mvn install

# Run

After mvn install, to launch the default crawling execution, type:

java -jar target/JohnnyDoop.jar

# Parameters

## Crawler

java -jar target/JohnnyDoop.jar -crawl 'http://mywebsite.com' 'myResults.txt' 'depth'

The optimal depth is 2

## Page Rank

java -jar target/JohnnyDoop.jar -rank myUrls.txt