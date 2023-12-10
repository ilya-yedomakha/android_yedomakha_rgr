package stu.cn.ua.rgr.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import stu.cn.ua.rgr.R;
import stu.cn.ua.rgr.model.Meteorite;

public class MeteoritesAdapter extends RecyclerView.Adapter<MeteoritesAdapter.MeteoriteViewHolder>
        implements View.OnClickListener {

    private List<Meteorite> meteorites = Collections.emptyList();

    private MeteoriteListener listener;

    public MeteoritesAdapter(MeteoriteListener listener) {
        setHasStableIds(true);
        this.listener = listener;
    }

    public void setMeteorites(List<Meteorite> meteorites) {
        this.meteorites = meteorites;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return meteorites.get(position).getId();
    }

    @NonNull
    @Override
    public MeteoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.item_meteorite, parent, false);
        return new MeteoriteViewHolder(root, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MeteoriteViewHolder holder, int position) {
        Meteorite meteorite = meteorites.get(position);
        holder.nameTextView.setText(meteorite.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        holder.yearTextView.setText(String.valueOf(sdf.format(meteorite.getYear())));
        holder.fallTextView.setText(String.valueOf(meteorite.getFall()));
        holder.itemView.setTag(meteorite);
    }

    @Override
    public int getItemCount() {
        return meteorites.size();
    }

    @Override
    public void onClick(View v) {
        Meteorite meteorite = (Meteorite) v.getTag();
        listener.onMeteoriteChosen(meteorite);
    }

    static class MeteoriteViewHolder extends RecyclerView.ViewHolder {
        private TextView fallTextView;
        private TextView yearTextView;
        private TextView nameTextView;

        public MeteoriteViewHolder(@NonNull View itemView, View.OnClickListener listener) {
            super(itemView);
            fallTextView = itemView.findViewById(R.id.fallTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            itemView.setOnClickListener(listener);
        }
    }

    public interface MeteoriteListener {
        void onMeteoriteChosen(Meteorite meteorite);
    }
}
