# AttendanceTracker
An attendance system for WRDSB designed to track attendance for clubs and events.

Ideas:
* Store data in Firebase
  * Index by student number (1 student number is linked to many clubs/events since many students do more than 1 club/event)
* Code program in Java using Swing (uses AWT in its backend)
* Make it user-friendly - easy design for user flow
* Package it as an exe/msi
* Create Python script allowing for the data in the Firebase to be analyzed. Creates a CSV with first column student numbers and then codes/names of clubs/events they have qualified for - will need value of credit too. Most likely 3-4 letter codes e.g. Computer Science Club can be CSC.
* Work with SJAM to implement the system
* Follow similar model as used in science club and computer science club
* Maximize speed of scanning students in - Firebase allows for more than 1 scanner running at once!
* Test with student numbers - don't need to worry about duplicate names anymore
* Work with teachers to create script mapping student numbers to names - confidential data we can work with the spreadsheet data we currently have - similar format for the huge spreadsheet
* Maybe remove dependence on hardware (barcode scanners)
* Create Android application  - NOTE: this is a "wishlist" item

Timeline:
* July 16 - Barcode Scanner input working, basic GUI working
* July 23 - some kind communication to Firebase working
* July 30 - Integration with Firebase complete 
* August 6 - Nice GUI interface coded - PC application fully working - Testing phase
* August 13 - Android application under development 
* August 20 - Android app can communicate to firebase
* August 27 - Android app has nice, clean UI 
