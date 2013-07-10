package cz.robyer.gamework.app.service;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cz.robyer.gamework.scenario.message.Message;
import cz.robyer.gamework.app.R;

/**
 * 
 * @author Robert Pösel
 */
public class MessageAdapter extends ArrayAdapter<Message> {
	Context context; 
    int layoutResourceId;    
    List<Message> data = null;
    
    public MessageAdapter(Context context, int layoutResourceId, List<Message> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DataHolder holder = null;
        
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new DataHolder();
            holder.title = (TextView)row.findViewById(R.id.title);
            holder.subtitle = (TextView)row.findViewById(R.id.subtitle);
            
            row.setTag(holder);
        } else {
            holder = (DataHolder)row.getTag();
        }
        
        Message message = data.get(position);
        holder.title.setText(message.getTitle());
        holder.subtitle.setText(message.getValue());
        
        return row;
    }
    
    static class DataHolder
    {
        TextView title;
        TextView subtitle;
    }
    
}
