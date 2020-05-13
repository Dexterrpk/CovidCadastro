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
import java.util.Locale;
import java.util.TimeZone;

import com.dexter.covid.R;
import com.dexter.covid.database.model.Cadastro;

public class CadastroAdapter extends RecyclerView.Adapter<CadastroAdapter.MyViewHolder> {

    private Context context;
    private List<Cadastro> cadastroList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cadastro;
        public TextView dot;
        public TextView timestamp;

        public MyViewHolder(View view) {
            super(view);
            cadastro = view.findViewById(R.id.cpf);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }


    public CadastroAdapter(Context context, List<Cadastro> cadastroList) {
        this.context = context;
        this.cadastroList = cadastroList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_cadastro, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int posicao) {
        Cadastro cadastro = cadastroList.get(posicao);

        holder.cadastro.setText(cadastro.getCadastro());

        holder.dot.setText(Html.fromHtml("&#8226;"));

        holder.timestamp.setText(formatDate(cadastro.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return cadastroList.size();
    }

    private String formatDate(String dateStr ) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = fmt.parse(dateStr);

            SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm dd MMM zz ");

            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}
