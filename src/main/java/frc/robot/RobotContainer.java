package frc.robot;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.epilogue.NotLogged;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.forge.Diagnostics;
import frc.robot.hardware.BenchWiring;
import frc.robot.hardware.Unit;
import frc.robot.subsystems.BenchStand;

/**
 * The subsystems of this unit, built from its hardware map, and the controls bound to them. This
 * repository is the electronics bench device only (heartbeat LED and lamp); the elevator stand's
 * code lives in VCHSRobots/EL-Test-Using-Forge-2026.
 */
@Logged
public class RobotContainer {
  /**
   * The Driver Station port that Forge's virtual Xbox pad is read from. Nothing is bound to it
   * here; it is kept so a person's controller goes on another port, as on the other stands.
   */
  public static final int FORGE_PAD_PORT = 0;

  final Unit unit;
  final CommandXboxController lampPad = new CommandXboxController(BenchWiring.LAMP_PAD_PORT);
  final BenchStand bench;

  @NotLogged private final Diagnostics diag = new Diagnostics();

  public RobotContainer(Unit unit) {
    this.unit = unit;
    bench = new BenchStand();
    configureBindings();
  }

  /** Read-backs on /Forge/Diag, once per robot cycle. */
  public void publishDiagnostics() {
    diag.publishRobot();
    diag.publishPad(FORGE_PAD_PORT, "Forge");
    diag.publishPad(BenchWiring.LAMP_PAD_PORT, "Lamp");
  }

  private void configureBindings() {
    // The lamp: right trigger sets the lamp brightness (0 to 1); hold A for the automatic sweep.
    bench.setDefaultCommand(bench.joystickLamp(lampPad::getRightTriggerAxis));
    lampPad.a().whileTrue(bench.sweepLamp());
  }
}
