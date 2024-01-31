{
  "type": "timeseries",
  "title": "${title}",
  "gridPos": {
    "x": 0,
    "y": 0,
    "w": 12,
    "h": 8
  },
  "datasource": {
    "uid": "bf5f79be-56ae-4a3e-869e-fa01fa2b3161",
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
      "source": "url",
      "format": "table",
      "url": "${apiUrl}",
      "url_options": {
        "method": "GET",
        "data": ""
      },
      "root_selector": "",
      "columns": [],
      "filters": [],
      "global_query_id": "",
      "parser": "backend"
    }
  ],
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
  "fieldConfig": {
    "defaults": {
      "custom": {
        "drawStyle": "line",
        "lineInterpolation": "linear",
        "barAlignment": 0,
        "lineWidth": 1,
        "fillOpacity": 0,
        "gradientMode": "none",
        "spanNulls": false,
        "insertNulls": false,
        "showPoints": "auto",
        "pointSize": 5,
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
            "value": null,
            "color": "green"
          },
          {
            "value": 80,
            "color": "red"
          }
        ]
      }
    },
    "overrides": []
  },
  "transformations": [
    {
      "id": "convertFieldType",
      "options": {
        "fields": {},
        "conversions": [
          {
            "targetField": "date",
            "destinationType": "time"
          }
        ]
      }
    },
    {
      "id": "extractFields",
      "options": {
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
  ]
}