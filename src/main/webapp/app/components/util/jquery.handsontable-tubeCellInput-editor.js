/**
 * Created by gaokangkang on 2017/6/20.
 */
/**
 * handsontable editor - Tube Editor
 * ------------------------------------------------------------
 */


(function ($, Handsontable) {

    'use strict';

    var TubeCellEditor = Handsontable.editors.TextEditor.prototype.extend();


    /**
     * ------------------------------------------------------------
     * Create custom editor
     * ------------------------------------------------------------
     */

    $.extend(TubeCellEditor.prototype, {

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

            // this.$input.off("focus").on("focus", function(){
            //     $(this).select();
            // });
            if (this.assign_tube) { return; }
            this.assign_tube = true;
        },




        /**
         * ------------------------------------------------------------
         * Open
         * ------------------------------------------------------------
         */

        open: function() {
            Handsontable.editors.TextEditor.prototype.open.call(this);
            var that = this;
            if (that.getInputElement().value){
                // setTimeout(function(){$(that.getInputElement()).select()},0);
            }
            if (that.originalValue && that.originalValue.flag == 2){
                that.close();
            }
        },

        // 从编辑器获取数据对象
        getValue: function() {
            // return this.getInputElement().value;

            var that = this;
            var value = this.getInputElement().value;
            var data = _.cloneDeep(this.originalValue);
            data.sampleCode = value;

            return data;

            if (that.originalValue){
                that.originalValue.sampleCode = value;
            } else {
                that.originalValue = value;
            }

            return that.originalValue; // returns currently selected date, for example "2013/09/15"
        },

        // 从数据对象获取编辑器
        setValue: function(newValue) {
            var that = this;
            // if (!that.originalValue){
            //     that.originalValue = newValue;
            // } else if (that.originalValue.sampleCode && that.originalValue.sampleCode !== " ") {
            //     newValue = that.originalValue.sampleCode;
            // } else {
            //     newValue = that.originalValue.sampleTempCode;
            // }
            var value = "";
            if (_.isObject(newValue) && newValue.sampleCode){
                value = newValue.sampleCode;
            } else {
                value = this.originalValue.sampleCode;
            }

            this.getInputElement().value = value||"";
        },

        saveValue: function(value, ctrlDown){
            console.log(this, arguments);
            var selection = void 0;
            var tmp = void 0;
            var cellData = _.cloneDeep(this.originalValue);

            // if ctrl+enter and multiple cells selected, behave like Excel (finish editing and apply to all cells)
            if (ctrlDown) {
                selection = this.instance.getSelected();

                if (selection[0] > selection[2]) {
                    tmp = selection[0];
                    selection[0] = selection[2];
                    selection[2] = tmp;
                }
                if (selection[1] > selection[3]) {
                    tmp = selection[1];
                    selection[1] = selection[3];
                    selection[3] = tmp;
                }
            } else {
                selection = [this.row, this.col, null, null];
            }
            if (cellData.sampleCode != value[0][0].sampleCode){
                this.instance.setDataAtCell(this.row, this.col, value[0][0], 'edit');
            }

        },

        // finishEditing: function(revertToOriginal, ctrlDown, callback){
        //     console.log("finishEditing", arguments);
        //     callback();
        // },

        getInputElement: function() {
            return this.TEXTAREA;
        }

    });


    /**
     * ------------------------------------------------------------
     * Register custom editor
     * ------------------------------------------------------------
     */

    Handsontable.editors.TubeCellEditor = TubeCellEditor;
    Handsontable.editors.registerEditor('tubeCellInput', TubeCellEditor);


}).call(this, jQuery, Handsontable);
