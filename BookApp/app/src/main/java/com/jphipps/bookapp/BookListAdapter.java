package com.jphipps.bookapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

//Recycler View adapter to display all of the received book data
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

    //Data source for the recycler view to hold all of the book data
    private ArrayList<BookDetails> bookList;

    //Layout inflater to add each book item to the recycler view
    private LayoutInflater inflater;

    //Class to represent each book entry in the recycler view and find the fields of the entry
    class BookViewHolder extends RecyclerView.ViewHolder {
        public final TextView txtTitle;
        public final TextView txtAuthor;
        public final TextView txtPublisher;
        public final TextView txtYear;
        public final TextView txtPrice;

        final BookListAdapter adapter;

        public BookViewHolder(View itemView, BookListAdapter adapter) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.tvBookTitle);
            txtAuthor = itemView.findViewById(R.id.tvBookAuthor);
            txtPublisher = itemView.findViewById(R.id.tvBookPublisher);
            txtYear = itemView.findViewById(R.id.tvBookYear);
            txtPrice = itemView.findViewById(R.id.tvBookPrice);
            this.adapter = adapter;
        }
    }

    //Constructor to set the recycler view's inflater and data source
    public BookListAdapter(Context context, ArrayList<BookDetails> bookList) {
        inflater = LayoutInflater.from(context);
        this.bookList = bookList;
    }

    //Function to add a new entry to the recycler view by inflating the view
    public BookListAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.booklist_item, parent, false);
        return new BookViewHolder(view, this);
    }

    //Function to set the information in the recycler view entry based on the position value given as a parameter
    public void onBindViewHolder(BookViewHolder holder, int position) {
        BookDetails book = bookList.get(position);
        holder.txtTitle.setText(book.getTitle());
        holder.txtAuthor.setText("Author(s): " + book.getAuthorInformation());
        holder.txtPublisher.setText("Publisher: " + book.getPublisher());
        holder.txtYear.setText("Year: " + book.getYear());
        holder.txtPrice.setText("Price: £" + book.getPrice());
    }

    //Function to get the number of items in the recycler view
    public int getItemCount() {
        return bookList.size();
    }

    //Function to update the data in the recycler view and refresh the view so that the new data is displayed
    public void updateData(ArrayList<BookDetails> bookList) {
        this.bookList.clear();
        this.bookList.addAll(bookList);
        notifyDataSetChanged();
    }
}