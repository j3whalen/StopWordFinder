import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.io.IOException;
/**
 * @author Joshua Whalen
 * Program That crawls web site and collects term 
 * frequency and prints them out in descending order 
 * and can search them
 */
public class practice {
	static int NumDocs = 100;
	static String AnalyzedTerm = "election";
	 //static String seedurl = "http://www.bbc.com/news";
	//static String seedurl = "http://www.foxnews.com/";
	//static String seedurl = "http://abcnews.go.com/";
	static String seedurl = "http://www.cnn.com/";
	//static String seedurl = "http://www.bridgew.edu";  
	static List<Float> percents = new ArrayList<Float>();
	public static void main(String[] args) throws IOException {


 
    	
    	run(seedurl);
    }
    public static void PrintSortedFreq(HashMap<String, Integer> termFreq, int count){
    	List<Map.Entry<String,Integer>> terms = new ArrayList<Map.Entry<String,Integer>>(
    		    termFreq.entrySet()
    		);
    		Collections.sort(terms,new Comparator<Map.Entry<String,Integer>>() {//make comparator 
    		        public int compare(Map.Entry<String,Integer> x, Map.Entry<String,Integer> y) {//compare the terms
    		            return Integer.compare(y.getValue(), x.getValue());
    		        }
    		    }
    		);
    		for (Map.Entry<String,Integer> x : terms) {
    			float z = (float)x.getValue()/(float)count*100;
    		    System.out.println("("+x.getKey()+")"+" <Frequency>= "+x.getValue() + " <Percentage>=" + z + "%");// print out the terms
    		}
    }
    private static void SearchTerms(HashMap<String, Integer> termFreq, int termcount){
    	Scanner in = new Scanner(System.in);
    	while(3==3){
    	System.out.print("Search a term: ");
    	String term;
    	term = in.nextLine().toLowerCase();
    	boolean x = termFreq.containsKey(term);
    	
        if (x){
        	System.out.println("yes the term, " + term + " exists");
        	System.out.println("("+term+")" + " <Frequency>="+ termFreq.get(term) + " <Percentage>="+ (float)termFreq.get(term)/(float)termcount*100 + "%");}
        else{System.out.println("Sorry that term does not exist, Try again");}
        

        }
    }
    private static void run(String seedurl)throws IOException{
    	HashSet<String> seen = new HashSet<String>();
    	Queue<String> urls = new LinkedList<String>();
    	HashMap<String, Integer> termFreq = new HashMap();
    	int count = 0;
    	int termcount = 0;
    	
    	System.out.println("CRAWLING");    	
    	for (int i = 0; i<NumDocs; i++){
    		System.out.println("Fetching " + seedurl + "..." + i );
    		Document doc = Jsoup.connect(seedurl).get(); // Connects to seedurl and gets the html from the page 
    		Elements links = doc.select("a[href]");//Extracts all links from seedurl
    		String text = doc.body().text(); // Gets all the text from the html

       
        		for(String word : text.split(" ")){//Separates words by spaces
        			word = word.replaceAll("[^a-zA-Z]", " ");//gets rid of all symbols and just keeps letters
        			word = word.toLowerCase();
        			Integer q = termFreq.get(word);// gets value of the key 
        			termcount++;
        			
        			if (q == null ) {  //Otherwise, puts the word into the HashMap
        				termFreq.put(word, 1);
        			if (word.contains(" ") ) {//gets rid of spaces
        				termFreq.remove(word);
                }
            }
        			else {
        				termFreq.put(word,q+1);}//otherwise updates term frequency of word
        	}

        System.out.println("Links:" + " ("+links.size() + ")");
        for (Element link : links) {
        	if(link != null && !seen.contains(link) && link.attr("abs:href").toString().contains(seedurl) && !seedurl.contains("#")){
        		//System.out.println("* a: "+ link.attr("abs:href") + "   ("+link.text()+")");
        		String absHref = link.attr("abs:href");
        		seen.add(absHref);
        		urls.add(absHref);
            	count++;           	
            }   
        	else{
        		//System.out.println("Skipping link that has already been crawled or link doesn't relate to Website");
        		}
        }  
        	seedurl = urls.poll();
    		if (i%10==0 && termFreq.containsKey(AnalyzedTerm)){percents.add((float)termFreq.get(AnalyzedTerm)/(float)termcount*100);}

    	}
    		System.out.println("Crawler found "+count+" links");
    		System.out.println("There are "+termFreq.size()+" unique terms in this website and " + termcount + " total terms");
    		PrintSortedFreq(termFreq, termcount);
    		
    		System.out.println("Analytics for the term " + "["+ AnalyzedTerm +"]");
        	int DocCounter = 0;
        		for (float perc : percents){
        			System.out.println("Number of Docs: "+DocCounter +" Percentage = "+ perc + "%");
        			DocCounter = DocCounter +10;
        			}
    		SearchTerms(termFreq,termcount);
    		

    	

    }
}

