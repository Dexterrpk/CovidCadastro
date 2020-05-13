package com.dexter.covid.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.dexter.covid.R;
import com.dexter.covid.database.DatabaseHelper;
import com.dexter.covid.database.model.Cadastro;
import com.dexter.covid.utils.MyDividerItemDecoration;
import com.dexter.covid.utils.RecyclerTouchListener;

public class MainActivity extends AppCompatActivity {
    private CadastroAdapter mAdapter;
    private List<Cadastro> cadastroList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView txtListaVazia;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        txtListaVazia = findViewById(R.id.empty_cadastros_view);

        db = new DatabaseHelper(this);

        cadastroList.addAll(db.getAllCadastros());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarCadastrosDialog(false, null, -1);
            }
        });

        mAdapter = new CadastroAdapter(this, cadastroList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        listavaziaView();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }


    private void createNote(String note) {

        long id = db.insertCadastro(note);

        Cadastro n = db.getCadastro(id);

        if (n != null) {
            cadastroList.add(0, n);

            mAdapter.notifyDataSetChanged();

            listavaziaView();
        }
    }


    private void updateNote(String note, int position) {
        Cadastro n = cadastroList.get(position);
        n.setCadastro(note);

        db.updateCadastro(n);

        cadastroList.set(position, n);
        mAdapter.notifyItemChanged(position);

        listavaziaView();
    }


    private void deleteCadastro(int posicao) {
        db.deleteCadastro(cadastroList.get(posicao));

        cadastroList.remove(posicao);
        mAdapter.notifyItemRemoved(posicao);

        listavaziaView();
    }

    private void showActionsDialog(final int posicao) {
        CharSequence colors[] = new CharSequence[]{"Editar", "Excluir"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha uma opção");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {
                    mostrarCadastrosDialog(true, cadastroList.get(posicao), posicao);
                } else {
                    deleteCadastro(posicao);
                }
            }
        });
        builder.show();
    }


    private void mostrarCadastrosDialog(final boolean shouldUpdate, final Cadastro cadastro, final int posicao) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.cpf_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inserirCadastro = view.findViewById(R.id.cpf);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_cadastro) : getString(R.string.lbl_edit_cadastro));

        if (shouldUpdate && cadastro != null) {
            inserirCadastro.setText(cadastro.getCadastro());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "atualizar" : "salvar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inserirCadastro.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Cadastre um CPF!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (shouldUpdate && cadastro != null) {
                    updateNote(inserirCadastro.getText().toString(), posicao);
                } else {
                    createNote(inserirCadastro.getText().toString());
                }
            }
        });
    }


    private void listavaziaView() {
        if (db.getContagemCadastros() > 0) {
            txtListaVazia.setVisibility(View.GONE);
        } else {
            txtListaVazia.setVisibility(View.VISIBLE);
        }
    }
}
