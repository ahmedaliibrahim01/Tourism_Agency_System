package business;

import dao.FacilityDAO;
import entity.Facility;
import entity.Hotel;

import java.util.ArrayList;

// Manages operations related to facilities
public class FacilityManager {
    private FacilityDAO facilityDao;

    public FacilityManager() {
        this.facilityDao = new FacilityDAO();
    }

    // Retrieves all facilities
    public ArrayList<Facility> findAll() {
        return this.facilityDao.findAll();
    }

    // Retrieves facilities of a specific hotel by ID
    public ArrayList<String> getHotelFacilities(int hotelId){
        return this.facilityDao.getHotelFacilities(hotelId);
    }

    // Provides information necessary for the table for hotel facilities
    public ArrayList<Object[]> getForTableHotelFacility(int size, int hotelId) {
        ArrayList<Object[]> facilityRowList = new ArrayList<>();
        for (String facility : this.getHotelFacilities(hotelId)) {
            Object[] rowObject = new Object[size];
            rowObject[0] = facility;
            facilityRowList.add(rowObject);
        }
        return facilityRowList;
    }

    // Provides information necessary for the table for general facilities
    public ArrayList<Object[]> getForTableFacilities(int size) {
        ArrayList<Object[]> facilityRowList = new ArrayList<>();
        for (Facility obj : this.findAll()) {
            int i = 0;
            Object[] rowObject = new Object[size];
            rowObject[i++] = obj.getName();
            facilityRowList.add(rowObject);
        }
        return facilityRowList;
    }
}
