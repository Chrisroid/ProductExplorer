# Product Explorer App

**Product Explorer** is an Android application designed to explore a list of products and view their details. The app uses a clean architecture approach, incorporating the MVVM (Model-View-ViewModel) pattern with **Kotlin**, **Coroutines**, **Android Views(XML)**, and **Unit Testing**.

---

## Screenshots

### List Screen
![List Screen](https://github.com/user-attachments/assets/30b6cefb-0458-4523-a787-bebe6024971c)

- The main screen of the app, displays a list of products.

### Details Screen
![Details Screen](https://github.com/user-attachments/assets/4c263b8e-548e-4497-9d9e-f00985642b6d)

- Displays detailed information about a selected product.

---

## Features

- **Product List Screen**: A screen that displays a list of products retrieved from the repository.
- **Product Details Screen**: Upon selecting a product from the list, the user is navigated to a screen with detailed information about the selected product.
- **Unit Testing**: Tests to ensure that the ViewModel and repository layers function as expected.
- **Coroutines for asynchronous tasks**: Used to fetch product data non-blocking.
- **Offline First**: The App is offline first. The products are fetched and inserted into a local database first, so whenever the user opens the app, if there is no internet, it shows the previous list and their details. The same scenario plays out if there is internet but fetches no new data.

---

## TradeOffs
- **Design Tradeoff**: Apart from Design Tradeoffs, no noticeable tradeoffs were made. If a more Intuitive UI was provided, it should have be more pleasing to the eye
---
## Architecture

- **MVVM (Model-View-ViewModel)**: This architecture ensures that the UI is separated from the business logic and makes the app more scalable and testable.
- **Repository Pattern**: Data is abstracted in a repository, making the data layer decoupled from the ViewModel.
- **XML**: Used for building the UI in a views way.
- **Mockk for Unit Testing**: Mocking external dependencies during testing.

---

## Technologies

- **Kotlin**: Programming language for Android development.
- **Hilt**: Used for Dependency Injection
- **Coroutines**: To handle background tasks asynchronously.
- **Room**: Local database for storing product data.
- **Retrofit**: For making network calls.
- **Mockk**: Mocking library for testing.
- **JUnit**: Unit testing framework.

---

## Setup and Installation

To get started with the app:

### Prerequisites
Ensure you have the following installed:

- Android Studio
- Kotlin
- Android SDK

### Steps

1. **Clone the repository:**

   git clone https://github.com/Chrisroid/ProductExplorer.git
   cd product-explorer
   
2. **Open the project in Android Studio.**
   - Open Android Studio and select **Open an existing project**. Navigate to the project directory and open it.

3. **Sync the project with Gradle.**
   - Make sure all dependencies are downloaded by syncing the project with Gradle.

4. **Run the app.**
   - Connect an Android device or start an emulator, and then run the app using the **Run** button in Android Studio.

---

## Running Unit Tests

To run unit tests and ensure everything is working:

### Run Unit Tests

./gradlew testDebugUnitTest

### Run UI Tests

If you have UI tests, use the following command:

./gradlew connectedDebugAndroidTest


## Known Issues

No Known Issues
All Tests Passed


---

## Acknowledgements

- Thanks to the recruiter mikro.africa





  
