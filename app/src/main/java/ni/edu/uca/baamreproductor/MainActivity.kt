package ni.edu.uca.baamreproductor

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ni.edu.uca.baamreproductor.databinding.RowSongBinding

class MainActivity : AppCompatActivity() {
    lateinit var rowSongBinding: RowSongBinding
    
    val fd by lazy {
        assets.openFd(songAct)
    }

    val mp by lazy {
        val m = MediaPlayer()
        m.setDataSource(
            fd.fileDescriptor,
            fd.startOffset,
            fd.length
        )
        fd.close()
        m.prepare()
        m
    }

    val controllers by lazy {
        listOf(R.id.btnPre, R.id.btnStop, R.id.btnPlay, R.id.btnNext).map {
            findViewById<MaterialButton>(it)
        }
    }

    object ci {
        val btnPrev = 0
        val btnStop = 1
        val btnPlay = 2
        val btnNext = 3
    }

    val nameSong by lazy {
        findViewById<TextView>(R.id.tvNameSong)
    }

     val songs by lazy {
          val lista = assets.list("")?.toList() ?: listOf()
          lista.filter { it.contains(".mp3") }
      }

     var songActIndex = 0
         set(value) {
             var v = if (value == -1) {
                 songs.size - 1
             } else {
                 value % songs.size
             }
             field = v
             songAct = songs[v]
         }

     lateinit var songAct: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rowSongBinding = RowSongBinding.inflate(layoutInflater)
        
        setContentView(R.layout.activity_main)
        controllers[ci.btnPlay].setOnClickListener(this::playClicked)
        controllers[ci.btnStop].setOnClickListener(this::stopClicked)
        controllers[ci.btnPrev].setOnClickListener(this::prevClicked)
        controllers[ci.btnNext].setOnClickListener(this::nextClicked)
        songAct = songs[songActIndex]
        nameSong.text = songAct
    }

    override fun onStart() {
        super.onStart()
        findViewById<RecyclerView>(R.id.rcvSongs).apply {
            adapter = AdapterSongs(songs, this@MainActivity)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    fun playClicked(v: View) {
        if (!mp.isPlaying) {
            mp.start()
            controllers[ci.btnPlay].setIconResource(R.drawable.btnpause)
            nameSong.visibility = View.VISIBLE
            nameSong.isSelected = true
        } else {
            mp.pause()
            controllers[ci.btnPlay].setIconResource(R.drawable.btnplay)
            nameSong.isSelected = false
        }
    }

    fun stopClicked(v: View) {
        if (mp.isPlaying) {
            mp.pause()
            controllers[ci.btnPlay].setIconResource(R.drawable.btnplay)
            nameSong.visibility = View.INVISIBLE
        }
        mp.seekTo(0)
    }
    fun nextClicked(v: View){
        songActIndex++
        refreshSong()
    }

    fun prevClicked(v:View){
        songActIndex--
        refreshSong()
    }

    fun refreshSong(){
        mp.reset()
        val fd = assets.openFd(songAct)
        mp.setDataSource(
            fd.fileDescriptor,
            fd.startOffset,
            fd.length
        )
        mp.prepare()
        playClicked(controllers[ci.btnPlay])
        nameSong.text = songAct
    }
}

