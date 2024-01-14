{
  "type": "stat",
  "title": "Panel Title",
  "gridPos": {
    "x": 0,
    "y": 0,
    "w": 12,
    "h": 8
  },
  "datasource": {
    "uid": "${infinityDatasourceId}",
    "type": "yesoreyeram-infinity-datasource"
  },
  "id": 2,
  "targets": [
    {
      "datasource": {
        "type": "yesoreyeram-infinity-datasource",
        "uid": "${infinityDatasourceId}"
      },
      "refId": "A",
      "type": "json",
      "source": "inline",
      "format": "table",
      "url": "https://github.com/grafana/grafana-infinity-datasource/blob/main/testdata/users.json",
      "url_options": {
        "method": "GET",
        "data": ""
      },
      "root_selector": "",
      "columns": [],
      "filters": [],
      "global_query_id": "",
      "data": "{\n \"fellowship of the ring\": 0,\n \"sauron\": 10\n}"
    }
  ],
  "fieldConfig": {
    "defaults": {
      "mappings": [],
      "thresholds": {
        "mode": "absolute",
        "steps": [
          {
            "color": "red",
            "value": null
          },
          {
            "value": 1,
            "color": "green"
          }
        ]
      },
      "color": {
        "mode": "thresholds"
      },
      "min": -1
    },
    "overrides": []
  },
  "transformations": [],
  "pluginVersion": "10.2.2",
  "options": {
    "reduceOptions": {
      "values": true,
      "calcs": [
        "lastNotNull"
      ],
      "fields": "/.*/"
    },
    "orientation": "auto",
    "textMode": "auto",
    "wideLayout": true,
    "colorMode": "value",
    "graphMode": "area",
    "justifyMode": "auto"
  }
}