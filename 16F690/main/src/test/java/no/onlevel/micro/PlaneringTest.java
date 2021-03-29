package no.onlevel.micro;


import no.onlevel.micro16F690.programs.PlaneringMicro;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PlaneringTest {
	List<String> code;
	PlaneringMicro planering;
	
	@Before
	public void init(){
		planering = new PlaneringMicro();
	}

	@Test
	public void lagAssemblyTest() {
		planering.printProgram().forEach(line -> {
			System.out.println(line);
		});
	}
	
}
