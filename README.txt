This app requires mp3 files to test which we have provided 3 in the music folder. To put them on an emulator simply drag and drop them. If they do not automatically install then make sure the SD card is initialized by draging down the toolbar and clicking the notification that mentions setting up the virtual SD card. The app searches the entire filesystem for mp3 files so if you have your own to test they will work as well. The mp3 files can be transfered onto a physical device through a cable as well.

To use the app you need two phones to properly test the syncing (1 phone can work for basic music player functionality):
Phone A
Phone B

A: Launch app and click host
B: Launch app and click client
B: Enter in IP address shown on Phone A
A: Add music to the music queue
A: Press the right arrow at the top right of the screen
A: Press play/pause/previous/next/sync

Ideally it should play the same music from both devices in near perfect synchronization but if not 

Known Bugs: 
	-Sometimes out of sync between clients and host
Areas for feedback: 
	-Additional improvements to layout/flow/functionality that would be helpful

Previous feedback
1. shuffle button/reorder/remove
	-Intend on adding but have not yet.
2. host/client more instuctions on how to use
	-Added text views to hopefully help instruct users
3. seeking (stretch goal)
	-Might add functionality to seek to certain time in a song if we have enough time
4. cleaner queue dividers
	-Added horizontal dividers
5. back stack gets large 
	- Music player and queue share the same activity but use two different fragments now
6. let client know about incorrect ip address
	-IP address format checking in place now but have not added check to see if it is a valid host IP address yet 
7. client see music queue
	-Hard to implement as it would require sending the entire queue over a web server, might add but most likely will not
8. Integrate with spotify, pandora, youtube etc...
	-Too much work and possibly infeasible due to the method we implemented synching up the phones as it requires the full song to be loaded from an mp3
	 and those services stream the music
