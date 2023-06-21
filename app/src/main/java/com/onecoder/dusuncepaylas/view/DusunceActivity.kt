package com.onecoder.dusuncepaylas.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.onecoder.dusuncepaylas.R
import com.onecoder.dusuncepaylas.adapter.DusunceAdapter
import com.onecoder.dusuncepaylas.databinding.ActivityDusunceBinding
import com.onecoder.dusuncepaylas.model.Paylasim

class DusunceActivity : AppCompatActivity() {
    lateinit var binding:ActivityDusunceBinding
    private lateinit var auth:FirebaseAuth
    val db=Firebase.firestore


    var paylasimListesi=ArrayList<Paylasim>()
    private lateinit var recyclerViewAdapter:DusunceAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.ana_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== R.id.cikis_yap){
            auth.signOut()
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else if(item.itemId== R.id.paylasim_yap){
            val intent=Intent(this, PaylasimActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDusunceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_dusunce)
        auth= Firebase.auth

        firebaseVerileriAl()
        var layoutManager= LinearLayoutManager(this,)
        binding.recyclerView.layoutManager=layoutManager
        recyclerViewAdapter=DusunceAdapter(paylasimListesi)
        binding.recyclerView.adapter=recyclerViewAdapter


    }

    fun firebaseVerileriAl(){
        db.collection("Paylasimlar").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
            if(error!=null){
                Toast.makeText(applicationContext,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if (snapshot!=null){
                    if(!snapshot.isEmpty){
                        val documents=snapshot.documents
                        paylasimListesi.clear()
                        for (document in documents){
                            val kullaniciAdi=document.get("kullaniciAdi") as String?
                            val kullaniciYorum=document.get("paylasilanYorum") as String?
                            val gorselUrl=document.get("gorselUrl") as String?

                            var indirilenPaylasim=Paylasim(kullaniciAdi, kullaniciYorum,gorselUrl)
                            paylasimListesi.add(indirilenPaylasim)
                        }

                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}