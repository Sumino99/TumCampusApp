package de.tum.in.tumcampus.auxiliary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import de.tum.in.tumcampus.R;
import de.tum.in.tumcampus.tumonline.TUMOnlineRequest;

/**
 * Easy accessible class for token management.
 * 
 * @author Sascha Moecker
 * 
 */
public class AccessTokenManager {
	public static final int MIN_LRZ_LENGTH = 7;

	private Context context;

	private String lrzId;

	public AccessTokenManager(Context context) {
		this.context = context;
	}

	/**
	 * get a new access token for TUMOnline by passing the lrz ID due to the
	 * simplicity of the given xml file we only need to parse the <token>
	 * element using an xml-parser is simply to much... just extract the pattern
	 * via regex
	 * 
	 * @param lrz_id
	 *            lrz user id
	 * @return the access token
	 */
	public String generateAccessToken(String lrz_id) {
		// we don't have an access token yet, though we take the constructor
		// with only one parameter to set the method
		TUMOnlineRequest request = new TUMOnlineRequest("requestToken", context, false);
		// add lrz_id to parameters
		request.setParameter("pUsername", lrz_id);
		// add readable name for TUMOnline
		request.setParameter("pTokenName", "TUMCampusApp-"
				+ android.os.Build.PRODUCT);

		// fetch the xml response of requestToken
		String strTokenXml = request.fetch();
		//Log.d("RAWOUTPUT", strTokenXml);
		// it is only one tag in that xml, let's do a regex pattern
		return strTokenXml.substring(
				strTokenXml.indexOf("<token>") + "<token>".length(),
				strTokenXml.indexOf("</token>"));
	}

	private String getLrzId() {
		if (lrzId == null || lrzId.equals("")) {
			lrzId = Utils.getSetting(context, Const.LRZ_ID);
		}
		return lrzId;
	}

	public boolean hasValidAccessToken() {
		String oldAccessToken = PreferenceManager.getDefaultSharedPreferences(
				context).getString(Const.ACCESS_TOKEN, "");
        return oldAccessToken != null && oldAccessToken.length() > 2;
	}

	/**
	 * Internal method for setting a new token. WARNING: Doesn't use shared
	 * preferences, but rather a parameter. Needed for the onPreferenceChanged
	 * callback, so as to use the new LRZ_ID value for the token generation
	 * before it is set (which happens right after the callback).
	 * 
	 * @param stringLRZID
	 */
	public boolean requestAccessToken(String stringLRZID) {
		try {
			if (!Utils.isConnected(context)) {
				Toast.makeText(context, R.string.no_internet_connection,
						Toast.LENGTH_LONG).show();
				return false;
			}
			// ok, do the request now
			String strAccessToken = generateAccessToken(stringLRZID);
			Log.d("AcquiredAccessToken", strAccessToken);

			// save access token to preferences
			Utils.setSetting(context, Const.ACCESS_TOKEN, strAccessToken);
			return true;

		} catch (Exception ex) {
            Log.e("TCA",context.getString(R.string.access_token_wasnt_generated),ex);
			// set access token to null
			Utils.setSetting(context, Const.ACCESS_TOKEN, null);
			Toast.makeText(context,
					context.getString(R.string.access_token_wasnt_generated),
					Toast.LENGTH_LONG).show();
		}
		return false;
	}

	public void setupAccessToken() {
		lrzId = PreferenceManager.getDefaultSharedPreferences(context).getString(Const.LRZ_ID, "");
		// check if lrz could be valid?
		if (lrzId.length() == MIN_LRZ_LENGTH) {
			// is access token already set?
            if (hasValidAccessToken()) {
				// show Dialog first
				new AlertDialog.Builder(context)
                        .setMessage(context.getString(R.string.dialog_new_token))
						.setPositiveButton(context.getString(R.string.yes),
								new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        requestAccessToken(getLrzId());
                                    }
                                })
						.setNegativeButton(context.getString(R.string.no), null)
						.show();
			} else {
				requestAccessToken(lrzId);
			}
		} else {
			Toast.makeText(context,
					context.getString(R.string.error_lrz_wrong),
					Toast.LENGTH_LONG).show();
		}
	}
}
