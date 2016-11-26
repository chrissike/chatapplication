package edu.hm.dako.chat.client.data.util;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.hm.dako.chat.client.data.util.ModelCalculator;


public class ModelCalculatorTest {

	List<Double> list;

	@Before
	public void initTest() {
		list = new ArrayList<Double>();
		list.add(1.1);
		list.add(2.0);
		list.add(2.9);
	}
	
	@Test
	public void testCalcAverageOfDouble() throws Exception {
		ModelCalculator calc = new ModelCalculator();
		Double average = calc.calcAverageOfDouble(list);
		assertTrue(average.equals(2.0D));
	}

	
}
