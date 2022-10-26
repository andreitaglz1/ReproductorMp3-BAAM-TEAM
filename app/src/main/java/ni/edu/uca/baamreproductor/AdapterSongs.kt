package ni.edu.uca.baamreproductor

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uca.baamreproductor.databinding.RowSongBinding

class AdapterSongs (val elementos: List<String>, val con:MainActivity):
    RecyclerView.Adapter<AdapterSongs.ViewHolder>(){

    var selected = -1

        class ViewHolder(val bind: RowSongBinding)
            :RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RowSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = elementos[position]
        with(holder.bind){
            rowSongName.text = elem
            if(position==selected){
                rowSong.setBackgroundColor(Color.LTGRAY)
            }
            else{
                rowSong.setBackgroundColor(Color.WHITE)
            }
            rowSong.setOnClickListener{
                con.songActIndex = position
                con.refreshSong()
                selected = position
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return elementos.size
    }

}

