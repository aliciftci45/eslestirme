package com.aliciftci45.eletirme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;




import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class userProfil extends AppCompatActivity  {




    TextView idAge,idHobi,idSex,idJob,idMusic,idBurc;
    ImageView userImage,selectPicture;
    Button idSave;

    Uri selected;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;

    private String[] Yas ={"14-16","16-19","19-25","25-30","30-35","35-45","45-55","55-99"};

    private String[] Hobi ={"Alkol İçmek","Yüzmek","Blog Yazmak","Kitap Okumak","Gezmek","TV İzlemek","Dil Öğrenmek"
            ,"Seyahat Etmek","Spor Yapmak","Balık Tutmak","Kamp Yapmak","Okçuluk","Fotoğraf Çekmek","Araştırma Yapmak"
            ,"Youtubeda Takılmak","Video Çekmek","Bisiklet Sürmek","Bahçe İşleri","Dans Etmek","Astroloji",
            "Gönüllü Olarak İyilik Yapmak"
            ,"Çizmek","Maç İzlemek","Yoga","Kusmak"};

    private String[] Music ={"Caz","Rap","Rock","Blues","Heavy Metal","Halk Müziği","Pop Müzik","Kelt Müziği","Klasik Müzik","R&B veya RnB"
            ,"Çağdaş Müzik","Doğu Müziği","Deep House","Club","Elektronik Müzik","Kulağıma Güzel Gelen Müzik"};

    private String[] Job ={"Öğrenci","Tercüman","Sağlıkcı","Teknoloji","Yazılım","Barolar Birliği","Emniyet Müdürlügü","Reklam", "Finans","Milli Eğitim Bakanlığı"
            ,"Sporcu","İnşaat Sektörü","Medya Sektörü","Youtub veya Twich","Aşcı","İşletme Sahibi","Siyaset","Şarkıcı","Hizmet Sektörü"
            ,"Eğlence Sektörü","Grafiker" ,"Sanatcı","Diğeri"};

    private String[] Sex ={"Kadın","Erkek"};


    private String[] Burc ={"Kova","Yay","Aslan","Oglak","Terazi","Yengec","Akrep","Koc","Boga","Ikizler","Basak","Balik"};


    private Spinner music;
    private Spinner job;
    private Spinner sex;
    private Spinner hobi;
    private Spinner age;
    private Spinner burc;
    private String currentUId;






    private ArrayAdapter<String> dataAdapterForAge;
    private ArrayAdapter<String> dataAdapterForMusic;
    private ArrayAdapter<String> dataAdapterForJob;
    private ArrayAdapter<String> dataAdapterForSex;
    private ArrayAdapter<String> dataAdapterForHobi;
    private ArrayAdapter<String> dataAdapterForBurc;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profil);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();








        userImage = findViewById(R.id.selectPicture);

        age = findViewById(R.id.age);
        hobi =findViewById(R.id.hobi);
        sex =findViewById(R.id.sex);
        job =findViewById(R.id.job);
        music =findViewById(R.id.music);
        idSave = findViewById(R.id.idMatch );
        burc = findViewById( R.id.burc );




        dataAdapterForAge = new ArrayAdapter<>(userProfil.this, android.R.layout.simple_spinner_item, Yas);
        dataAdapterForHobi = new ArrayAdapter<>(userProfil.this, android.R.layout.simple_spinner_item,Hobi);
        dataAdapterForSex = new ArrayAdapter<>(userProfil.this, android.R.layout.simple_spinner_item, Sex);
        dataAdapterForJob = new ArrayAdapter<>(userProfil.this, android.R.layout.simple_spinner_item,Job);
        dataAdapterForMusic = new ArrayAdapter<>(userProfil.this, android.R.layout.simple_spinner_item, Music);
        dataAdapterForBurc = new ArrayAdapter<>(userProfil.this, android.R.layout.simple_spinner_item, Burc);




        dataAdapterForAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForHobi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForMusic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForJob.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForBurc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        music.setAdapter(dataAdapterForMusic);
        sex.setAdapter(dataAdapterForSex);
        hobi.setAdapter(dataAdapterForHobi);
        job.setAdapter(dataAdapterForJob);
        age.setAdapter(dataAdapterForAge);
        burc.setAdapter(dataAdapterForBurc);









    }

    public void Save(View view){



        UUID uuidImage = UUID.randomUUID();

        final String imageName = "images/"+uuidImage+".jpg";
        StorageReference storageReferencee = storageReference.child(imageName);

        storageReferencee.putFile(selected).addOnSuccessListener(userProfil.this,new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadURL = uri.toString();
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userMail = user.getEmail();




                        String userAge =  age.getSelectedItem().toString();
                        String userSex =  sex.getSelectedItem().toString();
                        String userHobi =  hobi.getSelectedItem().toString();
                        String userJob =  job.getSelectedItem().toString();
                        String userMusic =  music.getSelectedItem().toString();
                        String userBurc =  burc.getSelectedItem().toString();









                        if (age.getSelectedItem().toString().equals( "14-16" )
                                && burc.getSelectedItem().toString().equals("Yay" )
                                && music.getSelectedItem().toString().equals( "Caz" ) && sex.getSelectedItem().toString().equals( "Erkek" )) {


                            databaseReference.child("Erkek"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Age" ).setValue( userAge );
                            databaseReference.child("Erkek"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Sex" ).setValue( userSex );
                            databaseReference.child("Erkek"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Hobi" ).setValue( userHobi );
                            databaseReference.child("Erkek"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Job" ).setValue( userJob );
                            databaseReference.child("Erkek"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Music" ).setValue( userMusic );
                            databaseReference.child("Erkek"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "resim" ).setValue( downloadURL );
                            databaseReference.child("Erkek"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Mail" ).setValue( userMail );
                            databaseReference.child("Erkek"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Burcunuz" ).setValue( userBurc );
                            databaseReference.child("Erkek"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "UserKey" ).setValue( "111" );







                        }else if (age.getSelectedItem().toString().equals( "14-16" )
                                && burc.getSelectedItem().toString().equals( "Kova" )
                                && music.getSelectedItem().toString().equals( "Caz" ) && sex.getSelectedItem().toString().equals( "Kadın" )) {


                            databaseReference.child("Kadın"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Age" ).setValue( userAge );
                            databaseReference.child("Kadın"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Sex" ).setValue( userSex );
                            databaseReference.child("Kadın"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Hobi" ).setValue( userHobi );
                            databaseReference.child("Kadın"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Job" ).setValue( userJob );
                            databaseReference.child("Kadın"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Music" ).setValue( userMusic );
                            databaseReference.child("Kadın"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "resim" ).setValue( downloadURL );
                            databaseReference.child("Kadın"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Mail" ).setValue( userMail );
                            databaseReference.child("Kadın"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "Burcunuz" ).setValue( userBurc );
                            databaseReference.child("Kadın"  ).child( "userAnswer" ).child( "Grup1" ).child( currentUId ).child( "UserKey" ).setValue( "121" );


                        }

                        Toast.makeText( userProfil.this,"Bilgiler Kaydedildi.", Toast.LENGTH_SHORT ).show();












                    }
                });




            }


        });
    }




    public void connection(View view) {




    }





    public void profilPicture(View view) {
        if (ContextCompat.checkSelfPermission(userProfil.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(userProfil.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);


        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode ==1 ){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            selected = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selected);
                userImage.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
                Toast.makeText(userProfil.this, "Lütfen Profil Resmi Seçiniz!", Toast.LENGTH_SHORT).show();

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
