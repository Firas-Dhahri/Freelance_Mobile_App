package tn.esprit.freelance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import tn.esprit.freelance.R;
import tn.esprit.freelance.entities.User;

public class UserAdapter extends ArrayAdapter<User> {
    private final Context context;
    private final List<User> users;

    public UserAdapter(Context context, List<User> users) {
        super(context, R.layout.user_list_item, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.user_list_item, parent, false);
        }

        User user = users.get(position);

        TextView fullNameView = rowView.findViewById(R.id.fullNameTextView);
        TextView emailView = rowView.findViewById(R.id.emailTextView);
        TextView phoneNumberView = rowView.findViewById(R.id.phoneNumberTextView);

        fullNameView.setText(user.getFullName());
        emailView.setText(user.getEmail());
        phoneNumberView.setText(user.getPhoneNumber());

        // Set text color if needed
        fullNameView.setTextColor(context.getResources().getColor(android.R.color.black));
        emailView.setTextColor(context.getResources().getColor(android.R.color.black));
        phoneNumberView.setTextColor(context.getResources().getColor(android.R.color.black));

        return rowView;
    }
}
