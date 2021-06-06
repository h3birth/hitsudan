// google apiを使用
let {google} = require('googleapis');
// httpを使用
let http = require('http');

let server = http.createServer(function(req, res) {
  // アクセストークン取得
  const r = getAccessToken();
  r.then( (val) => {
    // アクセストークンが取得成功したらトークンを描画
    res.write(val);
    res.end();
 } );
}).listen(8080); // 8080ポートで待ち受け

// アクセストークン取得関数
 function getAccessToken() {
  return new Promise(function(resolve, reject) {
    var key = require('./hitsudan-cf359-firebase-adminsdk-hzfz3-b02eef1b31.json');
    var jwtClient = new google.auth.JWT(
      key.client_email,
      null,
      key.private_key,
       [
         'https://www.googleapis.com/auth/firebase.messaging', 
       ],
      null
    );
    jwtClient.authorize(function(err, tokens) {
      if (err) {
        reject(err);
        return;
      }
      resolve(tokens.access_token);
    });
  });
}