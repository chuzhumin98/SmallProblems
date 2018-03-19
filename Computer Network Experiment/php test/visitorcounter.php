<?php
$c_file="counter.txt"; //文件名赋值给变量
if(!file_exists($c_file)) //如果文件不存在的操作
{
$myfile=fopen($c_file,"w"); //创建文件
fwrite($myfile,"0"); //置入“0”
fclose($myfile); //关闭文件
}
$t_num=file($c_file); //把文件内容读入变量
echo "欢迎！";

//必做1: 在下面添加代码统计访客数量 
$t_num[0] += 1;
 
//必做1结束

echo "您是本站第".$t_num[0]."位访客！"; //显示文件内容

//必做2: 在下面空白处添加代码实现动态显示图片功能
//要求首次访问和非首次访问显示不同图片
//先准备两张图片保存在htdocs文件夹下，或者新建文件夹保存

echo "<br /></br />"; //换行并空一行，让界面美观一些
if($t_num[0]!==1) {
	echo "<img src=\"img/others.jpg\" />";
}				//在这里添加代码显示图片1，
else {
	echo "<img src=\"img/No1.jpg\" />";
}    					//在这里添加显示图片2

//必做2结束

$myfile=fopen($c_file,"w"); //打开文件
fwrite($myfile,$t_num[0]); //写入新内容
fclose($myfile); //关闭文件

//选作：实现任何你想实现的功能 
echo "<br />";
echo <<<EOF
<script language="JavaScript">
document.write('<div id="timeShow" align="left" style="MARGIN-right:10px;font-size:16pt;font-family:等线;color:#00F"></div>');

EOF;
   
$n = chr(13); //换行符
   
echo "var y=".date("Y").";".$n; //记录年   
echo "var m=".date("n").";".$n; //记录月  
echo "var d=".date("j").";".$n; //记录日   
echo "var h=".date("H").";".$n; //记录时   
echo "var i=".date("i").";".$n; //记录分   
echo "var s=".date("s").";".$n; //记录秒 

echo <<<EOF
function setTime() {
	s++; //先将秒数加一
	if (s >= 60) {
		s -= 60;
		i++;
	}
	if (i >= 60) {
		i -= 60;
		h++;
	}
	if (h >= 24) {
		h -= 24;
		d++;
		if (d >= 28) {
			var monthDay = 31; //记录该月份的天数
			if (m == 4 || m == 6 || m == 9 || m == 11) {
				monthDay = 30;
			}
			if (m == 2) {
				if ((y % 4 == 0 && y % 100 != 0) || (y % 400 == 0)) {
					monthDay = 29; //闰年问题
				} else {
					monthDay = 28; //平年问题
				}
			}
			if (d > monthDay) {
				d -= monthDay;
				m++;
				if (m >= 13) {
					m -= 12;
					y++;
				}
			}
		}
	}
	timeShow.innerHTML = y + '年' + m + '月' + d + '日 ' + h + ':' + i + ':' + s;
	setTimeout('setTime()', 1000);
}
setTime();
</script>
EOF;
 

//选作结束
?>

