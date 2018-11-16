var fs = require('fs')

//第一个参数：文件路径
//第二个参数：文件内容
//第三个参数：回调函数
fs.writeFile('../../views/data/hello.txt', '你好，node js', function (error) {
    if (error) {
        console.log('写入失败')
    } else {
        console.log('文件写入成功')
    }
})