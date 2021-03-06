package com.projet.hiredoo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Detailpostule_activity extends Activity implements OnClickListener {
        
	private TextView postule_user, postule_date, postule_cv, postule_lm, postule_video;
	private Button postule_accept, postule_reject;
	private String json;
	private JSONObject jo;
	private String cv_name, lm_name, video_name, user_id;
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.detailpostule_view);
            
            // R�cup�ration des views
            postule_user   = (TextView)findViewById(R.id.detailpostule_user);
            postule_date   = (TextView)findViewById(R.id.detailpostule_date);
            postule_cv     = (TextView)findViewById(R.id.detailpostule_cv);
            postule_lm     = (TextView)findViewById(R.id.detailpostule_lm);
            postule_video  = (TextView)findViewById(R.id.detailpostule_video);
            postule_accept = (Button)findViewById(R.id.detailpostule_btnaccept);
            postule_reject = (Button)findViewById(R.id.detailpostule_btnreject);
            
            postule_user.setOnClickListener(this);
            postule_cv.setOnClickListener(this);
            postule_lm.setOnClickListener(this);
            postule_video.setOnClickListener(this);
            postule_accept.setOnClickListener(this);
            postule_reject.setOnClickListener(this);
            
    		// Recuperation des donnees
    		this.json = getIntent().getExtras().getString("data");
    		
    		// Formatage du resultat
    		try {
    			this.jo = new JSONObject(this.json);
    		}
    		catch (JSONException je) {
    			Toast.makeText(this, "Impossible de formater les donn�es", Toast.LENGTH_LONG).show();
    			return;
    		}
    		
    		// Remplissage de la page
    		remplirPage();
    }
        
    public void onBackPressed() {
            finish();
    }

    @Override
    public void onClick(View v) {
    	switch(v.getId()) {
    	case R.id.detailpostule_user:
			// Appel du web service
    		Async_get ag = new Async_get(this, Profilcandidate_activity.class);
			ag.execute(new String[] { Constante.url + Constante.user_getUserProfile + this.user_id });
    		break;
    		
    	case R.id.detailpostule_cv:
    		Intent cv_intent = new Intent(Intent.ACTION_VIEW);
    		String cv_link = Constante.url_files + this.cv_name;
    		cv_intent.setData(Uri.parse(cv_link));
    	    try  {
    	        startActivity(cv_intent);
    	    }
    	    catch (ActivityNotFoundException ex)  {
    	         Toast.makeText(this, "No Pdf Viewer", Toast.LENGTH_SHORT).show();
    	    }
    		break;
    		
    	case R.id.detailpostule_lm:
    		Intent lm_intent = new Intent(Intent.ACTION_VIEW);
    		String lm_link = Constante.url_files + this.lm_name;
    		lm_intent.setData(Uri.parse(lm_link));
    	    try  {
    	        startActivity(lm_intent);
    	    }
    	    catch (ActivityNotFoundException ex)  {
    	         Toast.makeText(this, "No Pdf Viewer", Toast.LENGTH_SHORT).show();
    	    }
    		break;
    		
    	case R.id.detailpostule_video:
			// Test de la connexion internet
			if(!Constante.isInternetAvailable(this)) {
				Toast.makeText(this, "Internet connection not available", Toast.LENGTH_LONG).show();
				return;
			}
			
			Intent video_intent = new Intent(this, Video_activity.class);
			try {
				video_intent.putExtra("type", Constante.video_streaming);
				video_intent.putExtra("name", this.video_name);
				startActivity(video_intent);
			}
			catch(ActivityNotFoundException ex) {
				Toast.makeText(this, "Activity introuvable.\n" + ex.getMessage(), Toast.LENGTH_LONG).show();
			}
    		break;
    		
    	case R.id.detailpostule_btnaccept:
    		String id;
    		String jobName;
        	
        	try {
        		id = this.jo.getJSONObject("user").get("id").toString();
        		jobName = this.jo.getJSONObject("job").get("title").toString();
    		}
    		catch (JSONException ex) {
    			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    			builder.setTitle("JSONException");
    			builder.setMessage("Cause: " + ex.getCause() + "\n\nMessage: " + ex.getMessage());
    			builder.create().show();
    			return;
    		}
        	
    		// Appel du web service GET
    		Async_get ag1 = new Async_get(this, null);
    		ag1.execute(new String[] { Constante.url + Constante.user_setNotif + id + "/" + Constante.application_accepted + "/" + jobName });
    		break;
    		
    	case R.id.detailpostule_btnreject:
    		String id2;
    		String jobName2;
        	
        	try {
        		id2 = this.jo.getJSONObject("user").get("id").toString();
        		jobName2 = this.jo.getJSONObject("job").get("title").toString();
    		}
    		catch (JSONException ex) {
    			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    			builder.setTitle("JSONException");
    			builder.setMessage("Cause: " + ex.getCause() + "\n\nMessage: " + ex.getMessage());
    			builder.create().show();
    			return;
    		}
        	
    		// Appel du web service GET
    		Async_get ag2 = new Async_get(this, null);
    		ag2.execute(new String[] { Constante.url + Constante.user_setNotif + id2 + "/" + Constante.application_rejected + "/" + jobName2 });
    		break;
    		
		default:
			AlertDialog.Builder error = new AlertDialog.Builder(this);
            error.setTitle("Internal error");
            error.setMessage("Listener caller unkwonen");
            error.create().show();
    	}
    }
    
    // Fonction de remplissage de la page
    private void remplirPage() {
    	// Recuperation des objets
    	JSONObject user, cv, lm, vid;
    	String date;
    	
    	try {
    		user = this.jo.getJSONObject("user");
        	cv   = this.jo.getJSONObject("cv");
        	lm   = this.jo.getJSONObject("lm");
        	vid  = this.jo.getJSONObject("video");
        	date = this.jo.get("postuleDate").toString();
		}
		catch (JSONException ex) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("JSONException1");
			builder.setMessage("Cause: " + ex.getCause() + "\n\nMessage: " + ex.getMessage());
			builder.create().show();
			return;
		}
    	
    	// Affichage des donn�es
    	try {
    		this.user_id = user.get("id").toString();
    		this.postule_user.setText(((user.has("name")) ? user.get("name").toString() : "") + " " + ((user.has("lastname")) ? user.get("lastname").toString() : ""));
    		this.postule_date.setText(Constante.transformDate(date));
    		this.cv_name = cv.get("name").toString();
    		this.lm_name = lm.get("name").toString();
    		this.video_name = vid.get("name").toString();
		}
		catch (JSONException ex) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("JSONException2");
			builder.setMessage("Cause: " + ex.getCause() + "\n\nMessage: " + ex.getMessage());
			builder.create().show();
			return;
		}
    	
    }
                
}
