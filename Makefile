.PHONY: test run simulator smoke
test:
	mvn -q test
run:
	mvn -pl hes-server -am spring-boot:run
simulator:
	mvn -pl hes-agent-simulator -am exec:java -Dexec.args="HES-SIM-001 http://localhost:8080"
smoke:
	pwsh -File scripts/smoke.ps1
