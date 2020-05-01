# DM-o-Tron

## Integration Test Status
[![Sauce Test Status](https://saucelabs.com/browser-matrix/breakqa.svg?auth=5a97f1cc1329af69931e085d95ae6250)](https://saucelabs.com/u/breakqa)

## Installation
Follow the setup guide in [Polymer Android Confluence](https://tangogroup.jira.com/wiki/spaces/MOB/pages/196476936/Android) as Android Studio and graphQL will both need to be set up

## Project Flavors
This project contains several flavors. Main development is done on the `Gloo` flavor, but there are many whitelabel variants.

## Android Development Tools and Libraries
A quick guide to some of the tools and libraries used in this project and what they are used for:
* SoundCloud - Cropping Images that are going to be uploaded from the Device to Gloo/Cloudinary to specific Aspect Ratios
* Retrofit - HTTP request library
* Glide - Loading and handling images
* Timber - Preferred logger over Androids default Log.
* Butterknife - Dependency injection libraries for binding fields to xml object
* Greenrobot - EventBus library, use only as a last solution for passing information around the app.
* Zendesk - SDK for implementing a Help Desk support section on the app
* Parceler - Utility class to easily create parcelable objects
* Facebook - Library to connect to Facebook. Used mainly to login to app as a facebook user
* Dagger - Dependecy injection libraries for binding fields to objects, creating a single instance for whole app.
* [SVG to VectorDrawable](http://inloop.github.io/svg2android/) - Render .svg files on multiple densities without binary images
* Transifex - The directory `.tx/` contains a Transifex repo that can be `tx push` and `tx pull`ed to automatically update translations
* Room - Google supported ORM for data persistence
* RXJava - a library for composing asynchronous and event-based programs using observable sequences for the Java VM

## Reusable App Components
### Gloo Cards
GlooCards are the base class for any and all cards that will be used within the app. GlooCards have functionality for loading, binding, and populating different types of card layouts. This includes components such as banners, avatars, text fields, and menus. Any new cards shall extend this class for easy usage in future production. Class Name for base Class is `GlooCard`

#### Types of Cards

Card | Description | Class Name
--- | --- | ---
ContentCard | Displays content. <br>Program, Collection, Etc.  | ProgramContentCard<br>CollectionContentCard
ChampionCard | Displays champion | ChampionCard
LandingPageCard | Displays landing page. | LandingPageCard

#### Types of layouts

Layout | Description | XML Name
--- | --- | ---
Primary | Main display card for content | view_content_card_primary
Horizontal | Main display for content in horizontal lists | view_content_card_secondary
Suggest | Display for content suggested cards | view_content_card_suggested
Share | Display for sharing content | view_content_card_share
Comment | Display for user comments | view_content_card_comment
Champion | Display for champions | view_champion_card
LandingPage| Display for landing pages. | view_landing_page_card

## OpenSans Font
Components created to use custom font. This font is used widely throughout the app so there are components created for easy implementation, also some styles were created to implement size, color, and font style.

#### Types of OpenSans Components

Component Type | Description | Class Name
--- | --- | ---
TextView | Displays font extended from TextView | OpenSansTextView
EditText | Displays font extended from EditText | OpenSansEditText
TabLayout | Displays font extended from TabLayout | OpenSansTabLayout
Button | Displays font extended from Button | OpenSansButton

Types of OpenSans styles - Regular, Italic, Bold, Italic-Bold
You may change the type with the field `app:fontStyleOpenSans` in xml

There are also many reusable text styles in the `styles_sans_text` styles resource xml

## Usage
### High Level App Structure
* [Crashlytics](https://fabric.io/home) is used for crash reporting. Another Android dev can add you to the account.
* The main app is currently translated into many other languages; you MUST update translations before doing a milestone or release build (the build will fail if translations are missing).
* White labels are specified in the app's `build.gradle` in the project root
* Debug and milestone builds point at staging, release builds point at production. You can change this on-the-fly in debug builds by tapping "Change Server" on the login screen. A dialog will appear that lets you point to a different server and saves it in shared prefs
* The application is 99% portrait-only - only video activities are in landscape.
* The app has an event bus, but only use it when absolutely necessary. EventComponent is the preferred way of pubsubbing views.
* The app uses branch.io for push notifications, and SendGrid for email deep links. The Router class handles incoming links from each.
* [GSON](https://github.com/google/gson) is used to de/serialize objects, and [Parceler](https://github.com/johncarl81/parceler) is used to generate Parcelable implementations for those objects
* Every request fires off a loading start/stop event pair which we use to show/hide the loading indicator. These events can be silenced (ex: for analytics calls that don't matter to the user) using `LoadingEventPoster.LoadingEventType`
* Everything is native except for some special `Program`s or `LandingPages` which use `WebViews`
* `RoutingActivity` and `Router` are used to direct the user to the correct screen in the app if we don't explicitly know it
* We go through Cloudinary for pretty much everything; use the `ImageLoader` class; utilize Cloudinary's ability to send correct image sizes (ie. don't load wallpapers into thumbnails)
* [Glide](https://github.com/bumptech/glide) is used for displaying images
* Intent extra keys and activity request codes are declared in `Constants`, while fragment tags are declared in the individual fragment classes
* New features are usually put behind a feature flag; these are added via the admin console and checked using the `FeatureFlags` class
* `EndpointConstants` contains the relevant API values; try to keep them defined under annotations
* Unit tests are contained in `app/src/test/java/com/gloo/polymerandroid/`, and can be run locally by switching the build configuration.

## Contributing
1. Clone it!
2. Create your feature branch: `git checkout -b feature/my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin feature/my-new-feature`
5. Submit a pull request :D
