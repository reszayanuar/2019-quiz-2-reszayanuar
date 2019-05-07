package id.ac.polinema.todoretrofit.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.ac.polinema.todoretrofit.R;
import id.ac.polinema.todoretrofit.models.Todo;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private Context context;
    private List<Todo> items;
    private OnTodoClickedListener listener;
    private OnTodoDeleteClickedListener listenerD;

    public TodoAdapter(Context context, OnTodoClickedListener listener, OnTodoDeleteClickedListener listenerD) {
        this.context = context;
        this.listener = listener;
        this.listenerD = listenerD;
    }

    public void setItems(List<Todo> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    public void setListener(OnTodoClickedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_todo, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Todo todo = items.get(i);
        viewHolder.bind(todo, listener, listenerD);
    }

    @Override
    public int getItemCount() {
        return (items != null) ? items.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView todoText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todoText = itemView.findViewById(R.id.text_todo);
        }

        public void bind(final Todo todo, final OnTodoClickedListener listener, final OnTodoDeleteClickedListener listenerD) {
            todoText.setText(todo.getTodo());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Pilih Edit atau Delete");
                    builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onClick(todo);
                        }
                    });
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("Konfirmasi Delete Todo");
                            alert.setMessage("Apa anda mau hapus '"+todo.getTodo()+"' ?");
                            alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alert.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    listenerD.onDeleteClicked(todo);
                                }
                            });
                            alert.create().show();
                        }
                    });
                    builder.create().show();
                }
            });
        }
    }

    public interface OnTodoClickedListener {
        void onClick(Todo todo);
    }

    public interface OnTodoDeleteClickedListener {
        void onDeleteClicked(Todo todo);
    }
}
