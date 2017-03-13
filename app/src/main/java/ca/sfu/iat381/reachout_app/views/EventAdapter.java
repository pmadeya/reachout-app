package ca.sfu.iat381.reachout_app.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * Created by kenanabdulkarim on 2017-03-11.
 */

import java.util.List;

import ca.sfu.iat381.reachout_app.R;
import ca.sfu.iat381.reachout_app.model.Event;
//import ca.sfu.iat381.reachout_app.views.EventAdapter;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context mContext;
    private int mNumberOfEvents;
    private List<Event> events;

    public EventAdapter(Context context, List<Event> events) {
        this.mContext = context;
        this.events = events;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {

        final Event event = events.get(position);
        holder.nameTv.setText(event.getName());
        holder.locationTv.setText(event.getLocation());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView venueTv;
        public TextView timeTv;
        public TextView locationTv;
        public TextView nameTv;
        public View mView;

        public ViewHolder(View itemView){
            super(itemView);
           // nameTv = (TextView) itemView.findViewById(R.id.);
            //itemNameTv is the id

            nameTv = (TextView) itemView.findViewById(R.id.eventName);
            locationTv = (TextView) itemView.findViewById(R.id.eventLocation);


        }

    }


}//end class


















