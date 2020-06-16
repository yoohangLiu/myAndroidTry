package model;

public class Category {
    private int categoryId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCatergory() {
        return catergory;
    }

    public void setCatergory(String catergory) {
        this.catergory = catergory;
    }

    private String catergory;
    public Category(Integer id,String catergory) {
        this.categoryId=id;
        this.catergory = catergory;
    }
    public Category(String catergory) {
        this.catergory = catergory;
    }
    public Category(){}

}
