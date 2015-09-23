package com.example.husong.knn_alogrithm;

import java.util.ArrayList;

/**
 * Created by husong on 2015/9/15.
 */

public class Classify_Algorithm {
    //分类器集合
    private knn_Cluster knnclusters[];
    //待测试的数据
    private ArrayList<knn_Node> Nodes_to_classify = new ArrayList<knn_Node>();

    public Classify_Algorithm(knn_Cluster[] knnclusters, ArrayList<knn_Node> knnPoints) {
        this.knnclusters = knnclusters;
        Nodes_to_classify = knnPoints;
    }

    //计算各个点到组中点的最近距离
    public void calEuclideanDistanceSum() {

        double temp = 0.0, sum = 0.0;

        //遍历所有测试数据
        for (int i = 0; i < Nodes_to_classify.size(); i++) {
            double[] dims = Nodes_to_classify.get(i).getDimension();
            //尚未分类，设置初始最近距离为无穷大；分类为null;
            Nodes_to_classify.get(i).setMin_distance(Double.MAX_VALUE);
            Nodes_to_classify.get(i).setClassification("null");

            //遍历所有分类器
            for (int n = 0; n < knnclusters.length; n++) {
                knn_Cluster cluster = knnclusters[n];
                ArrayList<knn_Node> Nodes_from_cluster = cluster.getKnnPoints();

                //遍历分类器中所有数据
                for (int k = 0; k < Nodes_from_cluster.size(); k++) {
                    //计算欧氏距离，维度为3
                    for (int j = 0; j < dims.length; j++) {
                        temp = Math.pow(dims[j] - Nodes_from_cluster.get(k).getDimension()[j], 2);
                        sum = sum + temp;
                    }
                    double EuclideanDistanceSum = Math.sqrt(sum);
                    if (EuclideanDistanceSum < Nodes_to_classify.get(i).getMin_distance()) {
                        //更新最短距离和分类
                        Nodes_to_classify.get(i).setMin_distance(EuclideanDistanceSum);
                        Nodes_to_classify.get(i).setClassification(cluster.getClusterName());
                    }
                    sum = 0.0;
                    temp = 0.0;
                }
            }
        }
    }
}
