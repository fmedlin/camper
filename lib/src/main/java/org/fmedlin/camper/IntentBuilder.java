package org.fmedlin.camper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.io.Serializable;

public class IntentBuilder {
    Context context;

    String action;
    String type;
    Uri data;
    Bundle extras;
    Pair<Uri,String> dataAndType;

    public static IntentBuilder with(@NonNull Context context) {
        return new IntentBuilder(context);
    }

    public static IntentBuilder with(String action) {
        return new ImplicitIntentBuilder(action);
    }

    public static IntentBuilder with(String action, Uri uri) {
        return new ImplicitIntentBuilder(action, uri);
    }

    public static IntentBuilder with(@NonNull Uri uri) {
        return new ImplicitIntentBuilder(uri);
    }

    public static IntentBuilder start(@NonNull Class<? extends Activity> target) {
        return new ExplicitIntentBuilder(target);
    }

    public static IntentBuilder copy(@NonNull Intent intent) {
        return new CopyIntentBuilder(intent);
    }


    private IntentBuilder() {
    }

    private IntentBuilder(Context context) {
        this.context = context;
    }

    public IntentBuilder from(Context context) {
        this.context = context;
        return this;
    }

    public IntentBuilder action(@NonNull String action) {
        this.action = action;
        return this;
    }

    public IntentBuilder type(String type) {
        this.type = type;
        return this;
    }

    public IntentBuilder data(Uri data) {
        this.data = data;
        return this;
    }

    public IntentBuilder dataAndType(Uri data, String type) {
        dataAndType = Pair.create(data, type);
        return this;
    }

    public IntentBuilder extra(@NonNull String key, String value) {
        getExtras().putString(key, value);
        return this;
    }

    public IntentBuilder extra(@NonNull String key, int value) {
        getExtras().putInt(key, value);
        return this;
    }

    public IntentBuilder extra(@NonNull String key, long value) {
        getExtras().putLong(key, value);
        return this;
    }

    public IntentBuilder extra(@NonNull String key, float value) {
        getExtras().putFloat(key, value);
        return this;
    }

    public IntentBuilder extra(@NonNull String key, boolean value) {
        getExtras().putBoolean(key, value);
        return this;
    }

    public IntentBuilder extra(@NonNull String key, Serializable value) {
        getExtras().putSerializable(key, value);
        return this;
    }

    // For testing only
    protected IntentBuilder assertType(Class<?> builderClass) {
        if (builderClass.isInstance(this)) {
            return this;
        }

        throw new IllegalStateException("Unexpected builder type");
    }

    private Bundle getExtras() {
        if (extras == null) {
            extras = new Bundle();
        }

        return extras;
    }

    public Intent build() {
        Intent intent = buildIntent();

        if (extras != null) {
            intent.putExtras(extras);
        }

        setDataAndType(intent);

        return intent;
    }

    protected Intent buildIntent() {
        if (action != null) {
            return new Intent(action);
        }

        return null;
    }

    private void setDataAndType(Intent intent) {
        if (dataAndType != null) {
            intent.setDataAndType(dataAndType.first, dataAndType.second);
            return;
        }

        if (data != null) {
            intent.setData(data);
        }

        if (type != null) {
            intent.setType(type);
        }
    }

    public Intent execute() {
        Intent intent = build();
        context.startActivity(intent);
        return intent;
    }

    static public class ImplicitIntentBuilder extends IntentBuilder {

        Uri uri;

        public ImplicitIntentBuilder(@NonNull Uri uri) {
            this.uri = uri;
        }

        public ImplicitIntentBuilder(String action) {
            this.action = action;
        }

        public ImplicitIntentBuilder(String action, Uri uri) {
            this.action = action;
            this.uri = uri;
        }

        @Override
        protected Intent buildIntent() {
            if (action == null) {
                throw new IllegalArgumentException("Implicit intent requires action");
            }

            if (uri == null) {
                return new Intent(action);
            } else {
                return new Intent(action, uri);
            }
        }
    }

    static public class ExplicitIntentBuilder extends IntentBuilder {

        Class<? extends Activity> target;

        public ExplicitIntentBuilder(@NonNull Class<? extends Activity> target) {
            this.target = target;
        }

        @Override
        protected Intent buildIntent() {
            if (context == null) {
                throw new IllegalArgumentException("Intent requires context");
            }

            return new Intent(context, target);
        }
    }

    static public class CopyIntentBuilder extends IntentBuilder {

        Intent original;

        public CopyIntentBuilder(@NonNull Intent original) {
            this.original = original;
        }

        @Override
        protected Intent buildIntent() {
            return new Intent(original);
        }
    }
}
