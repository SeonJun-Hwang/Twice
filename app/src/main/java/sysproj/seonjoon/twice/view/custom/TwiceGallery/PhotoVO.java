package sysproj.seonjoon.twice.view.custom.TwiceGallery;

class PhotoVO {

    private String imgPath;
    private boolean selected;

    PhotoVO(String imgPath, boolean selected) {
        this.imgPath = imgPath;
        this.selected = selected;
    }

    String getImgPath() {
        return imgPath;
    }

    boolean isSelected() {
        return selected;
    }

    void setSelected(boolean selected) {
        this.selected = selected;
    }

}
