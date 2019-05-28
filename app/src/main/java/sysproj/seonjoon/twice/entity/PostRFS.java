package sysproj.seonjoon.twice.entity;

public class PostRFS {
    private int repleCount;
    private int favoriteCount;
    private int sharedCount;

    public PostRFS(){
        repleCount = favoriteCount = sharedCount = 0;
    }

    public PostRFS(int repleCount, int favoriteCount, int sharedCount){
        this.repleCount = repleCount;
        this.favoriteCount = favoriteCount;
        this.sharedCount = sharedCount;
    }

    public int getRepleCount() {
        return repleCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public int getSharedCount() {
        return sharedCount;
    }
}
