package com.harshRajpurohit.musicPlayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/*用于接收通知栏中的操作按钮的点击事件
* 用于处理通知栏中的操作按钮的点击事件，根据点击的按钮执行相应的操作，包括播放音乐、暂停音乐、切换歌曲等，并更新界面和通知栏的状态。*/
class NotificationReceiver:BroadcastReceiver() {
    /*重写了 onReceive 方法，用于接收广播消息并执行相应的操作。*/
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            //当音乐列表包含多首歌曲时，仅播放下一首或前一首歌曲
            ApplicationClass.PREVIOUS -> if(PlayerActivity.musicListPA.size > 1) prevNextSong(increment = false, context = context!!)
            ApplicationClass.PLAY -> if(PlayerActivity.isPlaying) pauseMusic() else playMusic()
            ApplicationClass.NEXT -> if(PlayerActivity.musicListPA.size > 1) prevNextSong(increment = true, context = context!!)
            ApplicationClass.EXIT ->{
                exitApplication()
            }
        }
        /*
        如果动作是 ApplicationClass.PREVIOUS，且音乐列表中的歌曲数量大于 1，则调用 prevNextSong 方法，参数 increment 为 false，用于切换到上一首歌曲。
        如果动作是 ApplicationClass.PLAY，判断当前是否正在播放音乐，如果是，则调用 pauseMusic 方法暂停音乐，否则调用 playMusic 方法播放音乐。
        如果动作是 ApplicationClass.NEXT，且音乐列表中的歌曲数量大于 1，则调用 prevNextSong 方法，参数 increment 为 true，用于切换到下一首歌曲。
        如果动作是 ApplicationClass.EXIT，则调用 exitApplication 方法退出应用。
        */
    }
    /*用于播放音乐，设置播放状态和界面显示，并调用 MusicService 的 showNotification 方法更新通知栏。*/
    private fun playMusic(){
        PlayerActivity.isPlaying = true
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.pause_icon)
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.pause_icon)
        //for handling app crash during notification play - pause btn (While app opened through intent)
        try{ NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.pause_icon) }catch (_: Exception){}
    }

    /*用于暂停音乐，设置暂停状态和界面显示*/
    private fun pauseMusic(){
        PlayerActivity.isPlaying = false
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.play_icon)
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.play_icon)
        //for handling app crash during notification play - pause btn (While app opened through intent)
        try{ NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.play_icon) }catch (_: Exception){}
    }

    /*用于切换到上一首或下一首歌曲，根据参数 increment 的值决定切换的方向，同时更新界面显示和音乐播放状态。*/
    /*使用 Glide 库加载当前歌曲的封面图像，并更新界面上的歌曲名称和封面图像。*/
    private fun prevNextSong(increment: Boolean, context: Context){
        setSongPosition(increment = increment)
        PlayerActivity.musicService!!.createMediaPlayer()
        Glide.with(context)
            .load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_player_icon_slash_screen).centerCrop())
            .into(PlayerActivity.binding.songImgPA)
        PlayerActivity.binding.songNamePA.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
        Glide.with(context)
            .load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_player_icon_slash_screen).centerCrop())
            .into(NowPlaying.binding.songImgNP)
        NowPlaying.binding.songNameNP.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
        playMusic()
        PlayerActivity.fIndex = favouriteChecker(PlayerActivity.musicListPA[PlayerActivity.songPosition].id)
        if(PlayerActivity.isFavourite) PlayerActivity.binding.favouriteBtnPA.setImageResource(R.drawable.favourite_icon)
        else PlayerActivity.binding.favouriteBtnPA.setImageResource(R.drawable.favourite_empty_icon)
    }
}