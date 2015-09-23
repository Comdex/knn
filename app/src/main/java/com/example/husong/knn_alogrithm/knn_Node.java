package com.example.husong.knn_alogrithm;

/**
 * Created by husong on 2015/9/15.
 */
public class knn_Node {
    //点的id 和 加速度值
    private int index;
    private double dimension[];

    //点的最小距离和所属分类
    private double min_distance;
    private String classification;


    public knn_Node(int index, double[] dimension) {
        this.index = index;
        this.dimension = dimension;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double[] getDimension() {
        return dimension;
    }

    public void setDimension(double[] dimension) {
        this.dimension = dimension;
    }

    public double getMin_distance() {
        return min_distance;
    }

    public void setMin_distance(double min_distance) {
        this.min_distance = min_distance;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
