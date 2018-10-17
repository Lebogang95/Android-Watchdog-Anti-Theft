package za.co.lbnkosi.watchdog.utils;

public class Model1 {

    private String main_header,secondary_header;
    private int banner_image_1;

    public Model1(String main_header, String secondary_header, int banner_image){

        this.main_header = main_header;
        this.secondary_header = secondary_header;
        this.banner_image_1 = banner_image;

    }

    public String getMain_header() {
        return main_header;
    }

    public String getSecondary_header(){
        return secondary_header;
    }

    public int getBanner_image() {return banner_image_1;}

}
