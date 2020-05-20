package com.jphipps.bookapp;

import java.io.Serializable;
import java.util.ArrayList;

//Class to hold the details of a book
public class BookDetails implements Serializable {

    //Variables to hold the book's information such as title, author(s), publisher, release year and price
    private String title;
    private ArrayList<String> authors = new ArrayList<>();
    private String publisher;
    private String year;
    private String price;

    //Function to set the book's title
    void setTitle(String title) {
        this.title = title;
    }

    //Function to get the book's title
    String getTitle(){
        return this.title;
    }

    //Function to add a book's author
    void addAuthor(String author) {
        this.authors.add(author);
    }

    //Function to get the book's author(s) information
    String getAuthorInformation(){
        String authorString = "";
        for (int index = 0; index < authors.size(); index++){
            if(index < 2) {
                authorString = authorString + authors.get(index);
                if(authors.size() > 1) {
                    authorString = authorString + ", ";
                }
            }
            else {
                authorString = authorString + " Et al";
                break;
            }
        }
        return authorString;
    }

    //Function to set the book's publisher
    void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    //Function to get the book's publisher
    String getPublisher(){
        return this.publisher;
    }

    //Function to set the book's release year
    void setYear(String year) {
        this.year = year;
    }

    //Function to get the book's release year
    String getYear(){
        return this.year;
    }

    //Function to set the book's price
    void setPrice(String price) {
        this.price = price;
    }

    //Function to get the book's price
    String getPrice(){
        return this.price;
    }
}