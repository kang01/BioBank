/**
 * Created by zhuyu on 2017-04-20.
 */
/**
 * handsontable editor - Tube Editor
 * ------------------------------------------------------------
 */


(function ($, Handsontable) {

    'use strict';

    var TubeEditor = Handsontable.editors.TextEditor.prototype.extend();


    /**
     * ------------------------------------------------------------
     * Create custom editor
     * ------------------------------------------------------------
     */

    $.extend(TubeEditor.prototype, {

        /**
         * ------------------------------------------------------------
         * Init
         * ------------------------------------------------------------
         */

        init: function() {

            // if (typeof $.fn.pickadate === 'undefined') {
            //     throw new Error('Pickadate dependency not found. (https://github.com/amsul/pickadate.js)');
            // }

            // overwrite original
            Handsontable.editors.TextEditor.prototype.init.apply(this, arguments);
        },


        /**
         * ------------------------------------------------------------
         * Prepare
         * ------------------------------------------------------------
         */

        prepare: function (td, row, col, prop, value, cellProperties) {

            // overwrite original
            Handsontable.editors.TextEditor.prototype.prepare.apply(this, arguments);
            this.$input = $(this.TEXTAREA);

            if (this.assign_tube) { return; }
            this.assign_tube = true;
        },




        /**
         * ------------------------------------------------------------
         * Open
         * ------------------------------------------------------------
         */

        open: function() {
            console.log(this);
            console.log(this.TEXTAREA, this.TEXTAREA.value);
            Handsontable.editors.TextEditor.prototype.open.call(this);
            var that = this;
            setTimeout(function() {
                // that.TEXTAREA.style.width = that.TD.clientWidth - 10 + 'px';
                // that.TEXTAREA.style['word-wrap'] = 'break-word;';
                // console.log(that);
            });
        },

        // 从编辑器获取数据对象
        getValue: function() {
            // return this.getInputElement().value;

            var that = this;
            var value = this.getInputElement().value;
            if (that.originalValue){
                that.originalValue.sampleCode = value;
            } else {
                that.originalValue = value;
            }

            return that.originalValue; // returns currently selected date, for example "2013/09/15"
        },

        // 从数据对象获取编辑器
        setValue: function(newValue) {
            // var value = arguments[0] !== (void 0) ? arguments[0] : '';
            // value = value || '';
            // this.getInputElement().value = value;
            // return;

            var that = this;
            if (!that.originalValue){
                that.originalValue = newValue;
            } else if (that.originalValue.sampleCode && that.originalValue.sampleCode !== " ") {
                newValue = that.originalValue.sampleCode;
            } else {
                newValue = that.originalValue.sampleTempCode;
            }

            this.getInputElement().value = newValue;
        },

        getInputElement: function() {
            return this.TEXTAREA;
        }

    });


    /**
     * ------------------------------------------------------------
     * Register custom editor
     * ------------------------------------------------------------
     */

    Handsontable.editors.TubeEditor = TubeEditor;
    Handsontable.editors.registerEditor('tube', TubeEditor);


}).call(this, jQuery, Handsontable);
