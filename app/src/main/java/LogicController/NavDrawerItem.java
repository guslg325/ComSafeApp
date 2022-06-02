package LogicController;

public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private int imgId;
    public NavDrawerItem() {
    }
    public NavDrawerItem(boolean showNotify, String title, int imgId) {
        this.showNotify = showNotify;
        this.title = title;
        this.imgId = imgId;
    }
    public boolean isShowNotify() {
        return showNotify;
    }
    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getImgId(){
        return imgId;
    }
    public void setImgId(int imgId){
        this.imgId = imgId;
    }
}
