{
  "annotations": {
    "list": [
      {
        "builtIn": 1,
        "datasource": {
          "type": "grafana",
          "uid": "-- Grafana --"
        },
        "enable": true,
        "hide": true,
        "iconColor": "rgba(0, 211, 255, 1)",
        "name": "Annotations & Alerts",
        "type": "dashboard"
      }
    ]
  },
  "editable": true,
  "fiscalYearStartMonth": 0,
  "graphTooltip": 0,
  "links": [],
  "liveNow": false,
  "panels": [ ${panelsAsString}
  ],
  "refresh": "",
  "schemaVersion": 38,
  "tags": [],
  "templating": {
    "list": [
      {
        "current": {
          "selected": true,
          "text": "nix-team",
          "value": "nix-team"
        },
        "datasource": {
          "type": "yesoreyeram-infinity-datasource",
          "uid": "${infinityDatasourceId}"
        },
        "definition": "Infinity- (infinity) json",
        "hide": 1,
        "includeAll": false,
        "multi": false,
        "name": "team",
        "options": [],
        "query": {
          "infinityQuery": {
            "columns": [],
            "filters": [],
            "format": "table",
            "parser": "backend",
            "refId": "variable",
            "root_selector": "",
            "source": "url",
            "type": "json",
            "url": "${apiUrl}/teams",
            "url_options": {
              "data": "",
              "method": "GET"
            }
          },
          "query": "",
          "queryType": "infinity"
        },
        "refresh": 1,
        "regex": "",
        "skipUrlSync": false,
        "sort": 0,
        "type": "query"
      },
      {
        "current": {
          "selected": false,
          "text": "nix-app",
          "value": "nix-app"
        },
        "datasource": {
          "type": "yesoreyeram-infinity-datasource",
          "uid": "${infinityDatasourceId}"
        },
        "definition": "Infinity- (infinity) json",
        "hide": 0,
        "includeAll": false,
        "multi": false,
        "name": "application",
        "options": [],
        "query": {
          "infinityQuery": {
            "columns": [],
            "filters": [],
            "format": "table",
            "refId": "variable",
            "root_selector": "",
            "source": "url",
            "type": "json",
            "url": "${apiUrl}/team/${r"${team}"}/applicationIds",
            "url_options": {
              "data": "",
              "method": "GET"
            }
          },
          "query": "",
          "queryType": "infinity"
        },
        "refresh": 1,
        "regex": "",
        "skipUrlSync": false,
        "sort": 0,
        "type": "query"
      }
    ]
  },
  "time": {
    "from": "now-5y",
    "to": "now"
  },
  "timepicker": {},
  "timezone": "",
  "title": "${title}",
  "version": 1,
  "weekStart": ""
}