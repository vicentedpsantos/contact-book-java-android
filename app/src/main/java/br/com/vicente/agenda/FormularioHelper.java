package br.com.vicente.agenda;

import android.media.Rating;
import android.widget.EditText;
import android.widget.RatingBar;

import br.com.vicente.agenda.modelo.Contato;

/**
 * Created by Vicente on 01/04/2018.
 */

public class FormularioHelper {

    private final EditText campoNome;
    private final EditText campoEndereco;
    private final EditText campoTelefone;
    private final EditText campoSite;
    private final RatingBar campoNota;

    private Contato contato;

    public FormularioHelper(FormularioActivity activity){
        campoNome = activity.findViewById(R.id.formulario_nome);
        campoEndereco = activity.findViewById(R.id.formulario_address);
        campoTelefone = activity.findViewById(R.id.formulario_telephone);
        campoSite = activity.findViewById(R.id.formulario_website);
        campoNota = (RatingBar) activity.findViewById(R.id.formulario_ratingbar);
        contato = new Contato();

    }

    public Contato pegaContato() {
        contato.setNome(campoNome.getText().toString());
        contato.setEndereco(campoEndereco.getText().toString());
        contato.setTelefone(campoTelefone.getText().toString());
        contato.setSite(campoSite.getText().toString());
        contato.setNota((double) campoNota.getProgress());
        return contato;
    }

    public void preencheFormulario(Contato contato) {
        campoNome.setText(contato.getNome());
        campoEndereco.setText(contato.getEndereco());
        campoTelefone.setText(contato.getTelefone());
        campoSite.setText(contato.getSite());
        campoNota.setProgress(contato.getNota().intValue());
        this.contato = contato;
    }
}
