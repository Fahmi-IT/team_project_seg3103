package example;

public class MathProcessor {
    private final MathService service;

    public MathProcessor(MathService service) {
        this.service = service;
    }

    public int safeDivide(int a, int b) {
        if (b == 0) {
            service.log("Division by zero");
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return service.divide(a, b);
    }

    public int addAndLog(int a, int b) {
        int result = service.add(a, b);
        service.log("Addition result: " + result);
        return result;
    }
}
