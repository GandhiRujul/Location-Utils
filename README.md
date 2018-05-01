# Location-Utils


## Installation

Add repository url and dependency in application module gradle file:

	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
  
  	dependencies {
	     implementation 'com.github.GandhiRujul:Location-Utils:1.0.0'
	}
### Required 
 
 1) Need google play service location dependancy
   
```implementation 'com.google.android.gms:play-services-location:X.X.X' ``` 

 2) Need google-service.json file on your app level 

### Usage

Create Object of FusedLocationUtils as per your requirement 

```java
            locationUtils = FusedLocationUtils.getInstance(this);
            
            // set property as per requirement
            locationUtils.setInterval(1000);
            locationUtils.setRepeativeUpdate(true);
            
            // set listner where you want the result            
            locationUtils.setCurrentLocationListener(this);
            
            // start tracking location 
            locationUtils.createLocationRequest();
```
If you want get location continuesly then you have to set it as **setRepeativeUpdate(true)** , **setRepeativeUpdate** method is not compulsory.
 
**By Default setRepeativeUpdate gives single time location update.**

If you want set interval for getting location on perodical bases then you have to set it as **setInterval(1000)** ,, **setInterval** method is not compulsory.

**By Default setInterval gives location on 2 seconds.**

then you have to implement by *CurrentLocationListener* it will provide you three methods

```java
     @Override
        public void onLocationUpdate(Location location) {
           
        }
    
        @Override
        public void onNoLocationFound() {
           
        }
    
        @Override
        public void onRequestPermission() {
           
        }
```

### Developed by
[Rujul Gandhi](https://www.github.com/GandhiRujul)

