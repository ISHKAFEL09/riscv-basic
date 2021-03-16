name := "ChiselPrj"

version := "0.1"
val spinalVersion = "1.4.0"
scalaVersion := "2.12.1"

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)
libraryDependencies += "edu.berkeley.cs" % "chisel3_2.12" % "3.4.2"
//libraryDependencies += "edu.berkeley.cs" % "chisel-testers2_2.12" % "0.2.2"
libraryDependencies += "edu.berkeley.cs" % "chiseltest_2.12" % "0.3.2"
libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.3.0-SNAP2" % "test"
libraryDependencies += compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
//libraryDependencies += "com.github.spinalhdl" % "spinalhdl-core_2.12" % spinalVersion
//libraryDependencies += "com.github.spinalhdl" % "spinalhdl-lib_2.12" % spinalVersion
//libraryDependencies += compilerPlugin("com.github.spinalhdl" % "spinalhdl-idsl-plugin_2.12" % spinalVersion)

def scalacOptionsVersion(scalaVersion: String): Seq[String] = {
  Seq() ++ {
    // If we're building with Scala > 2.11, enable the compile option
    //  switch to support our anonymous Bundle definitions:
    //  https://github.com/scala/bug/issues/10047
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, scalaMajor: Long)) if scalaMajor < 12 => Seq()
      case _ => Seq("-Xsource:2.11")
    }
  }
}

scalacOptions ++= scalacOptionsVersion(scalaVersion.value)
fork := true