package com.nudge.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ADVANTAL on 11/13/2017.
 */
public class Product {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("downloadable")
    @Expose
    private Boolean downloadable;
    @SerializedName("virtual")
    @Expose
    private Boolean virtual;
    @SerializedName("permalink")
    @Expose
    private String permalink;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("regular_price")
    @Expose
    private String regularPrice;
    @SerializedName("sale_price")
    @Expose
    private Object salePrice;
    @SerializedName("price_html")
    @Expose
    private String priceHtml;
    @SerializedName("taxable")
    @Expose
    private Boolean taxable;
    @SerializedName("tax_status")
    @Expose
    private String taxStatus;
    @SerializedName("tax_class")
    @Expose
    private String taxClass;
    @SerializedName("managing_stock")
    @Expose
    private Boolean managingStock;
    @SerializedName("stock_quantity")
    @Expose
    private Object stockQuantity;
    @SerializedName("in_stock")
    @Expose
    private Boolean inStock;
    @SerializedName("backorders_allowed")
    @Expose
    private Boolean backordersAllowed;
    @SerializedName("backordered")
    @Expose
    private Boolean backordered;
    @SerializedName("sold_individually")
    @Expose
    private Boolean soldIndividually;
    @SerializedName("purchaseable")
    @Expose
    private Boolean purchaseable;
    @SerializedName("featured")
    @Expose
    private Boolean featured;
    @SerializedName("visible")
    @Expose
    private Boolean visible;
    @SerializedName("catalog_visibility")
    @Expose
    private String catalogVisibility;
    @SerializedName("on_sale")
    @Expose
    private Boolean onSale;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("button_text")
    @Expose
    private String buttonText;
    @SerializedName("weight")
    @Expose
    private Object weight;
    @SerializedName("dimensions")
    @Expose
    private Dimensions dimensions;
    @SerializedName("shipping_required")
    @Expose
    private Boolean shippingRequired;
    @SerializedName("shipping_taxable")
    @Expose
    private Boolean shippingTaxable;
    @SerializedName("shipping_class")
    @Expose
    private String shippingClass;
    @SerializedName("shipping_class_id")
    @Expose
    private Object shippingClassId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("reviews_allowed")
    @Expose
    private Boolean reviewsAllowed;
    @SerializedName("average_rating")
    @Expose
    private String averageRating;
    @SerializedName("rating_count")
    @Expose
    private Integer ratingCount;
    @SerializedName("related_ids")
    @Expose
    private List<Integer> relatedIds = null;
    @SerializedName("upsell_ids")
    @Expose
    private List<Object> upsellIds = null;
    @SerializedName("cross_sell_ids")
    @Expose
    private List<Object> crossSellIds = null;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("categories")
    @Expose
    private List<String> categories = null;
    @SerializedName("tags")
    @Expose
    private List<Object> tags = null;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("featured_src")
    @Expose
    private Boolean featuredSrc;
    @SerializedName("attributes")
    @Expose
    private List<Attribute> attributes = null;
    @SerializedName("downloads")
    @Expose
    private List<Object> downloads = null;
    @SerializedName("download_limit")
    @Expose
    private Integer downloadLimit;
    @SerializedName("download_expiry")
    @Expose
    private Integer downloadExpiry;
    @SerializedName("download_type")
    @Expose
    private String downloadType;
    @SerializedName("purchase_note")
    @Expose
    private String purchaseNote;
    @SerializedName("total_sales")
    @Expose
    private Integer totalSales;
    @SerializedName("variations")
    @Expose
    private List<Object> variations = null;
    @SerializedName("parent")
    @Expose
    private List<Object> parent = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDownloadable() {
        return downloadable;
    }

    public void setDownloadable(Boolean downloadable) {
        this.downloadable = downloadable;
    }

    public Boolean getVirtual() {
        return virtual;
    }

    public void setVirtual(Boolean virtual) {
        this.virtual = virtual;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    public Object getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Object salePrice) {
        this.salePrice = salePrice;
    }

    public String getPriceHtml() {
        return priceHtml;
    }

    public void setPriceHtml(String priceHtml) {
        this.priceHtml = priceHtml;
    }

