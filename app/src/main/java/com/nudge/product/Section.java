package com.nudge.product;
public class Section {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String short_description;
    private String price;
    private String regular_price;
    private String sale_price;
    private String total_sales;
    private String image;
    private  String external_url;
    public Section() {
    }
    public Section(String id, String name,String slug,String description,String short_description, String price,String regular_price,String sale_price,String total_sales,String image,String external_url) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.short_description = short_description;
        this.price = price;
        this.regular_price = regular_price;
        this.sale_price = sale_price;
        this.total_sales = total_sales;
        this.image = image;
        this.external_url = external_url;
    }


    public String getExternal_url() {
        return external_url;
    }

    public void setExternal_url(String external_url) {
        this.external_url = external_url;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getShort_description() {
        return short_description;
    }
    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getRegular_price() {
        return regular_price;
    }
    public void setRegular_price(String regular_price) {
        this.regular_price = regular_price;
    }
    public String getSale_price() {
        return sale_price;
    }
    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }
    public String getTotal_sales() {
        return total_sales;
    }
    public void setTotal_sales(String total_sales) {
        this.total_sales = total_sales;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
