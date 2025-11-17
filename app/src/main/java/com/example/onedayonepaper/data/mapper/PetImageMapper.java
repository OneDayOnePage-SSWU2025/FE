package com.example.onedayonepaper.data.mapper;

import com.example.onedayonepaper.R;

public class PetImageMapper {
    //아무것도 없을 때 처리 필요
    public static int getCharacterImage(int petType, int totalBook) {
        switch (petType) {
            case 0:
                if (totalBook < 3){
                    return R.drawable.monkey_egg;
                } else if(totalBook < 5){
                    return R.drawable.monkey_baby;
                } else if (totalBook < 7 ) {
                    return R.drawable.monkey_kids;
                } else {
                    return R.drawable.monkey_adult;
                }
            case 1:
                if (totalBook < 3){
                    return R.drawable.cat_egg;
                } else if(totalBook < 5){
                    return R.drawable.cat_baby;
                } else if (totalBook < 7 ) {
                    return R.drawable.cat_kids;
                } else {
                    return R.drawable.cat_adult;
                }
            case 2:
                if (totalBook < 3){
                    return R.drawable.rabbit_egg;
                } else if(totalBook < 5){
                    return R.drawable.rabbit_baby;
                } else if (totalBook < 7 ) {
                    return R.drawable.rabbit_kids;
                } else {
                    return R.drawable.rabbit_adult;
                }
            default:
                return R.drawable.monkey_adult;
        }
    }
}
