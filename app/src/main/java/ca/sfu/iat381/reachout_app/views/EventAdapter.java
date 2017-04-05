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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ca.sfu.iat381.reachout_app.R;
import ca.sfu.iat381.reachout_app.model.Event;
//import ca.sfu.iat381.reachout_app.views.EventAdapter;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context mContext;
    private int mNumberOfEvents;
    private Calendar calendar;
    private Date event_date;
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

        View itemView = inflater.inflate(R.layout.card_event_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {

        final Event event = events.get(position);
        holder.nameTv.setText(event.getName());
        holder.locationTv.setText(event.getVenue());


        //String returned from JSON
        //"start_time":"2017-07-25 19:00:00"

        //Create a date object for the start time
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            event_date = parser.parse(event.getTime());
            calendar = Calendar.getInstance();
            calendar.setTime(event_date);
            System.out.println(calendar.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Can't parse date!", Toast.LENGTH_SHORT).show();
        }


        holder.monthTv.setText(new SimpleDateFormat("MMM").format(calendar.getTime()));
        holder.eventDateNumberCircle.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToEventDetails = new Intent(mContext, EventDetails.class);
//                Bundle myBundle = new Bundle();
//
//                myBundle.putSerializable("event_details", event);

//                String latitude = event.getLatitude();
//                String longitude = event.getLongitude();
//
//                goToEventDetails.putExtra("location_pinpoint_longitude", latitude);
//                goToEventDetails.putExtra("location_pinpoint_latitude", longitude);

                goToEventDetails.putExtra("Class", "EventAdapter");
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
        public TextView eventDateNumberCircle;
        public TextView monthTv;
        public TextView locationTv;
        public TextView nameTv;
        public View mView;

        public ViewHolder(View itemView){
            super(itemView);
           // nameTv = (TextView) itemView.findViewById(R.id.);
            //itemNameTv is the id

            nameTv = (TextView) itemView.findViewById(R.id.eventName);
            locationTv = (TextView) itemView.findViewById(R.id.eventLocation);
            monthTv = (TextView) itemView.findViewById(R.id.eventMonth);
            eventDateNumberCircle = (TextView) itemView.findViewById(R.id.eventDateNumber);
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


















