// MediaPlayDlg.cpp : 实现文件
//

#include "stdafx.h"
#include "bigJob.h"
#include "MediaPlayDlg.h"
#include "afxdialogex.h"


// MediaPlayDlg 对话框

IMPLEMENT_DYNAMIC(MediaPlayDlg, CDialogEx)

MediaPlayDlg::MediaPlayDlg(CWnd* pParent /*=NULL*/)
	: CDialogEx(MediaPlayDlg::IDD, pParent)
{

}

MediaPlayDlg::~MediaPlayDlg()
{
}

void MediaPlayDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_OCX1, m_mediaPlay);
}


BEGIN_MESSAGE_MAP(MediaPlayDlg, CDialogEx)
END_MESSAGE_MAP()


// MediaPlayDlg 消息处理程序
BEGIN_EVENTSINK_MAP(MediaPlayDlg, CDialogEx)
	ON_EVENT(MediaPlayDlg, IDC_OCX1, 6506, MediaPlayDlg::DoubleClickOcx1, VTS_I2 VTS_I2 VTS_I4 VTS_I4)
END_EVENTSINK_MAP()


void MediaPlayDlg::DoubleClickOcx1(short nButton, short nShiftState, long fX, long fY)
{
  // TODO: 在此处添加消息处理程序代码
  CString Type=L"MP4";
  HRSRC res=FindResource (NULL,MAKEINTRESOURCE(IDR_MP4_MYSHUO),Type);
  HGLOBAL gl=LoadResource (NULL,res); 
  //返回指向资源内存的地址的指针
  LPVOID lp=LockResource(gl);
  //保存的临时文件名
  CString filename=L"myshuo.mp4";
 
  HANDLE fp= CreateFile(filename,GENERIC_WRITE,0,NULL,CREATE_ALWAYS,0,NULL);
  DWORD a;
  //sizeofResource 得到资源文件的大小
  if (!WriteFile (fp,lp,SizeofResource (NULL,res),&a,NULL))
  return;
  //关闭句柄
  CloseHandle (fp);
  //释放内存
  FreeResource (gl); 
 
  //查找flash文件并加载
  TCHAR strCurDrt[500];
  int nLen = ::GetCurrentDirectory(500,strCurDrt);
  if( strCurDrt[nLen]!='\\' )
  {
  strCurDrt[nLen++] = '\\';
  strCurDrt[nLen] = '\0';
  }
   
  CString strFileName = strCurDrt;
  strFileName += L"myshuo.mp4";
  m_mediaPlay.put_URL(strFileName);   
		 // 传递媒体文件到播放器，并开始播放
}
