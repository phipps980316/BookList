package com.jphipps.bookapp;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;

//Class to parse xml files from a url
//Class is a subclass of NetworkHandler
public class XMLHandler extends NetworkHandler {

    //Implement the parseFile function from the superclass to begin reading the xml
    protected ArrayList<BookDetails> parseFile(InputStream inputStream) throws Exception {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readXML(parser);
        } finally {
            inputStream.close();
        }
    }

    //Function to look for the <book> tag in xml and read the child tags relating to the book
    //Repeat until the </library> tag has been reached which shows that the end of the file has been reached
    //Return the list of book details
    private ArrayList<BookDetails> readXML(XmlPullParser parser) throws Exception {
        ArrayList<BookDetails> fetchedBookList = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, NAMESPACE, LIBRARY_TAG_STRING);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(BOOK_TAG_STRING)) fetchedBookList.add(readRecord(parser));
        }
        return fetchedBookList;
    }

    //Function to read all the child tags of the <book> tag and populate a book details object with that information
    //Returns the book detail object
    private BookDetails readRecord(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, BOOK_TAG_STRING);

        BookDetails book = new BookDetails();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            switch (name) {
                case TITLE_TAG_STRING:
                    String title = readDetail(parser, TITLE_TAG_STRING);
                    book.setTitle(title);
                    break;
                case AUTHOR_TAG_STRING:
                    String author = readDetail(parser, AUTHOR_TAG_STRING);
                    book.addAuthor(author);
                    break;
                case PUBLISHER_TAG_STRING:
                    String publisher = readDetail(parser, PUBLISHER_TAG_STRING);
                    book.setPublisher(publisher);
                    break;
                case YEAR_TAG_STRING:
                    String year = readDetail(parser, YEAR_TAG_STRING);
                    book.setYear(year);
                    break;
                case PRICE_TAG_STRING:
                    String price = readDetail(parser, PRICE_TAG_STRING);
                    book.setPrice(price);
                    break;
                default:
                    break;
            }
        }
        return book;
    }

    //Function to read a child tag from the xml file
    private String readDetail(XmlPullParser parser, String tag) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        String detail = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return detail;
    }

    //Function to get the text from inside the next tag
    private String readText(XmlPullParser parser) throws Exception {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}


