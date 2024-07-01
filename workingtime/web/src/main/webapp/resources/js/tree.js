/**
   * Display tree of Question Pools.
   * Events:
   * When select a Pool, the name and id of the selected pool will be set to inputs: poolName, poolId in home.jsp
   */
$(function () {
    $('#jstree').jstree({
        "core" : {
            'data' : {
                'url' : function(node) {
                    return node.id === '#' ? _ctx + 'getNodeRoot' : _ctx + 'getNodeChildren';
                },
                'dataType' : 'json',
                'data' : function(node) {
                    if (node.id === '#') {
                        return {
                            'id' : 0 // ID của node gốc
                        };
                    } else {
                        return {
                            'id' : node.id
                        };
                    }
                }
            },
            "plugins" : [ "core", "unique", "search", "state", "types", "wholerow" ]
        }
    });
    // 7 bind to events triggered on the tree
    $('#jstree').on("changed.jstree", function (e, data) {
      var id = data.selected[0];

      console.log('data.selected=' + data.selected + ';id=' + id);
      
      if (typeof data.node !== "undefined") {
          var title = data.node["a_attr"].title;

          console.log("title=" + title);
          
          // Reflect the main form
          $('#configuration_header').text(title);
      } else {
          console.log("Could not get the Title of Question Pool.");
      }
      
      
    });
    // 8 interact with the tree - either way is OK
    $('jstree').on('click', function () {
      $('#jstree').jstree(true).select_node('child_node_1');
      $('#jstree').jstree('select_node', 'child_node_1');
      $.jstree.reference('#jstree').select_node('child_node_1');
    });
  });
