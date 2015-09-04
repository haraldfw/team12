import lejos.nxt.*;
import lejos.robotics.subsumption.*;

class Hitra	{


	public static void main(String[] args) throws Exception {

		final TouchSensor trykk = new TouchSensor(SensorPort.S1);
		final TouchSensor trykk1 = new TouchSensor(SensorPort.S2);
		final LightSensor lys = new LightSensor(SensorPort.S3);
		final SoundSensor lyd = new SoundSensor(SensorPort.S4);
		int lysgrense = lys.readValue();

		Motor.A.setSpeed(200);
		Motor.B.setSpeed(200);
		Motor.C.setSpeed(500);

		lys.setFloodlight(true);

		for(int i = 0; i <10;){
			Motor.A.forward();
			Motor.B.forward();

			if(trykk.isPressed() || trykk1.isPressed()){
				i++;
				Sound.buzz();
				Motor.A.backward();
				Motor.B.backward();
				Thread.sleep(1000);

				Motor.A.forward();
				Motor.B.backward();
				Thread.sleep(1000);

			} else if(lysgrense < 40){
				i++;
				Sound.buzz();
				Motor.A.backward();
				Motor.B.backward();
				Thread.sleep(1000);

				Motor.A.forward();
				Motor.B.backward();
				Thread.sleep(1000);
			}
		}
	}
}
