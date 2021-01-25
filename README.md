# imc-coding-challenge

Built With 🛠
-------
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - Kotlin's way of way of writing asynchronous, non-blocking code
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying data changes
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android
- [Koin](https://github.com/InsertKoinIO/com.egoriku.landing.koin) - Dependency Injection Framework


## Source Tree
```
└── dev
    └── arpan
        └── imc
            └── demo
                ├── background
                │   └── NotificationSchedulerWorker.kt
                ├── data
                │   ├── local
                │   │   └── db
                │   │       ├── AppDatabase.kt
                │   │       └── UsersDao.kt
                │   ├── model
                │   │   ├── CurrentLocation.kt
                │   │   ├── ResultWrapper.kt
                │   │   └── User.kt
                │   └── UserRepository.kt
                ├── DemoApplication.kt
                ├── di
                │   └── Koin.kt
                ├── location
                │   └── LocationRetriever.kt
                ├── prefs
                │   └── PreferenceStorage.kt
                ├── ui
                │   ├── home
                │   │   ├── HomeFragment.kt
                │   │   └── HomeViewModel.kt
                │   ├── login
                │   │   ├── LoginFragment.kt
                │   │   ├── LoginFrom.kt
                │   │   └── LoginViewModel.kt
                │   ├── main
                │   │   ├── MainActivity.kt
                │   │   └── MainViewModel.kt
                │   ├── Navigator.kt
                │   ├── profile
                │   │   ├── ProfileFragment.kt
                │   │   └── ProfileViewModel.kt
                │   └── signup
                │       ├── SignUpFragment.kt
                │       ├── SignUpFrom.kt
                │       └── SignUpViewModel.kt
                └── utils
                    ├── BindingAdapter.kt
                    ├── DateUtils.kt
                    ├── Event.kt
                    ├── Extensions.kt
                    ├── LiveFrom.kt
                    ├── Notify.kt
                    └── Validator.kt

```
## Challenge Brief:
-------
Create a simple working app with 4 pages as described :

 

1) Signup Page (First Name, Last Name, Email ID, Password (single field), Mobile Number)

Page to register a user with his First Name, Last Name, Email ID, Password (single field) & Mobile Number. All data to be stored on device Local Storage (using SQLite) for validation at login. Store data along with the user’s Latitude and Longitude afters showing appropriate location permission requests (Current Location).

              

2) Login Page (with email, password)

Page to accept users email and password input and validate the credentials to login the user from data stored in Local Storage.

              

3) Home Page

One single button on the home page ("Pick Time"). On clicking the button present the user with a time picker with default date as the current date. Save the user's selected time in Local Storage.

Form Validation to be done for this form to pick only future times of the day and not past time.

              
rin
4) Profile Page

Page to display the user’s registered data retrieved from local storage along with stored Latitude and Longitude.

              

Functionality :

A local push notification must be pushed on the device at the time picked by the user. The push notification should include a Title, Body and an Image of your choice. Clicking the push notification should open the app on the Profile Page and display the user's registration data (along with Latitude and Longitude) that is stored in Local Storage. Request permissions as necessary for Location Permissions and Notification Permissions as needed on Registration Page/Home Page.