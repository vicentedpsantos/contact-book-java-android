package br.com.vicente.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.vicente.agenda.modelo.Contato;

/**
 * Created by Vicente on 01/04/2018.
 */

public class ContatoDAO extends SQLiteOpenHelper{

    public ContatoDAO(Context context) {
        super(context, "Agenda", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE Contatos (id INTEGER PRIMARY KEY, nome TEXT NOT NULL, endereco TEXT, telefone TEXT, site TEXT, nota REAL);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS Contatos;";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void insere(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosDoContato(contato);

        db.insert("Contatos", null, dados);
    }

    @NonNull
    private ContentValues pegaDadosDoContato(Contato contato) {
        ContentValues dados = new ContentValues();
        dados.put("nome", contato.getNome());
        dados.put("endereco", contato.getEndereco());
        dados.put("telefone", contato.getTelefone());
        dados.put("site", contato.getSite());
        dados.put("nota", contato.getNota());
        return dados;
    }

    public List<Contato> buscaContatos() {
        String sql = "SELECT * from Contatos;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Contato> contatos = new ArrayList<Contato>();

        while(c.moveToNext()){
            Contato contato = new Contato();
            contato.setId(c.getLong(c.getColumnIndex("id")));
            contato.setNome(c.getString(c.getColumnIndex("nome")));
            contato.setEndereco(c.getString(c.getColumnIndex("endereco")));
            contato.setTelefone(c.getString(c.getColumnIndex("telefone")));
            contato.setSite(c.getString(c.getColumnIndex("site")));
            contato.setNota(c.getDouble(c.getColumnIndex("nota")));

            contatos.add(contato);
        }
        c.close();
        return contatos;
    }

    public void deleta(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();

        String[] params = {contato.getId().toString()};
        db.delete("Contatos", "id = ?", params);


    }

    public void altera(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosDoContato(contato);
        String[] params = {contato.getId().toString()};

        db.update("Contatos", dados, "id = ?", params);
    }
}
