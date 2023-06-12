package com.harshRajpurohit.musicPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harshRajpurohit.musicPlayer.databinding.ActivityPlaylistBinding
import com.harshRajpurohit.musicPlayer.databinding.AddPlaylistDialogBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/*在音乐播放器应用中实现展示播放列表、添加新的播放列表以及显示对话框等功能。*/
class PlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var adapter: PlaylistViewAdapter

    companion object{
        // 用于存储音乐播放列表的实例。
        var musicPlaylist: MusicPlaylist = MusicPlaylist()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.currentTheme[MainActivity.themeIndex])
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.playlistRV.setHasFixedSize(true)
        binding.playlistRV.setItemViewCacheSize(13)
        binding.playlistRV.layoutManager = GridLayoutManager(this@PlaylistActivity, 2)
        adapter = PlaylistViewAdapter(this, playlistList = musicPlaylist.ref)
        binding.playlistRV.adapter = adapter
        binding.backBtnPLA.setOnClickListener { finish() }
        binding.addPlaylistBtn.setOnClickListener { customAlertDialog() }

        if(musicPlaylist.ref.isNotEmpty()) binding.instructionPA.visibility = View.GONE
        /*设置活动的主题。
        使用ActivityPlaylistBinding类的静态方法inflate来绑定布局文件。
        将绑定的布局文件设置为活动的内容视图。
        设置播放列表的RecyclerView（binding.playlistRV）的一些属性，如固定大小、缓存大小和布局管理器。
        创建PlaylistViewAdapter实例，并将其设置为RecyclerView的适配器。
        设置返回按钮（binding.backBtnPLA）的点击事件，点击时关闭活动。
        设置添加播放列表按钮（binding.addPlaylistBtn）的点击事件，点击时显示自定义的对话框。
        */
    }

    /*使用LayoutInflater从XML布局文件中创建自定义的对话框视图。
      使用MaterialAlertDialogBuilder创建对话框，并设置视图、标题和确定按钮的点击事件。
      显示对话框，并设置对话框按钮的背景样式。
      */
    private fun customAlertDialog(){
        val customDialog = LayoutInflater.from(this@PlaylistActivity).inflate(R.layout.add_playlist_dialog, binding.root, false)
        val binder = AddPlaylistDialogBinding.bind(customDialog)
        val builder = MaterialAlertDialogBuilder(this)
        val dialog = builder.setView(customDialog)
            .setTitle("Playlist Details")
            .setPositiveButton("ADD"){ dialog, _ ->
                val playlistName = binder.playlistName.text
                val createdBy = binder.yourName.text
                if(playlistName != null && createdBy != null)
                    if(playlistName.isNotEmpty() && createdBy.isNotEmpty())
                    {
                        addPlaylist(playlistName.toString(), createdBy.toString())
                    }
                dialog.dismiss()
            }.create()
        dialog.show()
        setDialogBtnBackground(this, dialog)

    }
    /*添加播放列表：
    * 检查是否已存在同名的播放列表。
      如果存在，显示一个短时提示消息。
      如果不存在，创建一个新的Playlist对象，并将其添加到musicPlaylist中的引用列表中。
      更新适配器以刷新播放列表的显示。
      * */
    private fun addPlaylist(name: String, createdBy: String){
        var playlistExists = false
        for(i in musicPlaylist.ref) {
            if (name == i.name){
                playlistExists = true
                break
            }
        }
        if(playlistExists) Toast.makeText(this, "Playlist Exist!!", Toast.LENGTH_SHORT).show()
        else {
            val tempPlaylist = Playlist()
            tempPlaylist.name = name
            tempPlaylist.playlist = ArrayList()
            tempPlaylist.createdBy = createdBy
            val calendar = Calendar.getInstance().time
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            tempPlaylist.createdOn = sdf.format(calendar)
            musicPlaylist.ref.add(tempPlaylist)
            adapter.refreshPlaylist()
        }
    }

    /*当活动恢复时，通知适配器数据发生了变化，以更新播放列表的显示。*/
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}