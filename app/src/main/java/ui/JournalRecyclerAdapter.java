package ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.self.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.Journal;

public class JournalRecyclerAdapter extends RecyclerView.Adapter<JournalRecyclerAdapter.JournalViewHolder> {
    private Context context;
    private LayoutInflater journalListInflater;
    private List<Journal> journalList;

    public JournalRecyclerAdapter(Context context, List<Journal> journalList) {
        this.context = context;
        this.journalList = journalList;
    }

    public JournalRecyclerAdapter()
    {

    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = journalListInflater.from(context).inflate(R.layout.journal_row,parent,false);
        return new JournalViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull  JournalRecyclerAdapter.JournalViewHolder holder, int position) {
            Journal journal=journalList.get(position);
            final String imageURL;
            holder.titletxt.setText(journal.getTitle());
            holder.thoughttxt.setText(journal.getThought());
            imageURL=journal.getImageUrl();
            String timeago= (String) DateUtils.getRelativeTimeSpanString(journal.getDate().getSeconds()*1000);
            holder.datetxt.setText(timeago);
            holder.name.setText(journal.getUsername());
        Picasso.get().load(imageURL).fit().placeholder(R.drawable.image_two).into(holder.image);

    }



    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public class JournalViewHolder extends RecyclerView.ViewHolder {
        public TextView titletxt;
        public  TextView thoughttxt;
        public TextView datetxt,name;
        public ImageView image;
        private Uri imageuri;

        String userId,username;
        private ImageButton shareButton;
        public JournalViewHolder(@NonNull final View itemView , Context ctx) {
            super(itemView);
            context=ctx;
            titletxt=itemView.findViewById(R.id.journal_title_list);
            thoughttxt=itemView.findViewById(R.id.journal_thought_list);
            datetxt=itemView.findViewById(R.id.journal_timestamp_list);
            image=itemView.findViewById(R.id.journal_image_list);
            name=itemView.findViewById(R.id.journal_row_username);
            shareButton=itemView.findViewById(R.id.journal_row_share_button);


            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Journal journal=journalList.get(getAdapterPosition());


                    Intent intent= new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    imageuri=Uri.parse(journal.getImageUrl());
                    intent.putExtra(Intent.EXTRA_SUBJECT,journal.getTitle());
                    intent.putExtra(Intent.EXTRA_TEXT,journal.getThought());
                    intent.putExtra(Intent.EXTRA_STREAM,imageuri);
                    intent.setType("image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(Intent.createChooser(intent,"Share..."));



                }
            });

        }
    }
}
