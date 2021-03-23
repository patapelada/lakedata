# Lake Data
Speichern und Abfragen von lokalisierten Messwerten über gesichertes REST-API.

## Authorisierung
Registrierte User haben auf für sie zugewiesene Locations folgende Rechte:
- Abfrage der verschiedenen Messwert Typen
- Abfrage aller bestehenden Messwerte für entsprechende Location
- Abfrage Location information
- Hinzufügen neuer Messwerte

Durch den Application-Admin erhälst du ein API-KEY mit welchem du dich bei jedem Request authentifizieren musst. Dies geschieht entweder über das Header attribut *Authorization* oder über den Requestparameter *apiKey*.

## Endpoints

### Verfügbare Messwerttypen
```sh
/api/measurementType
```
```sh
curl -XGET -H 'Authorization: API_KEY' -H "Content-type: application/json" 'https://lake-data.herokuapp.com/api/measurementType'
```
Example response:
```json
[
    {
        "id":"WT",
        "name":"Water Temperature"
    },
    {
        "id":"AT",
        "name":"Air Temperature"
    }
]
```
#### Einzelner Typ

```sh
/api/measurementType/{Type Id}
```
```sh
curl -XGET -H 'Authorization: API_KEY' -H "Content-type: application/json" 'https://lake-data.herokuapp.com/api/measurementType/WT'
```
***

### Verfügbare Locations
```sh
/api/location
```
```sh
curl -XGET -H 'Authorization: API_KEY' -H "Content-type: application/json" 'https://lake-data.herokuapp.com/api/location'
```
Example response:
```json
[
    {
        "id":"hirsi",
        "name":"hirsgarten cham",
        "measurements":null
    }
]
```
### Einzelne Location
```sh
/api/location/{location id}
```
```sh
curl -XGET -H 'Authorization: API_KEY' -H "Content-type: application/json" 'https://lake-data.herokuapp.com/api/location/hirsi'
```
Example response:
```json
{   
    "id":"hirsi",
    "name":"hirsgarten cham",
    "measurements":null
}
```
***
### Location Messwerte abfragen
```sh
/api/location/{location id}/measurement
```
```sh
curl -XGET -H 'Authorization: API_KEY' -H "Content-type: application/json" 'https://lake-data.herokuapp.com/api/location/hirsi/measurement'
```
Example response:
```json
[
    {
        "id":1,
        "type":"WT",
        "value":15.5,
        "timestamp":"01.02.2021 04:36:18 UTC"
    },
    {
        "id":2,
        "type":"WT",
        "value":16.4,
        "timestamp":"02.02.2021 04:36:18 UTC"
    }
]
```

### Messwert Hinzufügen
```
/api/location/{location id}/measurement
```
```sh
 curl -XPOST -H 'Authorization: API_KEY' -H "Content-type: application/json" -d '{"type": "WT","value": 15.5,"timestamp": "02.02.2021 05:36:18 +01:00"}' 'https://lake-data.herokuapp.com/api/location/hirsi/measurement'
```
Post Data Schema:
```json
{
    "type": String {Measurement Type Id} (bspw. "WT"),
    "value": Double (bspw 16.4),
    "timestamp": String {Fomat:dd.MM.yyyy HH:mm:ss z} (bspw "02.02.2021 04:36:18 +01:00")
}
```
