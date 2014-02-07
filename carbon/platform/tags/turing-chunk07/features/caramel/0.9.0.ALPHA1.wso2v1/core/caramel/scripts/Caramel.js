var Caramel = ( function () {

    var modules = {};

    var requirs = {};

    var templates = {};

    var initializers = {};

    var bloks = {};

    var data;

    var md = require("markdown").Markdown;

    var converter = new md.Converter();

    var Handle = require("handlebars").Handlebars;

    var setData = function (d) {
        data = d;
    };

    var getData = function () {
        return data;
    };

    var getUser = function () {
        return session.get("logged.user");
    };

    var setUser = function (user) {
        session.put("logged.user", user);
    };

    var getThemeFile = function (path) {
        var p, index, theme = getUserTheme();
        if (theme.subtheme) {
            p = getThemePath() + "subthemes/" + theme.subtheme + "/" + path;
            index = p.indexOf("?");
            if (new File(p.substring(0, index == -1 ? p.length : index)).isExists()) {
                return p;
            }
        }
        return getThemePath() + path;
    };

    var getThemesPath = function () {
        return "/themes/";
    };

    var getThemePath = function () {
        return getThemesPath() + getUserTheme().base + "/";
    };

    /**
     * Fetching the block
     * @param name
     */
    var getBlockFile = function (name) {
        var blockPath = getBlocksDir() + name + "/block.js";
        var bf = new File(blockPath);

        if (!bf.isExists()) {
            return getDefaultBlock(name);
        }
        return getBlocksDir() + name + "/block.js";
    };

    /**
     * Reading the content files: markdowns and other minimal inputs
     * @param name
     */
    var buildInputFromContent = function (name) {
        var inputJson = require(getBlocksDir() + name + "/content/inputs.json");
        var contentDir = new File(getBlocksDir() + name + "/content");
        if (contentDir.isDirectory()) {
            var flist = contentDir.listFiles();
            for (var f in flist) {
                if (flist.hasOwnProperty(f)) {
                    if (flist[f].getName().split('.')[1] === 'md') {
                        flist[f].open('r');
                        inputJson[flist[f].getName().split('.')[0]] = converter.makeHtml(flist[f].readAll());
                        flist[f].close();
                    }
                }
            }
        }

        return inputJson;
    };

    /**
     *  When there is no block file associated, a default block is returned
     * @param name = the block name
     */
    var getDefaultBlock = function (name) {

        var inputJson = buildInputFromContent(name);

        return function () {
            block(name, {
                initialize:function (data) {

                },

                getInputs:function () {
                    return inputJson;
                },

                getOutputs:function (inputs) {
                    return inputs;
                },

                getStaticBlocks:function () {
                    return [];
                },

                getInputBlocks:function () {
                    return [];
                }
            });
        };
    };

    var getInitializerFile = function (name) {
        return getThemeFile("templates/" + name + "/initializer.js");
    };

    var getTemplateFile = function (name) {
        return getThemeFile("templates/" + name + "/template.mu");
    };

    var getTemplatePath = function (themeDir, name) {
        return themeDir + "templates/" + name + "/template.mu";
    };

    var getModuleFile = function (name) {
        return getModulesDir() + name + "/module.js";
    };

    var getBlocksDir = function () {
        return "/blocks/";
    };

    var getThemesDir = function () {
        return "/themes/";
    };

    var getModulesDir = function () {
        return "/modules/";
    };

    var getSiteConf = function () {
	    return require("/conf/site.json");
    };

    var getTheme = function () {
        //TODO : remove following lines if theme switching need to be avoided
        var site = require("/conf/site.json"), theme = request.getParameter("theme"), subtheme = request.getParameter("subtheme");
        return {
            base:theme ? theme : site.theme.base,
            subtheme:subtheme ? subtheme : site.theme.subtheme
        };
    };

    var getUserTheme = function () {
        return session.get("theme") ? session.get("theme") : getTheme();
    };

    var mergeParams = function (extInputs, defInputs) {
        var key, obj;
        extInputs = extInputs || {};
        for (key in defInputs) {
            if (defInputs.hasOwnProperty(key)) {
                obj = extInputs[key];
                if (!obj) {
                    extInputs[key] = defInputs[key];
                }
            }
        }
        return extInputs;
    };

    var renderBlock = function (name, inputs, outputs, populate) {
        //initializeTemplate({name:name, params:null}, Caramel);

        var init, tmpl, blok, log = new Log();

        tmpl = template(name);
        if (!tmpl) {
            log.error("Template header and footer includes are missing for : " + name);
        }
        if (populate) {
            blok = block(name);

            if (!inputs) {
                inputs = blok.getInputs ? blok.getInputs() : {};
            } else {
                mergeParams(inputs, blok.getInputs ? blok.getInputs() : null);
            }

            if (blok.getOutputs) {
                outputs = blok.getOutputs(inputs);
            } else if (blok.getInputs) {
                outputs = inputs;
            } else {
                outputs = {};
            }
        }
        init = initializer(name);
        if (init.postInitialize) {
            init.postInitialize(inputs, outputs);
        }
        //fn(inputs, outputs, Caramel);
        return new Handle.SafeString(Handle.compile(tmpl)(outputs));
    };

    var inheritParent = function (blok, name) {
        var parent = require(getBlockFile(name));
        for (var prop in parent) {
            if (parent.hasOwnProperty(prop)) {
                if (!blok[prop]) {
                    blok[prop] = parent[prop];
                }
            }
        }
    };

    var initializeBlock = function (obj) {
        if (!obj) {
            return;
        }
        var extInputs, defInputs, parent, tmpl, inputBlocks, outputBlocks, outputs, tmplInitializer, bloks, i, length, name = obj.name, blok = block(name), log = new Log();

        template(name);
        extInputs = obj.inputs || (obj.inputs = {});
        defInputs = blok.getInputs ? blok.getInputs() : {};
        mergeParams(extInputs, defInputs);

        if (blok.getInputBlocks) {
            inputBlocks = blok.getInputBlocks();
            length = inputBlocks.length;
            for (i = 0; i < length; i++) {
                initializeBlocks(inputBlocks[i], extInputs);
            }
        }

        if (blok.getOutputs) {
            outputs = blok.getOutputs(extInputs);
        } else if (blok.getInputs) {
            outputs = extInputs;
        } else {
            outputs = {};
        }

        obj.outputs = outputs;
        if (blok.getOutputBlocks) {
            outputBlocks = blok.getOutputBlocks();
            length = outputBlocks.length;
            for (i = 0; i < length; i++) {
                initializeBlocks(outputBlocks[i], outputs);
            }
        }

        if (blok.getStaticBlocks) {
            bloks = blok.getStaticBlocks();
            length = bloks.length;
            for (i = 0; i < length; i++) {
                initializeBlock({
                    name:bloks[i],
                    inputs:null
                });
            }
        }
    };

    // [ "foo", "bar", "mar"]
    // [{ "name" : "foo/bar", params : {}}]
    var initializeBlocks = function (keys, inputs) {
        if (!inputs) {
            return;
        }
        var i, length, values, last;
        if (typeof keys !== "string") {
            length = keys.length;
            values = inputs[keys[0]];
            last = (length == 1);
            if (values instanceof Array) {
                length = values.length;
                for (i = 0; i < length; i++) {
                    if (last) {
                        initializeBlock(values[i]);
                    } else {
                        initializeBlocks(keys.slice(1), values[i]);
                    }
                }
            } else {
                if (last) {
                    initializeBlock(values);
                } else {
                    initializeBlocks(keys.slice(1), values);
                }
            }
            return;
        } else {
            values = inputs[keys];
        }

        if (values instanceof Array) {
            length = values.length;
            for (i = 0; i < length; i++) {
                initializeBlock(values[i]);
            }
        } else {
            initializeBlock(values);
        }
    };

    var insertData = function (Caramel, template, parent, name, key, value) {
        var keys, values, data = getData();
        data = data[parent] || (data[parent] = {});
        data = data[name] || (data[name] = {});
        data = data[template] || (data[template] = {});

        keys = data.keys || (data.keys = []);
        values = data.values || (data.values = {});

        keys.push(key);
        values[key] = value;
    };

    var printData = function (tmpls) {
        var key, tmpl, keys, values, i, length, print = "";
        for (key in tmpls) {
            if (tmpls.hasOwnProperty(key)) {
                tmpl = tmpls[key];
                keys = tmpl.keys;
                values = tmpl.values;
                length = keys.length;
                for (i = 0; i < length; i++) {
                    print += (values[keys[i]]) + "\n";
                }
            }
        }
        return print;
    };

    var getUrlMapping = function (path) {
        var urlMap = application.get("url.map"), url, configs, i, length, mapping, mappings, file;
        if (urlMap) {
            url = urlMap[path];
            return url ? url : path;
        }
        file = new File("/Caramelery.conf");
        file.open("r");
        configs = parse(file.readAll());
        file.close();

        urlMap = {};
        mappings = configs.urlMappings;
        length = mappings.length;
        for (i = 0; i < length; i++) {
            mapping = mappings[i];
            urlMap[mapping.path] = mapping.url;
        }
        application.put("url.map", urlMap);
        url = urlMap[path];
        return url ? url : path;
    };

    var getMappedUrl = function (path) {
        return getAbsoluteUrl(getUrlMapping(path));
    };

    var getAbsoluteUrl = function (path) {
        return site.context + path;
    };

    var module = function (name, module) {
        if (module) {
            return modules[name] = module;
        }
        module = modules[name];
        if (module) {
            return module;
        }
        require(getModuleFile(name));
        return modules[name];
    };

    var requir = function (path) {
        var obj = requirs[path];
        return obj ? obj : requirs[path] = require(path);
    };

    var block = function (name, blok) {
        var parent;
        if (blok) {
            return bloks[name] = blok;
        }
        blok = bloks[name];
        if (blok) {
            return blok;
        }
        //we need to include and initialize
        var blockFile = getBlockFile(name);
        if (blockFile instanceof Object) {
            blockFile();
        } else {
            require(blockFile);
        }

        blok = bloks[name];
        parent = blok.getParent;
        if (parent) {
            parent = parent();
            inheritParent(blok, parent);
        }
        if (blok.initialize) {
            //TODO which to pass into initialize method
            blok.initialize(getData());
        }
        return bloks[name];
    };

    var readTemplateFile = function (path) {
        var f = new File(path);
        f.open("r");
        var cont = f.readAll();
        f.close();
        return cont;
    };

    var template = function (name, tmpl) {
        var blok, parent, init;
        if (tmpl) {
            return templates[name] = tmpl;
        }
        tmpl = templates[name];
        if (tmpl) {
            return tmpl;
        }

        blok = block(name);
        parent = blok.getParent;
        if (parent) {
            name = parent();
        }

        tmpl = templates[name];
        if (tmpl) {
            return tmpl;
        }

        tmpl = readTemplateFile(getTemplateFile(name));
        init = initializer(name);
        if (init.preInitialize) {
            init.preInitialize();
        }

        Handle.registerPartial(name.replace("/", "_"), tmpl);
        template(name, tmpl);
    };

    var initializer = function (name, init) {
        var blok, parent;
        if (init) {
            return initializers[name] = init;
        }
        init = initializers[name];
        if (init) {
            return init;
        }

        blok = block(name);
        parent = blok.getParent;
        if (parent) {
            name = parent();
        }

        init = initializers[name];
        if (init) {
            return init;
        }

        require(getInitializerFile(name));
        return initializers[name];
    };

    var render = function (obj) {
        var init, tmpl, inputs, outputs, name = obj.name, log = new Log(), blok;
        setData(obj);
        initializeBlock(obj);

        tmpl = template(name);
        if (!tmpl) {
            log.error("Template header and footer includes are missing for : " + name);
        }
        inputs = obj.inputs;
        blok = block(name);

        if (blok.getOutputs) {
            outputs = blok.getOutputs(inputs);
        } else if (blok.getInputs) {
            outputs = inputs;
        } else {
            outputs = {};
        }
        init = initializer(name);
        if (init.postInitialize) {
            init.postInitialize(inputs, outputs);
        }

        print(Handle.compile(tmpl)(outputs));
    };

    var includeBlock = function (name, inputs) {
        return renderBlock(name, inputs, null, true);
    };

    var includeBlocks = function (bloks) {
        if (!bloks) {
            return;
        }

        var i, d, length;
        if (bloks instanceof Array) {
            length = bloks.length;
            for (i = 0; i < length; i++) {
                d = bloks[i];
                return renderBlock(d.name, d.inputs, d.outputs, false);
            }
        } else {
            return renderBlock(bloks.name, bloks.inputs, bloks.outputs, false);
        }
    };

    var addHeaderCSS = function (template, key, css) {
        css = '<link type="text/css" rel="stylesheet" href="' + site.context + getThemeFile(css) + '"/>';
        insertData(this, template, "header", "css", key, css);
    };

    var addHeaderCSSCode = function (template, key, css) {
        css = '<style type="text/css">' + css + '</style>';
        insertData(this, template, "header", "css", key, css);
    };

    var addHeaderJS = function (template, key, js) {
        if(js.indexOf("http://") < 0) {
                js = '<script type="text/javascript" src="' + site.context + getThemeFile(js) + '"></script>';
        } else {
            js = '<script type="text/javascript" src="' + js + '"></script>';
        }
        insertData(this, template, "header", "js", key, js);
    };

    var addHeaderJSCode = function (template, key, js) {
        js = '<script type="text/javascript">' + js + '</script>';
        insertData(this, template, "header", "js", key, js);
    };

    var addHeaderCode = function (template, key, code) {
        insertData(this, template, "header", "code", key, code);
    };

    var addFooterCSS = function (template, key, css) {
        css = '<link type="text/css" rel="stylesheet" href="' + site.context + getThemeFile(css) + '"/>';
        insertData(this, template, "footer", "css", key, css);
    };

    var addFooterCSSCode = function (template, key, css) {
        css = '<style type="text/css">' + css + '</style>';
        insertData(this, template, "footer", "css", key, css);
    };

    var addFooterJS = function (template, key, js) {
        js = '<script type="text/javascript" src="' + site.context + getThemeFile(js) + '"></script>';
        insertData(this, template, "footer", "js", key, js);
    };

    var addFooterJSCode = function (template, key, js) {
        js = '<script type="text/javascript">' + js + '</script>';
        insertData(this, template, "footer", "js", key, js);
    };

    var addFooterCode = function (template, key, code) {
        insertData(this, template, "footer", "code", key, code);
    };

    var includeJag = function (path) {
        include(getThemeFile(path));
    };

    /**
     * Handlebars helper functions
     */

        // Handlebars helpers to print js
    Handle.registerHelper("appContext", function () {
        return site.context;
    });


    // Handlebars helpers to print js
    Handle.registerHelper("printJS", function () {
        var d = Caramel.data();
        if (d.header && d.header.js) {
            return new Handle.SafeString((Caramel.printData(d.header.js)));
        }
    });

    // Handlebars helpers to print css
    Handle.registerHelper("printCSS", function () {
        var d = Caramel.data();
        if (d.header && d.header.css) {
            return new Handle.SafeString((Caramel.printData(d.header.css)));
        }
    });

    // Handlebars helpers to print code
    Handle.registerHelper("printCode", function (c) {
        var d = Caramel.data();
        if (d.header && d.header.code) {
            return new Handle.SafeString((Caramel.printData(d.header.code)));
        }
    });

    // Handlebars helper to includeBlocks
    Handle.registerHelper("include", includeBlocks);

    /**
     * Helper functions END
     */

    return {
        setUser:setUser,
        getUser:getUser,
        block:block,
        module:module,
        initializer:initializer,
        includeBlock:includeBlock,
        includeBlocks:includeBlocks,
        render:render,
        template:template,
        require:requir,
        getAbsoluteUrl:getAbsoluteUrl,
        getMappedUrl:getMappedUrl,
        printData:printData,
        getUserTheme:getUserTheme,
        getThemeFile:getThemeFile,
        getModulesDir:getModulesDir,
        data:getData,
        addHeaderCSS:addHeaderCSS,
        addHeaderCSSCode:addHeaderCSSCode,
        addHeaderJS:addHeaderJS,
        addHeaderJSCode:addHeaderJSCode,
        addHeaderCode:addHeaderCode,
        addFooterCSS:addFooterCSS,
        addFooterCSSCode:addFooterCSSCode,
        addFooterJS:addFooterJS,
        addFooterJSCode:addFooterJSCode,
        addFooterCode:addFooterCode,
        includeJag:includeJag,
	    getSiteConf:getSiteConf
    };

}());
