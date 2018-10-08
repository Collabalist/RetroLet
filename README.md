# **RetroLet**
It is a simple library which simplify the **RetroFit2.0**
works with

 - GET
 - POST
 - GET with File upload
 - POST with File upload

## Implementation
In your **build.gradle**
    
````groovy
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
````
In your application's **build.gradle**
    
````groovy
    dependencies {
	        implementation 'com.github.Collabalist:RetroLet:1.1'
	}
````

## Usage
**Get**
    
````java
     RetroLet.get("Base_URL","END_POINT")
                        .addQuery("key","value")//optional
                        .addHeader("key","value")//optional
                        .addFile("key",new File("File"))//optional
                        .build()
                        .setTimeOut(connectionTimeOut,readTimeout,writeTimeout)//optional
                        .execute(new RequestListener() {
                            @Override
                            public void beforeExecuting(String url, Map formInfo, int TAG) {
                                
                            }

                            @Override
                            public void onResponse(String response, int TAG) {

                            }

                            @Override
                            public void onError(String error, String message, int TAG) {

                            }
                        });
````

**Post**
    
````java
     RetroLet.post("Base_URL","END_POINT")
                        .addQuery("key","value")//optional
                        .addHeader("key","value")//optional
                        .addFile("key",new File("File"))//optional
                        .build()
                        .setTimeOut(connectionTimeOut,readTimeout,writeTimeout)//optional
                        .execute(new RequestListener() {
                            @Override
                            public void beforeExecuting(String url, Map formInfo, int TAG) {
                                
                            }

                            @Override
                            public void onResponse(String response, int TAG) {

                            }

                            @Override
                            public void onError(String error, String message, int TAG) {

                            }
                        });
````
