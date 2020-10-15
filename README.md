People Counter: A write-up

One of the first decisions I made was the deciding between the various methods in which you can interact with the various widgets/layouts in the inflated layout. I wouldn't choose findViewById because I don't find it a very elegant as you need to call it for each widget/layout, this can lead to some messy code where you have a wall of findViewById calls. Also the view hierarchy is walked each time you call findViewById, compared to Data Binding and View Binding which only needs a single pass. Kotlin Synthetics is another option, but I feel it's generally not a good practice because of the lack of null safety, it's fairly easy to accidentally reference a widget in another layout file because everything is in a global namespace, this can cause run-time errors and confusion. These two previous options I never really considered, so it was between Data Binding and View Binding. I chose View Binding because the build times for Data Binding are not as good because there is more work involved in processing the binding classes due to the layout expressions, 
but I still feel it's a very valid solution if you wanted to make use of that feature. I chose View Binding because it is very simple and performant and I personally prefer to just do the binding of the data in the Activity/Fragment as I find it slightly more readable and you can still make use of Kotlin delegates; but you could also expose a variable to the Data Binding layout and approach the binding of 
data to widgets in a more declarative way. 

Another decision was what mechanism I wanted to use to store the counter data. I chose to use Shared Preferences because the data were just integers, and it's very simple to read and write data
to shared preferences for easy access. The type of data matters a lot when deciding to use different storage solutions. SQL would be more suited for relational data that has more data fields
as this solution is optimized for retrieving data through indexing columns. To use Room or another ORM would require a lot of boilerplate code, such as setting up the database and 
creating entities; this would be overkill for very primitive data. Saving to external storage or internal storage also requires a lot of boiler plate and error-handling code, external storage 
would also make the data public to other applications which wasn't part of the spec. ViewModel + SaveStateHandle or restoring state bundle would work well for restoring state after rotation or process death but would
not work for retaining it between app restarts. I thought about passing shared preferences to the ViewModel and then writing to shared preferences once onCleared callback in ViewModel is called, but 
there is no flow control when an app is forcibly killed for example, so I realized this wouldn't work and that that one needs to plan around a known state which is why I wrote to shared preferences after
every value change in the activity. 

Another decision was to use the Kotlin Delegate - observable. The decision to use this was because we had two pieces of data that were affected by pressing either the + or - button and when those
2 different data changed, multiple things needed to happen such as writing it to the preferences, changing text color, etc. While I could've just added additional logic to the button's click listener function, 
I felt that would've made the code less readable, and code duplication is a concern as two different buttons could affect a variable and they both need the logic to handle it, and while you could 
abstract this to a function; it becomes more repetitious as you have more areas this variable can change. With Kotlin, there's a more idiomatic approach  where you can just apply the changes in one
area, and react to those changes in another through a specified function callback. 

I also decided to write some UI tests. I used Espresso as it's a very robust API for doing this. I didn't use Unit Test because this app is essentially just an activity. I feel I tested every single scenario,
including text color changes, and after rotation. 

