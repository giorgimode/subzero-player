subzero-player
===========

The Subzero Player is a media player application built using vlcj with a Swing rich-client user interface ([forked from vlcj-player](https://github.com/caprica/vlcj-player)). 

**Subzero Player is not just a player, it's core functionality is to make learning a new language as easy and smooth as possible**.
Subzero Player has a few killer features:
1) **Dictionary feature**: Add subtitle and watch the video file as usual with subtitles (subtitle must be manually added, not built it). 
When paused, there will be pop-up windows on screen with the translation of each word provided (feature can be toggled on/off)
2) **Ample language choice**: Choose language pair from over 100 language pairs 
3) **Second Subtitle feature**: Instead of using Dictionary feature, you can add the second subtitle, which means you can have two subtitles
running at the same time, e.g. one in english, second one in german. Second subtitle can be always shown or only shown when paused, so
its not too distracting. 
Second Subtitle feature and Dictionary feature cannot be both enabled at the same time.
4) **Streaming**: Stream video via URL, still able to use above features. Make sure your internet connection is fast enough to avoid lags. 

Generally the subzero-player tries to match the Qt interface of VLC with as many
of the same features implemented as possible.

However, it is not possible to get a 100% like-for-like implementation since
LibVLC, used by vlcj, exposes only a sub-set of the total functionality of VLC. 

Screenshot
----------

![vlcj-player](https://github.com/caprica/vlcj-player/raw/master/doc/vlcj-player.png "vlcj-player")

Features
--------

 - subtitle word translation
 - language pair list for subtitle translation
 - second subtitle
 - stream via URL  
 - audio player
 - video player
 - full-screen
 - audio equalizer
 - video adjustments
 - title selection
 - chapter navigation
 - audio track selection
 - video track selection
 - subtitle track selection
 - load external subtitle file
 - change audio device
 - change playback speed
 - capture and display native logs
 - capture and display video surface debug messages (e.g. to test mouse and keyboard events still work)
 - volume controls
 - mute
 - zoom/scale
 - aspect ratio
 - crop
 - always on top
 - video snapshots
 - redirect native output streams (on Linux)

...and a whole bunch of other nifty stuff.
 

Status
------

This project is currently a work-in-progress. There are some more exciting things to come, most notably a web portal and user profiles.
User will be able to save a word by clicking on it in the media player subtitle translation window. Later user can log in their portal
from browser and view list of words with translations and the context of the subtitle word was used.
Progress is tracked in Youtrack project management tool. Contact me at [sub0.project@gmail.com](sub0.project@gmail.com) to gain access
to project board.


How to run
---------------------------------


History
---------------------------------
Project was conceived when I moved to Germany in 2016 and struggled to force myself to learn the language (since my work environment was in English).
Watching movies in German and looking up each word was too time consuming, I was losing focus and interesting really fast. I did not find any fun/
engaging way to learn a new language at home after work. So I decided to take things in my own hands and create a media player with integrated
dictionary. 

License
-------

The subzero-player project is provided under the GPL, version 3 or later.
