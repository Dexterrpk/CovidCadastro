package com.dexter.covid.database.model;

public class Cadastro {
    public static final String TABLE_NAME = "cadastro";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CPF = "cpf";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String cpf;
    private String timestamp;


    // Create table SQLite
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CPF + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Cadastro() {
    }

    public Cadastro(int id, String cpf, String timestamp) {
        this.id = id;
        this.cpf = cpf;
        this.timestamp = timestamp ;
    }

    public int getId() {
        return id;
    }

    public String getCadastro() {
        return cpf;
    }

    public void setCadastro(String cadastro) {
        this.cpf = cadastro;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
