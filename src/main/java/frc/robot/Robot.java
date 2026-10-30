// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.DutyCycleOut;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // Replace 1 and 2 with the actual CAN IDs when we get to testing
  private final TalonFX motorA = new TalonFX(1);
  private final TalonFX motorB = new TalonFX(2);
  
  private final DutyCycleOut m_motorOutput = new DutyCycleOut(0.0);

  private final CommandXboxController m_driverController = new CommandXboxController(0);

  /** ad 
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    m_driverController.y().onTrue(
        new RunCommand(() -> {motorA.setControl(m_motorOutput.withOutput(0.08)); motorB.setControl(m_motorOutput.withOutput(0.08));}).withTimeout(3.0).andThen(new InstantCommand(this::stopMotors))
    );

    m_driverController.x().onTrue(
        new RunCommand(() -> {
          motorA.setControl(m_motorOutput.withOutput(0.08));
          motorB.setControl(m_motorOutput.withOutput(0.08));
        })
        .withTimeout(5.0)
        .andThen(new InstantCommand(this::stopMotors))
    );

    m_driverController.b().onTrue(
        new InstantCommand(this::stopMotors)
    );
  }

  private void stopMotors() {
    motorA.setControl(m_motorOutput.withOutput(0.0));
    motorB.setControl(m_motorOutput.withOutput(0.0));
  }

  /**
   * This function is called every 20 ms, no matter the mode.
   */
  @Override
  public void robotPeriodic() {
    // Processes all active 2026 command lifecycles and controller events
    CommandScheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        break;
      case kDefaultAuto:
      default:
        break;
    }
  }

  @Override
  public void teleopInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void disabledInit() {
    CommandScheduler.getInstance().cancelAll();
    stopMotors();
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
