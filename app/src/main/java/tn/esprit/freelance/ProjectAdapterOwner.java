package tn.esprit.freelance;
import android.app.Activity; // Import the Activity class
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.Project;

public class ProjectAdapterOwner extends RecyclerView.Adapter<ProjectAdapterOwner.ProjectViewHolder> {
    private final Context context;
    private final List<Project> projectList;
    private ApplicationDatabase db;

    public ProjectAdapterOwner(Context context, List<Project> projectList) {
        this.context = context;
        this.projectList = projectList;
        this.db = ApplicationDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.project_item_owner, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projectList.get(position);
        holder.projectName.setText(project.getName());
        holder.projectDescription.setText(project.getDescription());
        holder.projectOwner.setText(project.getProjectOwner());

        // Set click listener to open ProjectDetailActivity with the project ID
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProjectDetailOwner.class);
            intent.putExtra("PROJECT_ID", project.getId());
            context.startActivity(intent);
        });

        // Handle Update button click
        holder.updateButton.setOnClickListener(v -> {
            Intent updateIntent = new Intent(context, ModifyProject.class);
            updateIntent.putExtra("PROJECT_ID", project.getId());
            context.startActivity(updateIntent);
        });

        // Handle Delete button click
        holder.deleteButton.setOnClickListener(v -> {
            // Call a method to delete the project from the database
            deleteProject(project);
        });
    }

    // Method to delete the project
    private void deleteProject(Project project) {
        new Thread(() -> {
            db.projectDao().delete(project); // Remove project from Room database
            ((Activity) context).runOnUiThread(() -> {
                projectList.remove(project);
                notifyDataSetChanged();
                Toast.makeText(context, "Project deleted", Toast.LENGTH_SHORT).show();
            });
        }).start();
    } // Closing bracket for deleteProject method

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView projectName, projectDescription, projectOwner, updateButton, deleteButton;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.projectName);
            projectDescription = itemView.findViewById(R.id.projectDescription);
            projectOwner = itemView.findViewById(R.id.projectOwner);
            updateButton = itemView.findViewById(R.id.updateButton); // Ensure your layout has this ID
            deleteButton = itemView.findViewById(R.id.deleteButton); // Ensure your layout has this ID
        }
    }
}