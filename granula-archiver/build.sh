mvn -DskipTests clean package

version=0.1.1
mvn install:install-file -Dfile=target/granula-archiver-$version.jar -DpomFile=pom.xml -Dsources=target/granula-archiver-$version-sources.jar -DgroupId=nl.tudelft.pds.granula -DartifactId=granula-archiver -Dversion=$version -Dpackaging=jar


