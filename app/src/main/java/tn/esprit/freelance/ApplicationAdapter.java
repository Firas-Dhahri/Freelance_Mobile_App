package tn.esprit.freelance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tn.esprit.freelance.entities.Application;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {
    private List<Application> applications;
    private final Context context;
    private OnItemClickListener onItemClickListener;
    private OnAcceptClickListener onAcceptClickListener;
    public interface OnAcceptClickListener {
        void onAcceptClick(Application application);
    }

    public void setOnAcceptClickListener(OnAcceptClickListener listener) {
        this.onAcceptClickListener = listener;
    }
    public ApplicationAdapter(Context context, List<Application> applications) {
        this.context = context;
        this.applications = applications;
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
        Application application = applications.get(position);
        holder.tvApplicantName.setText(application.getNom());
        holder.tvApplicationDate.setText("Date: " + application.getDate());
        holder.tvApplicationStatus.setText(application.getStatus());

        // Vérifiez le rôle de l'utilisateur connecté
        SessionManager sessionManager = new SessionManager(context);
        String role = sessionManager.getRole();

        // Si le rôle de l'utilisateur est "FREELANCER", cachez le bouton Accepter
        if ("FREELANCER".equals(role)) {
            holder.btnAccepter.setVisibility(View.GONE);
        } else {
            holder.btnAccepter.setVisibility(View.VISIBLE);
            // Set the click listener for "Accepter" button
            holder.btnAccepter.setOnClickListener(v -> {
                if (onAcceptClickListener != null) {
                    onAcceptClickListener.onAcceptClick(application);  // Pass the application to the listener
                }
            });
        }
        // Set click listener for Detail button
        //holder.tvbtnDetail.setOnClickListener(v -> {
            // Pass only the application ID to DetailActivity
            //Intent intent = new Intent(context, DetailActivity.class);
           // intent.putExtra("applicationId", application.getId());  // Pass the ID of the selected application
         //   context.startActivity(intent);
       // });
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Application application);
    }

    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView tvApplicantName, tvApplicationDate, tvApplicationStatus;
        Button tvbtnDetail;
        Button btnAccepter;
        ApplicationViewHolder(View itemView) {
            super(itemView);
            tvApplicantName = itemView.findViewById(R.id.tvName);
            tvApplicationDate = itemView.findViewById(R.id.tvDate);
            tvApplicationStatus = itemView.findViewById(R.id.tvStatus);
            tvbtnDetail = itemView.findViewById(R.id.tvbtnDetail);
            btnAccepter = itemView.findViewById(R.id.btnAccepter);
        }
    }
}
