subzero-player
===========

The Subzero Player is a media player application built using vlcj with a Swing rich-client user interface ([forked from vlcj-player](https://github.com/caprica/vlcj-player)). 

**Subzero Player is not just a player, it's core functionality is to make learning a new language as easy and smooth as possible**.
Subzero Player has a few killer features:
 
  - **Dictionary feature**: Add subtitle and watch the video file as usual with subtitles (subtitle must be manually added, not built it). When paused, there will be pop-up windows on screen with the translation of each word provided (feature can be toggled on/off)
  - **Ample language choice**: Choose language pair from over 100 language pairs 
  - **Second Subtitle feature**: Instead of using Dictionary feature, you can add the second subtitle, which means you can have two subtitles running at the same time, e.g. one in english, second one in german.  Second subtitle can only be seen when paused, so that its not too distracting.  Second Subtitle feature and Dictionary feature cannot be both enabled at the same time.
  - **Streaming**: Stream video via URL, still able to use above features. Make sure your internet connection is fast enough to avoid lags. 

Generally the subzero-player tries to match the Qt interface of VLC with as many
of the same features implemented as possible.

However, it is not possible to get a 100% like-for-like implementation since
LibVLC, used by vlcj, exposes only a sub-set of the total functionality of VLC. 

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
 

Required Libraries 
-----------------------
 There were two small libraries developed for this application: *[dictionary agent](https://bitbucket.org/giorgimode/dictionary-agent)* and *[subtitle agent](https://bitbucket.org/giorgimode/subtitle-agent)*.
 **Dictionary Agent** is responsible for retrieving translations for a given set of words. Since there are multiple sources of dictionaries (e.g. MIT Wordnet binaries for English to English, simple text files and so on), there are different implementations of the interface, each one responsible for specific type of dictionary source. The interface can be extended and implemented for other kinds of sources in the future:

     public interface DictionaryService {
        Map<String, Map<String, List<String>>> retrieveDefinitions(String[] words);
    }
 **Subtitle Agent** is responsible for retrieving the list of words most recently or currently displayed on the screen. When user paused the player, current subtitle words displayed are retrieved via Subtitle Agent and passed to Dictionary Agent for translation. 

 Feel free to check out the source code, suggest improvements and pull requests. 

Status
------
This project is currently a work-in-progress. There are some more exciting things to come, most notably a web portal and user profiles.
User will be able to save a word by clicking on it in the media player subtitle translation window. Later user can log in their portal from browser and view list of words with translations and the context of the subtitle word was used.
Progress is tracked in Youtrack project management tool. Contact me at [sub0.project@gmail.com](sub0.project@gmail.com) to gain access
to project board.

Project Structure
----------
Subzero player folder expects the following structure

![dir-structure](https://s3.eu-central-1.amazonaws.com/subzero-player/screenshots/folder_structure.png "dir-structure")
  
   - *config.properties* will contain users credentials encrypted, remote URLs and SFTP locations (for authentication, downloading dictionary data, sending words to user profile etc).  
   - *lang* folder will contain directories for each language pair the user has downloaded, e.g. bg-de, de-en and so on. 
   - Subzero folder holds main application data: 
    - lib folder with VLC dll files and plugins
    - runner file, e.g. subzero.bat or subzero.sh
    - jar file with application and its dependency jars

How to run
---------------------------------
Check out source code and run `mvn clean install`
In target folder you will get subzero-($project.version)-jar-with-dependencies.jar
Recreate the folder structure as described in *Project Structure* part above. 
To make things easier, [download this archive](https://s3.eu-central-1.amazonaws.com/subzero-player/SubzeroPlayer.zip) with ready made project structure and replace existing jar with the one built by your maven. 
*lang* folder only contains en-en pair (english-english). Remote location to download other languages from media player's menu is not yet set up, but will be available soon. 


History
---------------------------------
Project was conceived when I moved to Germany in 2016 and struggled to force myself to learn the language (since my work environment was in English).
Watching movies in German and looking up each word was too time consuming, I was losing focus and interesting really fast. I did not find any fun/engaging way to learn a new language at home after work. So I decided to take things in my own hands and create a media player designed to the language learning process very smooth. 

License
-------

The subzero-player project is provided under the GPL, version 3 or later.

Screenshots
----------
Language pair menu: switch to a new pair, download language pair if not yet downloaded, or try to redownload if dictionary is corrupt or there is a better version available.
![language-pairs](https://s3.eu-central-1.amazonaws.com/subzero-player/screenshots/language-pairs.png "language-pairs")

Subtitle translation in action
![translation](https://s3.eu-central-1.amazonaws.com/subzero-player/screenshots/translations.png "translation")

![translation2](https://s3.eu-central-1.amazonaws.com/subzero-player/screenshots/translations2.png "translation2")

Two subtitles
![two-subs](https://s3.eu-central-1.amazonaws.com/subzero-player/screenshots/two-subs.png "two-subs")