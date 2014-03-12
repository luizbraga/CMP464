package utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import com.example.cmp464.Item;

/**
 * Parser that follows a tree structure according to the XML tags
 * First level: RSS
 * Second level: channel
 * Third level: item
 * Fourth level: title, link and description
 * 
 * Using this structure I was able to create a Item object for each item tag in the XML file
 * 
 * This code is loosely based in the code in this web site:
 * http://www.androidpit.com/java-guide-2-program-your-own-rss-reader
 * @author luizbraga
 *
 */
public class PullParser {
	
	public static List<Item> parse(String result) throws XmlPullParserException, IOException{
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser xmlparser = factory.newPullParser();
		xmlparser.setInput(new StringReader(result));
		xmlparser.nextTag();
		return readRss(xmlparser);
	}

	private static List<Item> readRss(XmlPullParser xmlparser) throws XmlPullParserException, IOException {
		List<Item> items = new ArrayList<Item>();
		xmlparser.require(XmlPullParser.START_TAG, null, "rss");
		
		while(xmlparser.next() != XmlPullParser.END_TAG){
			if (xmlparser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
			String name = xmlparser.getName();
			if(name.equals("channel")){
				items.addAll(readChannel(xmlparser));
			} else{
				skip(xmlparser);
			}
		}
		return items;
	}

	private static List<Item> readChannel(
			XmlPullParser xmlparser) throws XmlPullParserException, IOException {
		List<Item> items = new ArrayList<Item>();
		xmlparser.require(XmlPullParser.START_TAG, null, "channel");
		
		while(xmlparser.next() != XmlPullParser.END_TAG){
			if (xmlparser.getEventType() != XmlPullParser.START_TAG) {
				continue;
            }
			if(xmlparser.getName().equals("item")){
				// Call the method that will read each item tag and return an Item object with title, description and link
				items.add(readItem(xmlparser, new Item()));
			} else{
				skip(xmlparser);
			}
		}
		return items;
	}

	private static Item readItem(XmlPullParser xmlparser, Item item) throws XmlPullParserException, IOException {
		while(xmlparser.next()!= XmlPullParser.END_TAG){
			if (xmlparser.getEventType() != XmlPullParser.START_TAG) {
				continue;
            }
			if(xmlparser.getName().equals("title")){
				if(xmlparser.next() == XmlPullParser.TEXT){
					item.setTitle(xmlparser.getText());
					xmlparser.nextTag();
				}
			}
			else if(xmlparser.getName().equals("link")){
				if(xmlparser.next() == XmlPullParser.TEXT){
					item.setLink(xmlparser.getText());
					xmlparser.nextTag();
				}
			}
			else if(xmlparser.getName().equals("description")){
				if(xmlparser.next() == XmlPullParser.TEXT){
					item.setDescription(xmlparser.getText().substring(0, xmlparser.getText().lastIndexOf(".<img ")+1));
					xmlparser.nextTag();
				}
			} else{
				skip(xmlparser);
			}
		}
		return item;
	}
	
	private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
