package org.fmedlin.camper;

import android.content.Intent;
import android.net.Uri;

import com.fmedlin.intentbuilder.BuildConfig;

import org.fmedlin.camper.IntentBuilder.ExplicitIntentBuilder;
import org.fmedlin.camper.IntentBuilder.ImplicitIntentBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.io.Serializable;

import static org.assertj.android.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class IntentBuilderTest {

    @Test
    public void testStartActivityWithExplicitIntent() {
        Intent intent = IntentBuilder.start(TargetActivity.class)
                .from(RuntimeEnvironment.application)
                .assertType(ExplicitIntentBuilder.class)
                .execute();
        assertThat(ShadowApplication.getInstance().getNextStartedActivity()).isEqualTo(intent);
    }

    @Test
    public void testTypeAndExtraSetters() {
        Intent intent = IntentBuilder.with(RuntimeEnvironment.application)
                .action("intent action")
                .type("text/plain")
                .extra("string extra", "string")
                .extra("int extra", 1)
                .extra("long extra", 2L)
                .extra("float extra", 3f)
                .extra("boolean extra", true)
                .extra("serializable extra", (Serializable) "serialized")
                .build();

        assertThat(intent).hasAction("intent action")
                .hasType("text/plain")
                .hasExtra("string extra", "string")
                .hasExtra("int extra", 1)
                .hasExtra("long extra", 2L)
                .hasExtra("float extra", 3f)
                .hasExtra("boolean extra", true)
                .hasExtra("serializable extra", "serialized");
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
}