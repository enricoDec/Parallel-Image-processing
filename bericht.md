# Belegaufgabe 4

## Grayscale, Histogramm und Helligkeit mit OpenMP

### von Enrico Gamil Toros de Chadarevian und Sonja Albers

1. [Aufgabe](#1-aufgabe)
2. [Aufbau](#2-aufbau)
3. [Tests](#3-tests)
4. [Schlussfolgerung](#4-schlussfolgerung)

## 1. Aufgabe

Das Ziel der Aufgabe ist es, PNG- oder JPG-Dateien aus dem RGB-Format in drei andere Ausgabeformate umzuwandeln:

1. ein Grauwertbild
2. ein Histogramm
3. ein Bild mit veränderter Helligkeit

Dabei sollen die Performance-Unterschiede zwischen den Umwandlungen mit und ohne die Nutzung von OpenMP verglichen
werden. Zusätzlich kann die Parallelisierung mit Hilfe von Threads in den Vergleich einbezogen werden.

*Fun Fact: SW-Bilder haben nur 1 Bit Farbtiefe, Grauwertbilder hingegen 8 Bit.*

## 2. Aufbau

- Jede Aufgabe 2 Varianten:
    - Variante 1 (Blocking): die neuen Werte der Pixel werden in einem Thread gerechnet und direkt im Bild Modell
      geändert. Der Bild Modell wird dafür Synchronisiert (Semaphor) um Race Conditions zu vermeiden. Das heisst jedes
      Thread can Parallel die neuen Werte berechnen aber nicht parallel schreiben.
    - Variante 2 (Non Blocking): die neuen Werte der Pixel werden in einem Thread gerechnet und in einem eigenen
      Speicher (Thread spezifisch) gespeichert. Nachdem alle Threads beendet wurden, werden alle ergebnisse gesammelt
      und erst dann im Bild Modell geändert
    - Alle Varianten teilen sich einem gemeinsamen Speicher, welches die "Originale" Pixel Werte besitzt (zum Lesen also
      nicht Synchronisiert).

### 2.1 Grauwertbild

### 2.2 Histogramm

### 2.3 veränderte Helligkeit

## 3. Tests

(Sind, nur vorschlage)

- Als erstes wurde ich zeigen, dass die Threads wirklich Parallel arbeit (screenshots?)
- Dann erstmal zum beispiel alle messwerte von Brightness mit 6 Thread und x Bild auf eine Graphic plotten um eine
  Übersicht der ergebnisse zu geben. Es sollte damit hoffentlich erkennbar sein, dass die ergebnisse recht uniform
  sind (-> heisst man hat oft genug den experiment wiederholt?).
- Dann auf eine Grafik ein Experiment plotten zum Beispiel Brightness non Blocking mit 1,2,3,... Threads. Es sollte sich
  hoffentlich damit sich zeigen lassen, dass more Threads -> mo better.
- Varianten vergleichen (Blocking vs Non Blocking)
- Bild Auflösung macht unterschied?
-

## 4. Schlussfolgerung

- circa 50% der laufzeit ist Bild aus Disk lesen -> eigenliche bottleneck. Solche kleine aufgaben auf Threads zu
  verteilen macht kaum unterschied. Man sollte an dieser Stelle eigentlich optimieren man könnte Beispielsweise die Bild
  Datei on demand in fragmente auslesen.