    public Boolean getTaxable() {
        return taxable;
    }

    public void setTaxable(Boolean taxable) {
        this.taxable = taxable;
    }

    public String getTaxStatus() {
        return taxStatus;
    }

    public void setTaxStatus(String taxStatus) {
        this.taxStatus = taxStatus;
    }

    public String getTaxClass() {
        return taxClass;
    }

    public void setTaxClass(String taxClass) {
        this.taxClass = taxClass;
    }

    public Boolean getManagingStock() {
        return managingStock;
    }

    public void setManagingStock(Boolean managingStock) {
        this.managingStock = managingStock;
    }

    public Object getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Object stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public Boolean getBackordersAllowed() {
        return backordersAllowed;
    }

    public void setBackordersAllowed(Boolean backordersAllowed) {
        this.backordersAllowed = backordersAllowed;
    }

    public Boolean getBackordered() {
        return backordered;
    }

    public void setBackordered(Boolean backordered) {
        this.backordered = backordered;
    }

    public Boolean getSoldIndividually() {
        return soldIndividually;
    }

    public void setSoldIndividually(Boolean soldIndividually) {
        this.soldIndividually = soldIndividually;
    }

    public Boolean getPurchaseable() {
        return purchaseable;
    }

    public void setPurchaseable(Boolean purchaseable) {
        this.purchaseable = purchaseable;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getCatalogVisibility() {
        return catalogVisibility;
    }

    public void setCatalogVisibility(String catalogVisibility) {
        this.catalogVisibility = catalogVisibility;
    }

    public Boolean getOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public Boolean getShippingRequired() {
        return shippingRequired;
    }

    public void setShippingRequired(Boolean shippingRequired) {
        this.shippingRequired = shippingRequired;
    }

    public Boolean getShippingTaxable() {
        return shippingTaxable;
    }

    public void setShippingTaxable(Boolean shippingTaxable) {
        this.shippingTaxable = shippingTaxable;
    }

    public String getShippingClass() {
        return shippingClass;
    }

    public void setShippingClass(String shippingClass) {
        this.shippingClass = shippingClass;
    }

    public Object getShippingClassId() {
        return shippingClassId;
    }

    public void setShippingClassId(Object shippingClassId) {
        this.shippingClassId = shippingClassId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Boolean getReviewsAllowed() {
        return reviewsAllowed;
    }

    public void setReviewsAllowed(Boolean reviewsAllowed) {
        this.reviewsAllowed = reviewsAllowed;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public List<Integer> getRelatedIds() {
        return relatedIds;
    }

    public void setRelatedIds(List<Integer> relatedIds) {
        this.relatedIds = relatedIds;
    }

    public List<Object> getUpsellIds() {
        return upsellIds;
    }

    public void setUpsellIds(List<Object> upsellIds) {
        this.upsellIds = upsellIds;
    }

    public List<Object> getCrossSellIds() {
        return crossSellIds;
    }

    public void setCrossSellIds(List<Object> crossSellIds) {
        this.crossSellIds = crossSellIds;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Boolean getFeaturedSrc() {
        return featuredSrc;
    }

    public void setFeaturedSrc(Boolean featuredSrc) {
        this.featuredSrc = featuredSrc;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Object> getDownloads() {
        return downloads;
    }

    public void setDownloads(List<Object> downloads) {
        this.downloads = downloads;
    }

    public Integer getDownloadLimit() {
        return downloadLimit;
    }

    public void setDownloadLimit(Integer downloadLimit) {
        this.downloadLimit = downloadLimit;
    }

    public Integer getDownloadExpiry() {
        return downloadExpiry;
    }

    public void setDownloadExpiry(Integer downloadExpiry) {
        this.downloadExpiry = downloadExpiry;
    }

    public String getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }

    public String getPurchaseNote() {
        return purchaseNote;
    }

    public void setPurchaseNote(String purchaseNote) {
        this.purchaseNote = purchaseNote;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }

    public List<Object> getVariations() {
        return variations;
    }

    public void setVariations(List<Object> variations) {
        this.variations = variations;
    }

    public List<Object> getParent() {
        return parent;
    }

    public void setParent(List<Object> parent) {
        this.parent = parent;
    }
}
