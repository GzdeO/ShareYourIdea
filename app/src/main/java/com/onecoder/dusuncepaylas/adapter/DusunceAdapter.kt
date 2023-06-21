package com.onecoder.dusuncepaylas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onecoder.dusuncepaylas.databinding.RecyclerRowBinding
import com.onecoder.dusuncepaylas.model.Paylasim
import com.squareup.picasso.Picasso

class DusunceAdapter(val paylasimListesi:ArrayList<Paylasim>):
    RecyclerView.Adapter<DusunceAdapter.PaylasimHolder>() {
    class PaylasimHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DusunceAdapter.PaylasimHolder {
        val binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PaylasimHolder(binding)
    }

    override fun getItemCount(): Int {
        return paylasimListesi.size
    }

    override fun onBindViewHolder(holder: DusunceAdapter.PaylasimHolder, position: Int) {
        holder.binding.recyclerRowKullaniciAdi.text=paylasimListesi[position].kullaniciAdi
        holder.binding.recyclerRowPaylasimMesaji.text=paylasimListesi[position].kullaniciYorum
        if(paylasimListesi[position].gorselUrl!=null){
            holder.binding.recyclerRowImageview.visibility= View.VISIBLE
            Picasso.get().load(paylasimListesi[position].gorselUrl).into(holder.binding.recyclerRowImageview)
        }

    }
}