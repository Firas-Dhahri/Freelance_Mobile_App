package tn.esprit.freelance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import tn.esprit.freelance.entities.Project;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private Context context;
    private List<Project> projectList;
    private OnApplyClickListener onApplyClickListener;

    public interface OnApplyClickListener {
        void onApplyClick(Project project);
    }

    public ProjectAdapter(Context context, List<Project> projectList, OnApplyClickListener onApplyClickListener) {
        this.context = context;
        this.projectList = projectList;
        this.onApplyClickListener = onApplyClickListener;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.project_item, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder holder, int position) {
        Project project = projectList.get(position);

        // Set the project data in your views
        holder.projectName.setText(project.getName());
        holder.projectDescription.setText(project.getDescription());
        holder.projectOwner.setText(project.getProjectOwner());

        // Handle click on the "postule" ImageView
        holder.postule.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApplyJobActivity.class);
            intent.putExtra("project_id", project.getId()); // Pass the project ID
            context.startActivity(intent);
        });    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView projectName, projectDescription, projectOwner;
        ImageView postule;

        public ProjectViewHolder(View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.projectName);
            projectDescription = itemView.findViewById(R.id.projectDescription);
            projectOwner = itemView.findViewById(R.id.projectOwner);
            postule = itemView.findViewById(R.id.postule);
        }
    }
}
