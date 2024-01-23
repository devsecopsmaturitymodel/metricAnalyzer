{
  "datasource": {
    "type": "yesoreyeram-infinity-datasource",
    "uid": "${infinityDatasourceId}"
  },
  "fieldConfig": {
    "defaults": {
      "custom": {
        "drawStyle": "line",
        "lineInterpolation": "stepAfter",
        "barAlignment": 0,
        "lineWidth": 1,
        "fillOpacity": 0,
        "gradientMode": "none",
        "spanNulls": false,
        "insertNulls": false,
        "showPoints": "auto",
        "pointSize": 1,
        "stacking": {
          "mode": "none",
          "group": "A"
        },
        "axisPlacement": "auto",
        "axisLabel": "",
        "axisColorMode": "text",
        "axisBorderShow": false,
        "scaleDistribution": {
          "type": "linear"
        },
        "axisCenteredZero": false,
        "hideFrom": {
          "tooltip": false,
          "viz": false,
          "legend": false
        },
        "thresholdsStyle": {
          "mode": "off"
        }
      },
      "color": {
        "mode": "palette-classic"
      },
      "mappings": [],
      "thresholds": {
        "mode": "absolute",
        "steps": [
          {
            "color": "green",
            "value": null
          }
        ]
      }
    },
    "overrides": [
      {
        "__systemRef": "hideSeriesFrom",
        "matcher": {
          "id": "byNames",
          "options": {
            "mode": "exclude",
            "names": [
              "sauron-saruman"
            ],
            "prefix": "All except:",
            "readOnly": true
          }
        },
        "properties": [
          {
            "id": "custom.hideFrom",
            "value": {
              "legend": false,
              "tooltip": false,
              "viz": true
            }
          }
        ]
      }
    ]
  },
  "gridPos": {
    "h": 8,
    "w": 12,
    "x": 12,
    "y": 1
  },
  "id": 8,
  "options": {
    "tooltip": {
      "mode": "single",
      "sort": "none"
    },
    "legend": {
      "showLegend": true,
      "displayMode": "list",
      "placement": "bottom",
      "calcs": []
    }
  },
  "pluginVersion": "10.2.2",
  "targets": [
    {
      "columns": [],
      "data": "",
      "datasource": {
        "type": "yesoreyeram-infinity-datasource",
        "uid": "infinity-datasource-uuid"
      },
      "filters": [],
      "format": "table",
      "global_query_id": "",
      "groq": "*",
      "parser": "backend",
      "refId": "A",
      "root_selector": "",
      "source": "url",
      "type": "json",
      "uql": "parse-json",
      "url": "${apiUrl}",
      "url_options": {
        "data": "",
        "method": "GET"
      }
    }
  ],
  "title": "${title}",
  "transformations": [
    {
      "id": "convertFieldType",
      "options": {
        "conversions": [
          {
            "destinationType": "time",
            "targetField": "date"
          }
        ],
        "fields": {}
      }
    },
    {
      "id": "extractFields",
      "options": {
        "format": "json",
        "source": "entries"
      }
    },
    {
      "id": "organize",
      "options": {
        "excludeByName": {
          "entries": true
        },
        "indexByName": {},
        "renameByName": {}
      }
    },
    {
      "id": "prepareTimeSeries",
      "options": {}
    }
  ],
  "type": "timeseries",
  "description": "${description}"
}