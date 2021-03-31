package ar.com.meli.entrevista.dto;

public class Node {

    private Float upperBound;
    private Float lowerBound;
    private Integer level;
    private Boolean flag;
    private Float totalValue;
    private Float totalWeight;

    public Node() {}

    public Node(Node cpy)
    {
        this.totalValue = cpy.totalValue;
        this.totalWeight = cpy.totalWeight;
        this.upperBound = cpy.upperBound;
        this.lowerBound = cpy.lowerBound;
        this.level = cpy.level;
        this.flag = cpy.flag;
    }

    public Float getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(Float upperBound) {
        this.upperBound = upperBound;
    }

    public Float getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Float lowerBound) {
        this.lowerBound = lowerBound;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Float totalValue) {
        this.totalValue = totalValue;
    }

    public Float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Float totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void inicializate() {
        this.totalValue = 0f;
        this.totalWeight = 0f;
        this.upperBound = 0f;
        this.lowerBound = 0f;
        this.level = 0;
        this.flag = false;
    }

}
