package edu.hm.dako.chat.client.data.util;

import java.util.List;

public class ModelCalculator {

	public Double calcAverageOfDouble(List<Double> list) {
		Double sum = 0.0d;
		for (Double d : list)
			sum += d;
		return sum / list.size();
	}

	public Double getStdDev(List<Double> list, Double avg) {
		return Math.sqrt(getVariance(list, avg));
	}

	private Double getVariance(List<Double> list, Double avg) {
		Double temp = 0.0;
		for (Double a : list)
			temp += (a - avg) * (a - avg);
		return temp / list.size();
	}
}