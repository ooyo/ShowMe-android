package profiletab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import detail.EventDetailActivity;
import models.BonusModel;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by appleuser on 10/29/16.
 */

public class BonusAdapter extends RecyclerView.Adapter<BonusAdapter.MyViewHolder> {

    private Context mContext;
    private Globals globals;
    private List<BonusModel> albumList;
    private BonusAdapter followUserAdapter;
    private List<BonusModel> followUserList;
    public TextView bonusUser, bonusDescription;
    public RelativeLayout bonusBG;
    public CardView bonusCard;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);
            bonusCard = (CardView) view.findViewById(R.id.eventDetailBonusCard);
            bonusUser = (TextView) view.findViewById(R.id.eventDetailBonusUser);
            bonusDescription = (TextView) view.findViewById(R.id.eventDetailBonusDescription);
            bonusBG = (RelativeLayout) view.findViewById(R.id.eventDetailBonusBG);

            globals = new Globals();
            bonusUser.setTypeface(globals.robotoLight(mContext));
            bonusUser.setTextSize(16);

            bonusDescription.setTypeface(globals.robotoLight(mContext));
            bonusDescription.setTextSize(20);
        }
    }


    public BonusAdapter(Context mContext, List<BonusModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;

    }

    @Override
    public BonusAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bonus_card, parent, false);

        return new BonusAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BonusAdapter.MyViewHolder holder, int position) {
        final BonusModel bonus = albumList.get(position);

        //holder.cardTitle.setText(event.getTitle());
        //holder.cardLike.setText(event.getLike());
        if(bonus.getCategory().equalsIgnoreCase("Activity")){
            bonusBG.setBackgroundResource(R.color.colorActivity);
        }else if(bonus.getCategory().equalsIgnoreCase("Coffee Shop")){
            bonusBG.setBackgroundResource(R.color.colorCoffee);
        }else if(bonus.getCategory().equalsIgnoreCase("Lounge")){
            bonusBG.setBackgroundResource(R.color.colorLounge);
        }else if(bonus.getCategory().equalsIgnoreCase("Mall")){
            bonusBG.setBackgroundResource(R.color.colorMall);
        }else if(bonus.getCategory().equalsIgnoreCase("Night Club")){
            bonusBG.setBackgroundResource(R.color.colorNight);
        }else if(bonus.getCategory().equalsIgnoreCase("Pub")){
            bonusBG.setBackgroundResource(R.color.colorPub);
        }else if(bonus.getCategory().equalsIgnoreCase("Restaurant")){
            bonusBG.setBackgroundResource(R.color.colorRestaurant);
        }else if(bonus.getCategory().equalsIgnoreCase("Shop")){
            bonusBG.setBackgroundResource(R.color.colorShop);
        }else if(bonus.getCategory().equalsIgnoreCase("Theatre")){
            bonusBG.setBackgroundResource(R.color.colorTheatre);
        }else{
            bonusBG.setBackgroundResource(R.color.colorPrimary);
        }

        bonusUser.setText(bonus.getBonus_place());
        bonusDescription.setText(bonus.getBonus_description());
    }



    @Override
    public int getItemCount() {
        return albumList.size();
    }


}
