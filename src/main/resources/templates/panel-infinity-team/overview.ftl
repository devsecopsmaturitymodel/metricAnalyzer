{
  "datasource": {
    "type": "yesoreyeram-infinity-datasource"
  },
  "fieldConfig": {
    "defaults": {
      "custom": {
        "align": "auto",
        "cellOptions": {
          "type": "color-background"
        },
        "inspect": false,
        "filterable": true,
        "minWidth": 200
      },
      "unitScale": true,
      "mappings": [
        {
          "options": {
            "false": {
              "color": "red",
              "index": 0
            },
            "true": {
              "color": "green",
              "index": 1
            }
          },
          "type": "value"
        }
      ],
      "thresholds": {
        "mode": "absolute",
        "steps": [
          {
            "color": "green",
            "value": null
          },
          {
            "color": "red",
            "value": 80
          }
        ]
      },
      "color": {
        "mode": "thresholds"
      }
    },
    "overrides": []
  },
  "gridPos": {
    "h": 8,
    "w": 12,
    "x": 12,
    "y": 32
  },
  "options": {
    "showHeader": true,
    "cellHeight": "sm",
    "footer": {
      "show": false,
      "reducer": [
        "sum"
      ],
      "countRows": false,
      "fields": "",
      "enablePagination": true
    }
  },
  "pluginVersion": "10.4.0-65875",
  "targets": [
    {
      "datasource": {
        "type": "yesoreyeram-infinity-datasource"
      },
      "columns": [],
      "data": "",
      "filters": [],
      "format": "table",
      "global_query_id": "",
      "refId": "A",
      "root_selector": "",
      "source": "url",
      "type": "json",
      "url": "${apiUrl}",
      "url_options": {
        "data": "",
        "method": "GET"
      },
      "parser": "backend"
    }
  ],
  "title": "${title}",
  "transformations": [
    {
      "id": "extractFields",
      "options": {
        "replace": true,
        "source": "entries"
      }
    },
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
      "id": "prepareTimeSeries",
      "options": {}
    }
  ],
  "type": "table",
  "description": "${description}"
}