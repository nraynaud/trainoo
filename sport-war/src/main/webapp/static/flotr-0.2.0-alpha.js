//Flotr 0.2.0-alpha Copyright (c) 2009 Bas Wenneker, <http://solutoire.com>, MIT License.
var Flotr = {version:"0.2.0-alpha",author:"Bas Wenneker",website:"http://www.solutoire.com",_registeredTypes:{lines:"drawSeriesLines",points:"drawSeriesPoints",bars:"drawSeriesBars",candles:"drawSeriesCandles",pie:"drawSeriesPie"},register:function(
        A, B) {
    Flotr._registeredTypes[A] = B + ""
},draw:function(B, D, A, C) {
    C = C || Flotr.Graph;
    return new C(B, D, A)
},getSeries:function(A) {
    return A.collect(function(C) {
        var B,C = (C.data) ? Object.clone(C) : {data:C};
        for (B = C.data.length - 1; B > -1; --B) {
            C.data[B][1] = (C.data[B][1] === null ? null : parseFloat(C.data[B][1]))
        }
        return C
    })
},merge:function(D, B) {
    var A = B || {};
    for (var C in D) {
        A[C] = (D[C] != null && typeof (D[C]) == "object" && !(D[C].constructor == Array || D[C].constructor == RegExp)
                && !Object.isElement(D[C])) ? Flotr.merge(D[C], B[C]) : A[C] = D[C]
    }
    return A
},getTickSize:function(E, D, A, B) {
    var H = (A - D) / E;
    var G = Flotr.getMagnitude(H);
    var C = H / G;
    var F = 10;
    if (C < 1.5) {
        F = 1
    } else {
        if (C < 2.25) {
            F = 2
        } else {
            if (C < 3) {
                F = ((B == 0) ? 2 : 2.5)
            } else {
                if (C < 7.5) {
                    F = 5
                }
            }
        }
    }
    return F * G
},defaultTickFormatter:function(A) {
    return A + ""
},defaultTrackFormatter:function(A) {
    return"(" + A.x + ", " + A.y + ")"
},defaultPieLabelFormatter:function(A) {
    return(A.fraction * 100).toFixed(2) + "%"
},getMagnitude:function(A) {
    return Math.pow(10, Math.floor(Math.log(A) / Math.LN10))
},toPixel:function(A) {
    return Math.floor(A) + 0.5
},toRad:function(A) {
    return -A * (Math.PI / 180)
},parseColor:function(D) {
    if (D instanceof Flotr.Color) {
        return D
    }
    var A,C = Flotr.Color;
    if ((A = /rgb\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*\)/.exec(D))) {
        return new C(parseInt(A[1]), parseInt(A[2]), parseInt(A[3]))
    }
    if ((A = /rgba\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]+(?:\.[0-9]+)?)\s*\)/.exec(D))) {
        return new C(parseInt(A[1]), parseInt(A[2]), parseInt(A[3]), parseFloat(A[4]))
    }
    if ((A = /rgb\(\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*\)/.exec(D))) {
        return new C(parseFloat(A[1]) * 2.55, parseFloat(A[2]) * 2.55, parseFloat(A[3]) * 2.55)
    }
    if ((A = /rgba\(\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\%\s*,\s*([0-9]+(?:\.[0-9]+)?)\s*\)/.exec(D))) {
        return new C(parseFloat(A[1]) * 2.55, parseFloat(A[2]) * 2.55, parseFloat(A[3]) * 2.55, parseFloat(A[4]))
    }
    if ((A = /#([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})/.exec(D))) {
        return new C(parseInt(A[1], 16), parseInt(A[2], 16), parseInt(A[3], 16))
    }
    if ((A = /#([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])/.exec(D))) {
        return new C(parseInt(A[1] + A[1], 16), parseInt(A[2] + A[2], 16), parseInt(A[3] + A[3], 16))
    }
    var B = D.strip().toLowerCase();
    if (B == "transparent") {
        return new C(255, 255, 255, 0)
    }
    return((A = C.lookupColors[B])) ? new C(A[0], A[1], A[2]) : false
},extractColor:function(B) {
    var A;
    do{
        A = B.getStyle("background-color").toLowerCase();
        if (!(A == "" || A == "transparent")) {
            break
        }
        B = B.up(0)
    } while (!B.nodeName.match(/^body$/i));
    return(A == "rgba(0, 0, 0, 0)") ? "transparent" : A
}};
Flotr.Graph = Class.create({initialize:function(B, C, A) {
    this.el = $(B);
    if (!this.el) {
        throw"The target container doesn't exist"
    }
    this.data = C;
    this.series = Flotr.getSeries(C);
    this.setOptions(A);
    this.lastMousePos = {pageX:null,pageY:null};
    this.selection = {first:{x:-1,y:-1},second:{x:-1,y:-1}};
    this.prevSelection = null;
    this.selectionInterval = null;
    this.ignoreClick = false;
    this.prevHit = null;
    this.constructCanvas();
    this.initEvents();
    this.findDataRanges();
    this.calculateTicks(this.axes.x);
    this.calculateTicks(this.axes.x2);
    this.calculateTicks(this.axes.y);
    this.calculateTicks(this.axes.y2);
    this.calculateSpacing();
    this.draw();
    this.insertLegend();
    if (this.options.spreadsheet.show) {
        this.constructTabs()
    }
},setOptions:function(B) {
    var P = {colors:["#00A8F0","#C0D800","#CB4B4B","#4DA74D","#9440ED"],title:null,subtitle:null,legend:{show:true,noColumns:1,labelFormatter:Prototype.K,labelBoxBorderColor:"#CCCCCC",labelBoxWidth:14,labelBoxHeight:10,labelBoxMargin:5,container:null,position:"nw",margin:5,backgroundColor:null,backgroundOpacity:0.85},xaxis:{ticks:null,showLabels:true,labelsAngle:0,title:null,titleAngle:0,noTicks:5,tickFormatter:Flotr.defaultTickFormatter,tickDecimals:null,min:null,max:null,autoscaleMargin:0,color:null},x2axis:{},yaxis:{ticks:null,showLabels:true,labelsAngle:0,title:null,titleAngle:90,noTicks:5,tickFormatter:Flotr.defaultTickFormatter,tickDecimals:null,min:null,max:null,autoscaleMargin:0,color:null},y2axis:{titleAngle:270},points:{show:false,radius:3,lineWidth:2,fill:true,fillColor:"#FFFFFF",fillOpacity:0.4},lines:{show:false,lineWidth:2,fill:false,fillColor:null,fillOpacity:0.4},bars:{show:false,lineWidth:2,barWidth:1,fill:true,fillColor:null,fillOpacity:0.4,horizontal:false,stacked:false},candles:{show:false,lineWidth:1,wickLineWidth:1,candleWidth:0.6,fill:true,upFillColor:"#00A8F0",downFillColor:"#CB4B4B",fillOpacity:0.5,barcharts:false},pie:{show:false,lineWidth:1,fill:true,fillColor:null,fillOpacity:0.6,explode:6,sizeRatio:0.6,startAngle:Math.PI
            / 4,labelFormatter:Flotr.defaultPieLabelFormatter,pie3D:false,pie3DviewAngle:(Math.PI / 2 * 0.8),pie3DspliceThickness:20},grid:{color:"#545454",backgroundColor:null,tickColor:"#DDDDDD",labelMargin:3,verticalLines:true,horizontalLines:true,outlineWidth:2},selection:{mode:null,color:"#B6D9FF",fps:20},mouse:{track:false,position:"se",relative:false,trackFormatter:Flotr.defaultTrackFormatter,margin:5,lineColor:"#FF3F19",trackDecimals:1,sensibility:2,radius:3},shadowSize:4,defaultType:"lines",HtmlText:true,fontSize:7.5,spreadsheet:{show:false,tabGraphLabel:"Graph",tabDataLabel:"Data",toolbarDownload:"Download CSV",toolbarSelectAll:"Select all"}};
    P.x2axis = Object.extend(Object.clone(P.xaxis), P.x2axis);
    P.y2axis = Object.extend(Object.clone(P.yaxis), P.y2axis);
    this.options = Flotr.merge((B || {}), P);
    this.axes = {x:{options:this.options.xaxis,n:1},x2:{options:this.options.x2axis,n:2},y:{options:this.options.yaxis,n:1},y2:{options:this.options.y2axis,n:2}};
    var H = [],C = [],K = this.series.length,N = this.series.length,D = this.options.colors,A = [],G = 0,M,J,I,O,E;
    for (J = N - 1; J > -1; --J) {
        M = this.series[J].color;
        if (M != null) {
            --N;
            if (Object.isNumber(M)) {
                H.push(M)
            } else {
                A.push(Flotr.parseColor(M))
            }
        }
    }
    for (J = H.length - 1; J > -1; --J) {
        N = Math.max(N, H[J] + 1)
    }
    for (J = 0; C.length < N;) {
        M = (D.length == J) ? new Flotr.Color(100, 100, 100) : Flotr.parseColor(D[J]);
        var F = G % 2 == 1 ? -1 : 1;
        var L = 1 + F * Math.ceil(G / 2) * 0.2;
        M.scale(L, L, L);
        C.push(M);
        if (++J >= D.length) {
            J = 0;
            ++G
        }
    }
    for (J = 0,I = 0; J < K; ++J) {
        O = this.series[J];
        if (O.color == null) {
            O.color = C[I++].toString()
        } else {
            if (Object.isNumber(O.color)) {
                O.color = C[O.color].toString()
            }
        }
        if (!O.xaxis) {
            O.xaxis = this.axes.x
        }
        if (O.xaxis == 1) {
            O.xaxis = this.axes.x
        } else {
            if (O.xaxis == 2) {
                O.xaxis = this.axes.x2
            }
        }
        if (!O.yaxis) {
            O.yaxis = this.axes.y
        }
        if (O.yaxis == 1) {
            O.yaxis = this.axes.y
        } else {
            if (O.yaxis == 2) {
                O.yaxis = this.axes.y2
            }
        }
        O.lines = Object.extend(Object.clone(this.options.lines), O.lines);
        O.points = Object.extend(Object.clone(this.options.points), O.points);
        O.bars = Object.extend(Object.clone(this.options.bars), O.bars);
        O.candles = Object.extend(Object.clone(this.options.candles), O.candles);
        O.pie = Object.extend(Object.clone(this.options.pie), O.pie);
        O.mouse = Object.extend(Object.clone(this.options.mouse), O.mouse);
        if (O.shadowSize == null) {
            O.shadowSize = this.options.shadowSize
        }
    }
},constructCanvas:function() {
    var C = this.el,B,D,A;
    this.canvas = C.select(".flotr-canvas")[0];
    this.overlay = C.select(".flotr-overlay")[0];
    C.childElements().invoke("remove");
    C.setStyle({position:"relative",cursor:"default"});
    this.canvasWidth = C.getWidth();
    this.canvasHeight = C.getHeight();
    B = {width:this.canvasWidth,height:this.canvasHeight};
    if (this.canvasWidth <= 0 || this.canvasHeight <= 0) {
        throw"Invalid dimensions for plot, width = " + this.canvasWidth + ", height = " + this.canvasHeight
    }
    if (!this.canvas) {
        D = this.canvas = new Element("canvas", B);
        D.className = "flotr-canvas";
        D = D.writeAttribute("style", "position:absolute;left:0px;top:0px;")
    } else {
        D = this.canvas.writeAttribute(B)
    }
    C.insert(D);
    if (Prototype.Browser.IE) {
        D = window.G_vmlCanvasManager.initElement(D)
    }
    this.ctx = D.getContext("2d");
    if (!this.overlay) {
        A = this.overlay = new Element("canvas", B);
        A.className = "flotr-overlay";
        A = A.writeAttribute("style", "position:absolute;left:0px;top:0px;")
    } else {
        A = this.overlay.writeAttribute(B)
    }
    C.insert(A);
    if (Prototype.Browser.IE) {
        A = window.G_vmlCanvasManager.initElement(A)
    }
    this.octx = A.getContext("2d");
    if (window.CanvasText) {
        CanvasText.enable(this.ctx);
        CanvasText.enable(this.octx);
        this.textEnabled = true
    }
},getTextDimensions:function(F, C, B, D) {
    if (!F) {
        return{width:0,height:0}
    }
    if (!this.options.HtmlText && this.textEnabled) {
        var E = this.ctx.getTextBounds(F, C);
        return{width:E.width + 2,height:E.height + 6}
    } else {
        var A = this.el.insert('<div style="position:absolute;top:-10000px;' + B + '" class="' + D + ' flotr-dummy-div">'
                + F + "</div>").select(".flotr-dummy-div")[0];
        dim = A.getDimensions();
        A.remove();
        return dim
    }
},loadDataGrid:function() {
    if (this.seriesData) {
        return this.seriesData
    }
    var A = this.series;
    var B = [];
    for (i = 0; i < A.length; ++i) {
        A[i].data.each(function(D) {
            var C = D[0],F = D[1];
            if (r = B.find(function(G) {
                return G[0] == C
            })) {
                r[i + 1] = F
            } else {
                var E = [];
                E[0] = C;
                E[i + 1] = F;
                B.push(E)
            }
        })
    }
    B = B.sortBy(function(C) {
        return C[0]
    });
    return this.seriesData = B
},showTab:function(B, C) {
    var A = "canvas, .flotr-labels, .flotr-legend, .flotr-legend-bg, .flotr-title, .flotr-subtitle";
    switch (B) {case"graph":this.datagrid.up().hide();this.el.select(A).invoke("show");this.tabs.data.removeClassName("selected");this.tabs.graph.addClassName("selected");break;case"data":this.constructDataGrid();this.datagrid.up().show();this.el.select(A).invoke("hide");this.tabs.data.addClassName("selected");this.tabs.graph.removeClassName("selected");break}
},constructTabs:function() {
    var A = new Element("div", {className:"flotr-tabs-group",style:"position:absolute;left:0px;top:" + this.canvasHeight
            + "px;width:" + this.canvasWidth + "px;"});
    this.el.insert({bottom:A});
    this.tabs = {graph:new Element("div", {className:"flotr-tab selected",style:"float:left;"}).update(this.options.spreadsheet.tabGraphLabel),data:new Element("div", {className:"flotr-tab",style:"float:left;"}).update(this.options.spreadsheet.tabDataLabel)};
    A.insert(this.tabs.graph).insert(this.tabs.data);
    this.el.setStyle({height:this.canvasHeight + this.tabs.data.getHeight() + 2 + "px"});
    this.tabs.graph.observe("click", (function() {
        this.showTab("graph")
    }).bind(this));
    this.tabs.data.observe("click", (function() {
        this.showTab("data")
    }).bind(this))
},constructDataGrid:function() {
    if (this.datagrid) {
        return this.datagrid
    }
    var D,B,L = this.series,J = this.loadDataGrid();
    var K = this.datagrid = new Element("table", {className:"flotr-datagrid",style:"height:100px;"});
    var C = ["<colgroup><col />"];
    var F = ['<tr class="first-row">'];
    F.push("<th>&nbsp;</th>");
    for (D = 0; D < L.length; ++D) {
        F.push('<th scope="col">' + (L[D].label || String.fromCharCode(65 + D)) + "</th>");
        C.push("<col />")
    }
    F.push("</tr>");
    for (B = 0; B < J.length; ++B) {
        F.push("<tr>");
        for (D = 0; D < L.length + 1; ++D) {
            var M = "td";
            var G = (J[B][D] != null ? Math.round(J[B][D] * 100000) / 100000 : "");
            if (D == 0) {
                M = "th";
                var I;
                if (this.options.xaxis.ticks) {
                    var E = this.options.xaxis.ticks.find(function(N) {
                        return N[0] == J[B][D]
                    });
                    if (E) {
                        I = E[1]
                    }
                } else {
                    I = this.options.xaxis.tickFormatter(G)
                }
                if (I) {
                    G = I
                }
            }
            F.push("<" + M + (M == "th" ? ' scope="row"' : "") + ">" + G + "</" + M + ">")
        }
        F.push("</tr>")
    }
    C.push("</colgroup>");
    K.update(C.join("") + F.join(""));
    if (!Prototype.Browser.IE) {
        K.select("td").each(function(N) {
            N.observe("mouseover", function(O) {
                N = O.element();
                var P = N.previousSiblings();
                K.select("th[scope=col]")[P.length - 1].addClassName("hover");
                K.select("colgroup col")[P.length].addClassName("hover")
            });
            N.observe("mouseout", function() {
                K.select("colgroup col.hover, th.hover").each(function(O) {
                    O.removeClassName("hover")
                })
            })
        })
    }
    var H = new Element("div", {className:"flotr-datagrid-toolbar"}).insert(new Element("button", {type:"button",className:"flotr-datagrid-toolbar-button"}).update(this.options.spreadsheet.toolbarDownload).observe("click", this.downloadCSV.bind(this))).insert(new Element("button", {type:"button",className:"flotr-datagrid-toolbar-button"}).update(this.options.spreadsheet.toolbarSelectAll).observe("click", this.selectAllData.bind(this)));
    var A = new Element("div", {className:"flotr-datagrid-container",style:"left:0px;top:0px;width:" + this.canvasWidth
            + "px;height:" + this.canvasHeight + "px;overflow:auto;"});
    A.insert(H);
    K.wrap(A.hide());
    this.el.insert(A);
    return K
},selectAllData:function() {
    if (this.tabs) {
        var B,A,E,D,C = this.constructDataGrid();
        this.showTab("data");
        (function() {
            if ((E = C.ownerDocument) && (D = E.defaultView) && D.getSelection && E.createRange && (B = window.getSelection())
                    && B.removeAllRanges) {
                A = E.createRange();
                A.selectNode(C);
                B.removeAllRanges();
                B.addRange(A)
            } else {
                if (document.body && document.body.createTextRange && (A = document.body.createTextRange())) {
                    A.moveToElementText(C);
                    A.select()
                }
            }
        }).defer();
        return true
    } else {
        return false
    }
},downloadCSV:function() {
    var D,A = '"x"',C = this.series,E = this.loadDataGrid();
    for (D = 0; D < C.length; ++D) {
        A += '%09"' + (C[D].label || String.fromCharCode(65 + D)) + '"'
    }
    A += "%0D%0A";
    for (D = 0; D < E.length; ++D) {
        if (this.options.xaxis.ticks) {
            var B = this.options.xaxis.ticks.find(function(F) {
                return F[0] == E[D][0]
            });
            if (B) {
                E[D][0] = B[1]
            }
        } else {
            E[D][0] = this.options.xaxis.tickFormatter(E[D][0])
        }
        A += E[D].join("%09") + "%0D%0A"
    }
    if (Prototype.Browser.IE) {
        A = A.gsub("%09", "\t").gsub("%0A", "\n").gsub("%0D", "\r");
        window.open().document.write(A)
    } else {
        window.open("data:text/csv," + A)
    }
},initEvents:function() {
    this.overlay.stopObserving();
    this.overlay.observe("mousedown", this.mouseDownHandler.bind(this));
    this.overlay.observe("mousemove", this.mouseMoveHandler.bind(this));
    this.overlay.observe("click", this.clickHandler.bind(this))
},findDataRanges:function() {
    var J = this.series,G = this.axes;
    G.x.datamin = 0;
    G.x.datamax = 0;
    G.x2.datamin = 0;
    G.x2.datamax = 0;
    G.y.datamin = 0;
    G.y.datamax = 0;
    G.y2.datamin = 0;
    G.y2.datamax = 0;
    if (J.length > 0) {
        var C,A,D,H,F,B,I,E;
        for (C = 0; C < J.length; ++C) {
            B = J[C].data,I = J[C].xaxis,E = J[C].yaxis;
            if (B.length > 0 && !J[C].hide) {
                if (!I.used) {
                    I.datamin = I.datamax = B[0][0]
                }
                if (!E.used) {
                    E.datamin = E.datamax = B[0][1]
                }
                I.used = true;
                E.used = true;
                for (D = B.length - 1; D > -1; --D) {
                    H = B[D][0];
                    if (H < I.datamin) {
                        I.datamin = H
                    } else {
                        if (H > I.datamax) {
                            I.datamax = H
                        }
                    }
                    for (A = 1; A < B[D].length; A++) {
                        F = B[D][A];
                        if (F < E.datamin) {
                            E.datamin = F
                        } else {
                            if (F > E.datamax) {
                                E.datamax = F
                            }
                        }
                    }
                }
            }
        }
    }
    this.findXAxesValues();
    this.calculateRange(G.x);
    this.extendXRangeIfNeededByBar(G.x);
    if (G.x2.used) {
        this.calculateRange(G.x2);
        this.extendXRangeIfNeededByBar(G.x2)
    }
    this.calculateRange(G.y);
    this.extendYRangeIfNeededByBar(G.y);
    if (G.y2.used) {
        this.calculateRange(G.y2);
        this.extendYRangeIfNeededByBar(G.y2)
    }
},calculateRange:function(D) {
    var F = D.options,C = F.min != null ? F.min : D.datamin,A = F.max != null ? F.max : D.datamax,E;
    if (A - C == 0) {
        var B = (A == 0) ? 1 : 0.01;
        C -= B;
        A += B
    }
    D.tickSize = Flotr.getTickSize(F.noTicks, C, A, F.tickDecimals);
    if (F.min == null) {
        E = F.autoscaleMargin;
        if (E != 0) {
            C -= D.tickSize * E;
            if (C < 0 && D.datamin >= 0) {
                C = 0
            }
            C = D.tickSize * Math.floor(C / D.tickSize)
        }
    }
    if (F.max == null) {
        E = F.autoscaleMargin;
        if (E != 0) {
            A += D.tickSize * E;
            if (A > 0 && D.datamax <= 0) {
                A = 0
            }
            A = D.tickSize * Math.ceil(A / D.tickSize)
        }
    }
    D.min = C;
    D.max = A
},extendXRangeIfNeededByBar:function(A) {
    if (A.options.max == null) {
        var D = A.max,B,I,F,E,H = [],C = null;
        for (B = 0; B < this.series.length; ++B) {
            I = this.series[B];
            F = I.bars;
            E = I.candles;
            if (I.axis == A && (F.show || E.show)) {
                if (!F.horizontal && (F.barWidth + A.datamax > D) || (E.candleWidth + A.datamax > D)) {
                    D = A.max + I.bars.barWidth
                }
                if (F.stacked && F.horizontal) {
                    for (j = 0; j < I.data.length; j++) {
                        if (I.bars.show && I.bars.stacked) {
                            var G = I.data[j][0];
                            H[G] = (H[G] || 0) + I.data[j][1];
                            C = I
                        }
                    }
                    for (j = 0; j < H.length; j++) {
                        D = Math.max(H[j], D)
                    }
                }
            }
        }
        A.lastSerie = C;
        A.max = D
    }
},extendYRangeIfNeededByBar:function(A) {
    if (A.options.max == null) {
        var D = A.max,B,I,F,E,H = [],C = null;
        for (B = 0; B < this.series.length; ++B) {
            I = this.series[B];
            F = I.bars;
            E = I.candles;
            if (I.yaxis == A && F.show && !I.hide) {
                if (F.horizontal && (F.barWidth + A.datamax > D) || (E.candleWidth + A.datamax > D)) {
                    D = A.max + F.barWidth
                }
                if (F.stacked && !F.horizontal) {
                    for (j = 0; j < I.data.length; j++) {
                        if (I.bars.show && I.bars.stacked) {
                            var G = I.data[j][0];
                            H[G] = (H[G] || 0) + I.data[j][1];
                            C = I
                        }
                    }
                    for (j = 0; j < H.length; j++) {
                        D = Math.max(H[j], D)
                    }
                }
            }
        }
        A.lastSerie = C;
        A.max = D
    }
},findXAxesValues:function() {
    for (i = this.series.length - 1; i > -1; --i) {
        s = this.series[i];
        s.xaxis.values = s.xaxis.values || [];
        for (j = s.data.length - 1; j > -1; --j) {
            s.xaxis.values[s.data[j][0]] = {}
        }
    }
},calculateTicks:function(D) {
    var B = D.options,E,H;
    D.ticks = [];
    if (B.ticks) {
        var G = B.ticks,I,F;
        if (Object.isFunction(G)) {
            G = G({min:D.min,max:D.max})
        }
        for (E = 0; E < G.length; ++E) {
            I = G[E];
            if (typeof (I) == "object") {
                H = I[0];
                F = (I.length > 1) ? I[1] : B.tickFormatter(H)
            } else {
                H = I;
                F = B.tickFormatter(H)
            }
            D.ticks[E] = {v:H,label:F}
        }
    } else {
        var A = D.tickSize * Math.ceil(D.min / D.tickSize),C;
        for (E = 0; A + E * D.tickSize <= D.max; ++E) {
            H = A + E * D.tickSize;
            C = B.tickDecimals;
            if (C == null) {
                C = 1 - Math.floor(Math.log(D.tickSize) / Math.LN10)
            }
            if (C < 0) {
                C = 0
            }
            H = H.toFixed(C);
            D.ticks.push({v:H,label:B.tickFormatter(H)})
        }
    }
},calculateSpacing:function() {
    var L = this.axes,N = this.options,H = this.series,D = N.grid.labelMargin,M = L.x,A = L.x2,J = L.y,K = L.y2,F = 2,G,E,C,I;
    [M,A,J,K].each(function(P) {
        var O = "";
        if (P.options.showLabels) {
            for (G = 0; G < P.ticks.length; ++G) {
                C = P.ticks[G].label.length;
                if (C > O.length) {
                    O = P.ticks[G].label
                }
            }
        }
        P.maxLabel = this.getTextDimensions(O, {size:N.fontSize,angle:Flotr.toRad(P.options.labelsAngle)}, "font-size:smaller;", "flotr-grid-label");
        P.titleSize = this.getTextDimensions(P.options.title, {size:N.fontSize * 1.2,angle:Flotr.toRad(P.options.titleAngle)}, "font-weight:bold;", "flotr-axis-title")
    }, this);
    I = this.getTextDimensions(N.title, {size:N.fontSize * 1.5}, "font-size:1em;font-weight:bold;", "flotr-title");
    this.titleHeight = I.height;
    I = this.getTextDimensions(N.subtitle, {size:N.fontSize}, "font-size:smaller;", "flotr-subtitle");
    this.subtitleHeight = I.height;
    if (N.show) {
        F = Math.max(F, N.points.radius + N.points.lineWidth / 2)
    }
    for (E = 0; E < N.length; ++E) {
        if (H[E].points.show) {
            F = Math.max(F, H[E].points.radius + H[E].points.lineWidth / 2)
        }
    }
    var B = this.plotOffset = {left:0,right:0,top:0,bottom:0};
    B.left = B.right = B.top = B.bottom = F;
    B.bottom += (M.options.showLabels ? (M.maxLabel.height + D) : 0) + (M.options.title ? (M.titleSize.height + D) : 0);
    B.top += (A.options.showLabels ? (A.maxLabel.height + D) : 0) + (A.options.title ? (A.titleSize.height + D) : 0) + this.subtitleHeight
            + this.titleHeight;
    B.left += (J.options.showLabels ? (J.maxLabel.width + D) : 0) + (J.options.title ? (J.titleSize.width + D) : 0);
    B.right += (K.options.showLabels ? (K.maxLabel.width + D) : 0) + (K.options.title ? (K.titleSize.width + D) : 0);
    B.top = Math.floor(B.top);
    this.plotWidth = this.canvasWidth - B.left - B.right;
    this.plotHeight = this.canvasHeight - B.bottom - B.top;
    M.scale = this.plotWidth / (M.max - M.min);
    A.scale = this.plotWidth / (A.max - A.min);
    J.scale = this.plotHeight / (J.max - J.min);
    K.scale = this.plotHeight / (K.max - K.min)
},draw:function() {
    this.drawGrid();
    this.drawLabels();
    this.drawTitles();
    if (this.series.length) {
        this.el.fire("flotr:beforedraw", [this.series,this]);
        for (var A = 0; A < this.series.length; A++) {
            if (!this.series[A].hide) {
                this.drawSeries(this.series[A])
            }
        }
    }
    this.el.fire("flotr:afterdraw", [this.series,this])
},tHoz:function(A, B) {
    B = B || this.axes.x;
    return(A - B.min) * B.scale
},tVert:function(B, A) {
    A = A || this.axes.y;
    return this.plotHeight - (B - A.min) * A.scale
},drawGrid:function() {
    var B,E = this.options,A = this.ctx;
    if (E.grid.verticalLines || E.grid.horizontalLines) {
        this.el.fire("flotr:beforegrid", [this.axes.x,this.axes.y,E,this])
    }
    A.save();
    A.translate(this.plotOffset.left, this.plotOffset.top);
    if (E.grid.backgroundColor != null) {
        A.fillStyle = E.grid.backgroundColor;
        A.fillRect(0, 0, this.plotWidth, this.plotHeight)
    }
    A.lineWidth = 1;
    A.strokeStyle = E.grid.tickColor;
    A.beginPath();
    if (E.grid.verticalLines) {
        for (var D = 0; D < this.axes.x.ticks.length; ++D) {
            B = this.axes.x.ticks[D].v;
            if ((B == this.axes.x.min || B == this.axes.x.max) && E.grid.outlineWidth != 0) {
                continue
            }
            A.moveTo(Math.floor(this.tHoz(B)) + A.lineWidth / 2, 0);
            A.lineTo(Math.floor(this.tHoz(B)) + A.lineWidth / 2, this.plotHeight)
        }
    }
    if (E.grid.horizontalLines) {
        for (var C = 0; C < this.axes.y.ticks.length; ++C) {
            B = this.axes.y.ticks[C].v;
            if ((B == this.axes.y.min || B == this.axes.y.max) && E.grid.outlineWidth != 0) {
                continue
            }
            A.moveTo(0, Math.floor(this.tVert(B)) + A.lineWidth / 2);
            A.lineTo(this.plotWidth, Math.floor(this.tVert(B)) + A.lineWidth / 2)
        }
    }
    A.stroke();
    if (E.grid.outlineWidth != 0) {
        A.lineWidth = E.grid.outlineWidth;
        A.strokeStyle = E.grid.color;
        A.lineJoin = "round";
        A.strokeRect(0, 0, this.plotWidth, this.plotHeight)
    }
    A.restore();
    if (E.grid.verticalLines || E.grid.horizontalLines) {
        this.el.fire("flotr:aftergrid", [this.axes.x,this.axes.y,E,this])
    }
},drawLabels:function() {
    var C = 0,D,B,E,F,G,J = this.options,I = this.ctx,H = this.axes;
    for (E = 0; E < H.x.ticks.length; ++E) {
        if (H.x.ticks[E].label) {
            ++C
        }
    }
    B = this.plotWidth / C;
    if (!J.HtmlText && this.textEnabled) {
        var A = {size:J.fontSize,adjustAlign:true};
        D = H.x;
        A.color = D.options.color || J.grid.color;
        for (E = 0; E < D.ticks.length && D.options.showLabels && D.used; ++E) {
            G = D.ticks[E];
            if (!G.label || G.label.length == 0) {
                continue
            }
            A.angle = Flotr.toRad(D.options.labelsAngle);
            A.halign = "c";
            A.valign = "t";
            I.drawText(G.label, this.plotOffset.left + this.tHoz(G.v, D), this.plotOffset.top + this.plotHeight + J.grid.labelMargin, A)
        }
        D = H.x2;
        A.color = D.options.color || J.grid.color;
        for (E = 0; E < D.ticks.length && D.options.showLabels && D.used; ++E) {
            G = D.ticks[E];
            if (!G.label || G.label.length == 0) {
                continue
            }
            A.angle = Flotr.toRad(D.options.labelsAngle);
            A.halign = "c";
            A.valign = "b";
            I.drawText(G.label, this.plotOffset.left + this.tHoz(G.v, D), this.plotOffset.top + J.grid.labelMargin, A)
        }
        D = H.y;
        A.color = D.options.color || J.grid.color;
        for (E = 0; E < D.ticks.length && D.options.showLabels && D.used; ++E) {
            G = D.ticks[E];
            if (!G.label || G.label.length == 0) {
                continue
            }
            A.angle = Flotr.toRad(D.options.labelsAngle);
            A.halign = "r";
            A.valign = "m";
            I.drawText(G.label, this.plotOffset.left - J.grid.labelMargin, this.plotOffset.top + this.tVert(G.v, D), A)
        }
        D = H.y2;
        A.color = D.options.color || J.grid.color;
        for (E = 0; E < D.ticks.length && D.options.showLabels && D.used; ++E) {
            G = D.ticks[E];
            if (!G.label || G.label.length == 0) {
                continue
            }
            A.angle = Flotr.toRad(D.options.labelsAngle);
            A.halign = "l";
            A.valign = "m";
            I.drawText(G.label, this.plotOffset.left + this.plotWidth + J.grid.labelMargin, this.plotOffset.top + this.tVert(G.v, D), A);
            I.save();
            I.strokeStyle = A.color;
            I.beginPath();
            I.moveTo(this.plotOffset.left + this.plotWidth - 8, this.plotOffset.top + this.tVert(G.v, D));
            I.lineTo(this.plotOffset.left + this.plotWidth, this.plotOffset.top + this.tVert(G.v, D));
            I.stroke();
            I.restore()
        }
    } else {
        if (H.x.options.showLabels || H.x2.options.showLabels || H.y.options.showLabels || H.y2.options.showLabels) {
            F = ['<div style="font-size:smaller;color:' + J.grid.color + ';" class="flotr-labels">'];
            D = H.x;
            if (D.options.showLabels) {
                for (E = 0; E < D.ticks.length; ++E) {
                    G = D.ticks[E];
                    if (!G.label || G.label.length == 0) {
                        continue
                    }
                    F.push('<div style="position:absolute;top:' + (this.plotOffset.top + this.plotHeight + J.grid.labelMargin)
                            + "px;left:" + (this.plotOffset.left + this.tHoz(G.v, D) - B / 2) + "px;width:" + B + "px;text-align:center;"
                            + (D.options.color ? ("color:" + D.options.color + ";") : "") + '" class="flotr-grid-label">'
                            + G.label + "</div>")
                }
            }
            D = H.x2;
            if (D.options.showLabels && D.used) {
                for (E = 0; E < D.ticks.length; ++E) {
                    G = D.ticks[E];
                    if (!G.label || G.label.length == 0) {
                        continue
                    }
                    F.push('<div style="position:absolute;top:' + (this.plotOffset.top - J.grid.labelMargin - D.maxLabel.height)
                            + "px;left:" + (this.plotOffset.left + this.tHoz(G.v, D) - B / 2) + "px;width:" + B + "px;text-align:center;"
                            + (D.options.color ? ("color:" + D.options.color + ";") : "") + '" class="flotr-grid-label">'
                            + G.label + "</div>")
                }
            }
            D = H.y;
            if (D.options.showLabels) {
                for (E = 0; E < D.ticks.length; ++E) {
                    G = D.ticks[E];
                    if (!G.label || G.label.length == 0) {
                        continue
                    }
                    F.push('<div style="position:absolute;top:' + (this.plotOffset.top + this.tVert(G.v, D) - D.maxLabel.height
                            / 2) + "px;left:0;width:" + (this.plotOffset.left - J.grid.labelMargin) + "px;text-align:right;"
                            + (D.options.color ? ("color:" + D.options.color + ";") : "") + '" class="flotr-grid-label">'
                            + G.label + "</div>")
                }
            }
            D = H.y2;
            if (D.options.showLabels && D.used) {
                I.save();
                I.strokeStyle = D.options.color || J.grid.color;
                I.beginPath();
                for (E = 0; E < D.ticks.length; ++E) {
                    G = D.ticks[E];
                    if (!G.label || G.label.length == 0) {
                        continue
                    }
                    F.push('<div style="position:absolute;top:' + (this.plotOffset.top + this.tVert(G.v, D) - D.maxLabel.height
                            / 2) + "px;right:0;width:" + (this.plotOffset.right - J.grid.labelMargin) + "px;text-align:left;"
                            + (D.options.color ? ("color:" + D.options.color + ";") : "") + '" class="flotr-grid-label">'
                            + G.label + "</div>");
                    I.moveTo(this.plotOffset.left + this.plotWidth - 8, this.plotOffset.top + this.tVert(G.v, D));
                    I.lineTo(this.plotOffset.left + this.plotWidth, this.plotOffset.top + this.tVert(G.v, D))
                }
                I.stroke();
                I.restore()
            }
            F.push("</div>");
            this.el.insert(F.join(""))
        }
    }
},drawTitles:function() {
    var D,C = this.options,F = C.grid.labelMargin,B = this.ctx,A = this.axes;
    if (!C.HtmlText && this.textEnabled) {
        var E = {size:C.fontSize,color:C.grid.color,halign:"c"};
        if (C.subtitle) {
            B.drawText(C.subtitle, this.plotOffset.left + this.plotWidth / 2, this.titleHeight + this.subtitleHeight - 2, E)
        }
        E.weight = 1.5;
        E.size *= 1.5;
        if (C.title) {
            B.drawText(C.title, this.plotOffset.left + this.plotWidth / 2, this.titleHeight - 2, E)
        }
        E.weight = 1.8;
        E.size *= 0.8;
        E.adjustAlign = true;
        if (A.x.options.title && A.x.used) {
            E.halign = "c";
            E.valign = "t";
            E.angle = Flotr.toRad(A.x.options.titleAngle);
            B.drawText(A.x.options.title, this.plotOffset.left + this.plotWidth / 2, this.plotOffset.top + A.x.maxLabel.height
                    + this.plotHeight + 2 * F, E)
        }
        if (A.x2.options.title && A.x2.used) {
            E.halign = "c";
            E.valign = "b";
            E.angle = Flotr.toRad(A.x2.options.titleAngle);
            B.drawText(A.x2.options.title, this.plotOffset.left + this.plotWidth / 2, this.plotOffset.top - A.x2.maxLabel.height
                    - 2 * F, E)
        }
        if (A.y.options.title && A.y.used) {
            E.halign = "r";
            E.valign = "m";
            E.angle = Flotr.toRad(A.y.options.titleAngle);
            B.drawText(A.y.options.title, this.plotOffset.left - A.y.maxLabel.width - 2 * F, this.plotOffset.top + this.plotHeight
                    / 2, E)
        }
        if (A.y2.options.title && A.y2.used) {
            E.halign = "l";
            E.valign = "m";
            E.angle = Flotr.toRad(A.y2.options.titleAngle);
            B.drawText(A.y2.options.title, this.plotOffset.left + this.plotWidth + A.y2.maxLabel.width + 2 * F, this.plotOffset.top
                    + this.plotHeight / 2, E)
        }
    } else {
        D = ['<div style="color:' + C.grid.color + ';" class="flotr-titles">'];
        if (C.title) {
            D.push('<div style="position:absolute;top:0;left:' + this.plotOffset.left + "px;font-size:1em;font-weight:bold;text-align:center;width:"
                    + this.plotWidth + 'px;" class="flotr-title">' + C.title + "</div>")
        }
        if (C.subtitle) {
            D.push('<div style="position:absolute;top:' + this.titleHeight + "px;left:" + this.plotOffset.left + "px;font-size:smaller;text-align:center;width:"
                    + this.plotWidth + 'px;" class="flotr-subtitle">' + C.subtitle + "</div>")
        }
        D.push("</div>");
        D.push('<div class="flotr-axis-title" style="font-weight:bold;">');
        if (A.x.options.title && A.x.used) {
            D.push('<div style="position:absolute;top:' + (this.plotOffset.top + this.plotHeight + C.grid.labelMargin + A.x.titleSize.height)
                    + "px;left:" + this.plotOffset.left + "px;width:" + this.plotWidth + 'px;text-align:center;" class="flotr-axis-title">'
                    + A.x.options.title + "</div>")
        }
        if (A.x2.options.title && A.x2.used) {
            D.push('<div style="position:absolute;top:0;left:' + this.plotOffset.left + "px;width:" + this.plotWidth + 'px;text-align:center;" class="flotr-axis-title">'
                    + A.x2.options.title + "</div>")
        }
        if (A.y.options.title && A.y.used) {
            D.push('<div style="position:absolute;top:' + (this.plotOffset.top + this.plotHeight / 2 - A.y.titleSize.height
                    / 2) + 'px;left:0;text-align:right;" class="flotr-axis-title">' + A.y.options.title + "</div>")
        }
        if (A.y2.options.title && A.y2.used) {
            D.push('<div style="position:absolute;top:' + (this.plotOffset.top + this.plotHeight / 2 - A.y.titleSize.height
                    / 2) + 'px;right:0;text-align:right;" class="flotr-axis-title">' + A.y2.options.title + "</div>")
        }
        D.push("</div>");
        this.el.insert(D.join(""))
    }
},drawSeries:function(A) {
    A = A || this.series;
    var C = false;
    for (var B in Flotr._registeredTypes) {
        if (A[B] && A[B].show) {
            this[Flotr._registeredTypes[B]](A);
            C = true
        }
    }
    if (!C) {
        this[Flotr._registeredTypes[this.options.defaultType]](A)
    }
},plotLine:function(I, F) {
    var O = this.ctx,A = I.xaxis,K = I.yaxis,J = this.tHoz.bind(this),M = this.tVert.bind(this),H = I.data;
    if (H.length < 2) {
        return
    }
    var E = J(H[0][0], A),D = M(H[0][1], K) + F;
    O.beginPath();
    O.moveTo(E, D);
    for (var G = 0; G < H.length - 1; ++G) {
        var C = H[G][0],N = H[G][1],B = H[G + 1][0],L = H[G + 1][1];
        if (N === null || L === null) {
            continue
        }
        if (N <= L && N < K.min) {
            if (L < K.min) {
                continue
            }
            C = (K.min - N) / (L - N) * (B - C) + C;
            N = K.min
        } else {
            if (L <= N && L < K.min) {
                if (N < K.min) {
                    continue
                }
                B = (K.min - N) / (L - N) * (B - C) + C;
                L = K.min
            }
        }
        if (N >= L && N > K.max) {
            if (L > K.max) {
                continue
            }
            C = (K.max - N) / (L - N) * (B - C) + C;
            N = K.max
        } else {
            if (L >= N && L > K.max) {
                if (N > K.max) {
                    continue
                }
                B = (K.max - N) / (L - N) * (B - C) + C;
                L = K.max
            }
        }
        if (C <= B && C < A.min) {
            if (B < A.min) {
                continue
            }
            N = (A.min - C) / (B - C) * (L - N) + N;
            C = A.min
        } else {
            if (B <= C && B < A.min) {
                if (C < A.min) {
                    continue
                }
                L = (A.min - C) / (B - C) * (L - N) + N;
                B = A.min
            }
        }
        if (C >= B && C > A.max) {
            if (B > A.max) {
                continue
            }
            N = (A.max - C) / (B - C) * (L - N) + N;
            C = A.max
        } else {
            if (B >= C && B > A.max) {
                if (C > A.max) {
                    continue
                }
                L = (A.max - C) / (B - C) * (L - N) + N;
                B = A.max
            }
        }
        if (E != J(C, A) || D != M(N, K) + F) {
            O.moveTo(J(C, A), M(N, K) + F)
        }
        E = J(B, A);
        D = M(L, K) + F;
        O.lineTo(E, D)
    }
    O.stroke()
},plotLineArea:function(J, D) {
    var S = J.data;
    if (S.length < 2) {
        return
    }
    var L,G = 0,N = this.ctx,Q = J.xaxis,B = J.yaxis,E = this.tHoz.bind(this),M = this.tVert.bind(this),H = Math.min(Math.max(0, B.min), B.max),F = true;
    N.beginPath();
    for (var O = 0; O < S.length - 1; ++O) {
        var R = S[O][0],C = S[O][1],P = S[O + 1][0],A = S[O + 1][1];
        if (R <= P && R < Q.min) {
            if (P < Q.min) {
                continue
            }
            C = (Q.min - R) / (P - R) * (A - C) + C;
            R = Q.min
        } else {
            if (P <= R && P < Q.min) {
                if (R < Q.min) {
                    continue
                }
                A = (Q.min - R) / (P - R) * (A - C) + C;
                P = Q.min
            }
        }
        if (R >= P && R > Q.max) {
            if (P > Q.max) {
                continue
            }
            C = (Q.max - R) / (P - R) * (A - C) + C;
            R = Q.max
        } else {
            if (P >= R && P > Q.max) {
                if (R > Q.max) {
                    continue
                }
                A = (Q.max - R) / (P - R) * (A - C) + C;
                P = Q.max
            }
        }
        if (F) {
            N.moveTo(E(R, Q), M(H, B) + D);
            F = false
        }
        if (C >= B.max && A >= B.max) {
            N.lineTo(E(R, Q), M(B.max, B) + D);
            N.lineTo(E(P, Q), M(B.max, B) + D);
            continue
        } else {
            if (C <= B.min && A <= B.min) {
                N.lineTo(E(R, Q), M(B.min, B) + D);
                N.lineTo(E(P, Q), M(B.min, B) + D);
                continue
            }
        }
        var I = R,K = P;
        if (C <= A && C < B.min && A >= B.min) {
            R = (B.min - C) / (A - C) * (P - R) + R;
            C = B.min
        } else {
            if (A <= C && A < B.min && C >= B.min) {
                P = (B.min - C) / (A - C) * (P - R) + R;
                A = B.min
            }
        }
        if (C >= A && C > B.max && A <= B.max) {
            R = (B.max - C) / (A - C) * (P - R) + R;
            C = B.max
        } else {
            if (A >= C && A > B.max && C <= B.max) {
                P = (B.max - C) / (A - C) * (P - R) + R;
                A = B.max
            }
        }
        if (R != I) {
            L = (C <= B.min) ? L = B.min : B.max;
            N.lineTo(E(I, Q), M(L, B) + D);
            N.lineTo(E(R, Q), M(L, B) + D)
        }
        N.lineTo(E(R, Q), M(C, B) + D);
        N.lineTo(E(P, Q), M(A, B) + D);
        if (P != K) {
            L = (A <= B.min) ? B.min : B.max;
            N.lineTo(E(K, Q), M(L, B) + D);
            N.lineTo(E(P, Q), M(L, B) + D)
        }
        G = Math.max(P, K)
    }
    N.lineTo(E(G, Q), M(H, B) + D);
    N.closePath();
    N.fill()
},drawSeriesLines:function(C) {
    C = C || this.series;
    var B = this.ctx;
    B.save();
    B.translate(this.plotOffset.left, this.plotOffset.top);
    B.lineJoin = "round";
    var D = C.lines.lineWidth;
    var A = C.shadowSize;
    if (A > 0) {
        B.lineWidth = A / 2;
        var E = D / 2 + B.lineWidth / 2;
        B.strokeStyle = "rgba(0,0,0,0.1)";
        this.plotLine(C, E + A / 2);
        B.strokeStyle = "rgba(0,0,0,0.2)";
        this.plotLine(C, E);
        if (C.lines.fill) {
            B.fillStyle = "rgba(0,0,0,0.05)";
            this.plotLineArea(C, E + A / 2)
        }
    }
    B.lineWidth = D;
    B.strokeStyle = C.color;
    if (C.lines.fill) {
        B.fillStyle = C.lines.fillColor != null ? C.lines.fillColor : Flotr.parseColor(C.color).scale(null, null, null, C.lines.fillOpacity).toString();
        this.plotLineArea(C, 0)
    }
    this.plotLine(C, 0);
    B.restore()
},drawSeriesPoints:function(C) {
    var B = this.ctx;
    B.save();
    B.translate(this.plotOffset.left, this.plotOffset.top);
    var D = C.lines.lineWidth;
    var A = C.shadowSize;
    if (A > 0) {
        B.lineWidth = A / 2;
        B.strokeStyle = "rgba(0,0,0,0.1)";
        this.plotPointShadows(C, A / 2 + B.lineWidth / 2, C.points.radius);
        B.strokeStyle = "rgba(0,0,0,0.2)";
        this.plotPointShadows(C, B.lineWidth / 2, C.points.radius)
    }
    B.lineWidth = C.points.lineWidth;
    B.strokeStyle = C.color;
    B.fillStyle = C.points.fillColor != null ? C.points.fillColor : C.color;
    this.plotPoints(C, C.points.radius, C.points.fill);
    B.restore()
},plotPoints:function(C, E, I) {
    var A = C.xaxis,F = C.yaxis,J = this.ctx,D,B = C.data;
    for (D = B.length - 1; D > -1; --D) {
        var H = B[D][0],G = B[D][1];
        if (H < A.min || H > A.max || G < F.min || G > F.max) {
            continue
        }
        J.beginPath();
        J.arc(this.tHoz(H, A), this.tVert(G, F), E, 0, 2 * Math.PI, true);
        if (I) {
            J.fill()
        }
        J.stroke()
    }
},plotPointShadows:function(D, B, F) {
    var A = D.xaxis,G = D.yaxis,J = this.ctx,E,C = D.data;
    for (E = C.length - 1; E > -1; --E) {
        var I = C[E][0],H = C[E][1];
        if (I < A.min || I > A.max || H < G.min || H > G.max) {
            continue
        }
        J.beginPath();
        J.arc(this.tHoz(I, A), this.tVert(H, G) + B, F, 0, Math.PI, false);
        J.stroke()
    }
},drawSeriesBars:function(B) {
    var A = this.ctx,D = B.bars.barWidth,C = Math.min(B.bars.lineWidth, D);
    A.save();
    A.translate(this.plotOffset.left, this.plotOffset.top);
    A.lineJoin = "miter";
    A.lineWidth = C;
    A.strokeStyle = B.color;
    this.plotBarsShadows(B, D, 0, B.bars.fill);
    if (B.bars.fill) {
        A.fillStyle = B.bars.fillColor != null ? B.bars.fillColor : Flotr.parseColor(B.color).scale(null, null, null, B.bars.fillOpacity).toString()
    }
    this.plotBars(B, D, 0, B.bars.fill);
    A.restore()
},plotBars:function(K, N, D, Q) {
    var U = K.data;
    if (U.length < 1) {
        return
    }
    var S = K.xaxis,B = K.yaxis,P = this.ctx,F = this.tHoz.bind(this),O = this.tVert.bind(this);
    for (var R = 0; R < U.length; R++) {
        var J = U[R][0],I = U[R][1];
        var E = true,L = true,A = true;
        var H = 0;
        if (K.bars.stacked) {
            S.values.each(function(W, V) {
                if (V == J) {
                    H = W.stack || 0;
                    W.stack = H + I
                }
            })
        }
        if (K.bars.horizontal) {
            var C = H,T = J + H,G = I,M = I + N
        } else {
            var C = J,T = J + N,G = H,M = I + H
        }
        if (T < S.min || C > S.max || M < B.min || G > B.max) {
            continue
        }
        if (C < S.min) {
            C = S.min;
            E = false
        }
        if (T > S.max) {
            T = S.max;
            if (S.lastSerie != K && K.bars.horizontal) {
                L = false
            }
        }
        if (G < B.min) {
            G = B.min
        }
        if (M > B.max) {
            M = B.max;
            if (B.lastSerie != K && !K.bars.horizontal) {
                L = false
            }
        }
        if (Q) {
            P.beginPath();
            P.moveTo(F(C, S), O(G, B) + D);
            P.lineTo(F(C, S), O(M, B) + D);
            P.lineTo(F(T, S), O(M, B) + D);
            P.lineTo(F(T, S), O(G, B) + D);
            P.fill()
        }
        if (K.bars.lineWidth != 0 && (E || A || L)) {
            P.beginPath();
            P.moveTo(F(C, S), O(G, B) + D);
            P[E ? "lineTo" : "moveTo"](F(C, S), O(M, B) + D);
            P[L ? "lineTo" : "moveTo"](F(T, S), O(M, B) + D);
            P[A ? "lineTo" : "moveTo"](F(T, S), O(G, B) + D);
            P.stroke()
        }
    }
},plotBarsShadows:function(I, K, C) {
    var T = I.data;
    if (T.length < 1) {
        return
    }
    var R = I.xaxis,A = I.yaxis,P = this.ctx,D = this.tHoz.bind(this),M = this.tVert.bind(this),N = this.options.shadowSize;
    for (var Q = 0; Q < T.length; Q++) {
        var H = T[Q][0],G = T[Q][1];
        var E = 0;
        if (I.bars.stacked) {
            R.values.each(function(V, U) {
                if (U == H) {
                    E = V.stackShadow || 0;
                    V.stackShadow = E + G
                }
            })
        }
        if (I.bars.horizontal) {
            var B = E,S = H + E,F = G,J = G + K
        } else {
            var B = H,S = H + K,F = E,J = G + E
        }
        if (S < R.min || B > R.max || J < A.min || F > A.max) {
            continue
        }
        if (B < R.min) {
            B = R.min
        }
        if (S > R.max) {
            S = R.max
        }
        if (F < A.min) {
            F = A.min
        }
        if (J > A.max) {
            J = A.max
        }
        var O = D(S, R) - D(B, R) - ((D(S, R) + N <= this.plotWidth) ? 0 : N);
        var L = Math.max(0, M(F, A) - M(J, A) - ((M(F, A) + N <= this.plotHeight) ? 0 : N));
        P.fillStyle = "rgba(0,0,0,0.05)";
        P.fillRect(Math.min(D(B, R) + N, this.plotWidth), Math.min(M(J, A) + N, this.plotWidth), O, L)
    }
},drawSeriesCandles:function(B) {
    var A = this.ctx,C = B.candles.candleWidth;
    A.save();
    A.translate(this.plotOffset.left, this.plotOffset.top);
    A.lineJoin = "miter";
    A.lineWidth = B.candles.lineWidth;
    this.plotCandlesShadows(B, C / 2);
    this.plotCandles(B, C / 2);
    A.restore()
},plotCandles:function(K, D) {
    var W = K.data;
    if (W.length < 1) {
        return
    }
    var T = K.xaxis,B = K.yaxis,P = this.ctx,E = this.tHoz.bind(this),O = this.tVert.bind(this);
    for (var S = 0; S < W.length; S++) {
        var U = W[S],J = U[0],L = U[1],I = U[2],X = U[3],N = U[4];
        var C = J,V = J + K.candles.candleWidth,G = Math.max(B.min, X),M = Math.min(B.max, I),A = Math.max(B.min, Math.min(L, N)),R = Math.min(B.max, Math.max(L, N));
        if (V < T.min || C > T.max || M < B.min || G > B.max) {
            continue
        }
        var Q = K.candles[L > N ? "downFillColor" : "upFillColor"];
        if (K.candles.fill && !K.candles.barcharts) {
            P.fillStyle = Flotr.parseColor(Q).scale(null, null, null, K.candles.fillOpacity).toString();
            P.fillRect(E(C, T), O(R, B) + D, E(V, T) - E(C, T), O(A, B) - O(R, B))
        }
        if (K.candles.lineWidth || K.candles.wickLineWidth) {
            var J,H,F = (K.candles.wickLineWidth % 2) / 2;
            J = Math.floor(E((C + V) / 2), T) + F;
            P.save();
            P.strokeStyle = Q;
            P.lineWidth = K.candles.wickLineWidth;
            P.lineCap = "butt";
            if (K.candles.barcharts) {
                P.beginPath();
                P.moveTo(J, Math.floor(O(M, B) + D));
                P.lineTo(J, Math.floor(O(G, B) + D));
                H = Math.floor(O(L, B) + D) + 0.5;
                P.moveTo(Math.floor(E(C, T)) + F, H);
                P.lineTo(J, H);
                H = Math.floor(O(N, B) + D) + 0.5;
                P.moveTo(Math.floor(E(V, T)) + F, H);
                P.lineTo(J, H)
            } else {
                P.strokeRect(E(C, T), O(R, B) + D, E(V, T) - E(C, T), O(A, B) - O(R, B));
                P.beginPath();
                P.moveTo(J, Math.floor(O(R, B) + D));
                P.lineTo(J, Math.floor(O(M, B) + D));
                P.moveTo(J, Math.floor(O(A, B) + D));
                P.lineTo(J, Math.floor(O(G, B) + D))
            }
            P.stroke();
            P.restore()
        }
    }
},plotCandlesShadows:function(H, C) {
    var T = H.data;
    if (T.length < 1 || H.candles.barcharts) {
        return
    }
    var Q = H.xaxis,A = H.yaxis,D = this.tHoz.bind(this),M = this.tVert.bind(this),N = this.options.shadowSize;
    for (var P = 0; P < T.length; P++) {
        var R = T[P],G = R[0],I = R[1],F = R[2],U = R[3],K = R[4];
        var B = G,S = G + H.candles.candleWidth,E = Math.max(A.min, Math.min(I, K)),J = Math.min(A.max, Math.max(I, K));
        if (S < Q.min || B > Q.max || J < A.min || E > A.max) {
            continue
        }
        var O = D(S, Q) - D(B, Q) - ((D(S, Q) + N <= this.plotWidth) ? 0 : N);
        var L = Math.max(0, M(E, A) - M(J, A) - ((M(E, A) + N <= this.plotHeight) ? 0 : N));
        this.ctx.fillStyle = "rgba(0,0,0,0.05)";
        this.ctx.fillRect(Math.min(D(B, Q) + N, this.plotWidth), Math.min(M(J, A) + N, this.plotWidth), O, L)
    }
},drawSeriesPie:function(G) {
    if (!this.options.pie.drawn) {
        var K = this.ctx,C = this.options,E = G.pie.lineWidth,I = G.shadowSize,R = G.data,D = (Math.min(this.canvasWidth, this.canvasHeight)
                * G.pie.sizeRatio) / 2,H = [];
        var L = 1;
        var P = Math.sin(G.pie.viewAngle) * G.pie.spliceThickness / L;
        var M = {size:C.fontSize * 1.2,color:C.grid.color,weight:1.5};
        var Q = {x:(this.canvasWidth + this.plotOffset.left) / 2,y:(this.canvasHeight - this.plotOffset.bottom) / 2};
        var O = this.series.collect(function(T, S) {
            if (T.pie.show) {
                return{name:(T.label || T.data[0][1]),value:[S,T.data[0][1]],explode:T.pie.explode}
            }
        });
        var B = O.pluck("value").pluck(1).inject(0, function(S, T) {
            return S + T
        });
        var F = 0,N = G.pie.startAngle,J = 0;
        var A = O.collect(function(S) {
            N += F;
            J = parseFloat(S.value[1]);
            F = J / B;
            return{name:S.name,fraction:F,x:S.value[0],y:J,explode:S.explode,startAngle:2 * N * Math.PI,endAngle:2 * (N
                    + F) * Math.PI}
        });
        K.save();
        if (I > 0) {
            A.each(function(V) {
                var S = (V.startAngle + V.endAngle) / 2;
                var T = Q.x + Math.cos(S) * V.explode + I;
                var U = Q.y + Math.sin(S) * V.explode + I;
                this.plotSlice(T, U, D, V.startAngle, V.endAngle, false, L);
                K.fillStyle = "rgba(0,0,0,0.1)";
                K.fill()
            }, this)
        }
        if (C.HtmlText) {
            H = ['<div style="color:' + this.options.grid.color + '" class="flotr-labels">']
        }
        A.each(function(c, X) {
            var W = (c.startAngle + c.endAngle) / 2;
            var V = C.colors[X];
            var Y = Q.x + Math.cos(W) * c.explode;
            var U = Q.y + Math.sin(W) * c.explode;
            this.plotSlice(Y, U, D, c.startAngle, c.endAngle, false, L);
            if (G.pie.fill) {
                K.fillStyle = Flotr.parseColor(V).scale(null, null, null, G.pie.fillOpacity).toString();
                K.fill()
            }
            K.lineWidth = E;
            K.strokeStyle = V;
            K.stroke();
            var b = C.pie.labelFormatter(c);
            var S = (Math.cos(W) < 0);
            var a = Y + Math.cos(W) * (G.pie.explode + D);
            var Z = U + Math.sin(W) * (G.pie.explode + D);
            if (c.fraction && b) {
                if (C.HtmlText) {
                    var T = "position:absolute;top:" + (Z - 5) + "px;";
                    if (S) {
                        T += "right:" + (this.canvasWidth - a) + "px;text-align:right;"
                    } else {
                        T += "left:" + a + "px;text-align:left;"
                    }
                    H.push('<div style="' + T + '" class="flotr-grid-label">' + b + "</div>")
                } else {
                    M.halign = S ? "r" : "l";
                    K.drawText(b, a, Z + M.size / 2, M)
                }
            }
        }, this);
        if (C.HtmlText) {
            H.push("</div>");
            this.el.insert(H.join(""))
        }
        K.restore();
        C.pie.drawn = true
    }
},plotSlice:function(B, H, A, E, D, F, G) {
    var C = this.ctx;
    G = G || 1;
    C.save();
    C.scale(1, G);
    C.beginPath();
    C.moveTo(B, H);
    C.arc(B, H, A, E, D, F);
    C.lineTo(B, H);
    C.closePath();
    C.restore()
},plotPie:function() {
},insertLegend:function() {
    if (!this.options.legend.show) {
        return
    }
    var H = this.series,I = this.plotOffset,B = this.options,b = [],A = false,O = this.ctx,R;
    var Q = H.findAll(function(c) {
        return(c.label && !c.hide)
    }).size();
    if (Q) {
        if (!B.HtmlText && this.textEnabled) {
            var T = {size:B.fontSize * 1.1,color:B.grid.color};
            var M = B.legend.position,N = B.legend.margin,L = B.legend.labelBoxWidth,Z = B.legend.labelBoxHeight,S = B.legend.labelBoxMargin,W = I.left
                    + N,U = I.top + N;
            var a = 0;
            for (R = H.length - 1; R > -1; --R) {
                if (!H[R].label || H[R].hide) {
                    continue
                }
                var E = B.legend.labelFormatter(H[R].label);
                a = Math.max(a, O.measureText(E, T))
            }
            var K = Math.round(L + S * 3 + a),C = Math.round(Q * (S + Z) + S);
            if (M.charAt(0) == "s") {
                U = I.top + this.plotHeight - (N + C)
            }
            if (M.charAt(1) == "e") {
                W = I.left + this.plotWidth - (N + K)
            }
            var P = Flotr.parseColor(B.legend.backgroundColor || "rgb(240,240,240)").scale(null, null, null, B.legend.backgroundOpacity
                    || 0.1).toString();
            O.fillStyle = P;
            O.fillRect(W, U, K, C);
            O.strokeStyle = B.legend.labelBoxBorderColor;
            O.strokeRect(Flotr.toPixel(W), Flotr.toPixel(U), K, C);
            var G = W + S;
            var F = U + S;
            for (R = 0; R < H.length; R++) {
                if (!H[R].label || H[R].hide) {
                    continue
                }
                var E = B.legend.labelFormatter(H[R].label);
                O.fillStyle = H[R].color;
                O.fillRect(G, F, L - 1, Z - 1);
                O.strokeStyle = B.legend.labelBoxBorderColor;
                O.lineWidth = 1;
                O.strokeRect(Math.ceil(G) - 1.5, Math.ceil(F) - 1.5, L + 2, Z + 2);
                O.drawText(E, G + L + S, F + (Z + T.size - O.fontDescent(T)) / 2, T);
                F += Z + S
            }
        } else {
            for (R = 0; R < H.length; ++R) {
                if (!H[R].label || H[R].hide) {
                    continue
                }
                if (R % B.legend.noColumns == 0) {
                    b.push(A ? "</tr><tr>" : "<tr>");
                    A = true
                }
                var E = B.legend.labelFormatter(H[R].label);
                b.push('<td class="flotr-legend-color-box"><div style="border:1px solid ' + B.legend.labelBoxBorderColor
                        + ';padding:1px"><div style="width:' + B.legend.labelBoxWidth + "px;height:" + B.legend.labelBoxHeight
                        + "px;background-color:" + H[R].color + '"></div></div></td><td class="flotr-legend-label">' + E
                        + "</td>")
            }
            if (A) {
                b.push("</tr>")
            }
            if (b.length > 0) {
                var V = '<table style="font-size:smaller;color:' + B.grid.color + '">' + b.join("") + "</table>";
                if (B.legend.container != null) {
                    $(B.legend.container).update(V)
                } else {
                    var D = "";
                    var M = B.legend.position,N = B.legend.margin;
                    if (M.charAt(0) == "n") {
                        D += "top:" + (N + I.top) + "px;"
                    } else {
                        if (M.charAt(0) == "s") {
                            D += "bottom:" + (N + I.bottom) + "px;"
                        }
                    }
                    if (M.charAt(1) == "e") {
                        D += "right:" + (N + I.right) + "px;"
                    } else {
                        if (M.charAt(1) == "w") {
                            D += "left:" + (N + I.left) + "px;"
                        }
                    }
                    var J = this.el.insert('<div class="flotr-legend" style="position:absolute;z-index:2;' + D + '">' + V
                            + "</div>").select("div.flotr-legend").first();
                    if (B.legend.backgroundOpacity != 0) {
                        var Y = B.legend.backgroundColor;
                        if (Y == null) {
                            var X = (B.grid.backgroundColor != null) ? B.grid.backgroundColor : Flotr.extractColor(J);
                            Y = Flotr.parseColor(X).adjust(null, null, null, 1).toString()
                        }
                        this.el.insert('<div class="flotr-legend-bg" style="position:absolute;width:' + J.getWidth() + "px;height:"
                                + J.getHeight() + "px;" + D + "background-color:" + Y + ';"> </div>').select("div.flotr-legend-bg").first().setStyle({opacity:B.legend.backgroundOpacity})
                    }
                }
            }
        }
    }
},getEventPosition:function(C) {
    var G = this.overlay.cumulativeOffset(),F = (C.pageX - G.left - this.plotOffset.left),E = (C.pageY - G.top - this.plotOffset.top),D = 0,B = 0;
    if (C.pageX == null && C.clientX != null) {
        var H = document.documentElement,A = document.body;
        D = C.clientX + (H && H.scrollLeft || A.scrollLeft || 0);
        B = C.clientY + (H && H.scrollTop || A.scrollTop || 0)
    } else {
        D = C.pageX;
        B = C.pageY
    }
    return{x:this.axes.x.min + F / this.axes.x.scale,x2:this.axes.x2.min + F / this.axes.x2.scale,y:this.axes.y.max - E
            / this.axes.y.scale,y2:this.axes.y2.max - E / this.axes.y2.scale,relX:F,relY:E,absX:D,absY:B}
},clickHandler:function(A) {
    if (this.ignoreClick) {
        this.ignoreClick = false;
        return
    }
    this.el.fire("flotr:click", [this.getEventPosition(A),this])
},mouseMoveHandler:function(A) {
    var B = this.getEventPosition(A);
    this.lastMousePos.pageX = B.absX;
    this.lastMousePos.pageY = B.absY;
    if (this.selectionInterval == null && (this.options.mouse.track || this.series.any(function(C) {
        return C.mouse && C.mouse.track
    }))) {
        this.hit(B)
    }
    this.el.fire("flotr:mousemove", [A,B,this])
},mouseDownHandler:function(C) {
    if (C.isRightClick()) {
        C.stop();
        var B = this.overlay;
        B.hide();
        function A() {
            B.show();
            $(document).stopObserving("mousemove", A)
        }
        $(document).observe("mousemove", A);
        return
    }
    if (!this.options.selection.mode || !C.isLeftClick()) {
        return
    }
    this.setSelectionPos(this.selection.first, C);
    if (this.selectionInterval != null) {
        clearInterval(this.selectionInterval)
    }
    this.lastMousePos.pageX = null;
    this.selectionInterval = setInterval(this.updateSelection.bind(this), 1000 / this.options.selection.fps);
    this.mouseUpHandler = this.mouseUpHandler.bind(this);
    $(document).observe("mouseup", this.mouseUpHandler)
},fireSelectEvent:function() {
    var A = this.axes,F = this.selection,C = (F.first.x <= F.second.x) ? F.first.x : F.second.x,B = (F.first.x <= F.second.x) ? F.second.x : F.first.x,E = (F.first.y
            >= F.second.y) ? F.first.y : F.second.y,D = (F.first.y >= F.second.y) ? F.second.y : F.first.y;
    C = A.x.min + C / A.x.scale;
    B = A.x.min + B / A.x.scale;
    E = A.y.max - E / A.y.scale;
    D = A.y.max - D / A.y.scale;
    this.el.fire("flotr:select", [{x1:C,y1:E,x2:B,y2:D},this])
},mouseUpHandler:function(A) {
    $(document).stopObserving("mouseup", this.mouseUpHandler);
    A.stop();
    if (this.selectionInterval != null) {
        clearInterval(this.selectionInterval);
        this.selectionInterval = null
    }
    this.setSelectionPos(this.selection.second, A);
    this.clearSelection();
    if (this.selectionIsSane()) {
        this.drawSelection();
        this.fireSelectEvent();
        this.ignoreClick = true
    }
},setSelectionPos:function(D, B) {
    var A = this.options,C = $(this.overlay).cumulativeOffset();
    if (A.selection.mode.indexOf("x") == -1) {
        D.x = (D == this.selection.first) ? 0 : this.plotWidth
    } else {
        D.x = B.pageX - C.left - this.plotOffset.left;
        D.x = Math.min(Math.max(0, D.x), this.plotWidth)
    }
    if (A.selection.mode.indexOf("y") == -1) {
        D.y = (D == this.selection.first) ? 0 : this.plotHeight
    } else {
        D.y = B.pageY - C.top - this.plotOffset.top;
        D.y = Math.min(Math.max(0, D.y), this.plotHeight)
    }
},updateSelection:function() {
    if (this.lastMousePos.pageX == null) {
        return
    }
    this.setSelectionPos(this.selection.second, this.lastMousePos);
    this.clearSelection();
    if (this.selectionIsSane()) {
        this.drawSelection()
    }
},clearSelection:function() {
    if (this.prevSelection == null) {
        return
    }
    var G = this.prevSelection,E = this.octx,C = this.plotOffset,A = Math.min(G.first.x, G.second.x),F = Math.min(G.first.y, G.second.y),B = Math.abs(G.second.x
            - G.first.x),D = Math.abs(G.second.y - G.first.y);
    E.clearRect(A + C.left - E.lineWidth, F + C.top - E.lineWidth, B + E.lineWidth * 2, D + E.lineWidth * 2);
    this.prevSelection = null
},setSelection:function(G) {
    var B = this.options,H = this.axes.x,A = this.axes.y,F = yaxis.scale,D = xaxis.scale,E = B.selection.mode.indexOf("x")
            != -1,C = B.selection.mode.indexOf("y") != -1;
    this.clearSelection();
    this.selection.first.y = E ? 0 : (A.max - G.y1) * F;
    this.selection.second.y = E ? this.plotHeight : (A.max - G.y2) * F;
    this.selection.first.x = C ? 0 : (G.x1 - H.min) * D;
    this.selection.second.x = C ? this.plotWidth : (G.x2 - H.min) * D;
    this.drawSelection();
    this.fireSelectEvent()
},drawSelection:function() {
    var C = this.prevSelection,F = this.selection,H = this.octx,I = this.options,A = this.plotOffset;
    if (C != null && F.first.x == C.first.x && F.first.y == C.first.y && F.second.x == C.second.x && F.second.y == C.second.y) {
        return
    }
    H.strokeStyle = Flotr.parseColor(I.selection.color).scale(null, null, null, 0.8).toString();
    H.lineWidth = 1;
    H.lineJoin = "round";
    H.fillStyle = Flotr.parseColor(I.selection.color).scale(null, null, null, 0.4).toString();
    this.prevSelection = {first:{x:F.first.x,y:F.first.y},second:{x:F.second.x,y:F.second.y}};
    var E = Math.min(F.first.x, F.second.x),D = Math.min(F.first.y, F.second.y),G = Math.abs(F.second.x - F.first.x),B = Math.abs(F.second.y
            - F.first.y);
    H.fillRect(E + A.left, D + A.top, G, B);
    H.strokeRect(E + A.left, D + A.top, G, B)
},selectionIsSane:function() {
    var A = this.selection;
    return Math.abs(A.second.x - A.first.x) >= 5 && Math.abs(A.second.y - A.first.y) >= 5
},clearHit:function() {
    if (this.prevHit) {
        var B = this.options,A = this.plotOffset,C = this.prevHit;
        this.octx.clearRect(this.tHoz(C.x) + A.left - B.points.radius * 2, this.tVert(C.y) + A.top - B.points.radius * 2, B.points.radius
                * 3 + B.points.lineWidth * 3, B.points.radius * 3 + B.points.lineWidth * 3);
        this.prevHit = null
    }
},hit:function(I) {
    var G = this.series,C = this.options,R = this.prevHit,H = this.plotOffset,D = this.octx,S,A,M,Q,L = {dist:Number.MAX_VALUE,x:null,y:null,relX:I.relX,relY:I.relY,absX:I.absX,absY:I.absY,mouse:null};
    for (Q = 0; Q < G.length; Q++) {
        s = G[Q];
        if (!s.mouse.track) {
            continue
        }
        S = s.data;
        A = (s.xaxis.scale * s.mouse.sensibility);
        M = (s.yaxis.scale * s.mouse.sensibility);
        for (var P = 0,B,E; P < S.length; P++) {
            if (S[P][1] === null) {
                continue
            }
            B = Math.pow(s.xaxis.scale * (S[P][0] - I.x), 2);
            E = Math.pow(s.yaxis.scale * (S[P][1] - I.y), 2);
            if (B < A && E < M && Math.sqrt(B + E) < L.dist) {
                L.dist = Math.sqrt(B + E);
                L.x = S[P][0];
                L.y = S[P][1];
                L.mouse = s.mouse
            }
        }
    }
    if (L.mouse && L.mouse.track && !R || (R && (L.x != R.x || L.y != R.y))) {
        var K = this.mouseTrack || this.el.select(".flotr-mouse-value")[0],F = "",J = C.mouse.position,N = C.mouse.margin,O = "opacity:0.7;background-color:#000;color:#fff;display:none;position:absolute;padding:2px 8px;-moz-border-radius:4px;border-radius:4px;white-space:nowrap;";
        if (!C.mouse.relative) {
            if (J.charAt(0) == "n") {
                F += "top:" + (N + H.top) + "px;"
            } else {
                if (J.charAt(0) == "s") {
                    F += "bottom:" + (N + H.bottom) + "px;"
                }
            }
            if (J.charAt(1) == "e") {
                F += "right:" + (N + H.right) + "px;"
            } else {
                if (J.charAt(1) == "w") {
                    F += "left:" + (N + H.left) + "px;"
                }
            }
        } else {
            if (J.charAt(0) == "n") {
                F += "bottom:" + (N - H.top - this.tVert(L.y) + this.canvasHeight) + "px;"
            } else {
                if (J.charAt(0) == "s") {
                    F += "top:" + (N + H.top + this.tVert(L.y)) + "px;"
                }
            }
            if (J.charAt(1) == "e") {
                F += "left:" + (N + H.left + this.tHoz(L.x)) + "px;"
            } else {
                if (J.charAt(1) == "w") {
                    F += "right:" + (N - H.left - this.tHoz(L.x) + this.canvasWidth) + "px;"
                }
            }
        }
        O += F;
        if (!K) {
            this.el.insert('<div class="flotr-mouse-value" style="' + O + '"></div>');
            K = this.mouseTrack = this.el.select(".flotr-mouse-value").first()
        } else {
            this.mouseTrack = K.setStyle(O)
        }
        if (L.x !== null && L.y !== null) {
            K.show();
            this.clearHit();
            if (L.mouse.lineColor != null) {
                D.save();
                D.translate(H.left, H.top);
                D.lineWidth = C.points.lineWidth;
                D.strokeStyle = L.mouse.lineColor;
                D.fillStyle = "#ffffff";
                D.beginPath();
                D.arc(this.tHoz(L.x), this.tVert(L.y), C.mouse.radius, 0, 2 * Math.PI, true);
                D.fill();
                D.stroke();
                D.restore()
            }
            this.prevHit = L;
            var T = L.mouse.trackDecimals;
            if (T == null || T < 0) {
                T = 0
            }
            K.innerHTML = L.mouse.trackFormatter({x:L.x.toFixed(T),y:L.y.toFixed(T)});
            K.fire("flotr:hit", [L,this])
        } else {
            if (R) {
                K.hide();
                this.clearHit()
            }
        }
    }
},saveImage:function(D, C, A, B) {
    var E = null;
    switch (D) {case"jpeg":case"jpg":E = Canvas2Image.saveAsJPEG(this.canvas, B, C, A);break;default:case"png":E = Canvas2Image.saveAsPNG(this.canvas, B, C, A);break;case"bmp":E = Canvas2Image.saveAsBMP(this.canvas, B, C, A);break}
    if (Object.isElement(E) && B) {
        this.restoreCanvas();
        this.canvas.hide();
        this.overlay.hide();
        this.el.insert(E.setStyle({position:"absolute"}))
    }
},restoreCanvas:function() {
    this.canvas.show();
    this.overlay.show();
    this.el.select("img").invoke("remove")
}});
Flotr.Color = Class.create({initialize:function(E, D, B, C) {
    this.rgba = ["r","g","b","a"];
    var A = 4;
    while (-1 < --A) {
        this[this.rgba[A]] = arguments[A] || ((A == 3) ? 1 : 0)
    }
    this.normalize()
},adjust:function(D, C, E, B) {
    var A = 4;
    while (-1 < --A) {
        if (arguments[A] != null) {
            this[this.rgba[A]] += arguments[A]
        }
    }
    return this.normalize()
},clone:function() {
    return new Flotr.Color(this.r, this.b, this.g, this.a)
},limit:function(B, A, C) {
    return Math.max(Math.min(B, C), A)
},normalize:function() {
    var A = this.limit;
    this.r = A(parseInt(this.r), 0, 255);
    this.g = A(parseInt(this.g), 0, 255);
    this.b = A(parseInt(this.b), 0, 255);
    this.a = A(this.a, 0, 1);
    return this
},scale:function(D, C, E, B) {
    var A = 4;
    while (-1 < --A) {
        if (arguments[A] != null) {
            this[this.rgba[A]] *= arguments[A]
        }
    }
    return this.normalize()
},distance:function(B) {
    if (!B) {
        return
    }
    B = new Flotr.parseColor(B);
    var C = 0;
    var A = 3;
    while (-1 < --A) {
        C += Math.abs(this[this.rgba[A]] - B[this.rgba[A]])
    }
    return C
},toString:function() {
    return(this.a >= 1) ? "rgb(" + [this.r,this.g,this.b].join(",") + ")" : "rgba(" + [this.r,this.g,this.b,this.a].join(",")
            + ")"
}});
Flotr.Color.lookupColors = {aqua:[0,255,255],azure:[240,255,255],beige:[245,245,220],black:[0,0,0],blue:[0,0,255],brown:[165,42,42],cyan:[0,255,255],darkblue:[0,0,139],darkcyan:[0,139,139],darkgrey:[169,169,169],darkgreen:[0,100,0],darkkhaki:[189,183,107],darkmagenta:[139,0,139],darkolivegreen:[85,107,47],darkorange:[255,140,0],darkorchid:[153,50,204],darkred:[139,0,0],darksalmon:[233,150,122],darkviolet:[148,0,211],fuchsia:[255,0,255],gold:[255,215,0],green:[0,128,0],indigo:[75,0,130],khaki:[240,230,140],lightblue:[173,216,230],lightcyan:[224,255,255],lightgreen:[144,238,144],lightgrey:[211,211,211],lightpink:[255,182,193],lightyellow:[255,255,224],lime:[0,255,0],magenta:[255,0,255],maroon:[128,0,0],navy:[0,0,128],olive:[128,128,0],orange:[255,165,0],pink:[255,192,203],purple:[128,0,128],violet:[128,0,128],red:[255,0,0],silver:[192,192,192],white:[255,255,255],yellow:[255,255,0]};
Flotr.Date = {format:function(F, E) {
    if (!F) {
        return
    }
    var A = function(H) {
        H = H.toString();
        return H.length == 1 ? "0" + H : H
    };
    var D = [];
    var C = false;
    for (var B = 0; B < E.length; ++B) {
        var G = E.charAt(B);
        if (C) {
            switch (G) {case"h":G = F.getUTCHours().toString();break;case"H":G = A(F.getUTCHours());break;case"M":G = A(F.getUTCMinutes());break;case"S":G = A(F.getUTCSeconds());break;case"d":G = F.getUTCDate().toString();break;case"m":G = (F.getUTCMonth()
                    + 1).toString();break;case"y":G = F.getUTCFullYear().toString();break;case"b":G = Flotr.Date.monthNames[F.getUTCMonth()];break}
            D.push(G);
            C = false
        } else {
            if (G == "%") {
                C = true
            } else {
                D.push(G)
            }
        }
    }
    return D.join("")
},timeUnits:{second:1000,minute:60 * 1000,hour:60 * 60 * 1000,day:24 * 60 * 60 * 1000,month:30 * 24 * 60 * 60 * 1000,year:365.2425
        * 24 * 60 * 60 * 1000},spec:[[1,"second"],[2,"second"],[5,"second"],[10,"second"],[30,"second"],[1,"minute"],[2,"minute"],[5,"minute"],[10,"minute"],[30,"minute"],[1,"hour"],[2,"hour"],[4,"hour"],[8,"hour"],[12,"hour"],[1,"day"],[2,"day"],[3,"day"],[0.25,"month"],[0.5,"month"],[1,"month"],[2,"month"],[3,"month"],[6,"month"],[1,"year"]],monthNames:["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"]};