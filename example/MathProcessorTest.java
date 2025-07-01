package example;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.easymock.*;

public class MathProcessorTest {

    @Test
    @DisplayName("Strict Mock")
    void testAddAndLog_StrictMock() {
        MathService mock = EasyMock.createStrictMock(MathService.class);
        MathProcessor processor = new MathProcessor(mock);

        EasyMock.expect(mock.add(2, 3)).andReturn(5);
        mock.log("Addition result: 5");

        EasyMock.replay(mock);

        int result = processor.addAndLog(2, 3);
        assertEquals(5, result);

        EasyMock.verify(mock);
    }

    @Test
    @DisplayName("Nice Mock")
    void testNiceMock_IgnoresUncalledMethods() {
        MathService mock = EasyMock.createNiceMock(MathService.class);
        MathProcessor processor = new MathProcessor(mock);

        EasyMock.expect(mock.add(4, 6)).andReturn(10);
        EasyMock.replay(mock);

        int result = processor.addAndLog(4, 6);
        assertEquals(10, result);

        EasyMock.verify(mock);
    }

    @Test
    @DisplayName("Exception Handling")
    void testSafeDivide_ThrowsException_UsingAndThrow() {
        MathService mock = EasyMock.createMock(MathService.class);
        MathProcessor processor = new MathProcessor(mock);

        mock.log("Division by zero");
        EasyMock.expectLastCall().once();

        EasyMock.replay(mock);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processor.safeDivide(10, 0);
        });

        assertEquals("Cannot divide by zero", exception.getMessage());

        EasyMock.verify(mock);
    }

    @Test
    @DisplayName("Call Variants")
    void testCallCounts() {
        MathService mock = EasyMock.createMock(MathService.class);
        MathProcessor processor = new MathProcessor(mock);

        EasyMock.expect(mock.add(1, 1)).andReturn(2).times(2);
        EasyMock.expect(mock.divide(10, 2)).andReturn(5).atLeastOnce();

        mock.log("Addition result: 2");
        EasyMock.expectLastCall().times(2);

        mock.log("Done");
        EasyMock.expectLastCall().anyTimes();

        EasyMock.replay(mock);

        assertEquals(2, processor.addAndLog(1, 1));
        assertEquals(2, processor.addAndLog(1, 1));
        assertEquals(5, processor.safeDivide(10, 2));
        mock.log("Done");

        EasyMock.verify(mock);
    }
}