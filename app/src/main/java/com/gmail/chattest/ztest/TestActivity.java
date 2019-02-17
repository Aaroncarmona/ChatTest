package com.gmail.chattest.ztest;

import android.os.Bundle;
import android.widget.EditText;

import com.gmail.chattest.R;
import com.gmail.chattest.common.L;
import com.gmail.chattest.model.Usuario;
import com.gmail.chattest.root.BaseActivity;
import com.gmail.chattest.ui.aadapter.AdapterUsuario;
import com.gmail.chattest.util.Util;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {
    private static final String TAG = TestActivity.class.getSimpleName();

    @BindView(R.id.et_usuario) EditText metUsuario;
    @BindView(R.id.et_edad) EditText metEdad;
    @BindView(R.id.rv_users) RecyclerView mrvUsers;

    private int count = 0;

    private FirebaseFirestore db;
    private List<Usuario> musuarios = new ArrayList<>();
    private AdapterUsuario madapterUsuario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);

        this.db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("users").addSnapshotListener( (snapshot , e) -> {
             musuarios = snapshot.toObjects(Usuario.class);
             update();
        });

        madapterUsuario = new AdapterUsuario();
        madapterUsuario.setData(musuarios);
        madapterUsuario.setOnItemClickListener( position -> {
            Util.toast(getBaseContext() , musuarios.get(position).getUsuario());
        });

        mrvUsers.setLayoutManager(new LinearLayoutManager(getBaseContext() , RecyclerView.VERTICAL , false));
        mrvUsers.setHasFixedSize(true);
        mrvUsers.addItemDecoration(
            new DividerItemDecoration(getApplicationContext() , DividerItemDecoration.VERTICAL));
        mrvUsers.setAdapter(madapterUsuario);

    }

    public void update(){
        madapterUsuario.setData(musuarios);
        madapterUsuario.notifyDataSetChanged();
    }

    @OnClick(R.id.btn)
    public void click(){
        if ( metEdad.getText().toString().equals("")
            || metUsuario.getText().toString().equals("")){
            Util.toast(getBaseContext() , getString(R.string.campos_vacios));
            return;
        }
        db.collection("users")
            .add(
                new Usuario(
                    metUsuario.getText().toString()
                    , Integer.parseInt(metEdad.getText().toString()))
            ).addOnSuccessListener( documentReference -> {
                L.d(TAG , "" , documentReference.getId());
            }).addOnFailureListener( e -> {
                L.e(TAG , "" , e.getMessage());
        });
        count++;
    }
}
