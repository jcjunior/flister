# README #

This is an Android App that has integration with The Movie Database API (https://www.themoviedb.org) and provide a list with several movies. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

1. Android SDK (Version 24 for compile)
2. Android Studio 2.2.3 (With Build Tools 24)

```
https://developer.android.com/studio/index.html
```

### Installing

Access your project folder

```
cd path_to_directory
```

Execute Gradle Wrapper in order to download all dependencies and build the project

```
gradlew build
```

Deploy the .apk into the device

```
gradlew installDebug
```

Search for the app into your device app list and run it manually (Gradle don't start it automatically)


## Built With

* **[Android SDK]** - Android Software Development Kit
https://developer.android.com/studio/index.html

* **[Android Annotations]** - Framework to speed up Android development
https://github.com/androidannotations/androidannotations/wiki

* **[Firebase Auth]** - Backend services for authentication provided by Google.
https://firebase.google.com/docs/auth/

* **[Retrofit 2]** - Type-safe HTTP client for Android
http://square.github.io/retrofit/

* **[Android Support API]** - Offers backward-compatible for features that are not built into the framework.
https://developer.android.com/topic/libraries/support-library/index.html)

* **[Bottom Bar]** - A third-party framework that mimics the new Material Design Bottom Bar Navigation Pattern
https://github.com/roughike/BottomBar

* **[ORMLite]** - A lightweigth ORM for java
http://ormlite.com/

* **[Glide]** - An image loading and caching library for Android focused on smooth scrolling
https://github.com/bumptech/glide


## Authors

* **José Carlos Junior** - *junior.jc@outlook.com*