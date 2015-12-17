import crawler.DoopCrawler;

/**
 * Created by nicolas on 12/15/15.
 */
public class JohnnyDoop {


    public JohnnyDoop(String inputFile) {

    }


    public static void main(String[] args) {

        /*
            args[0] -> command
            args[1] -> crawler seed
            args[2] -> file output
            args[3] -> depth
         */
        
        //Launching with default parameters
        if(args.length==0)
        {
            System.out.println("Default launch without parameter, crawling ulule.com with a depth of 2, check the result in ulule.com.txt");
            args = new String[]{"crawl","http://ulule.com","ulule.com.txt","1"};
        }
        
        switch (args[0]){
            case "crawl":
                if(args.length < 4) {
                    System.out.println("Missing parameters for crawler, example: -crawl 'http://website.com' 'output_file.txt' 'depth'");
                    System.exit(0);
                }

                DoopCrawler doopCrawler = new DoopCrawler();
                doopCrawler.crawl(args[1], args[2], Integer.valueOf(args[3]));
                break;

            case "rank":
                if(args.length<2)
                {
                    System.out.println("Please specify the file with crawled urls example: -rank myResults.txt");
                    System.exit(0);
                }
                new JohnnyDoop(args[1]);
                break;
        }
        

    }
}
