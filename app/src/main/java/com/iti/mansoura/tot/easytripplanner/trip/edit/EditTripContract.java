package com.iti.mansoura.tot.easytripplanner.trip.edit;

public interface EditTripContract {

    interface IEditTripView{
        void initComponent();
        void showMessage(String message);
    }

    interface IEditTripPresenter{
        /**
         * Validate Trip data before saving
         *
         * @param data tripName , tripDate
         * @param source sourceLocationName , lat , long
         * @param dest destinationLocationName , lat , long
         * @param notes user trip notes
         */
        void upcomingTripProcess(String[] data, String[] source, String[] dest, String[] notes,String tripUID,String firebaseUID);

        void historyTripProcess(String[] data, String[] source, String[] dest, String[] notes);
    }
}
