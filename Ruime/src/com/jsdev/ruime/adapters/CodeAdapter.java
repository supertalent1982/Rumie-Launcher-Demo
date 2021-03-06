package com.jsdev.ruime.adapters;

import java.util.List;

import com.jsdev.ruime.R;
import com.jsdev.ruime.structures.Code;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CodeAdapter extends BaseAdapter{
	
	private Context context;
	private List<Code> codeList;
	
	public CodeAdapter(Context context, List<Code> data) {
		this.context = context;
		this.codeList = data;
	}

	@Override
	public int getCount() {
		return codeList.size();
	}

	@Override
	public Object getItem(int position) {
		return codeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if (view == null) {
			view = View.inflate(context, R.layout.item_code, null);
		}
		
		TextView codeText = (TextView) view.findViewById(R.id.itemCodeText);
		TextView validText = (TextView) view.findViewById(R.id.itemValidText);
		
		Code code = codeList.get(position);
		
		codeText.setText(code.validCode);
		validText.setText("Valid for: " + String.valueOf(code.validDuration));
		
		return view;
	}

}
