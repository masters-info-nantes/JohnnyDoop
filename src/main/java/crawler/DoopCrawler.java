package crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Set;

/**
 * Created by nicolas on 12/6/15.
 */
public class DoopCrawler extends WebCrawler {

    public static String outputLines = "";

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            Boolean first = true;

            outputLines += url;
            System.out.println(url);
            // rank set to 1
            outputLines += "\t1\t";
            // // nb of outgoing link
            // outputLines += " " + links.size() + " ";
            for (WebURL webURL : links) {
                if (first) {
                    first = false;
                    // each outgoing link for url page key
                    outputLines += webURL.getURL();
                } else {
                    // each outgoing link for url page key
                    outputLines += "," + webURL.getURL();
                }

            }
            outputLines.replace("\n","");
            outputLines += "\n";
        }
    }

    public void crawl (String seed, String outputFile, int depth) {
        String crawlStorageFolder = "../tmp";
        int numberOfCrawlers = 8;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);

        config.setMaxDepthOfCrawling(Integer.valueOf(depth));

		/*
		 * Instantiate the controller for this crawl.
		 */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller;
        try {
            controller = new CrawlController(config, pageFetcher, robotstxtServer);


            controller.addSeed(seed);

			/*
			 * Start the crawl. This is a blocking operation, meaning that your
			 * code will reach the line after this only when crawling is
			 * finished.
			 */
            System.out.println("Crawler is running");



            controller.start(this.getClass(), numberOfCrawlers);



            File file = new File(System.getProperty("user.dir") + "/"+outputFile);
            FileUtils.deleteDirectory(file);

            file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(DoopCrawler.outputLines);

            bw.close();
            System.out.println("Finish");
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
