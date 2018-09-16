package com.example.randylaptop.proyectofinal;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Subir extends AppCompatActivity {
    public Button btnguardar;
    public ImageButton btnfoto;
    public TextView tvUbicacion;
    public EditText etComentario;
    private ImageView imgPost;

    private Location ubicacion;
    private LocationCallback callback;
    private LocationRequest locationRequest;
    private Geocoder geocoder;
    private FusedLocationProviderClient fused;
    private ImagePicker imagePicker;
    private Uri uriImg;

    private StorageReference mStorage;
    private  DatabaseReference mDatabase;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir);
        geocoder = new Geocoder(this, Locale.getDefault());
        btnguardar = findViewById(R.id.btnPost);
        btnfoto = findViewById(R.id.fotoPost);
        tvUbicacion = findViewById(R.id.postUbicacion);
        etComentario = findViewById(R.id.postDescripcion);
        imgPost = findViewById(R.id.imagenPost);
        mStorage = FirebaseStorage.getInstance().getReference("uploads");
        mDatabase = FirebaseDatabase.getInstance().getReference("uploads");


        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
                Intent i = new Intent(Subir.this,Inicio.class);
                startActivity(i);
                }
        });




        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imagePicker == null) {
                    imagePicker = new ImagePicker(Subir.this, null, new OnImagePickedListener() {
                        @Override
                        public void onImagePicked(Uri imageUri) {
                            uriImg = imageUri;
                            imgPost.setImageURI(imageUri);
                        }
                    });
                }
                imagePicker.choosePicture(true);

            }

            });

        fused = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    ubicacion = locationResult.getLocations().get(0);
                    fused.removeLocationUpdates(callback);
                    try {
                        List<Address> addresses = new ArrayList<>(geocoder.getFromLocation(
                                ubicacion.getLatitude(),
                                ubicacion.getLongitude(),
                                1));
                        tvUbicacion.setText(addresses.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            tvUbicacion.setText("eso no he naa");

        }
            fused.requestLocationUpdates(locationRequest, callback, null);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode,requestCode, data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

    private String getFileExtension(Uri uri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }

    private void upload(){
        if( uriImg!= null) {
            StorageReference file = mStorage.child(System.currentTimeMillis() + "." + getFileExtension(uriImg));
            file.putFile(uriImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"Publicacion exitosa",Toast.LENGTH_LONG).show();
                    Publicar publicar = new Publicar(etComentario.getText().toString(),tvUbicacion.getText().toString()
                            ,taskSnapshot.getDownloadUrl().toString());
                    String subira = mDatabase.push().getKey();
                    mDatabase.child(subira).setValue(publicar);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),":(",Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(getApplicationContext(),"Seleccionar imagen",Toast.LENGTH_SHORT).show();
        }

    }
}
