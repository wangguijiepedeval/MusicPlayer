package com.harshRajpurohit.musicPlayer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.harshRajpurohit.musicPlayer.MusicAdapter.MyHolder
import com.harshRajpurohit.musicPlayer.databinding.DetailsViewBinding
import com.harshRajpurohit.musicPlayer.databinding.MoreFeaturesBinding
import com.harshRajpurohit.musicPlayer.databinding.MusicViewBinding
/*RecyclerView 适配器
* 通过 MusicAdapter 类将音乐列表数据绑定到 RecyclerView 上，并根据不同的场景和用户交互，展示相应的视图和执行相应的操作*/
class MusicAdapter(private val context: Context, private var musicList: ArrayList<Music>, private val playlistDetails: Boolean = false,
private val selectionActivity: Boolean = false)
    : RecyclerView.Adapter<MyHolder>() {

    /*显示音乐信息的视图元素*/
    class MyHolder(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.songNameMV
        val album = binding.songAlbumMV
        val image = binding.imageMV
        val duration = binding.songDuration
        val root = binding.root
    }

    /*创建新的 ViewHolder 实例时被调用，并将 MusicViewBinding 绑定到视图。*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MusicViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    /*在每个列表项要显示时被调用，用于将音乐信息绑定到 ViewHolder 中的视图元素。
    其中包括设置音乐标题、专辑、封面图像和时长的显示，以及为列表项的点击事件设置不同的处理逻辑。
    */
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = musicList[position].title
        holder.album.text = musicList[position].album
        holder.duration.text = formatDuration(musicList[position].duration)
        Glide.with(context)
            .load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_player_icon_slash_screen).centerCrop())
            .into(holder.image)

        //"播放下一个"功能
        if(!selectionActivity)
            holder.root.setOnLongClickListener {
                val customDialog = LayoutInflater.from(context).inflate(R.layout.more_features, holder.root, false)
                val bindingMF = MoreFeaturesBinding.bind(customDialog)
                val dialog = MaterialAlertDialogBuilder(context).setView(customDialog)
                    .create()
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(0x99000000.toInt()))

                bindingMF.AddToPNBtn.setOnClickListener {
                    try {
                        if(PlayNext.playNextList.isEmpty()){
                            PlayNext.playNextList.add(PlayerActivity.musicListPA[PlayerActivity.songPosition])
                            PlayerActivity.songPosition = 0
                        }

                        PlayNext.playNextList.add(musicList[position])
                        PlayerActivity.musicListPA = ArrayList()
                        PlayerActivity.musicListPA.addAll(PlayNext.playNextList)
                    }catch (e: Exception){
                        Snackbar.make(context, holder.root,"Play A Song First!!", 3000).show()
                    }
                    dialog.dismiss()
                }

                bindingMF.infoBtn.setOnClickListener {
                    dialog.dismiss()
                    val detailsDialog = LayoutInflater.from(context).inflate(R.layout.details_view, bindingMF.root, false)
                    val binder = DetailsViewBinding.bind(detailsDialog)
                    binder.detailsTV.setTextColor(Color.WHITE)
                    binder.root.setBackgroundColor(Color.TRANSPARENT)
                    val dDialog = MaterialAlertDialogBuilder(context)
//                        .setBackground(ColorDrawable(0x99000000.toInt()))
                        .setView(detailsDialog)
                        .setPositiveButton("OK"){self, _ -> self.dismiss()}
                        .setCancelable(false)
                        .create()
                    dDialog.show()
                    dDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
                    setDialogBtnBackground(context, dDialog)
                    dDialog.window?.setBackgroundDrawable(ColorDrawable(0x99000000.toInt()))
                    val str = SpannableStringBuilder().bold { append("DETAILS\n\nName: ") }
                        .append(musicList[position].title)
                        .bold { append("\n\nDuration: ") }.append(DateUtils.formatElapsedTime(musicList[position].duration/1000))
                        .bold { append("\n\nLocation: ") }.append(musicList[position].path)
                    binder.detailsTV.text = str
                }

                return@setOnLongClickListener true
            }

        when{
            playlistDetails ->{
                holder.root.setOnClickListener {
                    sendIntent(ref = "PlaylistDetailsAdapter", pos = position)
                }
            }
            selectionActivity ->{
                holder.root.setOnClickListener {
                    if(addSong(musicList[position]))
                        holder.root.setBackgroundColor(ContextCompat.getColor(context, R.color.cool_pink))
                    else
                        holder.root.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

                }
            }
            else ->{
                holder.root.setOnClickListener {
                when{
                    MainActivity.search -> sendIntent(ref = "MusicAdapterSearch", pos = position)
                    musicList[position].id == PlayerActivity.nowPlayingId ->
                        sendIntent(ref = "NowPlaying", pos = PlayerActivity.songPosition)
                    else->sendIntent(ref="MusicAdapter", pos = position) } }
        }

         }
    }

    /*返回音乐列表的大小，确定要显示的列表项数量。*/
    override fun getItemCount(): Int {
        return musicList.size
    }

    /*更新列表*/
    fun updateMusicList(searchList : ArrayList<Music>){
        musicList = ArrayList()
        musicList.addAll(searchList)
        notifyDataSetChanged()
    }
    private fun sendIntent(ref: String, pos: Int){
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("index", pos)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context, intent, null)
    }
    private fun addSong(song: Music): Boolean{
        PlaylistActivity.musicPlaylist.ref[PlaylistDetails.currentPlaylistPos].playlist.forEachIndexed { index, music ->
            if(song.id == music.id){
                PlaylistActivity.musicPlaylist.ref[PlaylistDetails.currentPlaylistPos].playlist.removeAt(index)
                return false
            }
        }
        PlaylistActivity.musicPlaylist.ref[PlaylistDetails.currentPlaylistPos].playlist.add(song)
        return true
    }
    /*刷新播放列表*/
    fun refreshPlaylist(){
        musicList = ArrayList()
        musicList = PlaylistActivity.musicPlaylist.ref[PlaylistDetails.currentPlaylistPos].playlist
        notifyDataSetChanged()
    }
}