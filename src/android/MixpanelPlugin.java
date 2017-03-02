package com.alanhmak.cordova.plugin;

import android.content.Context;
import android.text.TextUtils;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.Map;
import java.util.HashMap;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MixpanelPlugin extends CordovaPlugin {
    private static String LOG_TAG = "MIXPANEL PLUGIN";
    private static MixpanelAPI mixpanel;
    private static boolean IS_DEBUG = false;

    private enum Action {
        // MIXPANEL API
        ALIAS("alias"),
        FLUSH("flush"),
        IDENTIFY("identify"),
        INIT("init"),
        RESET("reset"),
        TRACK("track"),
        REGISTER_SUPER_PROPERTIES("registerSuperProperties"),

        // PEOPLE API
        PEOPLE_SET("peopleSet"),
        PEOPLE_IDENTIFY("peopleIdentify"),
        PEOPLE_ADD_PUSH_DEVICE_TOKEN("peopleAddPushDeviceToken");

        private final String name;
        private static final Map<String, Action> lookup = new HashMap<String, Action>();

        static {
            for (Action a : Action.values()) lookup.put(a.getName(), a);
        }

        private Action(String name) { this.name = name; }
        public String getName() { return name; }
        public static Action get(String name) { return lookup.get(name); }
    }

    /**
     * helper fn that logs the err and then calls the err callback
     */
    private void error(CallbackContext cbCtx, String message) {
        LOG.e(LOG_TAG, message);
        cbCtx.error(message);
    }

    private void log(String message) {
        if(IS_DEBUG) return;
        LOG.e(LOG_TAG, message);
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext cbCtx) {
        // throws JSONException
        Action act = Action.get(action);

        if (act == null){
            this.error(cbCtx, "invalid action");
            return false;
        }

        if (mixpanel == null && Action.INIT != act) {
            this.error(cbCtx, "mixpanel has not been initialized");
            return false;
        }

        switch (act) {
            case ALIAS:
                return handleAlias(args, cbCtx);
            case FLUSH:
                return handleFlush(args, cbCtx);
            case IDENTIFY:
                return handleIdentify(args, cbCtx);
            case INIT:
                return handleInit(args, cbCtx);
            case RESET:
                return handleReset(args, cbCtx);
            case TRACK:
                return handleTrack(args, cbCtx);
            case REGISTER_SUPER_PROPERTIES:
                return handleRegisterSuperProperties(args, cbCtx);
            case PEOPLE_SET:
                return handlePeopleSet(args, cbCtx);
            case PEOPLE_IDENTIFY:
                return handlePeopleIdentify(args, cbCtx);
            case PEOPLE_ADD_PUSH_DEVICE_TOKEN:
                return handlePeopleAddPushDeviceToken(args, cbCtx);
            default:
                this.error(cbCtx, "invalid action");
                return false;
        }
    }

    @Override
    public void onDestroy() {
        if (mixpanel != null) {
            mixpanel.flush();
        }
        super.onDestroy();
    }


    //************************************************
    //  ACTION HANDLERS
    //   - return true:
    //     - to indicate action was executed with correct arguments
    //     - also if the action from sdk has failed.
    //  - return false:
    //     - arguments were wrong
    //************************************************

    private boolean handleAlias(JSONArray args, final CallbackContext cbCtx) {
        String aliasId = args.optString(0, "");
        if (TextUtils.isEmpty(aliasId)) {
            this.error(cbCtx, "missing alias id");
            return false;
        }
        this.log("mixpanel.alias aliasId:" + aliasId);
        mixpanel.alias(aliasId, null);
        cbCtx.success();
        return true;
    }

    private boolean handleFlush(JSONArray args, final CallbackContext cbCtx) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mixpanel.flush();
                cbCtx.success();
            }
        };
        this.log("mixpanel.flush");
        cordova.getThreadPool().execute(runnable);
        cbCtx.success();
        return true;
    }

    private boolean handleIdentify(JSONArray args, final CallbackContext cbCtx) {
        String uniqueId = args.optString(0, "");
        if (TextUtils.isEmpty(uniqueId) || uniqueId == "null") {
          this.log("mixpanel.identify uniqueId empty:" + mixpanel.getDistinctId() + ", " + mixpanel.getPeople().getDistinctId());
          mixpanel.identify(mixpanel.getDistinctId());
          mixpanel.getPeople().identify(mixpanel.getPeople().getDistinctId());
          cbCtx.success();
          return true;
        }
        this.log("mixpanel.identify uniqueId:" + uniqueId);
        mixpanel.identify(uniqueId);
        mixpanel.getPeople().identify(uniqueId);
        cbCtx.success();
        return true;
    }

    private boolean handleInit(JSONArray args, final CallbackContext cbCtx) {
        String token = args.optString(0, "");
        if (TextUtils.isEmpty(token)) {
            this.error(cbCtx, "missing token for mixpanel project");
            return false;
        }
        this.log("MixpanelAPI.getInstance token:" + token);
        Context ctx = cordova.getActivity();
        mixpanel = MixpanelAPI.getInstance(ctx, token);
        cbCtx.success();
        return true;
    }

    private boolean handleReset(JSONArray args, final CallbackContext cbCtx) {
        this.log("mixpanel.reset");
        mixpanel.reset();
        cbCtx.success();
        return true;
    }

    private boolean handleTrack(JSONArray args, final CallbackContext cbCtx) {
        String event = args.optString(0, "");
        if (TextUtils.isEmpty(event)) {
            this.error(cbCtx, "missing event name");
            return false;
        }

        JSONObject properties = args.optJSONObject(1);
        if (properties == null) {
            properties = new JSONObject();
        }
        this.log("mixpanel.track event:" + event + ", properties:" + properties.toString());
        mixpanel.track(event, properties);
        cbCtx.success();
        return true;
    }

    private boolean handleRegisterSuperProperties(JSONArray args, final CallbackContext cbCtx) {
        JSONObject properties = args.optJSONObject(0);
        if (properties == null) {
            properties = new JSONObject();
        }
        this.log("mixpanel.registerSuperProperties properties:" + properties.toString());
        mixpanel.registerSuperProperties(properties);
        cbCtx.success();
        return true;
    }

    private boolean handlePeopleAddPushDeviceToken(JSONArray args, final CallbackContext cbCtx) {
        String pushId = args.optString(0, "");
        if (TextUtils.isEmpty(pushId)) {
            this.error(cbCtx, "missing push id");
            return false;
        }
        this.log("mixpanel.getPeople().setPushRegistrationId registrationId:" + pushId);
        mixpanel.getPeople().setPushRegistrationId(pushId);
        cbCtx.success();
        return true;
    }

    private boolean handlePeopleIdentify(JSONArray args, final CallbackContext cbCtx) {
        String distinctId = args.optString(0, "");
        if (TextUtils.isEmpty(distinctId)) {
            this.error(cbCtx, "missing distinct id");
            return false;
        }
        this.log("mixpanel.getPeople().identify distinctId:" + distinctId);
        mixpanel.getPeople().identify(distinctId);
        cbCtx.success();
        return true;
    }

    private boolean handlePeopleSet(JSONArray args, final CallbackContext cbCtx) {
        JSONObject properties = args.optJSONObject(0);
        if (properties == null) {
            this.error(cbCtx, "missing people properties object");
            return false;
        }
        this.log("mixpanel.getPeople().set properties:" + properties.toString());
        mixpanel.getPeople().set(properties);
        cbCtx.success();
        return true;
    }
}
