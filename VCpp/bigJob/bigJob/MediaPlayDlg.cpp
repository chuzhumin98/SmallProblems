// MediaPlayDlg.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "bigJob.h"
#include "MediaPlayDlg.h"
#include "afxdialogex.h"


// MediaPlayDlg �Ի���

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


// MediaPlayDlg ��Ϣ�������
BEGIN_EVENTSINK_MAP(MediaPlayDlg, CDialogEx)
	ON_EVENT(MediaPlayDlg, IDC_OCX1, 6506, MediaPlayDlg::DoubleClickOcx1, VTS_I2 VTS_I2 VTS_I4 VTS_I4)
END_EVENTSINK_MAP()


void MediaPlayDlg::DoubleClickOcx1(short nButton, short nShiftState, long fX, long fY)
{
  // TODO: �ڴ˴������Ϣ����������
  CString Type=L"MP4";
  HRSRC res=FindResource (NULL,MAKEINTRESOURCE(IDR_MP4_MYSHUO),Type);
  HGLOBAL gl=LoadResource (NULL,res); 
  //����ָ����Դ�ڴ�ĵ�ַ��ָ��
  LPVOID lp=LockResource(gl);
  //�������ʱ�ļ���
  CString filename=L"myshuo.mp4";
 
  HANDLE fp= CreateFile(filename,GENERIC_WRITE,0,NULL,CREATE_ALWAYS,0,NULL);
  DWORD a;
  //sizeofResource �õ���Դ�ļ��Ĵ�С
  if (!WriteFile (fp,lp,SizeofResource (NULL,res),&a,NULL))
  return;
  //�رվ��
  CloseHandle (fp);
  //�ͷ��ڴ�
  FreeResource (gl); 
 
  //����flash�ļ�������
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
		 // ����ý���ļ���������������ʼ����
}
