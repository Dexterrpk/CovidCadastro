package com.dexter.covid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.dexter.covid.database.model.Cadastro;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "cadastro_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Cadastro.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Cadastro.TABLE_NAME);
        onCreate(db);
    }

    public long insertCadastro(String cpf) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Cadastro.COLUMN_CPF, cpf);

        long id = db.insert(Cadastro.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public Cadastro getCadastro(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Cadastro.TABLE_NAME,
                new String[]{Cadastro.COLUMN_ID, Cadastro.COLUMN_CPF, Cadastro.COLUMN_TIMESTAMP},
                Cadastro.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Cadastro cadastro = new Cadastro(
                cursor.getInt(cursor.getColumnIndex(Cadastro.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Cadastro.COLUMN_CPF)),
                cursor.getString(cursor.getColumnIndex(Cadastro.COLUMN_TIMESTAMP)));

        cursor.close();

        return cadastro;
    }

    public List<Cadastro> getAllCadastros() {
        List<Cadastro> cadastros = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Cadastro.TABLE_NAME + " ORDER BY " +
                Cadastro.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Cadastro cadastro = new Cadastro();
                cadastro.setId(cursor.getInt(cursor.getColumnIndex(Cadastro.COLUMN_ID)));
                cadastro.setCadastro(cursor.getString(cursor.getColumnIndex(Cadastro.COLUMN_CPF)));
                cadastro.setTimestamp(cursor.getString(cursor.getColumnIndex(Cadastro.COLUMN_TIMESTAMP)));

                cadastros.add(cadastro);
            } while (cursor.moveToNext());
        }

        db.close();

        return cadastros;
    }

    public int getContagemCadastros() {
        String countQuery = "SELECT  * FROM " + Cadastro.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int contagem = cursor.getCount();
        cursor.close();


        return contagem;
    }

    public int updateCadastro(Cadastro cadastro) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Cadastro.COLUMN_CPF, cadastro.getCadastro());

        return db.update(Cadastro.TABLE_NAME, values, Cadastro.COLUMN_ID + " = ?",
                new String[]{String.valueOf(cadastro.getId())});
    }

    public void deleteCadastro(Cadastro cadastro) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Cadastro.TABLE_NAME, Cadastro.COLUMN_ID + " = ?",
                new String[]{String.valueOf(cadastro.getId())});
        db.close();
    }
}
