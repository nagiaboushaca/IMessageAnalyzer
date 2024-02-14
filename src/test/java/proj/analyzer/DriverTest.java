package proj.analyzer;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import proj.analyzer.Driver;

/**
 * tester for the driver
 */
class DriverTest {

  /**
   * tester for the driver
   */
  @Test
  public void driverTest() {
    Driver d = new Driver();
    String[] args2 = new String[]{"i"};
    assertThrows(
        IllegalArgumentException.class,
        () -> Driver.main(args2)
    );
  }


}