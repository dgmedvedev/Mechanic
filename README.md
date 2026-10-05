# Mechanic

Android app for managing a vehicle fleet: cars, drivers, fuel consumption rates, and related normative documents.

## Author
Dmitry Medvedev

## Table of contents
* [Features](#features)
* [Architecture](#architecture)
* [Technologies](#technologies)
* [Requirements](#requirements)
* [Status](#status)
* [Contact information](#contact-information)

## Features
* **Cars** — search, add, edit, and delete vehicles (brand, model, year, plates, VIN, insurance, and related fields)
* **Drivers** — search, add, edit, and delete drivers (license and medical certificate validity)
* **Fuel consumption rates** — view and edit linear, summer, and winter rates per car
* **Normative documents** — download and view transport PDFs with `PdfRenderer`, including in-document text search with highlighted matches; files are cached on device, revalidated about once a day (ETag / Last-Modified), and opened offline when the network is unavailable. A download or update is confirmed first, including the file size
* **Expiry reminders** — optional notifications when a checkup, insurance, hull insurance, driving licence, or medical certificate is due in 30, 14, 7, or 1 day, or already expired. A daily background check posts the notification; tapping it opens the car or driver. On Android 13 and newer, turning reminders on asks for the notification permission
* **Backup** — export cars and drivers to a file, or restore them from one. Restore replaces current data and restarts the app
* **Theme** — system, light, or dark
* **Adaptive UI** — list-only on phones; from 600.dp width, a list-detail overlay with inline editing
* UI strings are localized in English and Russian

## Architecture
Clean Architecture with three layers:

* **presentation** — Jetpack Compose screens, ViewModels, Navigation Compose
* **domain** — models, repository interfaces, use cases, typed `Result` / `DomainError`
* **data** — Room database, DataStore preferences, WorkManager expiry checks, OkHttp document downloads, on-device PDF cache, PDFBox text search index, repository implementations

Presentation packages are organized by feature (`cars`, `drivers`, `docs`, `settings`). Hilt modules live in `app`.

## Technologies
* Kotlin
* Jetpack Compose & Material 3
* Clean Architecture
* MVVM
* Hilt
* Room
* DataStore
* WorkManager
* OkHttp
* PdfBox-Android
* Kotlinx Serialization
* Coroutines & Flow
* Navigation Compose
* Gradle Version Catalog
* KSP

## Requirements
* minSdk 24
* targetSdk / compileSdk 36
* JDK 17
* Android Studio Panda 4 (2025.3.4) or newer
* AGP 9.2.1, Gradle 9.7.0, Kotlin 2.3.21

## Status
Project is: _in progress_

## Contact information
* https://www.linkedin.com/in/dg-medvedev
* https://t.me/dgmedvedev
* dgmedvedev.it@gmail.com
