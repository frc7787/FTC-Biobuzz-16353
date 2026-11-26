package org.firstinspires.ftc.teamcode.control

import com.pedropathing.follower.Follower
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.commands.Commands.instant
import com.pedropathing.ivy.groups.Groups.sequential
import com.pedropathing.ivy.pedro.PedroCommands.follow
import com.pedropathing.paths.Path
import org.firstinspires.ftc.teamcode.Robot
import org.firstinspires.ftc.teamcode.subsystems.Flywheel


fun shoot(flywheel: Flywheel) = instant{flywheel.on()}

fun IntakeShoot(robot: Robot, toIntake: Path, toShoot: Path, follower: Follower): Command {
    return sequential(follow(follower, toIntake),
        robot.intake.intake(),
        follow(follower, toShoot),
        shoot(robot.flywheel))
}