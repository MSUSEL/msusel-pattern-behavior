Design Pattern Evolution tracking tool.

To use (pre-built):
1) Run the jar (in the target/ directory) as 'java -jar pattern-analysis-1.0-SNAPSHOT-jar-with-dependencies.jar'

To use (manual-build):

1) Download multiple versions of a software project to be analyzed, 
both the runnable version (jar is what this tool supports now) and the src files.
2) Run the design pattern detection tool built by  Tsantalis et al., found
[here](https://users.encs.concordia.ca/~nikolaos/pattern_detection.html), on each
version of the project under analysis. Note that the tool only runs on compiled 
files, so you must extract the .class files from the runnable jar. 
3) After the tool has finished identifying design patterns, export the output as
an xml file to a common directory named "pattern_detections/" under the working directory. Because we are analyzing
multiple versions of a project, it is helpful to name each xml file "pattern_detector{versionnNum}".
4) install srcML, a tool that decorates source code with xml, built by Collard and Maletic. 
srcML can be found [here](http://www.srcml.org/). If using Windows on a 64-bit machine, the executable for 
 srcML is located in the resources/ directory. Make sure you include srcML on your system's PATH. 
5) Set the configs and run the command 'mvnw.cmd package', with maven installed of course. This will package the project 
as a jar. Run the jar as 'java -jar pattern-analysis-1.0-SNAPSHOT-jar-with-dependencies.jar'




<br>
Configs:
Refer to config.properties for information regarding configs.
</br>


<br>
Currently working on:

Generating UML class diagrams from srcML output. Progress ~ 100%

Generating UML sequence diagrams from srcML output. Progress ~ 100%

Transforming UML data model to PlantUML. Progress ~ 50% (don't know if I will do sequence diagram generation)

Implementing SPS design pattern specifications in UML ~ 100% for now (State, Object Adapter, Template Method, Singleton, Observer)

Implementing IPS design pattern specifications in UML ~ 100% for now (State, Object Adapter, Template Method, Singleton, Observer)

conformance checking algos (Structural ~ 100%) (Behavioral ~ 100%)

Linking visualizer to UML segments ~ 0% (deciding against visualizer until I get results)

Metric suite ~ 100% for now, including:

Project_ID, Software_Version, Pattern_Type, Pattern_ID, Num_Participating_Classes, Num_Conforming_Structural_Roles, Num_NonConforming_Structural_Roles, Num_Conforming_Behavioral_Roles, Num_NonConforming_Behavioral_Roles, Num_Conforming_Roles_Total, Num_NonConforming_Roles_Total, SSize2, Afferent_Coupling, Efferent_Coupling, Coupling_Between_Pattern_Classes, Pattern_Structural_Integrity, Pattern_Behavioral_Integrity, Pattern_Integrity, Pattern_Instability</br>
