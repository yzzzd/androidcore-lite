# androidcore-lite
AndroidCore make you get the most rapid development.

[![Release](https://jitpack.io/v/yzzzd/androidcore-lite.svg)](https://jitpack.io/#yzzzd/androidcore-lite)

## Download
Add it to your build.gradle with:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
and:

```gradle
dependencies {
    implementation 'com.nuryazid:androidcore-lite:{latest version}'
}
```

## Usage

### 1. Extend CoreActivity
To get DataBinding and ViewModel implementation
```kotlin
class HomeActivity : CoreActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {
  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // auto generate binding and viewModel variable
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
  }
}
```

### 2. Extend BasicActivity
To get DataBinding without ViewModel implementation
```kotlin
class HomeActivity : BasicActivity<ActivityHomeBinding>(R.layout.activity_home) {
  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // auto generate binding variable
        binding.lifecycleOwner = this
  }
}
```

## License
Licensed under the Apache License, Version 2.0,
