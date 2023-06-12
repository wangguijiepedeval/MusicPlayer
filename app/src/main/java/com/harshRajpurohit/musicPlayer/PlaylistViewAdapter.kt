package com.harshRajpurohit.musicPlayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harshRajpurohit.musicPlayer.databinding.PlaylistViewBinding

/*自定义的RecyclerView适配器，用于展示播放列表的视图，
并处理删除播放列表、打开播放列表详情等操作。适配器还提供了刷新播放列表的功能。*/
class PlaylistViewAdapter(private val context: Context, private var playlistList: ArrayList<Playlist>) : RecyclerView.Adapter<PlaylistViewAdapter.MyHolder>() {

    /*定义了一些视图的引用，包括播放列表的图片、名称、根视图和删除按钮。*/
    class MyHolder(binding: PlaylistViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.playlistImg
        val name = binding.playlistName
        val root = binding.root
        val delete = binding.playlistDeleteBtn
    }

    /*创建每个播放列表项的视图持有者（MyHolder实例）。该方法通过绑定布局文件（PlaylistViewBinding）来创建视图。*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(PlaylistViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    /*用于绑定数据到每个播放列表项的视图。
    该方法会根据位置设置播放列表的名称、设置删除按钮的点击事件、设置播放列表项的点击事件、加载播放列表封面图像等。*/
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        if(MainActivity.themeIndex == 4){
            holder.root.strokeColor = ContextCompat.getColor(context, R.color.white)
        }
        holder.name.text = playlistList[position].name
        holder.name.isSelected = true
        holder.delete.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(context)
            builder.setTitle(playlistList[position].name)
                .setMessage("Do you want to delete playlist?")
                .setPositiveButton("Yes"){ dialog, _ ->
                    PlaylistActivity.musicPlaylist.ref.removeAt(position)
                    refreshPlaylist()
                    dialog.dismiss()
                }
                .setNegativeButton("No"){dialog, _ ->
                    dialog.dismiss()
                }
            val customDialog = builder.create()
            customDialog.show()

            setDialogBtnBackground(context, customDialog)
        }
        holder.root.setOnClickListener {
            val intent = Intent(context, PlaylistDetails::class.java)
            intent.putExtra("index", position)
            ContextCompat.startActivity(context, intent, null)
        }
        if(PlaylistActivity.musicPlaylist.ref[position].playlist.size > 0){
            Glide.with(context)
                .load(PlaylistActivity.musicPlaylist.ref[position].playlist[0].artUri)
                .apply(RequestOptions().placeholder(R.drawable.music_player_icon_slash_screen).centerCrop())
                .into(holder.image)
        }
    }

    /*返回播放列表数据的大小*/
    override fun getItemCount(): Int {
        return playlistList.size
    }
    /*用于刷新播放列表。该方法会更新适配器的播放列表数据，并通知适配器数据发生了变化。*/
    fun refreshPlaylist(){
        playlistList = ArrayList()
        playlistList.addAll(PlaylistActivity.musicPlaylist.ref)
        notifyDataSetChanged()
    }
}