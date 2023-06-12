# MusicPlayer
## 实验一

1.创建一个工程，名为MusicPlayer；:heavy_check_mark:

2.编写一个MainActivity，该Activity布局包含“本地”、“最近播放”、“我的收藏”三个按钮，按钮需要有文字和图标；:heavy_check_mark:

3.Activity的底部添加一个播放状态栏，添加一个播放按钮，点击时切换按钮的状态:heavy_check_mark:

### 结构：

- MainActivity.kt
- activity_main.xml
- 



## 实验二

1.创建一个Activity，名为LocalMusicActivity；:heavy_check_mark:

2.创   建   四   个Fragment，   分   别   名   为SingleSongFragment、AlbumFragment、SingerFragment、FolderFragment；:heavy_check_mark:

3.在Activity中添加一个ViewPager（androidx.viewpager.widget.ViewPager），并编写相应的Adapter，继承自FragmentPagerAdapter，重写相应的抽象方法，在getItem中返回对应的Fragment；:heavy_check_mark:

4.使用ContentResolver加载本地音乐，并根据相应的字段进行分组，传递给对应的Fragment；:heavy_check_mark:

5.每个Fragment中，添加一个RecyclerView以及对应的item来展示列表内容；:heavy_check_mark:

6.专辑、歌手和文件夹3个tab功能基本一致，均已一定规则进行分类，点击某个专辑、歌手或文件夹，将会进入该专辑、歌手或文件夹的二级界面。:heavy_check_mark:



## 实验三

1.创建播放界面PlayerActivity，播放界面提供必要的操作功能：

￮可拖动的进度条显示；:heavy_check_mark:

￮上一首:heavy_check_mark:

￮下一首:heavy_check_mark:

￮播放/暂停:heavy_check_mark:

￮播放模式选择（顺序播放、循环播放、单曲循环播放、随机播放）:heavy_check_mark:

￮收藏:heavy_check_mark:

￮弹出菜单:heavy_check_mark:

▪添加到歌单:heavy_check_mark:

▪查看专辑——定位到对应的专辑列表（如有）

▪查看歌手——定位到对应的歌手列表（如有）

▪进阶——设为铃声

￮进阶：歌词显示，可显示lrc歌词同步，歌词同步根据歌名从网络



## 实验四

1.播放过的音乐按先进先出的策略进入最近播放列表，==最近播放==通过room或者sqlite保存到本地；

2.点击收藏按钮时，将当前歌曲记录到收藏列表中，并保存至room；:heavy_check_mark:

3.点击实验一的主页“最近播放”和“我的收藏”按钮会进入对应的页面；:heavy_check_mark:



## 实验五

1.歌单由用户创建，在用户添加歌单时，可选择一个已经存在的歌单，或新建一个歌单，点击歌单后会进入歌单界面，界面支持全部播放该歌单的音乐，歌单数据保存到数据库中（room或者sqlite）:heavy_check_mark:





## 实验六

1.当播放音乐时，在系统通知栏上显示播放状态，可进行播放和暂停操作，点击可进入播放界面。:heavy_check_mark:
