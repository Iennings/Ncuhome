package com.example.ienning.ncuhome.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ienning.ncuhome.R;
import com.example.ienning.ncuhome.db.ItemBook;

import java.util.List;

/**
 * Created by ienning on 16-10-2.
 */

public class MyCardListAdapter extends RecyclerView.Adapter<MyCardListAdapter.BookViewHolder> {
    private Context context;
    private MyCardListAdapter.OnItemClickListener onRecyclerViewItemClickListener = null;
    private List<ItemBook> dataList;
    public MyCardListAdapter(Context context, List<ItemBook> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public MyCardListAdapter.BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_lib_book, viewGroup, false);
        BookViewHolder viewHolder = new BookViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder viewHolder, int i) {
        try {
            if (viewHolder instanceof BookViewHolder) {
                ItemBook itemBook = dataList.get(i);
                viewHolder.bookName.setText(itemBook.getTitle());
                viewHolder.bookAuthor.setText(itemBook.getAuthor());
                viewHolder.bookANS.setText(itemBook.getBarCode());
                viewHolder.bookRentTime.setText(itemBook.getBorrowDate());
                viewHolder.bookReturnTime.setText(itemBook.getReturnDate());
                viewHolder.bookLib.setText(itemBook.getLocation());
                viewHolder.bookAttach.setText(itemBook.getCkeck());
                /*
                viewHolder.bookRenew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("Ienning", "onClick: Testr " + "shu da shui");
                    }
                });
                */
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cardView;
        TextView bookName;
        TextView bookAuthor;
        TextView bookANS;
        TextView bookRentTime;
        TextView bookReturnTime;
        TextView bookLib;
        TextView bookAttach;
        Button bookRenew;
        public BookViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.library_book_cardview);
            bookName = (TextView) itemView.findViewById(R.id.library_book_name);
            bookAuthor = (TextView) itemView.findViewById(R.id.library_book_author);
            bookANS = (TextView) itemView.findViewById(R.id.library_book_ans);
            bookRentTime = (TextView) itemView.findViewById(R.id.library_book_rent_time);
            bookReturnTime = (TextView) itemView.findViewById(R.id.library_book_return_time);
            bookLib = (TextView) itemView.findViewById(R.id.library_book_library);
            bookAttach = (TextView) itemView.findViewById(R.id.library_book_attach);
            bookRenew = (Button) itemView.findViewById(R.id.library_book_renew);
            bookRenew.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (onRecyclerViewItemClickListener != null) {
                onRecyclerViewItemClickListener.onClick(itemView, getAdapterPosition());
            }
        }
    }
    public static interface OnItemClickListener {
        void onClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;
    }
}
