package in.dc297.mqttclpro.tasker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import in.dc297.mqttclpro.R;

public class PublishTaskerActivity extends AbstractPluginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_tasker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Bundle localeBundle = getIntent().getBundleExtra(in.dc297.mqttclpro.tasker.Intent.EXTRA_BUNDLE);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        if (null == savedInstanceState)
        {
            if (PluginBundleManager.isBundleValid(localeBundle))
            {
                Log.i("pubtaskact","bundle valid");
                final String message =
                        localeBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_MESSAGE);
                final String topic =
                        localeBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_TOPIC);
                ((EditText) findViewById(R.id.editText)).setText(message);
                ((EditText) findViewById(R.id.editText2)).setText(topic);
            }
        }
    }

    @Override
    public void finish()
    {
        if (!isCanceled())
        {
            final String topic = ((EditText) findViewById(R.id.editText)).getText().toString();
            final String message = ((EditText) findViewById(R.id.editText2)).getText().toString();

            if (message.length() > 0 && topic.length() > 0)
            {
                final Intent resultIntent = new Intent();

                /*
                 * This extra is the data to ourselves: either for the Activity or the BroadcastReceiver. Note
                 * that anything placed in this Bundle must be available to Locale's class loader. So storing
                 * String, int, and other standard objects will work just fine. Parcelable objects are not
                 * acceptable, unless they also implement Serializable. Serializable objects must be standard
                 * Android platform objects (A Serializable class private to this plug-in's APK cannot be
                 * stored in the Bundle, as Locale's classloader will not recognize it).
                 */
                final Bundle resultBundle =
                        PluginBundleManager.generateBundle(getApplicationContext(), message,topic);

                resultIntent.putExtra(in.dc297.mqttclpro.tasker.Intent.EXTRA_BUNDLE, resultBundle);
                /*
                 * The blurb is concise status text to be displayed in the host's UI.
                 */
                final String blurb = generateBlurb(getApplicationContext(), topic+" : "+message);
                resultIntent.putExtra(in.dc297.mqttclpro.tasker.Intent.EXTRA_STRING_BLURB, blurb);

                setResult(RESULT_OK, resultIntent)  ;
            }
        }

        super.finish();
    }

    static String generateBlurb(final Context context, final String message)
    {
        final int maxBlurbLength =
                context.getResources().getInteger(R.integer.max_blurb_length);

        if (message.length() > maxBlurbLength)
        {
            return message.substring(0, maxBlurbLength);
        }

        return message;
    }

}
