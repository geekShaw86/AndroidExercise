# android-exercise
This is an Android demo application using a technology stack of Kotlin + Coroutine + Hilt + MVVM.


# Presentation
This application is quite simple, it has only one page which will display an ISO-3 pin block after user input a pin


# Preview
![Display result](https://github.com/geekShaw86/AndroidExercise/blob/master/screenshots/screenshot1.png)

![Invalid pin number prompt](https://github.com/geekShaw86/AndroidExercise/blob/master/screenshots/screenshot2.png)


## Running Environment
This demo's minSdkVersion is 23, it means that devices on a version lower than Android 6.0 won't be able to run this application.


## Running tests
In your Android Studio's terminal, enter the root directory of the project and type: `./gradlew test`


## Architecture Components
This application involves concepts as below:
* Kotlin
* Coroutine
* Hilt
* ViewModel
* LiveData
* Unit Test


## Libraries
* [AndroidX](https://developer.android.com/jetpack/androidx)
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/)
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - for dependency injection
* [Coroutine](https://developer.android.com/kotlin/coroutines) - for asynchronous or non-blocking programming
* [MockK](https://mockk.io/) - for mocking objects in unit tests
* [Timber](https://github.com/JakeWharton/timber) - for logging
* [ktlint](https://ktlint.github.io/) - an anti-bikeshedding Kotlin linter with built-in formatter
