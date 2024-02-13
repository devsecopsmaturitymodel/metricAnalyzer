{
  "type": "table",
  "title": "${title}",
  "gridPos": {
    "x": 0,
    "y": 0,
    "w": 24,
    "h": 24
  },
  "datasource": {
    "uid": "${infinityDatasourceId}",
    "type": "yesoreyeram-infinity-datasource"
  },
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
  "fieldConfig": {
    "defaults": {
      "custom": {
        "align": "auto",
        "minWidth": 200,
        "cellOptions": {
          "type": "color-background",
          "mode": "basic"
        },
        "inspect": false
      },
      "mappings": [
        {
          "type": "value",
          "options": {
            "true": {
              "color": "dark-green",
              "index": 0
            },
            "false": {
              "color": "dark-red",
              "index": 1
            }
          }
        }
      ],
      "thresholds": {
        "mode": "absolute",
        "steps": [
          {
            "color": "text",
            "value": null
          }
        ]
      },
      "color": {
        "mode": "thresholds",
        "fixedColor": "dark-red"
      }
    },
    "overrides": []
  },
  "transformations": [
    {
      "id": "extractFields",
      "options": {
        "source": "entries",
        "replace": true
      }
    },
    {
      "id": "organize",
      "options": {
        "excludeByName": {},
        "indexByName": {
          "Application": 0,
          "Advanced app. metrics": 1,
          "Alerting": 2,
          "Centralized system logging": 3,
          "Conduction of simple threat modeling on technical level": 4,
          "Data privacy requirements": 5,
          "Defined build process": 6,
          "Definition of simple BCDR practices for critical components": 7,
          "Environment depending configuration parameters (secrets)": 8,
          "Reduction of the attack surface": 9,
          "Same artifact for environments": 10,
          "Security Training": 11,
          "Security champion": 12,
          "Security code review": 13,
          "Security requirements": 14,
          "Simple application metrics": 15,
          "Source Control Protection": 16,
          "Static analysis for important server side components": 17,
          "Test for stored secrets": 18,
          "date": 19
        },
        "renameByName": {}
      }
    }
  ],
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
    },
    "sortBy": [
      {
        "displayName": "team",
        "desc": false
      }
    ]
  },
  "description": "${description}",
  "pluginVersion": "10.2.2"
}