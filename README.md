# Intent Builder

## Examples

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
  .build();
```

### Copy constructor

``` java
public Intent copyIntent(Intent original, String emailAddress) {
  return IntentBuilder.copy(original)
    .extra(EXTRA_EMAIL, emailAddress)
    .build();
```

# Install

## Gradle

``` xml
repositories {
    jcenter()
}

dependencies {
    testCompile "org.fmedlin:camper:0.1.0"
}
```

# TODO

- [ ] Add all extra types
- [ ] Improve  README
- [ ] Add choosers to builder and sample
- [ ] Add live template xml file
- [ ] Add intent reader class
- [ ] Add bundle builder class
