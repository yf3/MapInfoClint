## Intro
The App enables the user to build a point of interest (POI) according to the current location, and post the POI to the target RESTful API endpoint through HTTP request.

- Permission
- User Location
- Map API provider
  - [Mapbox SDK for Android](https://docs.mapbox.com/android/maps/guides/)
- Camera
- Network
  - [Retrofit](https://square.github.io/retrofit/)
- File Storage
- Architecture
- App Navigation

## Checkin
The user is able to build a POI through the checkin feature: 

If the camera permission is granted by the user, the user is able to take photo with the in-App camera. When a photo is taken, the location will be stored along with the photo in the form of an Exif description tag.

The Gallery section shows all photos stored in the App-specific internal storage. The user can choose the photo wanted to upload through the in-App gallery, and a POI info-editing dialog fragment will show up. Upload the POI once finish editing.