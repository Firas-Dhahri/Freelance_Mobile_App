package tn.esprit.freelance;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import tn.esprit.freelance.R;
import tn.esprit.freelance.entities.Application;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {
    private List<Application> applications;

    public ApplicationAdapter(List<Application> applications) {
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
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView tvApplicantName, tvApplicationDate, tvApplicationStatus;

        ApplicationViewHolder(View itemView) {
            super(itemView);
            tvApplicantName = itemView.findViewById(R.id.tvName);
            tvApplicationDate = itemView.findViewById(R.id.tvDate);
            tvApplicationStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
