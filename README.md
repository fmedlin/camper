Intent Builder
==============
Some useful builders and utilities.

Install
-------

Install from gradle using jcenter.

``` xml
repositories {
    jcenter()
}

dependencies {
    compile "org.fmedlin:camper:0.1.0"
}
```

Examples
--------

### Start an explicit Activity

``` java
public class MainActivity {
  public void doit() {
    Intent intent = IntentBuilder.start(TargetActivity.class)
      .extra(EXTRA_ITEM, item)
      .extra(EXTRA_ID, 2L)
      .build();

    startActivity(this, intent);
  }
}
```

`IntentBuilder` can also start the activity

``` java
public class MainActivity {
  public void doit() {
    Intent intent = IntentBuilder.start(TargetActivity.class)
      .from(this)
      .extra(EXTRA_ITEM, item)
      .extra(EXTRA_ID, 2L)
      .execute();
  }
}
```
### Create implicit Intents

``` java
Intent intent = IntentBuilder.with(Intent.ACTION_VIEW, Uri.parse("http://google.com"))
  .build();
```

Or:

``` java
Intent intent = IntentBuilder.with(Intent.ACTION_VIEW)
  .data(Uri.parse("http://google.com"))
  .build();
```

Or:

``` java
Intent intent = IntentBuilder.with(Uri.parse("http://google.com"))
  .action(Intent.ACTION_VIEW)
  .build();
```

### Load up on extras

``` java
Intent intent = IntentBuilder.with(this)
  .action(ACTION_SEND)
  .type("text/plain")
  .extra(EXTRA_EMAIL, "joe@example.com")
  .extra(EXTRA_SUBJECT, "Please read")
  .extra(EXTRA_ID, 2L)
  .extra(EXTRA_PCT_COMPLETE, 3.141f)
  .extra(EXTRA_IS_EXTRA, true)
  .extra(EXTRA_STATUS, (Serializable) "Likes to read email")
  .category(Intent.CATEGORY_HOME, Intent.CATEGORY_BROWSABLE)
  .flags(Intent.FLAG_ACTIVITY_NO_ANIMATION, Intent.FLAG_ACTIVITY_NO_HISTORY)
  .fillIn(intent, fillInFlags)
  .build();
```

### Copy constructor

``` java
public Intent copyIntent(Intent original, String emailAddress) {
  return IntentBuilder.copy(original)
    .extra(EXTRA_EMAIL, emailAddress)
    .build();
```

### Choosers

Start a chooser on the intent if it resolves

``` java
IntentBuilder.with(Intent.ACTION_SEND)
  .chooser(getString(R.string.chooser_title))
  .execute(context, getPackageManager())
  .onResolveFailure(listener)
  .execute()
```

or just construct the chooser intent

``` java
Intent chooserIntent = IntentBuilder.with(Intent.ACTION_SEND)
  .chooser(getString(R.string.chooser_title))
  .build()

// I am responsible for resolving the activity.
startActivity(chooserIntent);
```

Todo
----

- [X] Add all extra types
- [X] Improve  README
- [X] Add bintray javadocs and sources
- [X] Add choosers to builder and sample
- [ ] Add travis.yml
- [ ] Add categories, flags, fillIns
- [ ] 0.2
- [ ] Add live template xml file
- [ ] Add intent reader class
- [ ] 0.3
- [ ] Add bundle builder class
- [ ] 0.4

Contributing
------------

* Clone the project
* Run unit tests with `./gradlew test`
* Make a pull request with your changes, including tests.

License
-------

```
Copyright 2016 by Fred Medlin.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
