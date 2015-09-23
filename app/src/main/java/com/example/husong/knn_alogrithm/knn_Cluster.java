package com.example.husong.knn_alogrithm;

import java.util.ArrayList;

/**
 * Created by husong on 2015/9/15.
 * 分类器
 */

public class knn_Cluster {

    //分类器名称
    private String clusterName;
    //分类器中的点集合
    private ArrayList<knn_Node> KnnPoints = new ArrayList<knn_Node>();

    public knn_Cluster(String clusterName, ArrayList<knn_Node> knnPoints) {
        this.clusterName = clusterName;
        KnnPoints = knnPoints;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public ArrayList<knn_Node> getKnnPoints() {
        return KnnPoints;
    }

    public void setKnnPoints(ArrayList<knn_Node> knnPoints) {
        KnnPoints = knnPoints;
    }
}
