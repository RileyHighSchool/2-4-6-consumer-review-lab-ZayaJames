import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * Class that contains helper methods for the Review Lab
 **/
public class Review {
  
  private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
  private static ArrayList<String> posAdjectives = new ArrayList<String>();
  private static ArrayList<String> negAdjectives = new ArrayList<String>();
 
  
  private static final String SPACE = " ";
  
  static{
    try {
      Scanner input = new Scanner(new File("cleanSentiment.csv"));
      while(input.hasNextLine()){
        String[] temp = input.nextLine().split(",");
        sentiment.put(temp[0],Double.parseDouble(temp[1]));
        //System.out.println("added "+ temp[0]+", "+temp[1]);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing cleanSentiment.csv");
    }
  
  
  //read in the positive adjectives in postiveAdjectives.txt
     try {
      Scanner input = new Scanner(new File("positiveAdjectives.txt"));
      while(input.hasNextLine()){
        String temp = input.nextLine().trim();
        // System.out.println(temp);
        posAdjectives.add(temp);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing postitiveAdjectives.txt\n" + e);
    }   
 
  //read in the negative adjectives in negativeAdjectives.txt
     try {
      Scanner input = new Scanner(new File("negativeAdjectives.txt"));
      while(input.hasNextLine()){
        negAdjectives.add(input.nextLine().trim());
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing negativeAdjectives.txt");
    }   
  }
  
  /** 
   * returns a string containing all of the text in fileName (including punctuation), 
   * with words separated by a single space 
   */
  public static String textToString( String fileName )
  {  
    String temp = "";
    try {
      Scanner input = new Scanner(new File(fileName));
      
      //add 'words' in the file to the string, separated by a single space
      while(input.hasNext()){
        temp = temp + input.next() + " ";
      }
      input.close();
      
    }
    catch(Exception e){
      System.out.println("Unable to locate " + fileName);
    }
    //make sure to remove any additional space that may have been added at the end of the string.
    return temp.trim();
  }
  
  /**
   * @returns the sentiment value of word as a number between -1 (very negative) to 1 (very positive sentiment) 
   */
  public static double sentimentVal( String word )
  {
    try
    {
      return sentiment.get(word.toLowerCase());
    }
    catch(Exception e)
    {
      return 0;
    }
  }
  
  // finds the total sentiment value of a review
  public static double totalSentiment(String fileName)
  {
    String review = textToString(fileName);
    double total = 0.0;

    while (review.indexOf(" ") > 0)
    {
      int space = review.indexOf(" ");
      String word = review.substring(0, space);
      double sentiment = sentimentVal(word);
      total += sentiment;
      review = review.substring(space+1);

    }
    return total;
  }

  /**
   * Returns the ending punctuation of a string, or the empty string if there is none 
   */
  public static String getPunctuation( String word )
  { 
    String punc = "";
    for(int i=word.length()-1; i >= 0; i--){
      if(!Character.isLetterOrDigit(word.charAt(i))){
        punc = punc + word.charAt(i);
      } else {
        return punc;
      }
    }
    return punc;
  }

      /**
   * Returns the word after removing any beginning or ending punctuation
   */
  public static String removePunctuation( String word )
  {
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(0)))
    {
      word = word.substring(1);
    }
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length()-1)))
    {
      word = word.substring(0, word.length()-1);
    }
    
    return word;
  }
 
  /** 
   * Randomly picks a positive adjective from the positiveAdjectives.txt file and returns it.
   */
  public static String randomPositiveAdj()
  {
    int index = (int)(Math.random() * posAdjectives.size());
    return posAdjectives.get(index);
  }
  
  /** 
   * Randomly picks a negative adjective from the negativeAdjectives.txt file and returns it.
   */
  public static String randomNegativeAdj()
  {
    int index = (int)(Math.random() * negAdjectives.size());
    return negAdjectives.get(index);
    
  }
  
  /** 
   * Randomly picks a positive or negative adjective and returns it.
   */
  public static String randomAdjective()
  {
    boolean positive = Math.random() < .5;
    if(positive){
      return randomPositiveAdj();
    } else {
      return randomNegativeAdj();
    }
  }

  public static int starRating(String fileName)
  {

    double totalSentiment = totalSentiment(fileName);
    if (totalSentiment < 15) 
    {
      if (totalSentiment < 10)
      {
        if (totalSentiment < 5)
        {
          if (totalSentiment < 0)
          {
            return 2;
          }
          return 3;
        }
        return 4;
      }
      return 5;
    }
    else
    {
      return 1;
    }
  }

  public static String fakeReview(String fileName, String posNegative, String posPositive)
  {
    // turns the review into a usable string
    String review = textToString(fileName);

    // placeholder for new string
    String newReview = "";

    // interates through the string to find positive or negative adjectives
    while (review.indexOf("*") > 0 && review.length() > 0)
    {
      // Finds a "*"
      int startLoc = review.indexOf("*");
      // copies over everything before the *
      newReview += review.substring(0, startLoc);
      // adds a random adjective to the new review
      newReview += randomAdjective();
      // removes the previous adjective
      int spaceAfterStar = review.indexOf(" ", startLoc);
      review = review.substring(spaceAfterStar);
    }
    newReview += review;

    return newReview;
  }

  public static String positiveReview(String fileName)
  {
    // turns review into a usable string in this method
    String review = textToString(fileName);
    
    // placeholder for new string
    String goodReview = "";

    // interates through the string to find adjectives
    while (review.indexOf("*") > 0 && review.length() > 0)
    {
      // Finds a "*"
      int startLoc = review.indexOf("*");
      // copies over everything before the *
      goodReview += review.substring(0, startLoc);
      // adds a random positive adjective to the new review
      goodReview += randomPositiveAdj();
      // removes the previous adjective
      int spaceAfterStar = review.indexOf(" ", startLoc);
      review = review.substring(spaceAfterStar);
    }
    goodReview += review;

    return goodReview;
  }

  public static String negativeReview(String fileName)
  {
    // turns review into a usable string in this method
    String review = textToString(fileName);
    
    // placeholder for new string
    String badReview = "";

    // interates through the string to find adjectives
    while (review.indexOf("*") > 0 && review.length() > 0)
    {
      // Finds a "*"
      int startLoc = review.indexOf("*");
      // copies over everything before the *
      badReview += review.substring(0, startLoc);
      // adds a random negative adjective to the new review
      badReview += randomNegativeAdj();
      // removes the previous adjective
      int spaceAfterStar = review.indexOf(" ", startLoc);
      review = review.substring(spaceAfterStar);
    }
    badReview += review;

    return badReview;
  }

  public static double totalSentiment2(String review)
  {
    double total = 0.0;

    while (review.indexOf(" ") > 0)
    {
      int space = review.indexOf(" ");
      String word = review.substring(0, space);
      double sentiment = sentimentVal(word);
      total += sentiment;
      review = review.substring(space+1);

    }
    return total;
  }
  
  
  public static void comparison(String fileName)
  {
    // turns review into a usable string in this method
    String review = textToString(fileName);
    
     // holds the zero to replace all "o"s
     String zero = "0";
    
     // template for final review
     String zeroedReview = "";

    // gets both of the sentiment values of the bad review and the good review
    double badVal = totalSentiment2(negativeReview(fileName));
    double goodVal = totalSentiment2(positiveReview(fileName));

    System.out.println("Positive Value: " + goodVal);
    System.out.println("Negative Value: " + badVal);

    // calculates the difference in sentiment value between the two
    double difference = goodVal - badVal;

    // interates through the string to find "o"s
    while (review.indexOf("o") > 0 && review.length() > 0)
    {
      // Finds an o
      int startLoc = review.indexOf("o");
      // copies over everything before the o
      zeroedReview += review.substring(0, startLoc);
      // adds a 0 to the string
      zeroedReview += zero;
      // removes the o
      review = review.substring(startLoc + 1);
    }
    zeroedReview += review;

    System.out.println(zeroedReview);
    System.out.println("Sentiment difference: " + difference);

  }

}



