package com.iti.mansoura.tot.easytripplanner.trip.add;

public interface AddTripContract {

    interface IAddTripView{
        void initComponent();
        void showMessage(String message);
    }

    interface IAddTripPresenter{
        /**
         * Validate Trip data before saving
         *
         * @param data tripName , tripDate
         * @param source sourceLocationName , lat , long
         * @param dest destinationLocationName , lat , long
         * @param notes user trip notes
         */
        void tripProcess(String [] data , String [] source , String [] dest , String [] notes);
    }
}
