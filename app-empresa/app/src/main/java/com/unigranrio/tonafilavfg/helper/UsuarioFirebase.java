package com.unigranrio.tonafilavfg.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.unigranrio.tonafilavfg.activity.HomeActivity;
import com.unigranrio.tonafilavfg.model.Usuario;

public class UsuarioFirebase {

    public static FirebaseUser getUsuarioAtual() {
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static String getIdentificadorUsuario() {
        return getUsuarioAtual().getUid();
    }

    public static String getIdUsuario() {
        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return auth.getCurrentUser().getUid();
    }

    public static boolean atualizarNomeUsuario(String nome) {

        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Erro ao atualizar nome de perfil.");
                    }
                }
            });
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void redirecionaUsuarioLogado(final Activity activity) {

        FirebaseUser usuarioAtual = getUsuarioAtual();
        if (usuarioAtual != null) {

            DatabaseReference usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase()
                    .child("usuarios")
                    .child(getIdentificadorUsuario());

            usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    String tipoUsuario = usuario.getTipo();
                    if (tipoUsuario.equals("empresa")) {
                        activity.startActivity(new Intent(activity, HomeActivity.class));
                    } else {
                        activity.startActivity(new Intent(activity, HomeActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
