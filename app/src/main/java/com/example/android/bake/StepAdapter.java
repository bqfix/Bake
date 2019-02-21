package com.example.android.bake;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bake.recipes.StepInstruction;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private List<StepInstruction> mStepInstructions;

    private final StepClickHandler mStepClickHandler;

    StepAdapter(StepClickHandler stepClickHandler) {
        mStepClickHandler = stepClickHandler;
    }


    //Inner ViewHolder Class
    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mStepIdText;
        private TextView mStepDescripText;

        //Constructor
        public StepViewHolder(@NonNull View itemView) {
            super(itemView);

            mStepIdText = (TextView) itemView.findViewById(R.id.step_id_tv);
            mStepDescripText = (TextView) itemView.findViewById(R.id.step_card_descrip_tv);

            itemView.setOnClickListener(this);
        }

        //On click, pass the Step to the StepClickHandler interface
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            StepInstruction stepInstruction = mStepInstructions.get(adapterPosition);
            mStepClickHandler.onItemClick(stepInstruction);
        }
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIDForListItem;
        boolean isLandscape = viewGroup.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            layoutIDForListItem = R.layout.step_recycler_item_horizontal;
        } else {
            layoutIDForListItem = R.layout.step_recycler_item;
        }
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIDForListItem, viewGroup, false);

        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder stepViewHolder, int position) {
        StepInstruction currentStep = mStepInstructions.get(position);
        stepViewHolder.mStepIdText.setText(currentStep.getmStepNumberString());
        stepViewHolder.mStepDescripText.setText(currentStep.getmShortDescription());

    }

    @Override
    public int getItemCount() {
        if (mStepInstructions == null) return 0;
        return mStepInstructions.size();
    }

    /**
     * Helper method to set StepInstruction List to existing StepAdapter to prevent creating a new one
     *
     * @param stepInstructions the new set of StepInstructions to be displayed
     */

    public void setSteps(List<StepInstruction> stepInstructions) {
        mStepInstructions = stepInstructions;
        notifyDataSetChanged();
    }

    //Interface to handle clicks, defined in MainActivity
    public interface StepClickHandler {
        void onItemClick(StepInstruction stepInstruction);
    }
}
