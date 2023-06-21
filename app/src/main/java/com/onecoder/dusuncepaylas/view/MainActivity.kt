package com.onecoder.dusuncepaylas.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.onecoder.dusuncepaylas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth= Firebase.auth
        val guncelKullanici=auth.currentUser
        if (guncelKullanici!=null){
            val intent=Intent(this, DusunceActivity::class.java)
            startActivity(intent)
            finish()
        }





    }

    fun kayitOl(view:View){
        val email=binding.emailText.text.toString()
        val parola=binding.parolaText.text.toString()
        val kullaniciAdi=binding.kullaniciAdiText.text.toString()

        auth.createUserWithEmailAndPassword(email,parola)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val guncelKullanici=auth.currentUser
                    val profilGuncellemeIstegi= userProfileChangeRequest {
                        displayName=kullaniciAdi
                    }
                    if(guncelKullanici!=null){
                        guncelKullanici.updateProfile(profilGuncellemeIstegi).addOnCompleteListener {task->

                        if (task.isSuccessful){
                            Toast.makeText(applicationContext,"Profil Kullanıcı adı eklendi. $kullaniciAdi",Toast.LENGTH_LONG).show()
                        }

                        }
                    }

                    guncelKullanici!!.updateProfile(profilGuncellemeIstegi)
                    intent= Intent(this, DusunceActivity::class.java)
                    startActivity(intent)
                    finish()

                }

            }.addOnFailureListener { exception->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
    }

    fun girisYap(view:View){
        val email=binding.emailText.text.toString()
        val parola=binding.parolaText.text.toString()

        if (parola!="" && email!=""){
            auth.signInWithEmailAndPassword(email,parola).addOnCompleteListener {task->
                if (task.isSuccessful){
                    val guncelKullanici=auth.currentUser?.displayName.toString()
                    Toast.makeText(this,"Hoşgeldin $guncelKullanici",Toast.LENGTH_LONG).show()

                    val intent=Intent(this, DusunceActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }



    }
}