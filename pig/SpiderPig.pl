-- Delete the result folder to avoid error when storing

rmf result

-- Load the file which contains the result of the crawler

crawlerResult = LOAD '$FILE' USING PigStorage('\\t') AS (url:chararray, rank:int, backlinks:chararray)
;

-- As the loading put the links into one big string, we need to separate each url with TOKENIZE

crawlerResult = FOREACH crawlerResult
	GENERATE url, rank, TOKENIZE(backlinks,',') AS backlinks:{link:tuple(url:chararray)}, backlinks AS strLinks
;

-- Count how many backlink there is for each url

outlinks = FOREACH crawlerResult
	GENERATE (float)rank/((float)COUNT(backlinks)) AS rank,	FLATTEN(backlinks) as url
;

-- Calculate the page rank

pageRank = FOREACH ( COGROUP outlinks BY url, crawlerResult BY url INNER)
	GENERATE FLATTEN(crawlerResult.url), ( 1 - 0.85 ) + 0.85 * SUM ( outlinks.rank ) AS rank, FLATTEN(crawlerResult.strLinks)
;

-- Store the result into the foler result/

STORE pageRank
	INTO 'result' USING PigStorage('\t')
;
