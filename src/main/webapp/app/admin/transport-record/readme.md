.withOption('searching', false)
.withOption('info', false)
.withOption('paging', false);
常规配置：

1.固定行列位置
fixedRowsTop:行数 //固定顶部多少行不能垂直滚动
fixedColumnsLeft:列数 //固定左侧多少列不能水平滚动
fill-handle="false" 单元格右下角不填充
destoryEditor(reverOriginal)
去除当前编辑器，并选中当前单元格，渲染上该效果。如果reverOriginal不是true则被编辑的数据将被保存，如果为true，则会恢复之前的数据，不保存新的数据到单元格。
isEmptyCol(col):根据列索引判断该列是否为空
isEmptyRow(row):根据行索引判断该行是否为空
setCellMeta(row,col,key,val)设置参数属性和值到指定行列的单元格
setDataAtCell(row,col,value,source)设置新值到一个单元格
populateFormArray(start,input,end,source,method,direction,deltas):使用二维数组填充单元格

其中，start:开始位置

            input：二维数组

            end：结束位置

            source：默认为populateFromArray

            method：默认为overwrite

            direction：left/right/top/bottom

            deltas:其值为一个数组
