package pt.selfgym.dtos;

public class SetsDTO {
    private Long exerciseSetId;
    private int variable; //serves for both duration and reps
    private int rest;
    private double weight;
    private int order_set;

    public SetsDTO()
    {
        
    }
    public SetsDTO( int variable, int rest, double weight, int order_set) {
        this.variable = variable;
        this.rest = rest;
        this.weight = weight;
        this.order_set = order_set;
    }

    public Long getId() {
        return exerciseSetId;
    }

    public void setId(Long id) {
        this.exerciseSetId = id;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getOrder_set() {
        return order_set;
    }

    public void setOrder_set(int order_set) {
        this.order_set = order_set;
    }

    public int getVariable() {
        return variable;
    }

    public void setVariable(int variable) {
        this.variable = variable;
    }
}
