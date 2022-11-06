# Parallel-Image-processing

## Aufgabe

Eine JPG oder PNG Datei, die mit Hilfe von OpenMP in eine andere Datei konvertiert werden muss

1. RGB -> Grayscale (Was mit PNG mit transparency)
2. RGB -> Histogramm
3. RGB -> Helligkeit

## Tests

### 1

- Single Threaded
- OpenCV
- MultiThreaded (2) vs MultiThreaded (with all cores)
- MultiThreaded by splitting image by rows
- MultiThreaded by splitting image by Pixel