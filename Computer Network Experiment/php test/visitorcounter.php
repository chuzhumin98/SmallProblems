<?php
$c_file="counter.txt"; //�ļ�����ֵ������
if(!file_exists($c_file)) //����ļ������ڵĲ���
{
$myfile=fopen($c_file,"w"); //�����ļ�
fwrite($myfile,"0"); //���롰0��
fclose($myfile); //�ر��ļ�
}
$t_num=file($c_file); //���ļ����ݶ������
echo "��ӭ��";

//����1: ��������Ӵ���ͳ�Ʒÿ����� 
$t_num[0] += 1;
 
//����1����

echo "���Ǳ�վ��".$t_num[0]."λ�ÿͣ�"; //��ʾ�ļ�����

//����2: ������հ״���Ӵ���ʵ�ֶ�̬��ʾͼƬ����
//Ҫ���״η��ʺͷ��״η�����ʾ��ͬͼƬ
//��׼������ͼƬ������htdocs�ļ����£������½��ļ��б���

echo "<br /></br />"; //���в���һ�У��ý�������һЩ
if($t_num[0]!==1) {
	echo "<img src=\"img/others.jpg\" />";
}				//��������Ӵ�����ʾͼƬ1��
else {
	echo "<img src=\"img/No1.jpg\" />";
}    					//�����������ʾͼƬ2

//����2����

$myfile=fopen($c_file,"w"); //���ļ�
fwrite($myfile,$t_num[0]); //д��������
fclose($myfile); //�ر��ļ�

//ѡ����ʵ���κ�����ʵ�ֵĹ��� 
echo "<br />";
echo <<<EOF
<script language="JavaScript">
document.write('<div id="timeShow" align="left" style="MARGIN-right:10px;font-size:16pt;font-family:����;color:#00F"></div>');

EOF;
   
$n = chr(13); //���з�
   
echo "var y=".date("Y").";".$n; //��¼��   
echo "var m=".date("n").";".$n; //��¼��  
echo "var d=".date("j").";".$n; //��¼��   
echo "var h=".date("H").";".$n; //��¼ʱ   
echo "var i=".date("i").";".$n; //��¼��   
echo "var s=".date("s").";".$n; //��¼�� 

echo <<<EOF
function setTime() {
	s++; //�Ƚ�������һ
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
			var monthDay = 31; //��¼���·ݵ�����
			if (m == 4 || m == 6 || m == 9 || m == 11) {
				monthDay = 30;
			}
			if (m == 2) {
				if ((y % 4 == 0 && y % 100 != 0) || (y % 400 == 0)) {
					monthDay = 29; //��������
				} else {
					monthDay = 28; //ƽ������
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
	timeShow.innerHTML = y + '��' + m + '��' + d + '�� ' + h + ':' + i + ':' + s;
	setTimeout('setTime()', 1000);
}
setTime();
</script>
EOF;
 

//ѡ������
?>

