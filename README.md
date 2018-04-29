Design Pattern Evolution tracking tool.

To use:

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
5) Set the configs and run Main.java.




<br>
Configs:
Because this tool is still in early stages, configs are being handled inside the source code, 
and specifically in the Main.java class. The default configs should provide enough information
to fill in your own configs.

_workingDirectory_: base directory for the entire project

_projectID_: identifier for each project under analysis

_interVersionKey_: link to location of a project

_interProjectKey_: link to location of source code files for a given project

_projectLanguage_: file extensions the code crawler will look for

_testProject_: temporary config used while building and testing the tool, refers to the project version. Output from srcML will go to a directory named _projectID_ + _testProject_

</br>


<br>
Currently working on:

Generating UML class diagrams from srcML output. Progress ~ 100%

Generating UML sequence diagrams from srcML output. Progress ~ 80%

Transforming UML data model to PlantUML. Progress ~ 50% (don't know if I will do sequence diagram generation)

Implementing design pattern specifications in UML ~ 12% (State pattern first target.)

conformance checking algos (Structural ~ 100%) (Behavioral ~ 50%)

Linking visualizer to UML segments ~ 0% (deciding against visualizer until I get results)

</br>