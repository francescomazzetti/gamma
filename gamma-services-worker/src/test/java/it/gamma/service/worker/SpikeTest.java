package it.gamma.service.worker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SpikeTest
{
	@Test
	public void test1() {
		String a = "xxx:yyy";
		assertEquals("xxx", a.split(":")[0]);
	}
}
