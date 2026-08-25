# Mechanic

Android app for managing a vehicle fleet: cars, drivers, fuel consumption rates, and related normative documents.

## Author & Contributor List
Dmitry Medvedev

## Table of contents
* [Architecture](#architecture)
* [Technologies](#technologies)
* [Requirements](#requirements)
* [Status](#status)
* [Contact information](#contact-information)

## Architecture
Clean Architecture with three layers:

* **presentation** — Jetpack Compose screens, ViewModels, Navigation Compose
* **domain** — models, repository interfaces, use cases
* **data** — Room database, `LocalDataSource`, repository implementations

Packages are organized by feature (`cars`, `drivers`, `docs`) rather than by UI type.

## Technologies
* Kotlin
* Jetpack Compose & Material 3
* Clean Architecture
* MVVM
* Hilt
* Room
* Coroutines & Flow
* Navigation Compose
* Gradle Version Catalog

## Requirements
* minSdk 24
* targetSdk / compileSdk 36
* JDK 17
* Android Studio with AGP 9.2.1 and Gradle 9.7.0

## Status
Project is: _in progress_

## Contact information
* https://www.linkedin.com/in/dg-medvedev
* https://t.me/dgmedvedev
* dgmedvedev.it@gmail.com
