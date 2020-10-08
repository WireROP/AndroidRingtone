package nemosofts.ringtone.Receiver;

import android.content.Context;
import android.os.AsyncTask;


import org.json.JSONArray;
import org.json.JSONObject;

import nemosofts.ringtone.BuildConfig;
import nemosofts.ringtone.SharedPref.Setting;
import nemosofts.ringtone.Method.Methods;

/**
 * Created by thivakaran
 */

public class LoadNemosofts extends AsyncTask<String, String, String> {

    private Methods methods;
    private NemosoftsListener aboutListener;
    private String message = "", verifyStatus = "0";

    public LoadNemosofts(Context context, NemosoftsListener aboutListener) {
        this.aboutListener = aboutListener;
        methods = new Methods(context);
    }

    @Override
    protected void onPreExecute() {
        aboutListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String url = BuildConfig.NEMOSOFTS+ Setting.nemosofts_key;
            String json = Methods.getJSONString(url);
            JSONObject jOb = new JSONObject(json);
            if (jOb.has("nemosofts")) {
                JSONArray jsonArray = jOb.getJSONArray("nemosofts");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objJson = jsonArray.getJSONObject(i);

                    if (!objJson.has("success")) {
                        String purchase_code = objJson.getString("purchase_code");
                        String product_name = objJson.getString("product_name");
                        String purchase_date = objJson.getString("purchase_date");
                        String buyer_name = objJson.getString("buyer_name");
                        String license_type = objJson.getString("license_type");
                        String nemosofts_key = objJson.getString("nemosofts_key");
                        String package_name = objJson.getString("package_name");

                        Setting.itemAbout = new ItemNemosofts(purchase_code, product_name, purchase_date, buyer_name, license_type, nemosofts_key, package_name);
                    }else {
                        verifyStatus = objJson.getString("success");
                        message = objJson.getString("msg");
                    }
                }
            }
            return "1";
        } catch (Exception ee) {
            ee.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        aboutListener.onEnd(s, verifyStatus, message);
        super.onPostExecute(s);
    }
}