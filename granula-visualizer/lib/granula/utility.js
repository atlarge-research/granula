/*
 * Copyright 2015 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


function getHttpParameters(name) {
    qs = document.location.search.split("+").join(" ");

    var params = {}, tokens,
        re = /[?&]?([^=]+)=([^&]*)/g;

    while (tokens = re.exec(qs)) {
        params[decodeURIComponent(tokens[1])]
            = decodeURIComponent(tokens[2]);
    }
    return params[name];
}

function isSameOrigin(url) {

    var loc = window.location,
        a = document.createElement('a');

    a.href = url;

    return a.hostname == loc.hostname &&
        a.port == loc.port &&
        a.protocol == loc.protocol;
}

function getFilename(fullPath) {
    return fullPath.replace(/^.*[\\\/]/, '');
}

function get2Digit(number) {
    return ("0" + number).slice(-2);
}

function getDomainURL() {
    return [location.protocol, '//', location.host, location.pathname].join('');
}

function printFast(object) {
    console.log(JSON.stringify(object))
}

function printSize(object) {
    if(object == undefined) {
        console.log("Undefined Object" + ": " +  '0');
    } else if(object.length == undefined) {
        console.log(object.constructor.name + ": " +  '1');
    } else {
        console.log(object[0].constructor.name + ": " +  object.length);
    }

}

function randomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
}

function getPredefinedColor(number) {
    if (number == 0) {
        return "#D33";
    } else if (number == 1) {
        return "#3D3";
    } else if (number == 2) {
        return "#33D";
    } else if (number == 3) {
        return "#3DD";
    } else if (number == 4) {
        return "#DD3";
    } else if (number == 5) {
        return "#D3D";
    } else {
        return getRandomColor();
    }
}

function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function getMetricPrefix(valueRange) {

    var numDigit = Math.log10(Math.abs(valueRange));

    if(isBetween(numDigit, 0, 3)) {
        return new MetricPrefix("", "1");
    } else if(isBetween(numDigit, 3, 6)) {
        return new MetricPrefix("k", "1000");
    } else if(isBetween(numDigit, 6, 9)) {
        return new MetricPrefix("M", "1000000");
    } else if(isBetween(numDigit, 9, 12)) {
        return new MetricPrefix("G", "1000000000");
    } else if(isBetween(numDigit, 12, 15)) {
        return new MetricPrefix("T", "1000000000000");
    } else if(isBetween(numDigit, -3, 0)) {
        return new MetricPrefix("m", "0.001");
    } else if(isBetween(numDigit, -6, -3)) {
        return new MetricPrefix("μ", "0.000001");
    } else {
        console.error("Unexpected valueRange " + valueRange)
    }
}

function MetricPrefix(symbol, baseValue) {
    this.symbol = symbol;
    this.baseValue = baseValue;
}

function isBetween(value, startValue, endValue) {
    return value >= startValue && value <=endValue;
}

function pickRandom(objects) {
    return objects[randomInt(0, objects.length - 1)];
}

/** escapest html special characters to html. */
function textToHtml(str) {

}

function getPrintableXml(xmlNode) {

    var xml = (new XMLSerializer()).serializeToString(xmlNode);

    xml = xml.replace(' xmlns="http://www.w3.org/1999/xhtml"', '');
    var text = vkbeautify.xml(xml);

    var pr_amp = /&/g;
    var pr_lt = /</g;
    var pr_gt = />/g;
    text = text.replace(pr_amp, '&amp;').replace(pr_lt, '&lt;').replace(pr_gt, '&gt;');
    text = text.replace(new RegExp("\n", "g"), '<br>');

    return text;

}









