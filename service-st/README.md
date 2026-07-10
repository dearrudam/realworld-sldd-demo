# service-st

System tests exercise the running service through MicroProfile REST Client, including health, greetings, and users behavior.

To perform black box tests locally against a running service (uses http://localhost:8080 by default):

```
mvn test-compile failsafe:integration-test
```

To test against a remote environment, set the BASE_URI environment variable:

```
export BASE_URI=https://deployed.com
mvn test-compile failsafe:integration-test
```
