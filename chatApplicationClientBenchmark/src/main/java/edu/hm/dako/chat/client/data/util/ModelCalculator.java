package edu.hm.dako.chat.client.data.util;

import java.util.Collections;
import java.util.List;

public class ModelCalculator {

	public Double calcAverageOfDouble(List<Double> list) {
		if(list != null) {
			if(!list.isEmpty()){
				Double sum = 0.0d;
				for (Double d : list)
					sum += d;
				return sum / list.size();							
			}
		}
		return 0.0;
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

	public Double getMaxOfList(List<Double> list) {
		return Collections.max(list);
	}
	
	public Double getMinOfList(List<Double> list) {
		return Collections.min(list);
	}
	
	
	public Double getMinOfDoubleList(List<Double> list) {
		Double temp = 0.0;
		if (list != null) {
			if (!list.isEmpty()) {
				temp = list.get(0);
				for (Double a : list) {
					if (a < temp) {
						temp = a;
					}
				}
			}
		}
		return temp;
	}
	
	public Integer getMinOfIntegerList(List<Integer> list) {
		Integer temp = 0;
		if (list != null) {
			if (!list.isEmpty()) {
				temp = list.get(0);
				for (Integer a : list) {
					if (a < temp) {
						temp = a;
					}
				}
			}
		}
		return temp;
	}
}
