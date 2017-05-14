var http = require('http');
var version=0;
var express = require('express');
var app = express();
var NowDate=new Date();
var day=NowDate.getDay();
var path   = require("path");
var ejs = require('ejs');
var dayNames=new Array("星期一","星期二","星期三","星期四","星期五","星期六","星期日");
var busboy = require('connect-busboy'); //middleware for form/file upload
var path = require('path');     //used for file path
var fs = require('fs-extra');       //File System - for file manipulation
var test1="02patching.app";
var shell = require("shelljs");
var fs = require('fs');
app.engine('html', require('ejs').renderFile);
app.set('view engine', 'ejs');
app.use("/public", express.static(__dirname + '/public'));
app.use("/Up_Load_Apk",express.static(__dirname + '/Up_Load_Apk'));
app.use(express.static('views'));
app.use(busboy());
app.set('views', path.join(__dirname, 'views'));
app.get('/',function(req,res){
     res.render('upload');
    //res.render('upload');
});


app.post('/update_check', function(req, res){
    console.log('接受到Check的请求'+'   '+NowDate.toLocaleString()+'('+dayNames[day]+')');
    res.json({"url":"http://140.112.29.194:3333/Hook/hook.zip","appname":test1,"versionCode":version,"updateMessage":"Fix some awsome bug :) ","number":1});
    res.end();
});

 app.post('/update_file', function (req, res, next)
  {
        var fstream;
        req.pipe(req.busboy);
        req.busboy.on('file', function (fieldname, file, filename) {
            console.log("Uploading: " + filename);

            //Path where image will be uploaded
            fl = version+".apk"
            fstream = fs.createWriteStream(__dirname + '/Up_Load_Apk/' + fl);
            file.pipe(fstream);
            fstream.on('close', function () {    
                console.log("Upload Finished of " + filename);              
                //res.redirect('back');
                console.log("Upload file successful!");        //where to go next
                //version=version+1;
                console.log("Now Version:"+version);
                shell.exec("python test.py "+version);
                var path_txt=__dirname + '/Step1_txt/' + version + ".txt"; 
                if (fs.existsSync(path_txt)) {
                 shell.exec("java -cp libsvm.jar:. Test_module "+version);
               }
               var path_json=__dirname + '/Result/' + version + ".json"; 
               var result=JSON.parse(fs.readFileSync(path_json));
               console.log(result);
               var Good_P=result['Good_probability'];
               var Bad_P=result['Bad_probability'];
               res.locals.title=Good_P;
               res.locals.success =Good_P;
               res.render('upload_success');
               version=version+1;
            });
        });
    });
 app.post('/update_file_OS', function(req,res,next) {
        var fstream;
        req.pipe(req.busboy);
        req.busboy.on('file', function (fieldname, file, filename) {
            console.log("Uploading_TXT: " + filename);

            //Path where image will be uploaded
            fl = version+".txt"
            fstream = fs.createWriteStream(__dirname + '/Up_Load_txt_step1/' + fl);
            file.pipe(fstream);
            fstream.on('close', function () {    
                console.log("TXT Upload Finished of " + filename);              
                //res.redirect('back');
                console.log("TXT Upload file successful!");        //where to go next
                //version=version+1;
                console.log("Now Version:"+version);
               // shell.exec("python test.py "+version);
                var path_txt=__dirname + '/Up_Load_txt_step1/' + version + ".txt"; 
                if (fs.existsSync(path_txt)) {
                 shell.exec("java -cp libsvm.jar:. Test_module_txt "+version);
               }
               var path_json=__dirname + '/TXT_Result/' + version + ".json"; 
               var result=JSON.parse(fs.readFileSync(path_json));
               console.log(result);
               var Good_P=result['Good_probability'];
               var Bad_P=result['Bad_probability'];
               //res.locals.title=Good_P;
               //res.locals.success =Good_P;
               //res.render('upload_success');
               res.send(Good_P);
               res.end();
               version=version+1;
            });
        });
    });
// 创建服务端
http.createServer(app).listen('3333', function() {
  console.log('清理缓存...');
  shell.exec("rm -r /home/user/Server/Up_Load_txt_step1/*.txt");
  shell.exec("rm -r /home/user/Server/Up_Load_txt_step2/*.txt");
  shell.exec("rm -r /home/user/Server/TXT_Result/*.json");
  shell.exec("rm -r /home/user/Server/Step1_txt/*.txt");
  shell.exec("rm -r /home/user/Server/Step2_txt/*.txt");
  console.log('清理完成...');
  console.log('启动服务器完成');
  console.log("Now Version:"+version);
}); 
