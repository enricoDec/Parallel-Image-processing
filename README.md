# Parallel-Image-processing
The aim of this project is to convert PNG or JPG files from the RGB format into three other output formats using parallelization:
1. A grayscale image
2. A histogram
3. An image with altered brightness

Originally, the project was intended to compare the performance differences between these conversions with and without the use of OpenMP. However, after consultation with Professor Dr. Nikita Kovalenko, it was agreed to solve the task using the Java internal Runnable interface. The complete implementation can be found on GitHub.

Two variants, a Blocking and a Non-Blocking version, were utilized for the implementation, and both will be compared later. The image file is read from shared memory, which is a thread-safe approach, eliminating the need for additional synchronization. Each thread processes its own row of pixels. In the Blocking version, shared memory is used, and individual threads write their results directly and in a synchronized manner. This prevents race conditions and reduces overhead when retrieving results after execution. However, this approach also leads to increased thread runtime because all threads can calculate in parallel but cannot write simultaneously. In the Non-Blocking version, each thread has its own memory for calculated values, avoiding race conditions as well. Once all threads have completed the calculations, the results are merged. However, additional time is required for result consolidation. The entire parallelization and synchronization are facilitated by the Java "Runnable" interface, and the code to be executed by the threads is simply written within the run method.

# Documentation (German)
[Bericht](Bericht.pdf)   
[Pres Folien](Algo_Aufgabe_04_Präsentation.pdf)

# ToC
1. [Converting an Image to Greyscale](#converting-an-image-to-greyscale)
2. [Changing Image Brightness](#changing-image-brightness)
3. [Creating Image RGB Histogram](#creating-image-rgb-histogram)
4. [Project](#project)
5. [Benchmarks](#benchmarks)
6. [Logger](#logger)

# Converting an Image to Greyscale

### Example:

```java
ParallelImageProcessor processor = new GreyScaleProcessor(2);
ProcessorResult result = processor.processImage(file);
BufferedImage image = result.getImage();
```

### Short version:

```java
ParallelImageProcessor processor = new GreyScaleProcessor(2);
BufferedImage image = processor.processImage(file).getImage();
```

| <img width="600" src="src/main/resources/images/original/animal/2.kitten_medium.jpg" alt="Original Image"/> | <img width="600" src="src/main/resources/images/greyscale/kitten.png" alt="Original Image in Greyscale"/> |
| :---------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------: |
|                                             **Original Image**                                              |                                      **Original Image in Greyscale**                                      |

# Changing Image Brightness

### Example:

```java
ParallelImageProcessor processor = new BrightnessProcessor(2, 2.0);
ProcessorResult result = processor.processImage(file);
BufferedImage image = result.getImage();
```

| <img width="600" src="src/main/resources/images/original/animal/2.kitten_medium.jpg" alt="Original Image"/> | <img width="600" src="src/main/resources/images/brightness/kitten.png" alt="Original Image with increased brightness"/> |
| :---------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------: |
|                                             **Original Image**                                              |                                      **Original Image with increased brightness**                                       |

# Creating Image RGB Histogram

### Example:

```java
ParallelImageProcessor processor = new HistogramProcessor(2);
ProcessorResult result = processor.processImage(file);
Histogram histogram = result.getHistogram();
histogram.saveHistogram(outputFile);
```

| <img width="600" src="src/main/resources/images/original/nature/2.nature_medium.jpeg" alt="Original Image"/> | <img width="600" src="src/main/resources/images/histogram/nature.png" alt="RGB Histogram of Original Image"/> |
| :----------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------: |
|                                              **Original Image**                                              |                                      **RGB Histogram of Original Image**                                      |

The Histogram can also be shown in an interactive Interface:

```java
ParallelImageProcessor processor = new HistogramProcessor(threadPoolSize);
Histogram histogram = processor.processImage(file).getHistogram();
histogram.showHistogram();
```

# Project

The main Goal of this Project was to test different variants of processing images in java with Threads. Mainly two different Variant ‘Blocking’ and ‘Non-Blocking’ have been developed for each Task (RGB->Greyscale, RGB->Brightness and RGB->Histogram). The different Task and Variant can be seen in the [class UML](#class-uml).  
Both ‘Blocking’ and ‘Non-Blocking’ Task-variants share the Image file in the shared-memory to read data from it. This is not synchronized as it is thread-safe. 
The Non-Blocking variants uses non-shared-memory between Threads to store the results and merges them all together after all Threads have stopped. This gives the chance for each Threads to run in Parallel (still determined by OS).
The Blocking variant uses shared-memory to write directly the result (synchronized). This results to a higher thread run time but less overhead to retrieve the results after execution. 

### Class UML
<img width="600" align="center" src="src/main/resources/images/UML.png" alt="Class UML"/>

# Benchmarks
Processor: 1,4 GHz Quad-Core Intel Core i5  
Memory: 8 GB 2133 MHz LPDDR3  
OS: macOS Ventura Version 13.0.1 (22A400)  

For each Task and Variant the Task was executed 1000 times and the execution time was recorded. The benchmark results can be seen under
`/src/main/resources/testResults`, they include the raw results as csv and some graphics. The Tests can be found under `/src/test/java`.

## Greyscale
|                                              **Blocking**                                               |                                                **Non-Blocking**                                                |
| :-----------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------------: |
| <img width="600" src="/src/main/resources/testResults/greyscale/Diagram_Blockin_2.png" alt="Blocking"/> | <img width="600" src="/src/main/resources/testResults/greyscale/Diagram_NonBlockin_2.png" alt="Non-Blocking"/> |
| <img width="600" src="/src/main/resources/testResults/greyscale/Diagram_Blockin_3.png" alt="Blocking"/> | <img width="600" src="/src/main/resources/testResults/greyscale/Diagram_NonBlockin_3.png" alt="Non-Blocking"/> |

## Brightness
|                                               **Blocking**                                               |                                                **Non-Blocking**                                                 |
| :------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------: |
| <img width="600" src="/src/main/resources/testResults/brightness/Diagram_Blockin_2.png" alt="Blocking"/> | <img width="600" src="/src/main/resources/testResults/brightness/Diagram_NonBlockin_2.png" alt="Non-Blocking"/> |
| <img width="600" src="/src/main/resources/testResults/brightness/Diagram_Blockin_3.png" alt="Blocking"/> | <img width="600" src="/src/main/resources/testResults/brightness/Diagram_NonBlockin_3.png" alt="Non-Blocking"/> |

## Histogram
|                                              **Blocking**                                               |                                                **Non-Blocking**                                                 |
| :-----------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------: |
| <img width="600" src="/src/main/resources/testResults/histogram/Diagram_Blockin_2.png" alt="Blocking"/> | <img width="600" src="/src/main/resources/testResults/histogram/Diagram_NonBlockin_2.png" alt="Non-Blocking"/>  |
| <img width="600" src="/src/main/resources/testResults/histogram/Diagram_Blockin_3.png" alt="Blocking"/> | <img width="600" src="/src/main/resources/testResults/histogram/Diagram_NonBlockin_3.png" alt="Non-Blocking"/> |

# Logger
```java
private static final Logger logger = Logger.getInstance();

logger.start(Logger.TYPE.DEBUG, null);
logger.close();
