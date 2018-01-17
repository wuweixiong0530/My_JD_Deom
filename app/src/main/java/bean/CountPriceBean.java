package bean;

/**
 * Created by Administrator on 2018/1/12,0012.
 */

public class CountPriceBean {
    private String priceString;
    private int count;

    public CountPriceBean(String priceString, int count) {
        this.priceString = priceString;
        this.count = count;
    }

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
