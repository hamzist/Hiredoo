package com.projet.hiredoo;

import android.app.Activity;
import android.os.Bundle;

public class Modifierprofil_activity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailjob_view);
		
	}
	
	public void onBackPressed() {
		finish();
	}

}