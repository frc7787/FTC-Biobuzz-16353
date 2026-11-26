package org.firstinspires.ftc.teamcode.control

import com.pedropathing.ivy.Command
import com.pedropathing.ivy.commands.Commands
import com.pedropathing.ivy.commands.Commands.waitMs
import com.pedropathing.ivy.groups.Groups.parallel
import com.pedropathing.ivy.groups.Groups.sequential
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.subsystems.Flywheel

val timer = ElapsedTime()
fun shootThree(flywheel: Flywheel, velocity: Double): Command = Command.build()
    .setStart {flywheel.runWithVelocity(velocity); timer.reset()}
    .setExecute(flywheel::periodic)
    .until{timer.milliseconds() > 1000}
    .setEnd {flywheel.off}
    .requiring(flywheel)

fun otherShootThree(flywheel: Flywheel, velocity: Double): Command =
    flywheel.shootThree(velocity).with(flywheel.periodic())
