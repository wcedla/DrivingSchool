package com.wcedla.driving_school.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wcedla.driving_school.R;

import java.util.List;

public class StudyProgressSelectAdapter extends BaseAdapter {

    Context context;
    List<String> progressTextList;
    int level;

    public StudyProgressSelectAdapter(Context context, List<String> progressTextList, int level) {
        this.context = context;
        this.progressTextList = progressTextList;
        this.level = level;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return progressTextList.size();
    }

    @Override
    public Object getItem(int position) {
        return progressTextList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StudyProgressHolder studyProgressHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_study_progress_text_item, parent, false);
            studyProgressHolder = new StudyProgressHolder();
            studyProgressHolder.textView = convertView.findViewById(R.id.progress_text);
            convertView.setTag(studyProgressHolder);
        } else {
            studyProgressHolder = (StudyProgressHolder) convertView.getTag();
        }
        if (position <= level) {
            studyProgressHolder.textView.setTextColor(Color.RED);
        } else {
            studyProgressHolder.textView.setTextColor(Color.parseColor("#000000"));
        }
        studyProgressHolder.textView.setText(progressTextList.get(position));
        return convertView;
    }

    class StudyProgressHolder {
        TextView textView;
    }
}
