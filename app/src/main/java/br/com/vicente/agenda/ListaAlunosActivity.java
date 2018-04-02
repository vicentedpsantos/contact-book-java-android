package br.com.vicente.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.vicente.agenda.dao.ContatoDAO;
import br.com.vicente.agenda.modelo.Contato;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaContatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        listaContatos = (ListView) findViewById(R.id.lista_contatos);

        listaContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View itemClicado, int posicao, long id) {
                Contato contato = (Contato) listaContatos.getItemAtPosition(posicao);
                Intent intentVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intentVaiProFormulario.putExtra("contato", contato);
                startActivity(intentVaiProFormulario);
            }
        });

        Button novoAluno = findViewById(R.id.botao_adicionar);
        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intentVaiProFormulario);
            }
        });

        registerForContextMenu(listaContatos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    private void carregaLista() {
        ContatoDAO dao = new ContatoDAO(this);
        List<Contato> contatos = dao.buscaContatos();
        dao.close();


        ArrayAdapter<Contato> adapter = new ArrayAdapter<Contato>(this, android.R.layout.simple_list_item_1, contatos);
        listaContatos.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem delete = menu.add("Delete");
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                Contato contato = (Contato) listaContatos.getItemAtPosition(info.position);

                ContatoDAO dao = new ContatoDAO(ListaAlunosActivity.this);
                dao.deleta(contato);
                dao.close();

                carregaLista();
                return false;
            }
        });
    }

}
