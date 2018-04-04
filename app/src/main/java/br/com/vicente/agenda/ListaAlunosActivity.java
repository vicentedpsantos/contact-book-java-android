package br.com.vicente.agenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Browser;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Contato contato = (Contato) listaContatos.getItemAtPosition(info.position);

        //Criação da opção de ligar para contato através do menu de contexto
        MenuItem ligar = menu.add("Call");
        ligar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this,
                                                        new String[]{Manifest.permission.CALL_PHONE}, 01);//o request code serve para diferenciar os requests por permissão.
                                                                                                                      // atraves do metodo onRequestPermissionResult, é possível mudar o comportamento do código
                                                                                                                      // para cada request code diferente que houver.
                } else {
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:" + contato.getTelefone()));

                    startActivity(intentLigar);
                    return false;
                }

                return false;
            }
        });

        //Criação da opção de visualizar no mapa
        String enderecoDoContato = contato.getEndereco();
        MenuItem verNoMapa = menu.add("Location on map");
        Intent intentVerNoMapa = new Intent(Intent.ACTION_VIEW);
        verNoMapa.setIntent(intentVerNoMapa);
        intentVerNoMapa.setData(Uri.parse("geo:0,0?q="+enderecoDoContato));

        //Criação da opção de enviar SMS ao contato
        String numeroTelefone = contato.getTelefone();
        MenuItem enviarMensagem = menu.add("Send text");
        Intent intentEnviarMensagem = new Intent(Intent.ACTION_VIEW);
        intentEnviarMensagem.setData(Uri.parse("sms:" + numeroTelefone));
        enviarMensagem.setIntent(intentEnviarMensagem);

        //Criação da opção de ir até o website cadastrado no contato
        MenuItem irParaWebsite = menu.add("Go to Website");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);

        String site = contato.getSite();

        if (!site.startsWith("http://")){
            site = "http://" + site;
        }
        intentSite.setData(Uri.parse(site));
        irParaWebsite.setIntent(intentSite);

        //Criação da opção de deletar contato no menu de contexto
        MenuItem delete = menu.add("Delete");
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ContatoDAO dao = new ContatoDAO(ListaAlunosActivity.this);
                dao.deleta(contato);
                dao.close();

                carregaLista();
                return false;
            }
        });
    }
}
