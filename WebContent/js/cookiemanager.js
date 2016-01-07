/**
 * Created by zhang_000 on 14-1-9.
 */

var destination = "192.168.1.1:8080";

function setCookie(cname,cvalue,day)
{
    var _date = new Date();
    _date.setDate(_date.getDate()+day);
    _date.toGMTString();
    document.cookie=cname+"=" + escape(cvalue) + ";expires="+_date.toGMTString();
}

function getCookie(c_name){
    if (document.cookie.length>0){
        var c_start=document.cookie.indexOf(c_name + "=")
        if (c_start!=-1){
            c_start=c_start + c_name.length+1
            var c_end=document.cookie.indexOf(";",c_start)
            if (c_end==-1)
                c_end=document.cookie.length
            return unescape(document.cookie.substring(c_start,c_end));
        }
    }
    return '';
}

function getStatus()
{
    var id=getCookie('id');
    var sq_id=getCookie('sqid');
    return {
        id:id,
        sq_id:sq_id
    };
}