
main: libs/Jama-1.0.3.jar out/Assignment7.class out/AssignmentConfig.class out/Simulator.class out/TwoLayerNetwork.class out/Feature.class
	java -cp out:./libs/Jama-1.0.3.jar Assignment7

clean:
	rm out/*.class
	rmdir out

reset: 
	rm libs/*.jar
	rmdir libs
	rm out/*.class
	rmdir out

out/Assignment7.class: Assignment7.java
	javac -d out -cp .:./libs/Jama-1.0.3.jar Assignment7.java

out/AssignmentConfig.class: AssignmentConfig.java
	javac -d out AssignmentConfig.java

out/Simulator.class: Simulator.java
	javac -d out -cp .:./libs/Jama-1.0.3.jar Simulator.java

out/Feature.class: Feature.java
	javac -d out -cp .:./libs/Jama-1.0.3.jar Feature.java

out/TwoLayerNetwork.class: TwoLayerNetwork.java
	javac -d out -cp .:./libs/Jama-1.0.3.jar TwoLayerNetwork.java

libs/Jama-1.0.3.jar:
	wget -P libs https://math.nist.gov/javanumerics/jama/Jama-1.0.3.jar

