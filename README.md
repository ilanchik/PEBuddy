* Login Fragment *
1. Login working with both online and offline capabilities
2. If offline, must sign up first, which will then redirect you to dashboard
3. Then you may log in later using login screen as data is saved locally
4. ***Do not use real passwords***
    I have not set up encryption yet for passwords and sensitive data
5. If using internet connection:
    When you close the app and re-open it, it checks to see if you are still logged in
    and will bypass login and direct you to dashboard
6. You may use any email (or a fake one). Just make sure to add @ and .com, .net, etc..

*Workout Fragment*
1. Stop button will completely stop workout, without recording any data
    This will need to be fixed and I would like to implement a pause button
2. You may start workout again from the beginning by pressing start
3. I have set up all the functionality, so that I can easily manipulate workouts for
    each level. The current workouts you see will change.


*** NOTES ***
1. Heart rate monitor not set up. There was a lot of code that I still needed to
    understand.
2. Level will be selected upon sign up for now, until heart rate monitor is set up
3. Messages fragment designed, but not functional yet
4. I have set the time limit for the workouts to be 30 seconds right now, for testing
    purposes, so that you do not have to wait the full 25 min before testing the
    results fragment
5. Settings fragment is functional, but only for testing purposes. It will allow you to switch
    between Beginner, Intermediate, and Advanced workouts for current session only. If you logout
    and log back in, it will default back to the level which you signed up with.
6. Rotation

********************************* 04/30/2022 ***************************************
sending new apk today 05/02/2022

*** FIXES ***
1. Fixed to include back button navigation (addToBackStack())
    The back button will now allow you to move to the previous accessed fragments.
    I also made sure that you cannot navigate to login using the back button (it will just exit the app), so that the user must log out.
2. I have also corrected the loading wheel so that it disappears if offline login incorrect.
3. When the user creates an offline account, then tries to login with internet access, the login would fail because the account was not created online.
   I have quickly fixed this issue by checking the offline database after failed login, which will now allow you to login.
4. Fixed the issue where app crashes upon clicking start for workout.
    This was due to android version 12 (API 31-32), there was a line of code that android didn't like.

*** IMPROVEMENTS ***
1. Worked on tightening the UI design, so views are not spread too far apart.
2. Updated the settings fragment to include name, username, and email change.
    This is now fully functional, and will allow user to change their personal data (minus password, which I will implement next round)
3. On workout fragment, user may now click on the specific workout upon completion, which will grey
    out the CardView.

*** STILL FIXING/NEEDS IMPLEMENTATION ***
1. Heart rate monitor
2. Messages/Notifications fragment still needs functionality
3. Rotation
4. Still trying to resolve Griffin's issue regarding false network connectivity
5. Still need to implement a way for user local data to update online database
    (Broadcast receiver? Check every so often for internet, then update)
6. When user clicks on a workout as complete (greyed out), should record that specific workout.
    Or, if any incomplete, will store those incomplete workouts.
7. Will change to actual workout time for next round.

********************************* 05/04/2022 ***************************************
Clicking on profile photo will allow you to change photo
When clicking stop/cancel in middle of workout, will record workout as incomplete.
Send message is now functional. Will store in JSON file and online database. This will be further developed later.
Fixed backstack, when user is directed to workout fragment, the back button will exit the app, so the user does not have issues with overlapping views
(popbackstack)
Added alert dialogue to warn/ask if user wants to log out.

*check if fields missing in signup*

Added functionality to notification. For now, if user clicks on the missing workout, will redirect you
to complete workout.
    Still need a way to remove the workout
    Need to sync with calendar to make sure it is correct week

    Need a way to navigate back to original workout if user selected another tab