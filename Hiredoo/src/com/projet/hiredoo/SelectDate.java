package com.projet.hiredoo;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class SelectDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	private TextView date;
	private String type;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar calendar = Calendar.getInstance();
		int dd = calendar.get(Calendar.DAY_OF_MONTH);
		int mm = calendar.get(Calendar.MONTH);
		int yy = calendar.get(Calendar.YEAR);
		return new DatePickerDialog(getActivity(), this, yy, mm, dd);
	}
	
	public void initialise(TextView date, String type) {
		this.date = date;
		this.type = type;
	}
	
	public void onDateSet(DatePicker view, int yy, int mm, int dd) {
		this.date.setText(dd + "/" + mm + "/" + yy);
		
		if(this.type.equals(Constante.date_from_type))
			Constante.date_from = yy + "-" + mm + "-" + dd + "T00:00:00";
		else if(this.type.equals(Constante.date_to_type))
			Constante.date_to = yy + "-" + mm + "-" + dd + "T00:00:00";
		else
			Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_LONG).show();
	}
}
