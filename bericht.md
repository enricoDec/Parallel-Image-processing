# Belegaufgabe 4

## Grayscale, Histogramm und Helligkeit mit OpenMP

### von Enrico Gamil Toros de Chadarevian und Sonja Albers

1. [Aufgabe](#1-aufgabe)
2. [Aufbau](#2-aufbau)
3. [Tests](#3-tests)
4. [Schlussfolgerung](#4-schlussfolgerung)

## 1. Aufgabe

Das Ziel der Aufgabe ist es, mit Hilfe von Parallelisierung Informationen aus PNG- oder JPG-Dateien herauszulesen und folgendes zu erstellen:

1. ein Grauwertbild
2. ein Histogramm
3. ein Bild mit veränderter Helligkeit

Der Hauptfokus liegt dabei auf der Umsetzung der Parallelisierung der Aufgaben, nicht auf den Algorithmen selbst. Es wurden zwei verschiedene Varianten der Parallelisierung genutzt, wobei beide sich den Speicher mit den Ausgangspixelwerten teilen. Das Auslesen ist demnach nicht synchronisiert.

### Variante 1: Blocking
Hier werden die neuen Pixelwerte in Threads berechnet und die veränderten Werte sofort in einer neuen Bilddatei gespeichert. Um Race-Conditions zu vermeiden, sind die Abläufe so synchronisiert, dass die Threads zwar parallel rechnen können, aber nur einer schreiben kann. 

### Variante 2: Non-Blocking
In dieser Variante werden die neuen Pixelwerte ebenfalls in eigenen Threads berechnet, allerdings werden sie vorerst in einem eigenen Cache des Threads gespeichert. Erst wenn alle Threads ihre Berechnungen beendet haben, werden die Ergebnisse gesammelt und daraus eine neue Bilddatei erstellt.

## 2. Aufbau und Funktionsweise

- Übersicht der Klassen und Beschreibung der Zusammenhänge – Klassendiagramm

Jeweils: beschreiben, wie die Werte berechnet werden

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

## 4. Schlussfolgerung

- circa 50% der laufzeit ist Bild aus Disk lesen -> eigenliche bottleneck. Solche kleine aufgaben auf Threads zu
  verteilen macht kaum unterschied. Man sollte an dieser Stelle eigentlich optimieren man könnte Beispielsweise die Bild
  Datei on demand in fragmente auslesen.
TODO: Selbst laufen lassen