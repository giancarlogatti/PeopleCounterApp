People Counter: A write-up

One of the first decisions was regarding how I would reference widgets in the layout in my activity. I wouldn't choose findViewById because it's not elegant as you will need to call findViewById for each widget you want to reference. Also the view hierarchy is walked each time you call findViewById, compared to other solutions that only need a single pass. Kotlin Synthetics is another option, but because of the lack of null safety due to the ID names being in a global namespace, this method is more prone to error. So it was between Data Binding and View Binding. I chose View Binding because the build times for Data Binding aren't as good as there is more work involved in processing the binding classes due to the layout expressions, 
but I still feel it's a very valid solution if you wanted to make use of that feature. I chose View Binding because it is very simple and performant and I personally prefer to just do the binding of the data in the Activity/Fragment as you can still make use of Kotlin delegates to achieve a reactive style that is very readable; but you could also expose a variable to the Data Binding layout and approach the binding of 
data to widgets in a more declarative way. 

Another decision was what to use to store the counter data. I chose to use Shared Preferences because I'm just storing integers, and it's very simple to read and write data
to shared preferences. SQL would be more suited for relational data that has more data fields
as this solution is optimized for retrieving data through indexing columns. To use Room or another ORM would require a lot of boilerplate code, and this would be overkill for very primitive data. Saving to external storage or internal storage also requires boiler plate and error-handling code; external storage 
would also make the data public to other applications which wasn't part of the spec. ViewModel + SaveStateHandle or restoring state bundle in onCreate/onRestoreInstanceState would work well for restoring state after rotation and surviving process death but would
not help with retaining data if user force quits. I thought about passing shared preferences to the ViewModel and then writing to shared preferences once onCleared is called, but 
there is no flow control when a user closes an app, for example, which is why I write to shared preferences every time the value changes.  

Another decision was to use the Kotlin Delegate - observable. The decision to use this was because we had two pieces of data that were affected by pressing either the + or - button and when those
2 different data changed, multiple things needed to happen such as writing it to the preferences, changing text color, etc. While I could've just added the additional logic to the button's click listener function, 
I felt that would've made the code less readable, and could cause code duplication as multiple areas can change variables potentially and you need code to handle that at every change point. While you could 
abstract that to a function, it becomes more repetitive as you there are more areas the variable can change. With Kotlin, there's a more idiomatic approach  where you can just apply the changes in one
area, and react to those changes in another through a specified function callback, with the flexibility of having the old value as well. 

I also decided to write some UI tests. I used Espresso as it's a very robust API for doing this. I didn't Unit Test because this app is essentially just an activity. I feel I tested every single scenario, including text color changes, and after rotation. 

