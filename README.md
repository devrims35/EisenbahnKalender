# Eisenbahn-Kalender für Android

Eine einfache Android-App im Querformat: rote Lokomotive und fünf farbige Waggons. Auf die Wagen tippen, um Wochentag, Tag, Monat, Wetter und Jahreszeit zu ändern. Eingaben werden automatisch auf dem Gerät gespeichert. Wochentag, Datum, Monat und Jahreszeit sind bei der ersten Nutzung vorbelegt. Wetter ist manuell einstellbar, keine Wetter-API.

## Ohne PC über GitHub die APK erstellen

1. Erstelle auf github.com ein neues leeres Repository mit dem Namen `EisenbahnKalender` (am einfachsten: öffentlich; bei privaten Repositories gelten gegebenenfalls andere Actions-Limits).
2. Entpacke dieses ZIP auf dem Smartphone. Lade die Dateien und Ordner **inklusive `.github/workflows/build-apk.yml`** in die oberste Ebene des GitHub-Repositories hoch. **Wichtig:** Nicht nur die ZIP-Datei als Einzeldatei hochladen! Der versteckte `.github`-Ordner muss vorhanden sein.
3. Öffne in GitHub den Reiter `Actions`, wähle `Android APK erstellen` und starte `Run workflow`, falls nicht bereits automatisch gestartet.
4. Nach erfolgreichem Durchlauf öffne den Workflow und lade unter `Artifacts` den Eintrag `EisenbahnKalender-APK` herunter. GitHub liefert eine ZIP-Datei mit der `app-debug.apk` darin. Entpacken und APK auf Android installieren.

**Hinweise:** Dies ist ein Quellcodeprojekt, nicht die fertige APK. GitHub Actions muss erst erfolgreich durchlaufen. Für Installation von APKs außerhalb des Play Stores muss Android die Installation aus der gewählten Quelle erlauben. Die GitHub-Actions-Ausführung wurde hier nicht live verifiziert.
