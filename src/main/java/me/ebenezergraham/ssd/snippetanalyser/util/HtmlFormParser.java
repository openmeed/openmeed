package me.ebenezergraham.ssd.snippetanalyser.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 @author Ebenezer Graham
 Created on 7/18/19
 */


public class HtmlFormParser {
 
 public static Document parse(String filename){
  Document doc = null;
  try {
   doc = Jsoup.connect("http://example.com").get();
  } catch (IOException e) {
   e.printStackTrace();
  }
  doc.select("p").forEach(System.out::println);
  return doc;
 }
}
