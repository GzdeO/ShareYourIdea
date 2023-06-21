package com.onecoder.dusuncepaylas.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.onecoder.dusuncepaylas.databinding.ActivityPaylasimBinding
import java.util.UUID
import java.util.zip.ZipEntry

class PaylasimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaylasimBinding
    val db=Firebase.firestore
    val storage=Firebase.storage
    private lateinit var auth:FirebaseAuth

    var secilenGorsel : Uri?=null
    var secilenBitmap: Bitmap?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPaylasimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth
    }

    fun paylas(view: View){

        if(secilenGorsel!=null){
            val reference=storage.reference
            val uuid=UUID.randomUUID()
            var gorselIsmi="${uuid}.jpg"
            val gorselReferansi=reference.child("gorseller").child(gorselIsmi)
            gorselReferansi.putFile(secilenGorsel!!).addOnSuccessListener {task->
                val yuklenenGorselReferansi=reference.child("gorseller").child(gorselIsmi)
                yuklenenGorselReferansi.downloadUrl.addOnSuccessListener { uri->
                    val downloadUrl=uri.toString()
                    veriTabaninaKaydet(downloadUrl)
                   /* val downloadUrl=uri.toString()
                    val paylasilanYorum=binding.paylasimText.text.toString()
                    val kullaniciAdi=auth.currentUser!!.displayName.toString()
                    val tarih=Timestamp.now()

                    val paylasimMap= hashMapOf<String,Any>()
                    paylasimMap.put("paylasilanYorum",paylasilanYorum)
                    paylasimMap.put("kullaniciAdi",kullaniciAdi)
                    paylasimMap.put("tarih",tarih)
                    paylasimMap.put("gorselUrl",downloadUrl)

                    db.collection("Paylasimlar").add(paylasimMap).addOnCompleteListener {task->
                        if(task.isSuccessful){
                            finish()
                        }

                    }.addOnFailureListener { exception->
                        Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                    } */
                }
            }.addOnFailureListener { exception->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }else{
            veriTabaninaKaydet(null)
        }


    }

    private fun veriTabaninaKaydet(downloadUrl:String?){

        val paylasilanYorum=binding.paylasimText.text.toString()
        val kullaniciAdi=auth.currentUser!!.displayName.toString()
        val tarih=Timestamp.now()

        val paylasimMap= hashMapOf<String,Any>()
        paylasimMap.put("paylasilanYorum",paylasilanYorum)
        paylasimMap.put("kullaniciAdi",kullaniciAdi)
        paylasimMap.put("tarih",tarih)

        if (downloadUrl!=null){
            paylasimMap.put("gorselUrl",downloadUrl)
        }

        db.collection("Paylasimlar").add(paylasimMap).addOnCompleteListener {task->
            if(task.isSuccessful){
                finish()
            }

        }.addOnFailureListener { exception->
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
    fun gorselEkle(view: View){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //İzin verilmemişse. İzin istememiz gerekiyorsa
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{
            //İzin zaten verilmiş. Direk galeriye gidebiliriz.
            val galeriIntent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode==1){
            if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //İzin verilmiş.
                val galeriIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode==2 && resultCode== RESULT_OK && data!=null){
            secilenGorsel=data.data
            binding.imageView.visibility=View.VISIBLE
            if(secilenGorsel!=null){
                if (Build.VERSION.SDK_INT>=28){
                    val source=ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap=ImageDecoder.decodeBitmap(source)
                    binding.imageView.setImageBitmap(secilenBitmap)
                }else{
                    secilenBitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    binding.imageView.setImageBitmap(secilenBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}