package ar.com.meli.entrevista.dto;

import java.util.ArrayList;
import java.util.List;

public class Response {

    public Response() {
        this.item_ids = new ArrayList<>();
        this.total = 0f;
    }

    public Response(List<String> item_ids, Float total) {
        this.item_ids = item_ids;
        this.total = total;
    }

    private List<String> item_ids;
    private Float total;

    public List<String> getItem_ids() {
        return item_ids;
    }

    public void setItem_ids(List<String> item_ids) {
        this.item_ids = item_ids;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

}
