package ca.sfu.iat381.reachout_app.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.io.Serializable;
import java.util.List;

import ca.sfu.iat381.reachout_app.R;
import ca.sfu.iat381.reachout_app.model.Event;
//import ca.sfu.iat381.reachout_app.views.EventAdapter;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context mContext;
    private int mNumberOfEvents;
    private List<Event> events;
   // final private EventItemClickListener mOnClickListener;

//    // interface to define listener
//    public interface EventItemClickListener {
//        void onListItemClick(int clickedEventIndex);
//    }

    public EventAdapter(Context context, List<Event> events) { //, EventItemClickListener listener) {
        this.mContext = context;
        //this.mOnClickListener = listener;
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
        holder.locationTv.setText(event.getVenue());
        holder.dateTv.setText(event.getTime());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "clicked!", Toast.LENGTH_SHORT).show();
                Intent goToEventDetails = new Intent(mContext, EventDetails.class);
//                Bundle myBundle = new Bundle();
//
//                myBundle.putSerializable("event_details", event);

//                String latitude = event.getLatitude();
//                String longitude = event.getLongitude();
//
//                goToEventDetails.putExtra("location_pinpoint_longitude", latitude);
//                goToEventDetails.putExtra("location_pinpoint_latitude", longitude);

                goToEventDetails.putExtra("Event_object", event);


                goToEventDetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(goToEventDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView venueTv;
        public TextView dateTv;
        public TextView locationTv;
        public TextView nameTv;
        public View mView;

        public ViewHolder(View itemView){
            super(itemView);
           // nameTv = (TextView) itemView.findViewById(R.id.);
            //itemNameTv is the id

            nameTv = (TextView) itemView.findViewById(R.id.eventName);
            locationTv = (TextView) itemView.findViewById(R.id.eventLocation);
            dateTv = (TextView) itemView.findViewById(R.id.eventDate);
            //itemView.setOnClickListener(this);
            mView = itemView;

        }

//        @Override
//        public void onClick(View v) {
//            int clickedPosition = getAdapterPosition();
//            mOnClickListener.onListItemClick(clickedPosition);
//        }
    }


}//end class


















