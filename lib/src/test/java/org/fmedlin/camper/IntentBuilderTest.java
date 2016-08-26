package org.fmedlin.camper;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.fmedlin.intentbuilder.BuildConfig;

import org.fmedlin.camper.IntentBuilder.ExplicitIntentBuilder;
import org.fmedlin.camper.IntentBuilder.ImplicitIntentBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.res.builder.RobolectricPackageManager;
import org.robolectric.shadows.ShadowApplication;

import java.io.Serializable;
import java.util.Arrays;

import static org.assertj.android.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 23, constants = BuildConfig.class, manifest=Config.NONE)
public class IntentBuilderTest {

    TestActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(TestActivity.class).create().get();
    }

    @Test
    public void testStartActivityWithExplicitIntent() {
        Intent intent = IntentBuilder.start(TargetActivity.class)
                .from(RuntimeEnvironment.application)
                .assertType(ExplicitIntentBuilder.class)
                .execute();

        assertThat(ShadowApplication.getInstance().getNextStartedActivity()).isEqualTo(intent);
    }

    @Test
    public void testExtraSetters() {
        CharSequence cs = "charsequence";
        CharSequence[] csa = new CharSequence[] { "cs1", "cs2", "cs3"};


        Intent intent = IntentBuilder.with(RuntimeEnvironment.application)
                .action("intent action")
                .type("text/plain")
                .extra("byte extra", (byte) 0x21)
                .extra("byte array extra", new byte[] {(byte) 0xaa, (byte) 0xbb})
                .extra("char extra", 'c')
                .extra("char array extra", new char[] {'a', 'b', 'c'})
                .extra("string extra", "string")
                .extra("string array extra", new String[] {"first", "second", "third"})
                .extra("cs extra", cs)
                .extra("cs array extra", csa)
                .extra("short extra", (short) 8)
                .extra("short array extra", new short[] {82, 83, 84})
                .extra("int extra", 1)
                .extra("int array extra", new int[] {42, 43, 44})
                .extra("long extra", 2L)
                .extra("long array extra", new long[] {2L, 3L})
                .extra("float extra", 3f)
                .extra("float array extra", new float[] {4f, 5f, 6f})
                .extra("double extra", 0.5)
                .extra("double array extra", new double[] {0.1, 0.2, 0.3})
                .extra("boolean extra", true)
                .extra("boolean array extra", new boolean[] {true, true, false, true})
                .extra("serializable extra", (Serializable) "serialized")
                .extra("parcelable extra", Uri.parse("http://google.com"))
                .extra("parcelable array extra", new Parcelable[]{ Uri.parse("http://google.com"), Uri.parse("http://apple.com")})
                .extraListOfString("string list extra", Arrays.asList("first", "second", "third"))
                .extraListOfCharSequence("cs list extra", Arrays.asList(csa))
                .extraListOfInteger("integer list extra", Arrays.asList(51, 52, 53))
                .extraListOfParcelable("parcelable list extra", Arrays.asList((Parcelable) Uri.parse("http://google.com"), Uri.parse("http://apple.com")))
                .build();

        assertThat(intent).hasAction("intent action")
                .hasType("text/plain")
                .hasExtra("byte extra", (byte) 0x21)
                .hasExtra("byte array extra", new byte[] {(byte) 0xaa, (byte) 0xbb})
                .hasExtra("char extra", 'c')
                .hasExtra("char array extra", new char[] {'a', 'b', 'c'})
                .hasExtra("string extra", "string")
                .hasExtra("string array extra", new String[] {"first", "second", "third"})
                .hasExtra("cs extra", "charsequence")
                .hasExtra("cs array extra", new CharSequence[] { "cs1", "cs2", "cs3"})
                .hasExtra("short extra", (short) 8)
                .hasExtra("short array extra", new short[] {82, 83, 84})
                .hasExtra("int extra", 1)
                .hasExtra("int array extra", new int[] {42,43, 44})
                .hasExtra("long extra", 2L)
                .hasExtra("long array extra", new long[] {2L, 3L})
                .hasExtra("float extra", 3f)
                .hasExtra("float array extra", new float[] {4f, 5f, 6f})
                .hasExtra("double extra", 0.5)
                .hasExtra("double array extra", new double[] {0.1, 0.2, 0.3})
                .hasExtra("boolean extra", true)
                .hasExtra("boolean array extra", new boolean[] {true, true, false, true})
                .hasExtra("serializable extra", "serialized")
                .hasExtra("parcelable extra", Uri.parse("http://google.com"))
                .hasExtra("parcelable array extra", new Parcelable[]{ Uri.parse("http://google.com"), Uri.parse("http://apple.com")})
                .hasExtra("string list extra", Arrays.asList("first", "second", "third"))
                .hasExtra("cs list extra", Arrays.asList(csa))
                .hasExtra("integer list extra", Arrays.asList(51, 52, 53))
                .hasExtra("parcelable list extra", Arrays.asList(Uri.parse("http://google.com"), Uri.parse("http://apple.com")));
    }

    @Test
    public void testBundleExtra() {
        Bundle b = new Bundle();
        b.putString("string extra", "bundle value");

        Intent intent = IntentBuilder.with(RuntimeEnvironment.application)
                .action("intent action")
                .extra("bundle extra", b)
                .build();

        assertThat(intent).hasAction("intent action")
                .hasExtra("bundle extra", b);
    }

    @Test
    public void testExtras() {
        Intent extraIntent = IntentBuilder.with(RuntimeEnvironment.application)
                .action("intent action")
                .extra("first", 1)
                .extra("second", 2)
                .build();

        Bundle extraBundle = new Bundle();
        extraBundle.putInt("one", 1);
        extraBundle.putInt("two", 2);

        Intent intent = IntentBuilder.with(RuntimeEnvironment.application)
                .action("intent action")
                .extras(extraIntent)
                .extras(extraBundle)
                .build();

        assertThat(intent).hasAction("intent action")
                .hasExtra("first", 1)
                .hasExtra("second", 2)
                .hasExtra("one", 1)
                .hasExtra("two", 2);
    }

    @Test
    public void testWhenDataShouldOverrideExistingType() {
        // Type is applied after data, so it will not be lost. Order doesn't matter.
        Intent intent = IntentBuilder.with(Uri.parse("http://theworld.org"))
                .action(Intent.ACTION_VIEW)
                .type("text/plain")
                .data(Uri.parse("http://google.com"))
                .build();
        assertThat(intent).hasType("text/plain");

        // DataAndType trumps individual data and type setters. Order doesn't matter.
        intent = IntentBuilder.with(Uri.parse("http://theworld.org"))
                .action(Intent.ACTION_VIEW)
                .type("text/plain")
                .dataAndType(Uri.parse("http://google.com"), "audio/*")
                .build();
        assertThat(intent).hasType("audio/*");
    }

    @Test
    public void testImplicitIntent() {
        Intent intent = IntentBuilder.with(Intent.ACTION_VIEW, Uri.parse("http://google.com"))
                .extra("string extra", "string")
                .assertType(ImplicitIntentBuilder.class)
                .build();

        RuntimeEnvironment.application.startActivity(intent);
        assertThat(ShadowApplication.getInstance().getNextStartedActivity()).isEqualTo(intent);
    }

    @Test
    public void testImplicitActionIntent() {
        Intent intent = IntentBuilder.with(Intent.ACTION_VIEW)
                .extra("string extra", "string")
                .assertType(ImplicitIntentBuilder.class)
                .build();

        RuntimeEnvironment.application.startActivity(intent);
        assertThat(ShadowApplication.getInstance().getNextStartedActivity()).isEqualTo(intent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImplicitIntentRequiresAction() {
        IntentBuilder.with(Uri.parse("http://google.com")).build();
    }

    @Test
    public void testCopyConstructor() {
        Intent original = new Intent();
        Intent copy = IntentBuilder.copy(original).build();
        assertThat(original).isNotEqualTo(copy);
    }

    @Ignore
    public void testChooserExecution() {
        setupResolver(Intent.ACTION_SEND);
        Application app = RuntimeEnvironment.application;

        IntentBuilder.with(Intent.ACTION_SEND)
                .chooser("Pick me")
                .execute(app.getApplicationContext(), app.getPackageManager())
                .onResolveFailure(new ResolveFailure() {
                    @Override
                    public void onFail() {
                        fail("Should not have failed activity resolution");
                    }
                });

        Intent intent = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertThat(intent).hasAction(Intent.ACTION_CHOOSER);
    }

    @Test
    public void testChooserBuilding() {
        Intent chooser = IntentBuilder.with(Intent.ACTION_SEND)
                .chooser("Pick me")
                .build();

        assertThat(chooser).hasAction(Intent.ACTION_CHOOSER);
    }

    private void setupResolver(String action) {
        Intent intent = new Intent(action);
        RobolectricPackageManager pm = RuntimeEnvironment.getRobolectricPackageManager();

        ResolveInfo resolveInfo = new ResolveInfo();
        try {
            resolveInfo.activityInfo = pm.getActivityInfo(activity.getComponentName(), 0);
        } catch (NameNotFoundException e) {
            fail(e.getMessage());
        }

        pm.addResolveInfoForIntent(intent, resolveInfo);
    }
}