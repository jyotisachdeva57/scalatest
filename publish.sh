sbt clean
sbt "project scalactic" clean +publishSigned
export SCALAJS_VERSION=0.6.32
sbt "project scalacticMacroJS" clean
sbt "project scalacticJS" clean +publishSigned
export SCALAJS_VERSION=1.0.1
sbt "project scalacticMacroJS" clean
sbt ++2.11.12 "project scalacticJS" clean publishSigned
sbt ++2.12.11 "project scalacticJS" clean publishSigned
sbt ++2.13.2 "project scalacticJS" clean publishSigned
sbt ++2.11.12 "project scalacticNative" clean publishSigned
sbt "project scalacticDotty" clean publishSigned
sbt "project scalactic" sonatypeBundleUpload

sbt clean
sbt scalatestCompatible/clean scalatestCompatible/publishSigned
sbt "project scalatest" clean +publishSigned
export SCALAJS_VERSION=0.6.32
sbt "project scalatestJS" clean +publishSigned
export SCALAJS_VERSION=1.0.1
sbt ++2.11.12 "project scalatestJS" clean publishSigned
sbt ++2.12.11 "project scalatestJS" clean publishSigned
sbt ++2.13.2 "project scalatestJS" clean publishSigned
sbt ++2.11.12 "project scalatestNative" clean publishSigned
sbt "project scalatestDotty" clean publishSigned

sbt clean
sbt "project scalatestApp" clean +publishSigned
export SCALAJS_VERSION=0.6.32
sbt "project scalatestAppJS" clean +publishSigned
export SCALAJS_VERSION=1.0.1
sbt ++2.11.12 "project scalatestAppJS" clean publishSigned
sbt ++2.12.11 "project scalatestAppJS" clean publishSigned
sbt ++2.13.2 "project scalatestAppJS" clean publishSigned
sbt ++2.11.12 "project scalatestAppNative" clean publishSigned
sbt "project scalatest" sonatypeBundleUpload
