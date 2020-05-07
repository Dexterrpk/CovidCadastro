package com.dexter.covid.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.dexter.covid.R;
import com.dexter.covid.database.model.Note;

public class CadastroAdapter extends RecyclerView.Adapter<CadastroAdapter.MyViewHolder> {

    private Context context;
    private List<Note> cadastroList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cadastro;
        public TextView dot;
        public TextView timestamp;

        public MyViewHolder(View view) {
            super(view);
            cadastro = view.findViewById(R.id.note);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }


    public CadastroAdapter(Context context, List<Note> cadastroList) {
        this.context = context;
        this.cadastroList = cadastroList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = cadastroList.get(position);

        holder.cadastro.setText(note.getCadastro());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(note.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return cadastroList.size();
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm dd MMM  ");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}
