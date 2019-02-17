package com.gmail.chattest.ui.firebasecloud;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.chattest.BuildConfig;
import com.gmail.chattest.R;
import com.gmail.chattest.common.L;
import com.gmail.chattest.common.enums.EResourceType;
import com.gmail.chattest.model.entity.Resources;
import com.gmail.chattest.oi.database.LocalDb;
import com.gmail.chattest.root.BaseActivity;
import com.gmail.chattest.ui.aadapter.AdapterResource;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.internal.Utils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CloudActivity extends BaseActivity {
    private static final String TAG = CloudActivity.class.getSimpleName();

    private AdapterResource adapterResource;
    private List<Resources> resourcesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoordinatorLayout root = new CoordinatorLayout(this);
        root.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout base = new LinearLayout(this);
        base.setLayoutParams( new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT));
        base.setPadding(15 , 0 , 15 , 0 );
        base.setOrientation(LinearLayout.VERTICAL);

        TextView inicio = new TextView(this);
        inicio.setLayoutParams( new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT));
        inicio.setText("Agrega un archivo");
        inicio.setTextColor(getResources().getColor(android.R.color.white));
        inicio.setPadding(5,20,5,20);
        inicio.setOnClickListener( v-> {
            showFileChooser();
        });
        inicio.setGravity(View.TEXT_ALIGNMENT_CENTER);
        inicio.setTextSize(30f);
        inicio.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        resourcesList = new ArrayList<>();

        adapterResource = new AdapterResource();
        adapterResource.setData(resourcesList);
        adapterResource.setListener( item -> {
            Resources current = resourcesList.get(item);

            showFile(getBaseContext() , Uri.parse(current.getLocalPath()) , fileExt(current.getLocalPath()));

            if (EResourceType.JPEG.is(current.getType())){
                L.d(TAG , "current" , "imagen");
            } else if ( EResourceType.MP3.is(current.getType())){
                L.d(TAG , "current", "audio");
            }
        });

        adapterResource.setOnLongListener( i -> {
            Resources current = resourcesList.get(i);
            if ( current.getRemotePath() != null )
                showWeb(current.getRemotePath());

        });

        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutParams( new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager( new LinearLayoutManager(this , RecyclerView.VERTICAL , false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration( new DividerItemDecoration(this , DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapterResource);


        root.addView( base );

        base.addView(inicio);
        base.addView(recyclerView);

        setContentView(root);

        update();

    }

    public static void showFile( Context context , String path , String filetype ) {

        Uri uri = null;

        if(android.os.Build.VERSION.SDK_INT >=24) {
            uri = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    new File(path));
        }else {
            uri = Uri.fromFile(new File(path));
        }

        showFile(context , uri , filetype );
    }

    public static void showFile(Context context , Uri uri, String filetype) {
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String mimeType =
                myMime.getMimeTypeFromExtension(filetype);

        intent.setDataAndType(uri, mimeType);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context, context.getString(R.string.aplicacion_no_encontrada), Toast.LENGTH_SHORT).show();

        }
    }

    @SuppressLint("CheckResult")
    public void update(){
        LocalDb.getInstance(getBaseContext()).resourcesDao().getAlltoObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( data -> {
                resourcesList = data;
                adapterResource.setData(resourcesList);
                adapterResource.notifyDataSetChanged();
            } , error -> {
                L.e(TAG , "ERR" , error.getMessage());
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    firestorage(uri);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getBaseContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public String getMimeType(File file) {
        String type = null;
        final String url = file.toString();
        final String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        if (type == null) {
            type = "image/*"; // fallback type. You might set it to */*
        }
        return type;
    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }


    public void showFile(File file){
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension(fileExt(file.getAbsolutePath()).substring(1));
        newIntent.setDataAndType(Uri.fromFile(file),mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No handler for this type of file.", Toast.LENGTH_LONG).show();
        }
    }



    @SuppressLint("CheckResult")
    public void firestorage(Uri uri) {
        String filename = "test_" + UUID.randomUUID().toString();
        String filetype = getMimeType(uri).split("/")[1];

        StorageReference mStorageRef;

        mStorageRef = FirebaseStorage.getInstance().getReference();

        Resources recurso = new Resources(filename , uri.toString() , filetype ,false);

        Observable.fromCallable( () -> LocalDb.getInstance(getBaseContext()).resourcesDao().insert(recurso)).subscribeOn(Schedulers.io())
        .doOnNext( id -> {

            recurso.setId(id.intValue());

            StorageReference riversRef = mStorageRef.child("images/"+recurso.getName()+"." + filetype);

            riversRef.getDownloadUrl().addOnSuccessListener( value ->  {
                L.d(TAG , "getdownloadurl" , "");
                recurso.setRemotePath(value.getPath());
            });

            //add file on Firebase and got Download Link
            riversRef.putFile(uri)
                .addOnProgressListener(taskSnapshot -> {
                    L.d(TAG , "progress" , "");
                    recurso.setProgress(regla(taskSnapshot.getBytesTransferred() , taskSnapshot.getTotalByteCount()));
                    Observable.fromCallable( () -> {
                        LocalDb.getInstance(getBaseContext()).resourcesDao().update(recurso);
                        return true;
                    }).subscribeOn(Schedulers.io())
                    .subscribe( data -> {

                    });
                }).addOnPausedListener(taskSnapshot -> {
                    L.d(TAG , "pause" , "");
                })
                .continueWithTask(task -> {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return riversRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Uri downUri = task.getResult();
                    Log.d(TAG, "onComplete: Url: "+ downUri.toString());
                    recurso.setRemotePath(downUri.toString());
                }
            }).addOnSuccessListener( snap -> {
                recurso.setRemote(true);

                Observable.fromCallable( () -> {
                    LocalDb.getInstance(getBaseContext()).resourcesDao().update(recurso);
                    return true;
                }).subscribeOn(Schedulers.io())
                        .subscribe( data -> {

                        });

                })
                .addOnCanceledListener( () ->
                        L.d(TAG , "cancel_listener" , "")
                )
                .addOnFailureListener( e  ->
                        L.e(TAG , "failure_listener" , e.getMessage()))
            ;


        })
        .subscribe( data -> {

        });

    }

    public int regla( long obtenido , long total ) {
        int tope = 100;
        return (int)((obtenido * tope ) / total ) ;
    }

    public void showWeb(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

}