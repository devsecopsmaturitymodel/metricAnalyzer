    {
      "datasource": {
        "type": "yesoreyeram-infinity-datasource",
        "uid": "${infinityDatasourceId}"
      },
      "fieldConfig": {
        "defaults": {
          "custom": {
            "hideFrom": {
              "legend": false,
              "tooltip": false,
              "viz": false
            },
            "scaleDistribution": {
              "type": "linear"
            }
          }
        },
        "overrides": []
      },
      "gridPos": {
        "h": 8,
        "w": 12,
        "x": 0,
        "y": 0
      },
      "id": 1,
      "options": {
        "calculate": false,
        "cellGap": 1,
        "color": {
          "exponent": 0.5,
          "fill": "dark-orange",
          "mode": "scheme",
          "reverse": false,
          "scale": "exponential",
          "scheme": "Oranges",
          "steps": 64
        },
        "exemplars": {
          "color": "rgba(255,0,255,0.7)"
        },
        "filterValues": {
          "le": 1e-9
        },
        "legend": {
          "show": true
        },
        "rowsFrame": {
          "layout": "auto"
        },
        "tooltip": {
          "show": true,
          "yHistogram": false
        },
        "yAxis": {
          "axisPlacement": "left",
          "reverse": false
        }
      },
      "pluginVersion": "10.2.2",
      "targets": [
        {
          "columns": [],
          "datasource": {
            "type": "yesoreyeram-infinity-datasource",
            "uid": "${infinityDatasourceId}"
          },
          "filters": [],
          "format": "table",
          "global_query_id": "",
          "refId": "A",
          "root_selector": "",
          "source": "url",
          "type": "json",
          "url": "https://github.com/grafana/grafana-infinity-datasource/blob/main/testdata/users.json",
          "url_options": {
            "data": "",
            "method": "GET"
          }
        }
      ],
      "title": "Panel Title",
      "type": "heatmap"
    }