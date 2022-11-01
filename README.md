# Parallel-Image-processing

## Aufgabe
Eine JPG oder PNG Datei, die in eine andere Datei konvertiert werden muss
1. RGB -> Grayscale
2. RGB -> Histogramm
3. RGB -> Helligkeit

```
+*+**+**+*+*+**+**+*+*+**+**+*   
**+**++*+*+*+**+**+*+*+**+**+*   
*++*****+++*+**+**+*+*+**+**+*   
+*+**+**+*+*+**+**+*+*+**+**+*  
+*+**+**+*+*+**+**+*+*+**+**+*   
+*+**+**+*+*+**+**+*+*+**+**+*   
```

1. Irgendwie pixeln verteilen 
2. X Threads starten (basierend auf anzahl der Kerne)
3. Threads konsumieren die Pixeln (als Reihe oder einzeln)
4. Bild erzeugen
5. Grau: G = 0.21 ∙ R + 0.72 ∙ G + 0.07 ∙ B