function writeStatus(inp) {
    document.getElementById("status").innerHTML = "";
    document.getElementById("status").appendChild(document.createElement('pre')).innerHTML = inp;
}

function syntaxHighlight(json) {
    json = json.replace(/&/g, "&amp;").replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = "code-number";
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = "code-key";
            } else {
                cls = "code-string";
            }
        } else if (/true|false/.test(match)) {
            cls = "code-boolean";
        } else if (/null/.test(match)) {
            cls = "code-null";
        }
        return "<span class=\"" + cls + "\">" + match + "</span>";
    });
}

function loadStatus() {
    var xhr;

    if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xhr = new XMLHttpRequest();
    } else {
        // code for IE6, IE5
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
    }

    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            var str;
            if (xhr.status == 200) {
                str = JSON.stringify(JSON.parse(xhr.responseText), undefined, 4);
            } else {
                var err;
                if (xhr.responseText !== null && xhr.responseText !== "") {
                    str = JSON.stringify(JSON.parse(xhr.responseText), undefined, 4);
                } else {
                    str = JSON.stringify({message: "Error with HTTP status code: " + xhr.status}, undefined, 4);
                }
            }
            writeStatus(syntaxHighlight(str));
        }
    };

    xhr.open("GET", "/admin/status", true);
    xhr.send();
}

loadStatus();