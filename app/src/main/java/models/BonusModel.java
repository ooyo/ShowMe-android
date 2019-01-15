package models;

/**
 * Created by appleuser on 10/29/16.
 */

public class BonusModel {
    private String bonus_place_id;
    private String bonus_place;
    private String bonus_description;
    private String bonus_end;
    private String category;

    public BonusModel(String bonus_place_id, String bonus_place, String bonus_description, String bonus_end, String category) {
        this.bonus_place_id = bonus_place_id;
        this.bonus_place = bonus_place;
        this.bonus_description = bonus_description;
        this.bonus_end = bonus_end;
        this.category = category;
    }

    public String getBonus_place_id() {
        return bonus_place_id;
    }

    public void setBonus_place_id(String bonus_place_id) {
        this.bonus_place_id = bonus_place_id;
    }

    public String getBonus_place() {
        return bonus_place;
    }

    public void setBonus_place(String bonus_place) {
        this.bonus_place = bonus_place;
    }

    public String getBonus_description() {
        return bonus_description;
    }

    public void setBonus_description(String bonus_description) {
        this.bonus_description = bonus_description;
    }

    public String getBonus_end() {
        return bonus_end;
    }

    public void setBonus_end(String bonus_end) {
        this.bonus_end = bonus_end;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
