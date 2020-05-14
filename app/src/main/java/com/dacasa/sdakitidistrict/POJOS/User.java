package com.dacasa.sdakitidistrict.POJOS;


/**.
 */
public class User {


    private String parish,church,district,position;

    public User() {
    }


    public User(String uid, String username, String email, String phone, String gender, String photo_url, String parish, String church,String district, String position) {

        this.district = district;
        this.parish = parish;
        this.church = church;
        this.position = position;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }




    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public String getChurch() {
        return church;
    }

    public void setChurch(String church) {
        this.church = church;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "User{" +
                ", parish='" + parish + '\'' +
                ", church='" + church + '\'' +
                ", district='" + district + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
